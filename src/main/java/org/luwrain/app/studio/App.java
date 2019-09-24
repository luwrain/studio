/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>

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

public final class App implements Application
{
    private Luwrain luwrain = null;
    private Strings strings = null;
    private Base base = null;
    private Actions actions = null;
    private ActionLists actionLists = null;

    private NewProjectArea newProjectArea = null;
    private TreeArea treeArea = null;
    private Area workArea = null;
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
	this.actions = new Actions(base, createLayouts());
	createAreas();
	layout = new AreaLayoutHelper(()->{
		luwrain.onNewAreaLayout();
		luwrain.announceActiveArea();
			    }, new AreaLayout(newProjectArea));
		//	    }, new AreaLayout(AreaLayout.LEFT_RIGHT_BOTTOM, treeArea, editArea, outputArea));
	loadProjectByArg();
	return new InitResult();
    }

    private void createAreas()
    {
	this.newProjectArea = new NewProjectArea(base, actions){
		@Override void onNewProject(Project project)
		{
		    NullCheck.notNull(project, "project");
		    base.activateProject(project);
		    layout.setBasicArea(treeArea);
		    treeArea.refresh();
		    luwrain.message("Готово");
		}
	    };

 	this.treeArea = new TreeArea(base.createTreeParams((area, obj)->actions.onTreeClick(obj))){
		@Override public boolean onInputEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.isSpecial() && !event.isModified())
			switch(event.getSpecial())
			{
			case TAB:
			    {
				final Area nextArea = layout.getLayout().getNextArea(this);
				if (nextArea == null)
				    return false;
				luwrain.setActiveArea(nextArea);
			    }
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

	this.outputArea = new NavigationArea(new DefaultControlContext(luwrain)) {
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
			    if (workArea == null)
				return false;
			    luwrain.setActiveArea(workArea);
			    return true;
			    /*
			      case ENTER:
			      return actions.onOutputClick(getHotPointY(), editArea);
			    */
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
			/*
			  case OK:
			  return actions.onOutputClick(getHotPointY(), editArea);
			*/
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

    Layouts createLayouts()
    {
	return new Layouts(){
	    @Override public boolean editing(Editing editing)
	    {
		NullCheck.notNull(editing, "editing");
		final Area workArea = createWorkArea(editing);
		if (workArea == null)
		    return false;
		App.this.workArea = workArea;
		layout.setBasicLayout(new AreaLayout(AreaLayout.LEFT_TOP_BOTTOM, treeArea, workArea, outputArea));
		luwrain.setActiveArea(workArea);
		return true;
	    }
	};
    }

    private Area createWorkArea(Editing editing)
    {
	NullCheck.notNull(editing, "editing");
	if (!(editing instanceof TextEditing))
	    return null;
	final TextEditing textEditing = (TextEditing)editing;
	final EditArea.Params editParams = textEditing.getEditParams(new DefaultControlContext(luwrain));
	return new EditArea(editParams) {
	    @Override public boolean onInputEvent(KeyboardEvent event)
	    {
		NullCheck.notNull(event, "event");
		if (event.isSpecial() && !event.isModified())
		    switch(event.getSpecial())
		    {
		    case TAB:
			{
			    final Area nextArea = layout.getLayout().getNextArea(this);
			    if (nextArea == null)
				return false;
			    luwrain.setActiveArea(nextArea);
			    return true;
			}
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
	final Part mainFile = proj.getMainSourceFile();
	if (mainFile == null)
	    return;
	/*
	  final SourceFile.Editing editing = mainFile.startEditing();
	  if (editing == null)
	  return;
	  try {
	  base.startEditing(editing);
	  }
	  catch(IOException e)
	  {
	  }
	*/
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
	base.closeApp();
    }
}
