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

package org.luwrain.studio.backends.java;

import java.io.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.studio.*;

final class SourceFile implements Part
{
            private final Project proj;
    private final File file;

    SourceFile(Project proj, File file)
    {
	NullCheck.notNull(proj, "proj");
	NullCheck.notNull(file, "file");
	this.proj = proj;
	this.file = file;
    }

    @Override public String getTitle()
    {
	return file.getName();
    }

    @Override public Part[] getChildParts()
    {
	return new Part[0];
    }

    @Override public Editing startEditing() throws IOException
    {
	return new Editing(file);
    }

    @Override public String toString()
    {
	return getTitle();
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof SourceFile))
	    return false;
	final SourceFile f = (SourceFile)o;
	return file.equals(f.file);
    }
}
