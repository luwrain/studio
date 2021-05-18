/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>

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
import java.io.*;

import org.luwrain.base.*;
import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.app.base.*;
import org.luwrain.core.script2.*;
import org.luwrain.script2.*;

public final class App extends AppBase<Strings>
{
    static public final String LOG_COMPONENT = "studio";

    private final String arg;
    private Conversations conv = null;
    private ProjectBaseLayout projectBaseLayout = null;
    private NewProjectLayout newProjectLayout = null;

    private ScriptCore scriptCore = null;
    private Object treeRoot = null;
    private Project proj = null;
    Editing editing = null;
    private final List<Editing> editings = new ArrayList();
    private Object[] compilationOutput = new Object[0];
    private final MutableLinesImpl outputText = new MutableLinesImpl();

    public App()
    {
	this(null);
    }

    public App(String arg)
    {
	super(Strings.NAME, Strings.class, "luwrain.studio");
	this.arg = arg;
    }

    @Override protected AreaLayout onAppInit() throws IOException
    {
		this.conv = new Conversations(this);
		loadScriptCore();
		this.projectBaseLayout = new ProjectBaseLayout(this);
			this.newProjectLayout = new NewProjectLayout(this);
	this.treeRoot = getStrings().treeRoot();
	setAppName(getStrings().appName());
	loadProjectByArg();
		if (this.proj == null)
	    	return newProjectLayout.getAreaLayout();
	    return projectBaseLayout.getAreaLayout();
    }

    private void loadScriptCore() throws IOException
    {
	this.scriptCore = new ScriptCore(getLuwrain());
	final File scriptsDir = new File(new File(getLuwrain().getFileProperty("luwrain.dir.data"), "studio"), "js");
	final File[] scripts = scriptsDir.listFiles();
	if (scripts == null)
	    return;
	for(File f: scripts)
	    if (f != null)
	    {
		Log.debug(LOG_COMPONENT, "loading " + f.getAbsolutePath());
		scriptCore.load(f);
	    }
	 }

    /*
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
	{
	    luwrain.message("Проект к запуску не готов.", Luwrain.MessageType.ERROR);
	    return true;
	}
	if (runControl.isContinuous())
	    return runBackground(runControl, outputRedrawing);
	return runForeground(runControl);
    }
    */

    private boolean runBackground(RunControl runControl, Runnable outputRedrawing)
    {
	/*
	NullCheck.notNull(runControl, "runControl");
	NullCheck.notNull(outputRedrawing, "outputRedrawing");
	this.runTask = new FutureTask(()->{
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
	*/
	return true;
    }

    private boolean runForeground(RunControl runControl)
    {
	/*
	NullCheck.notNull(runControl, "runControl");
	try {
	    runControl.getCallableObj().call();
	    luwrain.playSound(Sounds.DONE);
	}
	catch(Exception e)
	{
	    luwrain.crash(e);
	}
	return true;
	*/
	return false;
    }

    IDE getIde()
    {
	return new IDE(){
	    @Override public ScriptCore getScriptCore()
	    {
		return scriptCore;
	    }
	    @Override public void onFoldersUpdate()
		{
		}
	    @Override public Luwrain getLuwrainObj()
	    {
		return getLuwrain();
	    }
	};
    }

    void layout(AreaLayout layout)
    {
	NullCheck.notNull(layout, "layout");
	getLayout().setBasicLayout(layout);
    }

    Layouts layouts()
    {
	return new Layouts(){
	    @Override public void projectBase()
	    {
		setAreaLayout(projectBaseLayout);
		getLuwrain().announceActiveArea();
	    }
	};
    }

    void activateProject(Project proj)
    {
	NullCheck.notNull(proj, "proj");
	this.proj = proj;
	this.treeRoot = proj.getPartsRoot();
	projectBaseLayout.treeArea.refresh();
	layouts().projectBase();
    }

    private void loadProjectByArg()
    {
	if (arg == null || arg.isEmpty())
	    return;
	final File file = new File(arg);
	if (!file.exists() || file.isDirectory())
	    return;
	final TaskId taskId = newTaskId();
	runTask(taskId, ()->{
		final Project proj;
		try {
		    proj = new ProjectFactory(getIde()).load(file);
		}
		catch(IOException e)
		{
		    getLuwrain().crash(e);
		    return;
		}
		if (proj == null)
		{
		    //FIXME:message
		    return;
		}
		finishedTask(taskId, ()->{
			activateProject(proj);
			/*
	final Part mainFile = proj.getMainSourceFile();
	if (mainFile == null)
	    return;
	  final Editing editing = mainFile.startEditing();
	  if (editing == null)
	  return;
startEditing(editing);
*/
	    });
	    });
    }

        void startEditing(Editing editing) throws IOException
    {
	NullCheck.notNull(editing, "editing");
	Editing e = null;
	for(Editing ee: editings)
	    if (editing.equals(ee))
		e = ee;
	if (e == null)
	    editings.add(editing);
	final TextEditingLayout layout = new TextEditingLayout(this, projectBaseLayout, (TextEditing)editing);
	setAreaLayout(layout);
	getLuwrain().setActiveArea(layout.editArea);
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

    Project getProject()
    {
	return this.proj;
    }

    Lines getOutputModel()
    {
	return new OutputModel();
    }

    Conversations conv()
    {
	return this.conv;
    }

    @Override public boolean onEscape(InputEvent event)
    {
	NullCheck.notNull(event, "event");
	closeApp();
	return true;
    }

    @Override public void closeApp()
    {
	if (proj != null)
	    proj.close();
	proj = null;
	super.closeApp();
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

    interface Layouts
    {
	void projectBase();
    }
}
