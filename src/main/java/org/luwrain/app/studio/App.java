/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>

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

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.core.queries.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.app.base.*;
import org.luwrain.script.core.*;
import org.luwrain.script.*;

import org.luwrain.studio.proj.single.*;

public final class App extends AppBase<Strings>
{
    static public final String
	LOG_COMPONENT = "studio";

    private final String arg;
    final IDE ide = getIde();
    final ProjectFactory projFactory;
    private Conversations conv = null;
    private ProjectBaseLayout projectBaseLayout = null;
    private TextEditingLayout textEditingLayout = null;
    private NewProjectLayout newProjectLayout = null;

    private ScriptCore scriptCore = null;
        private Project proj = null;
    private Object treeRoot = null;
    Editing editing = null;
    private final List<Editing> editings = new ArrayList<>();
    private Object[] compilationOutput = new Object[0];
    private final MutableLinesImpl outputText = new MutableLinesImpl();

    public App() { this(null); }
    public App(String arg)
    {
	super(Strings.NAME, Strings.class, "luwrain.studio");
	setTabProcessing(false);
	this.projFactory = new ProjectFactory(ide);
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
	if (loadProjectByArg())
	    return (textEditingLayout != null)?textEditingLayout.getAreaLayout():projectBaseLayout.getAreaLayout();
	return newProjectLayout.getAreaLayout();
    }

    private boolean loadProjectByArg() throws IOException
    {
	if (arg == null || arg.isEmpty())
	    return false;
	final Project singleFileProj = SingleFileProject.newProject(getIde(), new File(arg));
	if (singleFileProj != null)
	{
	    this.proj = singleFileProj;
	    this.treeRoot = proj.getPartsRoot();
	    projectBaseLayout.treeArea.refresh();
	    final Editing editing = proj.getMainSourceFile().startEditing();
	    	    editings.add(editing);
	this.textEditingLayout = new TextEditingLayout(this, projectBaseLayout, (TextEditing)editing);
	    return true;
	}
	return loadProject(new File(arg));
    }

    private boolean loadProject(File file)
    {
	if (!file.exists() || file.isDirectory())
	    return false;
	final TaskId taskId = newTaskId();
	return runTask(taskId, ()->{
		final Project proj;
		try {
		    proj = new ProjectFactory(ide).load(file);
		}
		catch(Throwable e)
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
			//FIXME:do everything right here
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

    private void loadScriptCore() throws IOException
    {
	this.scriptCore = new ScriptCore(getLuwrain());
	final File scriptsDir = getLuwrain().getFileProperty(Luwrain.PROP_DIR_JS);
	final File[] scripts = scriptsDir.listFiles();
	if (scripts == null)
	    return;
	for(File f: scripts)
	    if (f != null && f.getName().startsWith("studio-"))
	    {
		Log.debug(LOG_COMPONENT, "loading " + f.getAbsolutePath());
		scriptCore.load(f);
	    }
    }

    private IDE getIde()
    {
	return new IDE(){
	    @Override public ScriptCore getScriptCore() { return scriptCore; }
	    @Override public App getAppBase() { return App.this; }
	    @Override public boolean loadProject(File file) { return App.this.loadProject(file); }
	    @Override public Luwrain getLuwrainObj() { return getLuwrain(); }
	    @Override public void onFoldersUpdate()
	    {
		if (projectBaseLayout != null)
		    projectBaseLayout.treeArea.refresh();
	    }
	    @Override public void onEditingUpdate()
	    {
		final TextEditingLayout layout = App.this.textEditingLayout;
		if (layout == null)
		    return ;
		getLuwrain().onAreaNewContent(layout.editArea);
		getLuwrain().onAreaNewHotPoint(layout.editArea);
	    }
	    @Override public void showWizard(LayoutBase wizardLayout)
	    {
		NullCheck.notNull(wizardLayout, "wizardLayout");
		setAreaLayout(wizardLayout);
	    }
	};
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

    void startEditing(Editing editing) throws IOException
    {
	Editing e = null;
	for(Editing ee: editings)
	    if (editing.equals(ee))
		e = ee;
	if (e == null)
	    editings.add(editing);
	this.textEditingLayout = new TextEditingLayout(this, projectBaseLayout, (TextEditing)editing);
	setAreaLayout(this.textEditingLayout);
	this.textEditingLayout.setActiveArea(this.textEditingLayout.editArea);
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

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    @Override public boolean onAreaQuery(Area area, AreaQuery query)
    {
	if (query.getQueryCode() == AreaQuery.CURRENT_DIR && query instanceof CurrentDirQuery)
	    return onDirectoryQuery((CurrentDirQuery)query);
	return super.onAreaQuery(area, query);
    }

    private boolean onDirectoryQuery(CurrentDirQuery query)
    {
	if (proj == null)
	    return false;
	final File f = new File("/tmp"); //app.file.getParentFile();
	if (f == null)
	    return false;
	query.answer(f.getAbsolutePath());
	return true;
    }

    @Override public void closeApp()
    {
	boolean hasUnsavedChanges = false;
	for(Editing e: editings)
	    if (e.hasUnsavedChanges())
	    {
		hasUnsavedChanges = true;
		break;
	    }
	if (hasUnsavedChanges)
	    switch(conv.unsavedChanges())
	    {
	    case CANCEL:
		return;
	    case SAVE:
		for(Editing e: editings)
		    if (e.hasUnsavedChanges())
			try {
			    e.save();
			}
			catch(IOException ex)
			{
			    crash(ex);
			    return;
			}
	    }
	for(Editing e: editings)
	    e.closeEditing();
	editings.clear();
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

        Project getProject()
    {
	return this.proj;
    }

    Lines getOutputModel()
    {
	return new OutputModel();
    }

    Conversations conv() { return this.conv; }
    boolean isSingleFileProject() { return this.proj instanceof SingleFileProject; }
    TextEditingLayout getTextEditingLayout() { return textEditingLayout; }

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
