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

public final class MainLayout extends LayoutBase
{
    private final App app;
    private final ProjectTreeArea projectTreeArea;
    private EditArea editArea;
    private NavigationArea outputArea = null;

    private final Editing editing;

    MainLayout(App app, ProjectTreeArea projectTreeArea, Editing editing)
    {
	NullCheck.notNull(app, "app");
	NullCheck.notNull(projectTreeArea, "projectTreeArea");
	this .app = app;
			this.editing = editing;
 	this.projectTreeArea = projectTreeArea;
	if (editing != null && (editing instanceof TextEditing))
	{
	    final TextEditing textEditing = (TextEditing)editing;
	    this.editArea = new EditArea(textEditing.getEditParams(new DefaultControlContext(app.getLuwrain()))) {
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
			return null;
		    }
		};
	} else
	    editArea = null;
	this.outputArea = new NavigationArea(new DefaultControlContext(app.getLuwrain())) {
		final Lines outputModel = app.getOutputModel();
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
		    return app.getStrings().outputAreaName();
		}
	    };
    }

    MainLayout(App app, ProjectTreeArea projectTreeArea)
    {
	this(app, projectTreeArea, null);
    }

    AreaLayout getLayout()
    {
	if (editArea == null)
	    return new AreaLayout(projectTreeArea);
	return new AreaLayout(AreaLayout.LEFT_RIGHT, projectTreeArea, editArea);
    }
}
