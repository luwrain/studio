
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

final Luwrain luwrain;
    final Strings strings;
    final Settings sett;



    final MutableLinesImpl fileText = new MutableLinesImpl();
    Editing openedEditing = null;
    Object[] compilationOutput = new Object[0];
    final MutableLinesImpl outputText = new MutableLinesImpl();

    Base (Luwrain luwrain, Strings strings)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
	this.luwrain = luwrain;
	this.strings = strings;
	this.sett = Settings.create(luwrain.getRegistry());

    }




    void startEditing(Editing editing) throws IOException
    {
	/*
	NullCheck.notNull(editing, "editing");
	final File file = editing.getFile();
	NullCheck.notNull(file, "file");
	final String wholeText = FileUtils.readTextFileSingleString(file, CHARSET);
	final String[] lines = FileUtils.universalLineSplitting(wholeText);
	fileText.setLines(lines);
	this.openedEditing = editing;
	*/
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


    Lines getOutputModel()
    {
	return new OutputModel();
    }

    /*
    ProjectType[] getNewProjectTypes()
    {
	return new ProjectFactory(luwrain).getNewProjectTypes();
    }
    */



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
