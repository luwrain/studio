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
import org.luwrain.app.base.*;

public final class TextEditingLayout extends LayoutBase
{
    private final App app;
    private final TreeArea treeArea;
    private final EditArea editArea;
    private final NavigationArea outputArea = null;
    private final TextEditing textEditing;

    TextEditingLayout(App app, ProjectBaseLayout projectBaseLayout, TextEditing textEditing)
    {
	NullCheck.notNull(app, "app");
	NullCheck.notNull(projectBaseLayout, "projectBaseLayout");
	NullCheck.notNull(textEditing, "textEditing");
	this .app = app;
	this.textEditing = textEditing;
	    this.treeArea = projectBaseLayout.treeArea;
	    this.editArea = new EditArea(textEditing.getEditParams(new DefaultControlContext(app.getLuwrain()))) {
		    @Override public boolean onInputEvent(InputEvent event)
		    {
			NullCheck.notNull(event, "event");
			if (app.onInputEvent(this, event))
			    return true;
			return super.onInputEvent(event);
		    }
		    @Override public boolean onSystemEvent(SystemEvent event)
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
	/*
	this.outputArea = new NavigationArea(new DefaultControlContext(app.getLuwrain())) {
		final Lines outputModel = app.getOutputModel();
		@Override public boolean onInputEvent(InputEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (app.onInputEvent(this, event))
			return true;
		    return super.onInputEvent(event);
		}
		@Override public boolean onSystemEvent(SystemEvent event)
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
	*/
    }

    void activate()
    {
	    app.getLuwrain().setActiveArea(editArea);
    }

    AreaLayout getLayout()
    {
	    	return new AreaLayout(AreaLayout.LEFT_RIGHT, treeArea, editArea);
    }
}