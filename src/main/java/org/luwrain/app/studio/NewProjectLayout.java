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

final class NewProjectLayout extends LayoutBase implements ListArea.ClickHandler
{
    private final App app;
    private final ListArea newProjectArea;

    NewProjectLayout(App app)
    {
	NullCheck.notNull(app, "app");
	this.app = app;
	this.newProjectArea = new ListArea(createParams()){
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
		@Override public boolean onAreaQuery(AreaQuery query)
		{
		    NullCheck.notNull(query, "query");
		    if (app.onAreaQuery(this, query))
			return true;
		    return super.onAreaQuery(query);
		}
	    };
    }

    @Override public boolean onListClick(ListArea listArea,int index,Object obj)
    {
	if (obj == null || !(obj instanceof ProjectType))
	    return false;
	final ProjectType projType = (ProjectType)obj;
	final File destDir = app.conv().newProjectDir();
	if (destDir == null)
	    return true;
	final ProjectFactory factory = new ProjectFactory(app.getIde());
	final Project proj;
	try {
	    proj = factory.create(projType.getId(), destDir);
	}
	catch(IOException e)
	{
	    app.getLuwrain().crash(e);
	    return true;
	}
	if (proj != null)
	    app.activateProject(proj);
	return true;
    }

    AreaLayout getLayout()
    {
	return new AreaLayout(newProjectArea);
    }

    private ListArea.Params createParams()
    {
	final ListArea.Params params = new ListArea.Params();
	params.context = new DefaultControlContext(app.getLuwrain());
	params.model = new ListUtils.FixedModel(new ProjectFactory(app.getIde()).getNewProjectTypes());
	params.appearance = new ListUtils.DefaultAppearance(params.context){
		@Override public void announceItem(Object item, Set<Flags> flags)
		{
		    NullCheck.notNull(item, "item");
		    NullCheck.notNull(flags, "flags");
		    app.getLuwrain().setEventResponse(DefaultEventResponse.listItem(app.getLuwrain().getSpeakableText(item.toString(), Luwrain.SpeakableTextType.NATURAL)));
		}
	    };
	params.clickHandler = this;
	params.name = app.getStrings().newProjectAreaName();
	return params;
    }
}
