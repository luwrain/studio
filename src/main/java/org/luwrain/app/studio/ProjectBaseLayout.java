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
import org.luwrain.studio.*;
import org.luwrain.app.base.*;

public final class ProjectBaseLayout extends LayoutBase implements TreeListArea.LeafClickHandler<Part>
{
    static final InputEvent
	KEY_TREE_TOGGLE = new InputEvent(InputEvent.Special.F5);

    private final App app;
    final TreeListArea treeArea;

    ProjectBaseLayout(App app)
    {
	super(app);
	this .app = app;
	final var params = new TreeListArea.Params<Part>();
	params.context = getControlContext();
	params.model = new ProjectTreeModel(app);
	params.name = app.getStrings().treeAreaName();
	params.leafClickHandler = this;
	this.treeArea = new TreeListArea<>(params){
		@Override public boolean onInputEvent(InputEvent event)
		{
		    //Switching to the edit area
		    if (event.equals(KEY_TREE_TOGGLE))
		    {
			if (app.getTextEditingLayout() == null)
			    return false;
			    app.getTextEditingLayout().activateEditArea(true);
			    return true;
			}
		    return super.onInputEvent(event);
		}
		@Override public boolean onSystemEvent(SystemEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.getType() == SystemEvent.Type.REGULAR && event.getCode() == SystemEvent.Code.ACTION)
		    {
			final ActionEvent actionEvent = (ActionEvent)event;
			final String actionName = actionEvent.getActionName();
					    final Object obj = treeArea.selected();
		    if (obj == null || !(obj instanceof Part))
			return false;
		    final Part part = (Part)obj;
		    final Part.Action[] actions = part.getActions();
		    for(Part.Action a: actions)
			if (a.getId().equals(actionName))
			    return a.onAction(app.ide);
		    return false;
		    }
		    return super.onSystemEvent(event);
		}
		@Override public Action[] getAreaActions()
		{
		    final Object obj = treeArea.selected();
		    if (obj == null || !(obj instanceof Part))
			return new Action[0];
		    final Part part = (Part)obj;
		    final Part.Action[] actions = part.getActions();
		    final List<Action> res = new ArrayList<>();
		    for(Part.Action a: actions)
		    {
			final Action action;
						if (a.getHotKey() != null)
						    action = new Action(a.getId(), a.getTitle(), a.getHotKey()); else
						    action = new Action(a.getId(), a.getTitle());
			res.add(action);
		    }
		    return res.toArray(new Action[res.size()]);
		}
	    };
	setAreaLayout(treeArea, null);
    }

    @Override public boolean onLeafClick(TreeListArea<Part> area, Part part)
    {
	try {
	    final Editing editing = part.startEditing();
	    if (editing == null)
		return false;
	    app.startEditing(editing);
	    return true;
	}
	catch(IOException e)
	{
	    app.crash(e);
	    return true;
	}
    }

    protected boolean activateEditArea(boolean closeTree)
    {
	return false;
    }
}
