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

package org.luwrain.studio;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.i18n.*;

import static org.luwrain.i18n.Lang.*;

public final class Extension extends EmptyExtension
{
    static final String
	LOG_COMPONENT = "studio";

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

        @Override public void i18nExtension(Luwrain luwrain, org.luwrain.i18n.I18nExtension i18n)
    {
	try {
	    i18n.addStrings(RU, org.luwrain.studio.edit.tex.Strings.NAME, new ResourceStringsObj(luwrain, getClass().getClassLoader(), getClass(), "edit-tex.properties").create(RU, org.luwrain.studio.edit.tex.Strings.class));
	    	    i18n.addStrings(RU, org.luwrain.studio.proj.wizards.Strings.NAME, new ResourceStringsObj(luwrain, getClass().getClassLoader(), getClass(), "wizards.properties").create(RU, org.luwrain.studio.proj.wizards.Strings.class));
		    	    i18n.addStrings(RU, org.luwrain.app.js.Strings.NAME, new ResourceStringsObj(luwrain, getClass().getClassLoader(), getClass(), "js.properties").create(RU, org.luwrain.app.js.Strings.class));
	}
	catch(java.io.IOException e)
	{
	    Log.error(LOG_COMPONENT, "unable to init i18n: " + e.getClass().getName() + ": " + e.getMessage());
	}
    }
}
