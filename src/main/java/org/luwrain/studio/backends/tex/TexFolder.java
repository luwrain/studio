/*
   Copyright 2012-2019 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

final class TexFolder implements Folder
{
        @SerializedName("name")
    private String name = "";

    @SerializedName("subfolders")
    private List<TexFolder> subfolders = null;
    
    @Override public Folder[] getSubfolders()
    {
	if (subfolders == null)
	    return new Folder[0];
	return subfolders.toArray(new Folder[subfolders.size()]);
    }
    
    @Override public SourceFile[] getSourceFiles()
    {
	return new SourceFile[0];
    }

    @Override public String toString()
    {
	return name;
    }
}


