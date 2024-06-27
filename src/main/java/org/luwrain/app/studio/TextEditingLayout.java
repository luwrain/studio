/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.studio.*;
import org.luwrain.app.base.*;

public final class TextEditingLayout extends LayoutBase
{
    private final App app;
    final TreeListArea<Part> treeArea;
    final EditArea editArea;
    final NavigationArea outputArea = null;
    private final TextEditing textEditing;
    private boolean showTree = true, showOutput = false;

    TextEditingLayout(App app, ProjectBaseLayout projectBaseLayout, TextEditing textEditing)
    {
	super(app);
	this .app = app;
	this.showTree = !app.isSingleFileProject();
	this.textEditing = textEditing;
	final ControlContext editingControlContext = new WrappingControlContext(getControlContext()){
		@Override public void onAreaNewHotPoint(Area area)
		{
		    textEditing.onNewHotPoint(area.getHotPointX(), area.getHotPointY());
		    super.onAreaNewHotPoint(area);
		}};
	this.treeArea = projectBaseLayout.treeArea;
	final EditArea.ChangeListener modificationListener = (editArea, lines, hotPoint)->textEditing.getModifiedFlag().set(true);
	final EditArea.Params editParams = textEditing.getEditParams(editingControlContext);
	if (editParams.changeListeners == null)
	    editParams.changeListeners = new ArrayList<>();
	editParams.changeListeners.add(modificationListener);
	this.editArea = new EditArea(editParams) {
		private final Map<String, Part.Action> actionsCache = new HashMap<>();
		@Override public boolean onInputEvent(InputEvent event)
		{
		    //Switching the tree
		    if (event.equals(ProjectBaseLayout.KEY_TREE_TOGGLE))
		    {
			if (app.isSingleFileProject())
			    return false;
			showTree = true;
			updateAreaLayout();
			app.setAreaLayout(TextEditingLayout.this);
			setActiveArea(treeArea);
			return true;
		    }
		    if (event.isSpecial() && !event.isModified())
			switch(event.getSpecial())
			{
			case TAB:
			    return fixIndent();
			}
		    return super.onInputEvent(event);
		}
				@Override public boolean onSystemEvent(SystemEvent event)
		{
		    if (event.getType() == SystemEvent.Type.REGULAR)
			switch(event.getCode())
			{
			case ACTION: {
			    			final ActionEvent actionEvent = (ActionEvent)event;
			final String actionName = actionEvent.getActionName();
			final Part.Action partAction = this.actionsCache.get(actionName);
			if (partAction == null)
			    return false;
			return partAction.onAction(app.ide);
			}
			case SAVE:
			    return onSave();
			}
		    return super.onSystemEvent(event);
		}
				@Override public Action[] getAreaActions()
		{
		    final Part.Action[] actions = textEditing.getActions();
		    actionsCache.clear();
		    final List<Action> res = new ArrayList<>();
		    int k = 1;
		    for(Part.Action a: actions)
		    {
			final Action action;
			if (a.getHotKey() != null)
			    action = new Action("action" + k++, a.getTitle(), a.getHotKey()); else
			    action = new Action("action" + k++, a.getTitle());
			this.actionsCache.put(action.name(), a);
			res.add(action);
		    }
		    return res.toArray(new Action[res.size()]);
		}
	    };
	updateAreaLayout();
    }

    private boolean onSave()
    {
	try {
	    if (!this.textEditing.save())
		return false;
	    app.message("Сохранено", Luwrain.MessageType.OK);
	    return true;
	}
	catch(IOException e)
	{
	    app.crash(e);
	    return true;
	}
    }

    private boolean fixIndent()
    {
	return false;
    }

    private void updateAreaLayout()
    {
	if (showTree)
	    setAreaLayout(AreaLayout.LEFT_RIGHT, treeArea, null, editArea, null); else
	    setAreaLayout(editArea, null);
    }

    boolean activateEditArea(boolean closeTree)
    {
	this.showTree = !closeTree;
	updateAreaLayout();
	app.setAreaLayout(this);
	setActiveArea(editArea);
	return true;
    }
}
