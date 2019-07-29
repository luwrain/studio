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

import java.io.*;
import java.util.*;

import com.google.gson.annotations.*;

import org.luwrain.core.*;
import org.luwrain.studio.*;

final class TexProject implements  org.luwrain.studio.Project
{
    @SerializedName("name")
    private String projName = null;

    @SerializedName("folders")
    private TexFolder rootFolder = null;

    private File projFile = null;
    private File projDir = null;
    private IDE ide = null;

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

    void finalizeLoading()
    {
	rootFolder.setProject(this);
    }

    @Override public org.luwrain.studio.RunControl run(Luwrain luwrain, org.luwrain.studio.Output output) throws IOException
    {
	NullCheck.notNull(luwrain, "luwrain");
	return null;
}

    @Override public void close(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
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

    @Override public org.luwrain.studio.Part getMainSourceFile()
    {
	return null;
    }
}
