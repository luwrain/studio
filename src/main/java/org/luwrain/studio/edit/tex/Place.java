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

package org.luwrain.studio.edit.tex;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;

import static org.luwrain.core.NullCheck.*;

public final class Place implements Part
{
    static private final Logger log = LogManager.getLogger();

    final IDE ide;
    final String name;
    final TexSourceFile sourceFile;
    final int x, y;

    Place(IDE ide, String name, TexSourceFile sourceFile,
	  int x, int y)
    {
	notNull(ide, "ide");
	notNull(name, "name");
	notNull(sourceFile, "sourceFile");
	this.ide = ide;
	this.name = name;
	this.sourceFile = sourceFile;
	this.x = x;
	this.y = y;
    }

    @Override public Part[] getChildParts()
    {
	return null;
    }

    @Override public Editing startEditing() throws IOException
    {
	final var file = sourceFile.getFile();
	log.trace("Opening for editing the tex file " + file.getAbsolutePath() + " at position " + x + "," + y);
	return new TexEditing(ide, sourceFile, x, y);
    }

    @Override public String getTitle() 
    {
	return name;
    }

    @Override public String toString()
    {
	return getTitle();
    }

    @Override public boolean equals(Object o)
    {
	if (o != null && o instanceof Place p)
	    return sourceFile.equals(p.sourceFile) && name.equals(p.name) &&
	    x == p.x && y == p.y;
	return false;
    }

    @Override public Part.Action[] getActions()
    {
	return new Action[0];
    }
}
