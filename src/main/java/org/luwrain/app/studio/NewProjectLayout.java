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

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.app.base.*;

final class NewProjectLayout extends LayoutBase implements ListArea.ClickHandler
{
    private final App app;
    private final ListArea newProjectArea;

    NewProjectLayout(App app)
    {
	super(app);
	this.app = app;
	final ListArea.Params params = new ListArea.Params();
	params.context = getControlContext();
	params.model = new ListUtils.FixedModel(new ProjectFactory(app.ide).getNewProjectTypes());
	params.appearance = new ListUtils.DefaultAppearance(params.context){
		@Override public void announceItem(Object item, Set<Flags> flags)
		{
		    NullCheck.notNull(item, "item");
		    NullCheck.notNull(flags, "flags");
		    app.setEventResponse(DefaultEventResponse.listItem(app.getLuwrain().getSpeakableText(item.toString(), Luwrain.SpeakableTextType.NATURAL)));
		}
	    };
	params.clickHandler = this;
	params.name = app.getStrings().newProjectAreaName();
	this.newProjectArea = new ListArea(params);
	setAreaLayout(newProjectArea, actions());
    }

    @Override public boolean onListClick(ListArea listArea,int index,Object obj)
    {
	if (obj == null || !(obj instanceof ProjectType))
	    return false;
	final ProjectType projType = (ProjectType)obj;
	final File destDir = new File("/tmp/proba");//app.conv().newProjectDir();
	if (destDir == null)
	    return true;
	final ProjectFactory factory = new ProjectFactory(app.ide);
factory.create(projType.getId(), destDir);
	return true;
    }
}
