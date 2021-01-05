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
import java.util.concurrent.*;

import com.google.gson.annotations.SerializedName;

import org.luwrain.core.*;

public final class Project implements  org.luwrain.studio.Project
{
    static final String LOG_COMPONENT = "studio-java";

    @SerializedName("key")
    private String key = null;

    @SerializedName("name")
    private String projName = null;

    @SerializedName("sources")
    private List<String> sources = null;

    private File projDir = null;
    private File projFile = null;
    private org.luwrain.studio.IDE ide;
    private boolean closed = false;
    private RootFolder rootFolder = null;
    private final List<SourceFile> sourceFiles = new LinkedList();
    private ExecutorService executor = Executors.newFixedThreadPool(4);

    @Override public void close()
    {
	this.closed = true;
	this.executor.shutdownNow();
    }

    void prepare(org.luwrain.studio.IDE ide, File projFile) throws IOException
    {
	NullCheck.notNull(ide, "ide");
	NullCheck.notNull(projFile, "projFile");
	this.ide = ide;
	this.projFile = projFile;
	this.projDir = projFile.getParentFile();
	if (projDir == null)
	    this.projDir = new File(".");
	if (this.projName == null || this.projName.trim().isEmpty())
	    this.projName = "Java project";
	loadSources();
	this.rootFolder = new RootFolder(this);
    }

    private void loadSources() throws IOException
    {
	for(String s: sources)
	    if (s != null && !s.isEmpty())
	{
	    readSource(new File(projDir, s));
	}
    }

    private void readSource(File f) throws IOException
    {
	NullCheck.notNull(f, "f");
	if (f.isDirectory())
	{
	    final File[] items = f.listFiles();
	    if (items != null)
		for(File i: items)
		    readSource(i);
	    return;
	}
	final String name = f.getName();
	if (name.length() < 6 || !name.toUpperCase().endsWith(".JAVA"))
	    return;
	sourceFiles.add(new SourceFile(this, f));
    }

    org.luwrain.studio.IDE getIde()
    {
	return this.ide;
    }

    boolean isClosed()
    {
	return this.closed;
    }

    File getProjectDir()
    {
	return projDir;
    }

    String getName()
    {
	return projName;
    }

    Executor getExecutor()
    {
	return this.executor;
    }

    long getPreloadFileSizeLimit()
    {
	return 102400;
    }

    SourceFile[] getSourceFiles()
    {
	return sourceFiles.toArray(new SourceFile[sourceFiles.size()]);
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
	return this.rootFolder;
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

}
