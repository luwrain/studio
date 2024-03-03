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
import java.io.*;
import com.google.gson.*;

final class Scripts
{
    static private final Gson gson = new Gson();

    List<Script> scripts = null;

    static Scripts load(File file) throws IOException
    {
	try (final var r = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))){
	    return gson.fromJson(r, Scripts.class);
	}
    }

    static void save(File file, Scripts scripts) throws IOException
    {
	try (final var w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))){
	    gson.toJson(scripts, w);
	    w.flush();
	}
    }
}
