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

import org.luwrain.studio.*;

import org.luwrain.core.*;

public class ClassPart implements Part
{
    protected final String name;

    public ClassPart(String name)
    {
	NullCheck.notNull(name, "name");
	this.name = name;
    }

    public String getName()
    {
	return name;
    }

    @Override public String getTitle()
    {
	return getName();
    }

    @Override public Part[] getChildParts()
    {
	return new Part[0];
    }

    @Override public Editing startEditing() throws IOException
    {
	return null;
    }

    @Override public String toString()
    {
	return name;
    }

    @Override public Action[] getActions()
    {
	return new Action[0];
    }
}
