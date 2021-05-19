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

public final class ProjectBaseLayout extends LayoutBase implements TreeArea.ClickHandler
{
    private final App app;
    final TreeArea treeArea;

    ProjectBaseLayout(App app)
    {
	super(app);
	this .app = app;
	final TreeArea.Params params = new TreeArea.Params();
	params.context = getControlContext();
	params.model = new CachedTreeModel(new TreeModel());
	params.name = app.getStrings().treeAreaName();
	params.clickHandler = this;
	this.treeArea = new TreeArea(params){
		private Part.Action[] actionsCache = null;
		@Override public boolean onSystemEvent(SystemEvent event)
		{
		    NullCheck.notNull(event, "event");
		    return super.onSystemEvent(event);
		}
		@Override public Action[] getAreaActions()
		{
		    this.actionsCache = getPartActions();
		    if (actionsCache != null)
			return getActions(this.actionsCache);
		    return new Action[0];
		    		}
	    };
	setAreaLayout(treeArea, null);
    }

    public Part.Action[] getPartActions()
    {
	final Object obj = treeArea.selected();
	if (obj == null || !(obj instanceof Part))
	    return null;
	    final Part part = (Part)obj;
return part.getActions();
    }

    private Action[] getActions(Part.Action[] partActions)
    {
	NullCheck.notNullItems(partActions, "partActions");
	    if (partActions == null)
		return null;
	    final List<Action> res = new ArrayList();
	    int k = 1;
	    for(Part.Action a: partActions)
		res.add(new Action("action" + k++, a.getTitle()));
	    return res.toArray(new Action[res.size()]);
    }

    @Override public boolean onTreeClick(TreeArea treeArea, Object obj)
    {
	NullCheck.notNull(treeArea, "treeArea");
	if (obj == null || !(obj instanceof Part))
	    return false;
	final Part part = (Part)obj;
	try {
	    final Editing editing = part.startEditing();
	    if (editing == null)
		return false;
	    app.startEditing(editing);
	    return true;
	}
	catch(IOException e)
	{
	    app.getLuwrain().crash(e);
	    return true;
	}
    }

    private final class TreeModel implements CachedTreeModelSource
    {
	@Override public Object getRoot()
	{
	    if (app.getProject() == null)
		return app.getStrings().treeRoot();
	    return app.getProject().getPartsRoot();
	}
	@Override public Object[] getChildObjs(Object obj)
	{
	    NullCheck.notNull(obj, "obj");
	    if (app.getProject() == null || !(obj instanceof Part))
		return new Object[0];
	    final Part part = (Part)obj;
	    final Part[] res = part.getChildParts();
	    if (res == null)
		return new Object[0];
	    for(int i = 0;i < res.length;i++)
		if (res[i] == null)
		    return new Object[0];
	    return res;
	}
    }
}
