/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.app.studio;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

import org.luwrain.base.*;
import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.util.*;

final class Base
{
    static final String CHARSET = "UTF-8";

    private final Luwrain luwrain;
    private final Strings strings;
    final Settings sett;
    final CodePronunciation codePronun;
    private final String treeRoot;

    private Project project = null;
    private FutureTask runTask = null;

    final MutableLinesImpl fileText = new MutableLinesImpl();
    SourceFile.Editing openedEditing = null;
    final EditCorrectorWrapper editCorrectorWrapper = new EditCorrectorWrapper();
    Object[] compilationOutput = new Object[0];
    final MutableLinesImpl outputText = new MutableLinesImpl();

    Base (Luwrain luwrain, Strings strings)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
	this.luwrain = luwrain;
	this.strings = strings;
	this.sett = Settings.create(luwrain.getRegistry());
	this.codePronun = new CodePronunciation(luwrain, strings);
	this.treeRoot = strings.treeRoot();
    }

    void activateProject(Project proj)
    {
	NullCheck.notNull(proj, "proj");
	this.project = proj;
    }

    Project getProject()
    {
	return project;
    }

    boolean isProjectRunning()
    {
	return runTask != null && !runTask.isDone();
    }

    boolean runProject(Runnable outputRedrawing) throws IOException
    {
	NullCheck.notNull(outputRedrawing, "outputRedrawing");
	if (project == null || isProjectRunning())
	    return false;
	outputText.clear();
	compilationOutput = new Object[0];
	outputRedrawing.run();
	final OutputControl output = new OutputControl(()->luwrain.runUiSafely(outputRedrawing));
	final RunControl runControl = project.run(luwrain, output);
	if (runControl == null)
	    return false;
	if (runControl.isSuitableForBackground())
	{
	    runTask = new FutureTask(()->{
		    try {
			runControl.getCallableObj().call();
			luwrain.playSound(Sounds.DONE);
		    }
		    catch(javax.script.ScriptException e)
		    {
			compilationOutput = new Object[]{new ScriptExceptionWrapper(e), ""};
luwrain.runUiSafely(outputRedrawing);
			luwrain.playSound(Sounds.ERROR);
		    }
		    catch(Exception e)
		    {
			luwrain.crash(e);
		    }
		}, null);
	    luwrain.executeBkg(runTask);
	    return true;
	}
	try {
	    runControl.getCallableObj().call();
	    luwrain.playSound(Sounds.DONE);
	}
	catch(Exception e)
	{
	    luwrain.crash(e);
	}
	return true;
    }

    void startEditing(SourceFile.Editing editing) throws IOException
    {
	NullCheck.notNull(editing, "editing");
	final File file = editing.getFile();
	NullCheck.notNull(file, "file");
	final String wholeText = FileUtils.readTextFileSingleString(file, CHARSET);
	final String[] lines = FileUtils.universalLineSplitting(wholeText);
	fileText.setLines(lines);
	this.openedEditing = editing;
    }

    PositionInfo getCompilationOutputPositionInfo(int index)
    {
	if (compilationOutput == null || index < 0 || index >= compilationOutput.length)
	    return null;
	final Object obj = compilationOutput[index];
	if (!(obj instanceof ScriptExceptionWrapper))
	    return null;
	final ScriptExceptionWrapper wrapper = (ScriptExceptionWrapper)obj;
	if (wrapper.ex.getLineNumber() <= 0)
	    return null;
	return new PositionInfo(wrapper.ex.getFileName(), wrapper.ex.getLineNumber(), wrapper.ex.getColumnNumber());
    }

    CachedTreeModelSource getTreeModel()
    {
	return new TreeModel();
    }

    Lines getOutputModel()
    {
	return new OutputModel();
    }

    private class TreeModel implements CachedTreeModelSource
    {
	@Override public Object getRoot()
	{
	    return treeRoot;
	}
	@Override public Object[] getChildObjs(Object obj)
	{
	    NullCheck.notNull(obj, "obj");
	    if (project == null)
		return new Object[0];
	    final Folder folder;
	    if (obj == treeRoot)
		folder = project.getFoldersRoot(); else
		if (obj instanceof Folder)
		    folder = (Folder)obj; else
		    return new Object[0];
	    if (folder == null)
		return new Object[0];
	    final List res = new LinkedList();
	    for(Folder f: folder.getSubfolders())
		res.add(f);
	    for(SourceFile f: folder.getSourceFiles())
		res.add(f);
	    return res.toArray(new Object[res.size()]);
	}
    }

    private final class OutputControl implements org.luwrain.studio.Output
    {
	private final Runnable listener;
	OutputControl(Runnable listener)
	{
	    NullCheck.notNull(listener, "listener");
	    this.listener = listener;
	}
	@Override public void reset(String[] lines)
	{
	    NullCheck.notNullItems(lines, "lines");
	    outputText.setLines(lines);
	    listener.run();
	}
        @Override public void addLine(String line)
	{
	    NullCheck.notNull(line, "line");
	    listener.run();
	    outputText.addLine(line);
	}
    }

    private final class OutputModel implements Lines
    {
	@Override public int getLineCount()
	{
	    if (compilationOutput != null && compilationOutput.length > 0)
	    {
		final int count = compilationOutput.length;
		return count > 0?count:1;
	    }
	    final int count = outputText.getLineCount();
	    return count > 0?count:1;
	}
	@Override public String getLine(int index)
	{
	    if (index < 0)
		throw new IllegalArgumentException("index (" + index + ") may not be negative");
	    if (compilationOutput != null && compilationOutput.length > 0)
	    {
		if (index >= compilationOutput.length)
		    return "";
		return compilationOutput[index].toString();
	    }
	    if (outputText.getLineCount() < 1)
		return "";
	    return outputText.getLine(index);
	}
    }

    static final class PositionInfo
    {
	final String fileName;
	final int lineNum;
	final int colNum;

	PositionInfo(String fileName, int lineNum, int colNum)
	{
	    NullCheck.notNull(fileName, "fileName");
	    if (lineNum <= 0)
		throw new IllegalArgumentException("lineNum (" + lineNum + ") must be greater than zero");
	    this.fileName = fileName;
	    this.lineNum = lineNum;
	    this.colNum = colNum;
	}
    }
}
