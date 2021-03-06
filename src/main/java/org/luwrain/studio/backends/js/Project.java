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

package org.luwrain.studio.backends.js;

import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import org.luwrain.core.*;

public final class Project implements  org.luwrain.studio.Project
{
    private File projDir = null;
    private File projFile = null;

    @SerializedName("projname")
    private String projName = null;

    @SerializedName("appname")
    private String appName = null;

    @SerializedName("type")
    private String projType = null;

    @SerializedName("files")
    private List<String> files;

    @SerializedName("mainfile")
private String mainFile = null;

    private String previouslyLoadedExtId = null;

    @Override public void close()
    {
    }

    void setProjectFile(File projFile)
    {
	NullCheck.notNull(projFile, "projFile");
	this.projFile = projFile;
	this.projDir = projFile.getParentFile();
	if (projDir == null)
	    this.projDir = new File(".");
    }

    void finalizeLoading()
    {
	if (files == null)
	    files = new LinkedList();
	if (mainFile == null || mainFile.isEmpty())
	    mainFile = files.get(0);
	if (projName == null || projName.trim().isEmpty())
	    projName = "The project";
	if (appName == null || appName.trim().isEmpty())
	    appName = projName;
    }

            @Override public org.luwrain.studio.Part getPartsRoot()
    {
	return null;//FIXME:
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
	@Override public boolean equals(Object o)
	{
	    		{
	    return o != null && (o instanceof RootFolder);
	}
	}
			@Override public String getTitle()
			{
			    return "fixme";
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


    @Override public org.luwrain.studio.Part getMainSourceFile()
    {
	 return null;
	 }
}
