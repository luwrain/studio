/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>

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

import com.google.gson.annotations.SerializedName;

import org.luwrain.core.*;
import org.luwrain.studio.*;

final class Folder implements org.luwrain.studio.Part
{
    @SerializedName("name")
    private String name = "";

    @SerializedName("subfolders")
    private List<Folder> subfolders = null;

    @SerializedName("sourceFiles")
    private List<SourceFile> sourceFiles = null;

    private Project proj = null;

    void setProject(Project proj)
    {
	NullCheck.notNull(proj, "proj");
	this.proj = proj;
	if (name == null)
	    name = "-";
	if (subfolders == null)
	    subfolders = new LinkedList();
	if (sourceFiles == null)
	    sourceFiles = new LinkedList();
	for(Folder f: subfolders)
	    f.setProject(proj);
    }

    @Override public org.luwrain.studio.Editing startEditing()
    {
	return null;
    }

    @Override public org.luwrain.studio.Part[] getChildParts()
    {
	final List<Part> res = new LinkedList();
	for(Folder f: subfolders)
	    res.add(f);
	for(SourceFile f: sourceFiles)
	    res.add(f);
	return res.toArray(new Part[res.size()]);
    }

    @Override public String getTitle()
    {
	return name;
    }

    @Override public String toString()
    {
	return getTitle();
    }

    @Override public boolean equals(Object o)
    {
	return o != null && (o instanceof Folder);
    }
}
