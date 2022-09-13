/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.studio.edit.tex;

import java.io.*;
import java.util.*;
import com.google.gson. annotations.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;

final class TexSourceFile implements Part
{
    @SerializedName("name")
    private String name = null;

    @SerializedName("path")
    private String path = null;

    private transient Project proj = null;
    private transient File projDir = null;
    private transient IDE ide = null;

    TexSourceFile()
    {
	this(null, null);
    }

    TexSourceFile(String name, String path)
    {
	this.name = name;
	this.path = path;
    }

    void init(Project proj, IDE ide)
    {
	NullCheck.notNull(proj, "proj");
	NullCheck.notNull(ide, "ide");
	this.proj = proj;
	this.ide = ide;
    }

    @Override public String getTitle()
    {
	return name != null?name:"NONAME";
    }

    @Override public Part[] getChildParts()
    {
	return new Part[0];
    }

    @Override public Editing startEditing() throws IOException
    {
	return new TexEditing(ide, new File(projDir, path));
    }

    @Override public String toString()
    {
	return getTitle();
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof TexSourceFile))
	    return false;
	final TexSourceFile f = (TexSourceFile)o;
	return path.equals(f.path);
    }

                @Override public org.luwrain.studio.Part.Action[] getActions()
    {
	return new Action[0];
    }

}
