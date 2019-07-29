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

import org.luwrain.core.*;
import org.luwrain.controls.*;

final class PySourceFile implements org.luwrain.studio.Part
{    private final File file;

    PySourceFile(File file)
    {
	NullCheck.notNull(file, "file");
	this.file = file;
    }

    @Override public String getTitle()
    {
	return file.getName();
    }

    @Override public org.luwrain.studio.Editing startEditing()
    {
	return null;
	    }

    @Override public org.luwrain.studio.Part[] getChildParts()
    {
	return new org.luwrain.studio.Part[0];
    }


    @Override public String toString()
    {
	return file.getName();
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof PySourceFile))
	    return false;
	final PySourceFile f = (PySourceFile)o;
	return file.equals(f.file);
    }
}
