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

public final class TextEditingLayout extends LayoutBase
{
    private final App app;
    final TreeArea treeArea;
    final EditArea editArea;
    final NavigationArea outputArea = null;
    private final TextEditing textEditing;

    TextEditingLayout(App app, ProjectBaseLayout projectBaseLayout, TextEditing textEditing)
    {
	super(app);
	NullCheck.notNull(projectBaseLayout, "projectBaseLayout");
	NullCheck.notNull(textEditing, "textEditing");
	this .app = app;
	this.textEditing = textEditing;
	this.treeArea = projectBaseLayout.treeArea;
	this.editArea = new EditArea(textEditing.getEditParams(getControlContext())) {
				@Override public boolean onSystemEvent(SystemEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.getType() == SystemEvent.Type.REGULAR)
			switch(event.getCode())
			{
			case SAVE:
			    return onSave();
			}
		    return super.onSystemEvent(event);
		}
	    };
	final Actions editActions = actions();
	setAreaLayout(AreaLayout.LEFT_RIGHT, treeArea, null, editArea, editActions);
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
}
