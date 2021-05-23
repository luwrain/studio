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

package org.luwrain.studio.backends.ly;

import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import org.luwrain.core.*;
import org.luwrain.studio.util.*;

final class Project extends ProjectBase implements  org.luwrain.studio.Project
{
    @SerializedName("name")
    private String projName = null;

    @SerializedName("sources")
    private List<String> sources = null;

    private LyFolder rootFolder = null;

    public void init(org.luwrain.studio.IDE ide, File projFile) throws IOException
    {
	NullCheck.notNull(ide, "ide");
	NullCheck.notNull(projFile, "projFile");
	super.initBase(ide, projFile);
    }

        @Override protected void readSourceFile(File f) throws IOException
    {
    }

        @Override public void close()
    {
    }

            @Override public org.luwrain.studio.Part getPartsRoot()
    {
	return rootFolder;
    }

    @Override public org.luwrain.studio.Part getMainSourceFile()
    {
	return null;
    }

    private final class RootFolder implements org.luwrain.studio.Part
    {
	@Override public org.luwrain.studio.Editing startEditing()
	{
	    return null;
	}
	@Override public org.luwrain.studio.Part[] getChildParts()
	{
	    return new org.luwrain.studio.Part[0];
	}
	@Override public String getTitle()
	{
	    return "kaka";
	}
	@Override public boolean equals(Object o)
	{
	    		{
	    return o != null && (o instanceof RootFolder);
	}
    }
	            @Override public org.luwrain.studio.Part.Action[] getActions()
    {
	return new Action[0];
    }

    }

        @Override public Project load(File file) throws IOException
    {
	return null;
    }

    
}
