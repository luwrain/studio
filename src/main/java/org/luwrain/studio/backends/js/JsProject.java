/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import org.luwrain.core.*;

public final class JsProject implements  org.luwrain.studio.Project
{
    enum Type {SIMPLE, APP};

    private String projName = "";
    private Type projType = Type.SIMPLE;
    private File[] projFiles = new File[0];
    private File mainFile = null;
    private String appName = "";

    private String previouslyLoadedExtId = null;

    public void load(File projFile) throws IOException
    {
	NullCheck.notNull(projFile, "projFile");
	final Properties props = new Properties();
	final InputStream is = new FileInputStream(projFile);
	try {
	    props.load(is);
	}
	finally {
	    is.close();
	}
	final String type = props.getProperty("project.type");
	if (type == null)
	    throw new IOException("Project properties do not have \'project.type\' value");
	switch (type.trim())
	{
	    case "js":
		this.projType = Type.SIMPLE;
		break;
	case "js-app":
	    this.projType = Type.APP;
	    break;
	default:
	    throw new IOException("Illegal project type \'" + type + "\', expecting \'js\'");
	}
	final String name = props.getProperty("project.name");
	if (name == null)
	    throw new IOException("Project properties do not have \'project.name\' value");
	if (name.trim().isEmpty())
	    throw new IOException("Project name may not be empty");
	this.projName = name;
	if (this.projType == Type.APP)
	{
	this.appName = props.getProperty("project.appname");
	if (this.appName == null)
	    this.appName = "";
	this.appName = this.appName.trim();
	}
	final String mainFile = props.getProperty("files.main");
	if (mainFile != null)
	{
	    final File parent = projFile.getParentFile();
	    if (parent != null)
		this.mainFile = new File(parent, mainFile); else
		this.mainFile = new File(mainFile);
	    projFiles = new File[]{this.mainFile};
	}
    }

    @Override public org.luwrain.studio.RunControl run(Luwrain luwrain, org.luwrain.studio.Output output) throws IOException
    {
	NullCheck.notNull(luwrain, "luwrain");
        switch(projType)
    {
    case SIMPLE:
	return runSimple(luwrain, output);
    case APP:
	return runApp(luwrain);
    default:
	return null;
    }
}

    private org.luwrain.studio.RunControl runSimple(Luwrain luwrain, org.luwrain.studio.Output output) throws IOException
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(output, "output");
	final String text = org.luwrain.util.FileUtils.readTextFileSingleString(mainFile, "UTF-8");
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
	    @Override public boolean isSuitableForBackground()
	    {
		return true;
	    }
	};
    }

    private org.luwrain.studio.RunControl runApp(Luwrain luwrain) throws IOException
    {
	NullCheck.notNull(luwrain, "luwrain");
	final String text = org.luwrain.util.FileUtils.readTextFileSingleString(mainFile, "UTF-8");
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
	    @Override public boolean isSuitableForBackground()
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

    private final class RootFolder implements org.luwrain.studio.Folder
    {
	@Override public org.luwrain.studio.Folder[] getSubfolders()
	{
	    return new org.luwrain.studio.Folder[0];
	}
	@Override public org.luwrain.studio.SourceFile[] getSourceFiles()
	{
	    Log.debug("proba", "requesting source files");
	    final List<org.luwrain.studio.SourceFile> res = new LinkedList();
	    for(File f: projFiles)
		res.add(new JsSourceFile(f));
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
