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
import java.io.*;

import org.luwrain.base.*;
import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;

public class App implements Application
{
    private Luwrain luwrain = null;
    private Strings strings = null;
    private Base base = null;
    private Actions actions = null;
    private ActionLists actionLists = null;

    private TreeArea treeArea = null;
    private EditArea editArea = null;
    private NavigationArea outputArea = null;
    private AreaLayoutHelper layout = null;

    private final String arg;

    public App()
    {
	arg = null;
    }

    public App(String arg)
    {
	NullCheck.notNull(arg, "arg");
	this.arg = arg;
    }

    @Override public InitResult onLaunchApp(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	final Object o =  luwrain.i18n().getStrings(Strings.NAME);
	if (o == null || !(o instanceof Strings))
	    return new InitResult(InitResult.Type.NO_STRINGS_OBJ, Strings.NAME);
	strings = (Strings)o;
	this.luwrain = luwrain;
	this.base = new Base(luwrain, strings);
	this.actionLists = new ActionLists(strings, base);
	this.actions = new Actions(luwrain, base, strings);
	createAreas();
	layout = new AreaLayoutHelper(()->{
		luwrain.onNewAreaLayout();
		luwrain.announceActiveArea();
	    }, new AreaLayout(AreaLayout.LEFT_RIGHT_BOTTOM, treeArea, editArea, outputArea));
	loadProjectByArg();
	return new InitResult();
    }

    private void createAreas()
    {
	final TreeArea.Params treeParams = new TreeArea.Params();
	treeParams.context = new DefaultControlEnvironment(luwrain);
	treeParams.model = new CachedTreeModel(base.getTreeModel());
	treeParams.name = strings.treeAreaName();
	treeParams.clickHandler = (area,obj)->{
	    NullCheck.notNull(obj, "obj");
	    if (!(obj instanceof SourceFile))
		return false;
	    final SourceFile sourceFile = (SourceFile)obj;
	    final SourceFile.Editing editing = sourceFile.startEditing();
	    if (editing == null)
		return false;
	    if (base.openedEditing != null && base.openedEditing.getFile().equals(editing.getFile()))
	    {
		luwrain.setActiveArea(editArea);
		return true;
	    }
	    luwrain.message(editing.getFile().getAbsolutePath());
	    try {
		base.startEditing(editing);
	    }
	    catch(IOException e)
	    {
		luwrain.message(luwrain.i18n().getExceptionDescr(e), Luwrain.MessageType.ERROR);
		return true;
	    }
	    editArea.setHotPoint(0, 0);
	    luwrain.onAreaNewContent(editArea);
	    luwrain.setActiveArea(editArea);
	    return true;
	};

 	treeArea = new TreeArea(treeParams){
		@Override public boolean onInputEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.isSpecial() && !event.isModified())
			switch(event.getSpecial())
			{
			case TAB:
			    luwrain.setActiveArea(editArea);
			    return true;
			}
		    return super.onInputEvent(event);
		}
		@Override public boolean onSystemEvent(EnvironmentEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.getType() != EnvironmentEvent.Type.REGULAR)
			return super.onSystemEvent(event);
		    switch(event.getCode())
		    {
		    case ACTION:
			return onCommonActions(event);
		    case CLOSE:
			closeApp();
			return true;
		    default:
			return super.onSystemEvent(event);
		    }
		}
		@Override public Action[] getAreaActions()
		{
		    return actionLists.getTreeActions();
		}
	    };

	final EditArea.Params editParams = new EditArea.Params();
	editParams.context = new DefaultControlEnvironment(luwrain);
	editParams.name = strings.editAreaName();
	editParams.content = base.fileText;
	editParams.correctorFactory = (corrector)->{
	    NullCheck.notNull(corrector, "corrector");
	    base.editCorrectorWrapper.setWrappedCorrector(corrector);
	    return base.editCorrectorWrapper;
	};

	editArea = new EditArea(editParams) {
		@Override public boolean onInputEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.isSpecial() && !event.isModified())
			switch(event.getSpecial())
			{
			case TAB:
			    luwrain.setActiveArea(outputArea);
			    return true;
			}
		    return super.onInputEvent(event);
		}
		@Override public boolean onSystemEvent(EnvironmentEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.getType() != EnvironmentEvent.Type.REGULAR)
			return super.onSystemEvent(event);
		    switch(event.getCode())
		    {
		    case ACTION:
			return onCommonActions(event);
		    case SAVE:
			return actions.onSaveEdit();
		    case CLOSE:
			closeApp();
			return true;
		    default:
			return super.onSystemEvent(event);
		    }
		}
		@Override public Action[] getAreaActions()
		{
		    return actionLists.getEditActions();
		}
		@Override public void announceLine(int index, String line)
		{
		    NullCheck.notNull(line, "line");
		    base.codePronun.announceLine(line);
		}
	    };

	outputArea = new NavigationArea(new DefaultControlEnvironment(luwrain)) {
		final Lines outputModel = base.getOutputModel();
		@Override public boolean onInputEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.isSpecial() && !event.isModified())
			switch(event.getSpecial())
			{
			case TAB:
			    luwrain.setActiveArea(treeArea);
			    return true;
			case BACKSPACE:
			    luwrain.setActiveArea(editArea);
			    return true;
			case ENTER:
			    return actions.onOutputClick(getHotPointY(), editArea);
			}
		    return super.onInputEvent(event);
		}
		@Override public boolean onSystemEvent(EnvironmentEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.getType() != EnvironmentEvent.Type.REGULAR)
			return super.onSystemEvent(event);
		    switch(event.getCode())
		    {
		    case ACTION:
			return onCommonActions(event);
		    case OK:
			return actions.onOutputClick(getHotPointY(), editArea);
		    case CLOSE:
			closeApp();
			return true;
		    default:
			return super.onSystemEvent(event);
		    }
		}
		@Override public Action[] getAreaActions()
		{
		    return actionLists.getOutputActions();
		}
				@Override public int getLineCount()
		{
		    return outputModel.getLineCount();
		}
		@Override public String getLine(int index)
		{
		    return outputModel.getLine(index);
		}
		@Override public String getAreaName()
		{
		    return strings.outputAreaName();
		}
	    };
    }

    private boolean onCommonActions(EnvironmentEvent event)
    {
	NullCheck.notNull(event, "event");
	if (ActionEvent.isAction(event, "open-project"))
	    return actions.onOpenProject(treeArea);
		if (ActionEvent.isAction(event, "run"))
	    return actions.onRun(outputArea);
			return false;
    }

    private void loadProjectByArg()
    {
	if (arg == null || arg.isEmpty())
	    return;
	final File file = new File(arg);
	if (!file.exists() || file.isDirectory())
	    return;
	final Project proj;
	try {
	    proj = ProjectFactory.load(file);
	}
	catch(IOException e)
	{
	    //FIXME: this notification isn't heard
	    luwrain.message(luwrain.i18n().getExceptionDescr(e), Luwrain.MessageType.ERROR);
	    return;
	}
	base.activateProject(proj);
	treeArea.refresh();
    }

    @Override public AreaLayout getAreaLayout()
    {
	return layout.getLayout();
    }

    @Override public String getAppName()
    {
	return strings.appName();
    }

    @Override public void closeApp()
    {
	if (base.getProject() != null)
	    base.getProject().close(luwrain);
	luwrain.closeApp();
    }
}
