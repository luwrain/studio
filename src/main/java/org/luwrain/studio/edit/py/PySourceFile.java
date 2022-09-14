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

package org.luwrain.studio.edit.py;

import java.io.*;
import java.util.*;
import com.google.gson. annotations.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;

final class PySourceFile implements Part
{
    @SerializedName("name")
    private String name = null;

    @SerializedName("path")
    private String path = null;

    private transient Project proj = null;
    private transient IDE ide = null;

        void setProject(Project proj)
    {
	NullCheck.notNull(proj, "proj");
	this.proj = proj;
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
	return new PyEditing(ide, new File(""));
    }

    @Override public String toString()
    {
	return getTitle();
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof PySourceFile))
	    return false;
	final PySourceFile f = (PySourceFile)o;
	return path.equals(f.path);
    }

                @Override public org.luwrain.studio.Part.Action[] getActions()
    {
	return new Action[0];
    }

}
