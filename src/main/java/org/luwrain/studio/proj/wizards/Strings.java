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

package org.luwrain.studio.proj.wizards;

import org.luwrain.core.annotations.*;

@ResourceStrings(name = "luwrain.studio.wizards", resource = "strings.properties", langs = { "en", "ru" })
public interface Strings
{
    static public final String
	NAME = "luwrain.studio.wizards";

    String next();
    String titleCannotBeEmpty();

    String texPresentationTitle();
        String texPresentationGreeting();
    String texPresentationInputTitle();
    String texPresentationInputAuthor();
    String texPresentationInputDate();
}
