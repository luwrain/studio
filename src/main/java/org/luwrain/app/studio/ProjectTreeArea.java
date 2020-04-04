/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>

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
import org.luwrain.template.*;

public final class ProjectTreeArea extends TreeArea
{
    private final App app;

    ProjectTreeArea(App app)
    {
	super(createParams(app));
	NullCheck.notNull(app, "app");
	this .app = app;
    }

    		@Override public boolean onInputEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (app.onInputEvent(this, event))
			return true;
		    return super.onInputEvent(event);
		}
    
		@Override public boolean onSystemEvent(EnvironmentEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (app.onSystemEvent(this, event))
			return true;
		    return super.onSystemEvent(event);
		}
    
		@Override public Action[] getAreaActions()
		{
		    return new Action[0];
		}

        static private TreeArea.Params createParams(App app)
    {
	NullCheck.notNull(app, "app");
	final TreeArea.Params params = new TreeArea.Params();
	params.context = new DefaultControlContext(app.getLuwrain());
	params.model = new CachedTreeModel(new TreeModel(app));
	params.name = app.getStrings().treeAreaName();
	params.clickHandler = (treeArea, obj)->{
	NullCheck.notNull(treeArea, "treeArea");
	    if (obj == null || !(obj instanceof Part))
		return false;
	    final Part part = (Part)obj;
	    final Editing editing;
	    try {
	    editing = part.startEditing();
	    }
	    catch(IOException e)
	    {
		app.getLuwrain().message(app.getLuwrain().i18n().getExceptionDescr(e));
		return true;
	    }
	    if (editing == null)
		return false;
	    //	    return layouts.editing(editing);
	return false;
	};
	return params;
    }

    static private final class TreeModel implements CachedTreeModelSource
    {
	private final App app;
	TreeModel(App app)
	{
	    NullCheck.notNull(app, "app");
	    this.app = app;
	}
	@Override public Object getRoot()
	{
	    return app.getTreeRoot();
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
