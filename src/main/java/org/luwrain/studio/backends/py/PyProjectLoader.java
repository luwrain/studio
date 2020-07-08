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

package org.luwrain.studio.backends.py;

import java.io.*;

import com.google.gson.annotations.SerializedName;
import com.google.gson.*;

import org.luwrain.studio.Project;

public final class PyProjectLoader
{
    public PyProject load(File projFile) throws IOException
    {
	final Gson gson = new Gson();
	final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(projFile)));
	final PyProject proj = gson.fromJson(reader, PyProject.class);
	proj.setProjectFile(projFile);
	proj.finalizeLoading();
	return proj;
    }
}