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

final class MainLayout extends LayoutBase
{
    private final App app;
    final EditArea editArea;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	this.editArea = new EditArea(editParams((params)->{
		}));
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
}
