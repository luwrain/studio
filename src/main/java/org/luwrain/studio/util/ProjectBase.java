/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.studio.util;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import com.google.gson.annotations.SerializedName;

import org.luwrain.core.*;

public class ProjectBase
{
    private File projDir = null;
    private File projFile = null;
    private org.luwrain.studio.IDE ide = null;
    
    protected void findSourceFiles(File f, String suff) throws IOException
    {
	NullCheck.notNull(f, "f");
	NullCheck.notEmpty(suff, "suff");
	if (f.isDirectory())
	{
	    final File[] items = f.listFiles();
	    if (items != null)
		for(File i: items)
		    findSourceFiles(i, suff);
	    return;
	}
	final String name = f.getName();
	if (name.length() < suff.length() + 1 || !name.toUpperCase().endsWith(suff.toUpperCase()))
	    return;
	readSourceFile(f);
    }

    protected void readSourceFile(File f) throws IOException
    {
    }

    org.luwrain.studio.IDE getIde()
    {
	return this.ide;
    }

    File getProjectDir()
    {
	return projDir;
    }

    File getProjectFile()
    {
	return this.projFile;
}
}
