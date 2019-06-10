/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.studio.backends.tex;

import java.io.*;
import java.util.*;

import com.google.gson.*;

import org.luwrain.core.*;
import org.luwrain.studio.*;

public final class TexProjectLoader
{
    public Project load(File projFile) throws IOException
    {
	final Gson gson = new Gson();
	final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(projFile)));
final TexProject proj = gson.fromJson(reader, TexProject.class);
proj.setProjectFile(projFile);
proj.finalizeLoading();
return proj;
    }
}
