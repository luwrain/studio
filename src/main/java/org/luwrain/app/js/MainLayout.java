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

package org.luwrain.app.js;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;

import static org.luwrain.controls.edit.EditUtils.*; 

final class MainLayout extends LayoutBase
{
    private final App app;
    final EditArea editArea;

    private Script script = null;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	this.script = app.getScripts().scripts.get(0);
	if (this.script.text == null)
	    this.script.text = Arrays.asList();
	this.editArea = new EditArea(editParams((params)->{
		    params.appearance = new DefaultEditAreaAppearance(getControlContext(), Luwrain.SpeakableTextType.PROGRAMMING);
		})){
		@Override public boolean onSystemEvent(org.luwrain.core.events.SystemEvent event)
		{
		    if (event.getType() == SystemEvent.Type.REGULAR)
			switch(event.getCode())
			{
			case SAVE:
			    return save();
			}
		    return super.onSystemEvent(event);
		}
	    };
	this.editArea.setText(this.script.text.toArray(new String[this.script.text.size()]));
	setAreaLayout(editArea, actions(
					action("run", "Исполнить", new InputEvent(InputEvent.Special.F9), this::run)
					));
    }

    private boolean run()
    {
	final var s = new ScriptText(Arrays.asList(editArea.getText()).stream().reduce("", (a, b)->{ return a + System.lineSeparator() + b; }));
	final String id;
	try {
	    id = getLuwrain().loadScript(s);
	}
	catch(ExtensionException e)
	{
	    if (e.getCause() != null && e.getCause() instanceof org.graalvm.polyglot.PolyglotException p)
		app.crash(p); else
		app.crash(e);
	    return true;
	}
	Log.debug("proba", id);
	return true;
    }

    private boolean save()
    {
	this.script .text = Arrays.asList(editArea.getText());
	app.save();
	app.message("Сохранено", Luwrain.MessageType.OK);
	return true;
    }
}
