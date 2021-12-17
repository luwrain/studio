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

package org.luwrain.studio.backends.tex;

import java.io.*;
import java.util.*;

import com.google.gson.*;
import com.google.gson.annotations.*;

import org.luwrain.core.*;
import org.luwrain.studio.*;

public final class TexProject implements  org.luwrain.studio.Project
{
    static public final String KEY = "---LUWRAIN-PROJECT-TEX---";

    @SerializedName("name")
    private String projName = null;

        @SerializedName("projKey")
    private String key = KEY;

    @SerializedName("folders")
    private TexFolder rootFolder = null;

    private transient File projFile = null;
    private transient File projDir = null;
    private transient IDE ide = null;
    private transient Strings strings = null;
    private transient final Gson gson = new Gson();

                @Override public org.luwrain.studio.Part getPartsRoot() { return rootFolder; }
    @Override public org.luwrain.studio.Part getMainSourceFile() { return null; }
    Strings getStrings() { return strings; }

    @Override public void close()
    {
	save();
    }

    void setProjectFile(File projFile)
    {
	final File parent = projFile.getParentFile();
	if (parent == null)
	    throw new IllegalArgumentException("projFile must have the not-null parent");
	this.projDir = parent;
	this.projFile = projFile;
    }

    File getProjectDir()
    {
	if (projDir == null)
	    throw new RuntimeException("The project does not have any project directory information");
	return projDir;
    }

    private void finalizeLoading(IDE ide)
    {
	NullCheck.notNull(ide, "ide");
	if (rootFolder == null)
	{
	    rootFolder = new TexFolder();
	    rootFolder.setName("Tex root");
    }
	rootFolder.init(this);
	this.strings = (Strings)ide.getLuwrainObj().i18n().getStrings(Strings.NAME);
	NullCheck.notNull(strings, "strings");
    }


    public String getProjName()
    {
	return this.projName != null?this.projName.trim():"";
    }

    public void setProjName(String projName)
    {
	NullCheck.notNull(projName, "projName");
	if (projName.trim().isEmpty())
	    throw new IllegalArgumentException("projName can't be empty");
	this.projName = projName.trim();
    }

    public TexFolder getRootFolder()
    {
	return this.rootFolder;
    }

    public void setRootFolder(TexFolder rootFolder)
    {
	NullCheck.notNull(rootFolder, "rootFolder");
	this.rootFolder = rootFolder;
    }

    void save()
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
	NullCheck.notNull(projFile, "projFile");
	NullCheck.notNull(ide, "ide");
	final Gson gson = new Gson();
	try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(projFile), "UTF-8"))) {
final TexProject proj = gson.fromJson(reader, TexProject.class);
proj.setProjectFile(projFile);
proj.finalizeLoading(ide);
return proj;
	}
    }

}
