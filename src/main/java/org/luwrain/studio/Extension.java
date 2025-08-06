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

package org.luwrain.studio;

import com.google.auto.service.*;

import org.luwrain.core.*;

@AutoService(org.luwrain.core.Extension.class)
public final class Extension extends EmptyExtension
{
    @Override public Command[] getCommands(Luwrain luwrain)
    {
	return new Command[]{
	    new SimpleShortcutCommand("studio"),
			    new SimpleShortcutCommand("js"),
		};
    }

    @Override public ExtensionObject[] getExtObjects(Luwrain luwrain)
    {
	return new ExtensionObject[]{
	    new Shortcut() {
		@Override public String getExtObjName() { return "studio"; }
		@Override public Application[] prepareApp(String[] args)
		{
		    if (args.length == 1)
			return new Application[]{new org.luwrain.app.studio.App(args[0])};
		    return new Application[]{new org.luwrain.app.studio.App()};
		}
	    },
	    new SimpleShortcut("js", org.luwrain.app.js.App.class)
	};
    }
}
