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

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.*;

import org.luwrain.core.*;
import org.luwrain.studio.*;

final class TexFolder implements Part
{
        @SerializedName("name")
    private String name = "";

    @SerializedName("subfolders")
    private List<TexFolder> subfolders = null;

    private TexProject proj = null;

    void setProject(TexProject proj)
    {
	NullCheck.notNull(proj, "proj");
	this.proj = proj;
	if (subfolders != null)
	    for(TexFolder f: subfolders)
		f.setProject(proj);
		if (sourceFiles != null)
	    for(TexSourceFile2 f: sourceFiles)
		f.setProject(proj);
    }

        @SerializedName("files")
    private List<TexSourceFile2> sourceFiles = null;

    @Override public Part [] getChildParts()
    {
	if (subfolders == null)
	    return new Part[0];
	return subfolders.toArray(new Part[subfolders.size()]);
    }

    @Override public Editing startEditing()
    {
	return null;
    }

    @Override public String getTitle()
    {
	return name;
    }

    @Override public String toString()
    {
	return name;
    }
}


