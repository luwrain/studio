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

package org.luwrain.studio.backends.py;

import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import org.luwrain.core.*;

public final class PyProject implements  org.luwrain.studio.Project
{
    private File projDir = null;
    private File projFile = null;

    @SerializedName("name")
    private String projName = null;

    @SerializedName("folders")
    private PyFolder rootFolder = null;

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
	if (rootFolder != null)
	    rootFolder.setProject(this);
	if (projName == null || projName.trim().isEmpty())
	    projName = "The project";
    }

    File getProjectDir()
    {
	return projDir;
    }

    @Override public org.luwrain.studio.RunControl run(Luwrain luwrain, org.luwrain.studio.Output output) throws IOException
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(output, "output");
	/*
	final String text = org.luwrain.util.FileUtils.readTextFileSingleString(new File(projFile.getParentFile(), mainFile), "UTF-8");
	final org.luwrain.core.script.Context context = new org.luwrain.core.script.Context();
	context.output = (line)->{
	    output.addLine(line);
	};
	final Callable callable = luwrain.runScriptInFuture(context, luwrain.getFileProperty("luwrain.dir.data"), text);
	return new org.luwrain.studio.RunControl(){
	    @Override public java.util.concurrent.Callable getCallableObj()
	    {
		return callable;
	    }
	    @Override public boolean isContinuous()
	    {
		return true;
	    }
	};
	*/
	return null;
    }

            @Override public org.luwrain.studio.Part getPartsRoot()
    {
	return rootFolder;
    }

    @Override public org.luwrain.studio.Flavor[] getBuildFlavors()
    {
	return new org.luwrain.studio.Flavor[0];
    }

    @Override public boolean build(org.luwrain.studio.Flavor flavor, org.luwrain.studio.Output output)
    {
	return false;
}


    @Override public void close(Luwrain luwrain)
{
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
    }
}
