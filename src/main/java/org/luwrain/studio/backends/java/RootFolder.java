/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>

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

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.studio.*;

final class RootFolder implements org.luwrain.studio.Part
{
    private final Project proj;
    private final ClassesRoot classesRoot;
    private final FilesRoot filesRoot;
    private SourceFile[] sourceFiles = new SourceFile[0];

    RootFolder(Project proj)
    {
	NullCheck.notNull(proj, "proj");
	this .proj = proj;
	this.classesRoot = new ClassesRoot();
	this.filesRoot = new FilesRoot();
	this.sourceFiles = proj.getSourceFiles();
    }

    @Override public org.luwrain.studio.Editing startEditing()
    {
	return null;
    }

    @Override public org.luwrain.studio.Part[] getChildParts()
    {
	return new Part[]{classesRoot, filesRoot};
    }

    @Override public String getTitle()
    {
	return proj.getName();
    }

    @Override public String toString()
    {
	return getTitle();
    }

    @Override public boolean equals(Object o)
	{
	    return o != null && (o instanceof RootFolder);
	}

    private final class ClassesRoot implements org.luwrain.studio.Part
{
    @Override public org.luwrain.studio.Editing startEditing()
    {
	return null;
    }
    @Override public org.luwrain.studio.Part[] getChildParts()
    {
	return new Part[0];
    }
    @Override public String getTitle()
    {
	return "Classes";
    }
        @Override public String toString()
    {
	return getTitle();
    }
        @Override public boolean equals(Object o)
    {
	    return o != null && (o instanceof ClassesRoot);
    }
	}

    	private final class FilesRoot implements org.luwrain.studio.Part
{
    @Override public org.luwrain.studio.Editing startEditing()
    {
	return null;
    }
    @Override public org.luwrain.studio.Part[] getChildParts()
    {
	return sourceFiles;
    }
    @Override public String getTitle()
    {
	return "Files";
    }
        @Override public String toString()
    {
	return getTitle();
    }
        @Override public boolean equals(Object o)
    {
	    return o != null && (o instanceof FilesRoot);
    }
	}
}
