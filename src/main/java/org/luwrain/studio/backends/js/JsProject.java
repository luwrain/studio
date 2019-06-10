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

package org.luwrain.studio.backends.js;

import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import org.luwrain.core.*;

public final class JsProject implements  org.luwrain.studio.Project
{
    private File projDir = null;
    private File projFile = null;

    @SerializedName("projname")
    private String projName = null;

    @SerializedName("appName")
    private String appName = null;

    @SerializedName("type")
    private String projType = null;

    @SerializedName("files")
    private List<String> files;

    @SerializedName("mainfile")
private String mainFile = null;

    private String previouslyLoadedExtId = null;

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

    @Override public org.luwrain.studio.RunControl run(Luwrain luwrain, org.luwrain.studio.Output output) throws IOException
    {
	NullCheck.notNull(luwrain, "luwrain");
        switch(projType)
    {
    case "simple":
	return runSimple(luwrain, output);
    case "app":
	return runApp(luwrain);
    default:
	return null;
    }
}

    private org.luwrain.studio.RunControl runSimple(Luwrain luwrain, org.luwrain.studio.Output output) throws IOException
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(output, "output");
	final String text = org.luwrain.util.FileUtils.readTextFileSingleString(new File(projFile.getParentFile(), mainFile), "UTF-8");
	final org.luwrain.core.script.Context context = new org.luwrain.core.script.Context();
	context.output = (line)->{
	    output.addLine(line);
	};
	final Callable
	callable = luwrain.runScriptInFuture(context, luwrain.getFileProperty("luwrain.dir.data"), text);
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
    }

    private org.luwrain.studio.RunControl runApp(Luwrain luwrain) throws IOException
    {
	NullCheck.notNull(luwrain, "luwrain");
	final String text = org.luwrain.util.FileUtils.readTextFileSingleString(new File(mainFile), "UTF-8");
	final Callable callable = ()->{
	    if (previouslyLoadedExtId != null && !previouslyLoadedExtId.isEmpty())
		luwrain.unloadDynamicExtension(previouslyLoadedExtId);
	    previouslyLoadedExtId = luwrain.loadScriptExtension(text);
	    if (appName != null && !appName.trim().isEmpty())
		luwrain.launchApp(appName);
	    return null;
	};
	return new org.luwrain.studio.RunControl(){
	    @Override public java.util.concurrent.Callable getCallableObj()
	    {
		return callable;
	    }
	    @Override public boolean isContinuous()
	    {
		return false;
	    }
	};
    }

    @Override public void close(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	if (previouslyLoadedExtId != null && !previouslyLoadedExtId.isEmpty())
	    luwrain.unloadDynamicExtension(previouslyLoadedExtId);
    }

            @Override public org.luwrain.studio.Folder getFoldersRoot()
    {
	return new RootFolder();
    }

    @Override public org.luwrain.studio.Flavor[] getBuildFlavors()
    {
	return new org.luwrain.studio.Flavor[0];
    }

    @Override public boolean build(org.luwrain.studio.Flavor flavor, org.luwrain.studio.Output output)
    {
	return false;
    }

    @Override public org.luwrain.studio.SourceFile getMainSourceFile()
    {
	return new JsSourceFile(new File(projDir, mainFile));
    }

    private final class RootFolder implements org.luwrain.studio.Folder
    {
	@Override public org.luwrain.studio.Folder[] getSubfolders()
	{
	    return new org.luwrain.studio.Folder[0];
	}
	@Override public org.luwrain.studio.SourceFile[] getSourceFiles()
	{
	    final List<org.luwrain.studio.SourceFile> res = new LinkedList();
	    for(String f: files)
		res.add(new JsSourceFile(new File(projDir, f)));
	    return res.toArray(new org.luwrain.studio.SourceFile[res.size()]);
	}
	@Override public boolean equals(Object o)
	{
	    		{
	    return o != null && (o instanceof RootFolder);
	}
    }
    }
}
