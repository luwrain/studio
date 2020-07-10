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
    private final String projName;
    private final ClassesRoot classesRoot;
    private final FilesRoot filesRoot;

    RootFolder(String projName)
    {
	NullCheck.notNull(projName, "projName");
	this .projName = projName;
	this.classesRoot = new ClassesRoot();
	this.filesRoot = new FilesRoot();
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
	return projName;
    }

    @Override public boolean equals(Object o)
	{
	    return o != null && (o instanceof RootFolder);
	}

	static private final class ClassesRoot implements org.luwrain.studio.Part
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
    @Override public boolean equals(Object o)
    {
	    return o != null && (o instanceof ClassesRoot);
    }
	}

    	static private final class FilesRoot implements org.luwrain.studio.Part
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
	return "Files";
    }
    @Override public boolean equals(Object o)
    {
	    return o != null && (o instanceof FilesRoot);
    }
	}
}
