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

package org.luwrain.studio;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.studio.backends.NewProjectCommands;

public final class Extension extends EmptyExtension
{
    @Override public Command[] getCommands(Luwrain luwrain)
    {
	final List<Command> res = new ArrayList();

	res.add(new SimpleShortcutCommand("studio"));
	res.addAll(Arrays.asList(new NewProjectCommands().get()));
	return res.toArray(new Command[res.size()]);
    }

    @Override public ExtensionObject[] getExtObjects(Luwrain luwrain)
    {
	return new ExtensionObject[]{

	    new Shortcut() {
		@Override public String getExtObjName()
		{
		    return "studio";
		}
		@Override public Application[] prepareApp(String[] args)
		{
		    NullCheck.notNullItems(args, "args");
		    if (args.length == 1)
			return new Application[]{new org.luwrain.app.studio.App(args[0])};
		    return new Application[]{new org.luwrain.app.studio.App()};
		}
	    },

	};
    }
}
