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

    @Override public InitResult onLaunchApp(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	final Object o =  luwrain.i18n().getStrings(Strings.NAME);
	if (o == null || !(o instanceof Strings))
	    return new InitResult(InitResult.Type.NO_STRINGS_OBJ, Strings.NAME);
	strings = (Strings)o;
	this.luwrain = luwrain;
	this.base = new Base(luwrain, strings);
	this.actionLists = new ActionLists(strings);
	this.actions = new Actions(luwrain, base, strings);
	createAreas();
	layout = new AreaLayoutHelper(()->{
		luwrain.onNewAreaLayout();
		luwrain.announceActiveArea();
	    }, new AreaLayout(AreaLayout.LEFT_RIGHT_BOTTOM, treeArea, editArea, outputArea));
	return new InitResult();
    }

    private void createAreas()
    {
	final TreeArea.Params treeParams = new TreeArea.Params();
	treeParams.environment = new DefaultControlEnvironment(luwrain);
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
	    if (base.openedFile != null && base.openedFile.equals(editing.getFile()))
	    {
		luwrain.setActiveArea(editArea);
		return true;
	    }
	    try {
		base.startEditing(editing);
	    }
	    catch(IOException e)
	    {
		//FIXME:
	    }
	    editArea.setHotPoint(0, 0);
	    luwrain.onAreaNewContent(editArea);
	    luwrain.setActiveArea(editArea);
	    return true;
	};

 	treeArea = new TreeArea(treeParams){
		@Override public boolean onKeyboardEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.isSpecial() && !event.isModified())
			switch(event.getSpecial())
			{
			case TAB:
			    luwrain.setActiveArea(editArea);
			    return true;
			}
		    return super.onKeyboardEvent(event);
		}
		@Override public boolean onEnvironmentEvent(EnvironmentEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.getType() != EnvironmentEvent.Type.REGULAR)
			return super.onEnvironmentEvent(event);
		    switch(event.getCode())
		    {
		    case ACTION:
			return onCommonActions(event);
		    case CLOSE:
			closeApp();
			return true;
		    default:
			return super.onEnvironmentEvent(event);
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
	editParams.correctorWrapperFactory = (corrector)->{
	    NullCheck.notNull(corrector, "corrector");
	    base.editCorrectorWrapper.setWrappedCorrector(corrector);
	    return base.editCorrectorWrapper;
	};

	editArea = new EditArea(editParams) {
		@Override public boolean onKeyboardEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.isSpecial() && !event.isModified())
			switch(event.getSpecial())
			{
			case TAB:
			    luwrain.setActiveArea(outputArea);
			    return true;
			}
		    return super.onKeyboardEvent(event);
		}
		@Override public boolean onEnvironmentEvent(EnvironmentEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.getType() != EnvironmentEvent.Type.REGULAR)
			return super.onEnvironmentEvent(event);
		    switch(event.getCode())
		    {
		    case ACTION:
			return onCommonActions(event);
		    case CLOSE:
			closeApp();
			return true;
		    default:
			return super.onEnvironmentEvent(event);
		    }
		}
		@Override public Action[] getAreaActions()
		{
		    return actionLists.getEditActions();
		}
	    };

	outputArea = new NavigationArea(new DefaultControlEnvironment(luwrain)) {
		@Override public boolean onKeyboardEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.isSpecial() && !event.isModified())
			switch(event.getSpecial())
			{
			case TAB:
			    luwrain.setActiveArea(treeArea);
			    return true;
			}
		    return super.onKeyboardEvent(event);
		}
		@Override public boolean onEnvironmentEvent(EnvironmentEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.getType() != EnvironmentEvent.Type.REGULAR)
			return super.onEnvironmentEvent(event);
		    switch(event.getCode())
		    {
		    case ACTION:
			return onCommonActions(event);
		    case CLOSE:
			closeApp();
			return true;
		    default:
			return super.onEnvironmentEvent(event);
		    }
		}
		@Override public Action[] getAreaActions()
		{
		    return actionLists.getOutputActions();
		}
				@Override public int getLineCount()
		{
		    return 1;
		}
		@Override public String getLine(int index)
		{
		    if (index < 0)
			throw new IllegalArgumentException("index (" + index + ") may not be negative");
		    return "";
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
	return false;
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
	luwrain.closeApp();
    }
}
