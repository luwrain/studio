/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

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

final class NewProjectLayout extends LayoutBase implements ListArea.ClickHandler<ProjectType>
{
    private final App app;
    private final ListArea newProjectArea;

    NewProjectLayout(App app)
    {
	super(app);
	this.app = app;
	final ListArea.Params<ProjectType> params = new ListArea.Params<>();
	params.context = getControlContext();
	params.model = new ListUtils.FixedModel<>(new ProjectFactory(app.ide, app.getStrings()).getNewProjectTypes());
	params.appearance = new ListUtils.DefaultAppearance<ProjectType>(params.context){
		@Override public void announceItem(ProjectType item, Set<Flags> flags)
		{
		    app.setEventResponse(DefaultEventResponse.listItem(app.getLuwrain().getSpeakableText(item.toString(), Luwrain.SpeakableTextType.NATURAL)));
		}
	    };
	params.clickHandler = this;
	params.name = app.getStrings().newProjectAreaName();
	this.newProjectArea = new ListArea<>(params);
	setAreaLayout(newProjectArea, actions());
    }

    @Override public boolean onListClick(ListArea listArea, int index, ProjectType projType)
    {
	//    	final File destDir = app.getConv().newProjectDir();
	final File destDir = new File("/x/proj");
	if (destDir == null)
	    return true;
	final ProjectFactory factory = new ProjectFactory(app.ide, app.getStrings());
	factory.create(projType.getId(), destDir);
	return true;
    }
}
