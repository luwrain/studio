/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.studio.proj.main;

import java.io.*;
import java.util.*;

import com.google.gson.*;
import com.google.gson.annotations.*;

import org.luwrain.core.*;
import org.luwrain.studio.*;

import static org.luwrain.core.NullCheck.*;

public final class ProjectImpl implements  org.luwrain.studio.Project
{
static final Gson gson = new Gson();

    private String projName = null;
    private Folder folders = null;

    private transient File projFile = null;
    private transient File projDir = null;
    private transient IDE ide = null;

    @Override public org.luwrain.studio.Part getPartsRoot() { return folders; }
    @Override public org.luwrain.studio.Part getMainSourceFile() { return null; }

    @Override public void close()
    {
	save();
    }

    public void setProjectFile(File projFile)
    {
	final File parent = projFile.getParentFile();
	if (parent == null)
	    throw new IllegalArgumentException("projFile must have the non-null parent");
	this.projDir = parent;
	this.projFile = projFile;
    }

    File getProjectDir()
    {
	if (projDir == null)
	    throw new RuntimeException("The project doesn't have any information about its project directory");
	return projDir;
    }

    private void finalizeLoading(IDE ide)
    {
	notNull(ide, "ide");
	if (folders == null)
	{
	    folders = new Folder();
	    folders.setName("Tex root");
    }
	folders.init(this, ide);
    }

    public String getProjName()
    {
	return this.projName != null?this.projName.trim():"";
    }

    public void setProjName(String projName)
    {
	notNull(projName, "projName");
	if (projName.trim().isEmpty())
	    throw new IllegalArgumentException("projName can't be empty");
	this.projName = projName.trim();
    }

    public Folder getRootFolder() {
	return this.folders;
    }

    public void setRootFolder(Folder rootFolder)
    {
	notNull(rootFolder, "rootFolder");
	this.folders = rootFolder;
    }

    public void save()
    {
	if (projFile == null)
	    throw new IllegalStateException("projFile is not set");
	try {
	    try (final BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(projFile), "UTF-8"))) {
		gson.toJson(this, w);
	    }
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
    }

    @Override public Project load(File projFile, IDE ide) throws IOException
    {
	final Gson gson = new Gson();
	try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(projFile), "UTF-8"))) {
final ProjectImpl proj = gson.fromJson(reader, ProjectImpl.class);
proj.setProjectFile(projFile);
proj.finalizeLoading(ide);
return proj;
	}
    }
}
