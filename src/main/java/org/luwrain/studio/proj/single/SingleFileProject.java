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

package org.luwrain.studio.proj.single;

import java.io.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.studio.*;

import org.luwrain.studio.edit.tex.*;

public final class SingleFileProject implements Project
{
    final IDE ide;
    final File projDir, sourceFile;

    private final Part mainPart;

    public SingleFileProject(IDE ide, File sourceFile, Part mainPart)
    {
	NullCheck.notNull(ide, "ide");
	NullCheck.notNull(sourceFile, "sourceFile");
	NullCheck.notNull(mainPart, "mainPart");
	this.ide = ide;
	this.sourceFile = sourceFile;
		this.projDir = this.sourceFile.getParentFile();
	this.mainPart = mainPart;
	if (mainPart instanceof TexSourceFile)
	    ((TexSourceFile)mainPart).init(this, ide);
    }

    @Override public File getProjectDir()
    {
	return projDir;
    }

    @Override public void close()
    {
    }

            @Override public Part getPartsRoot()
    {
	return mainPart;
    }

    @Override public Part getMainSourceFile()
    {
	return mainPart;
    }

    @Override public Project load(File file, org.luwrain.studio.IDE ide) throws IOException
    {
	return null;
    }

    static public SingleFileProject newProject(IDE ide, File file)
    {
	Log.debug("studio", "trying the single file project for " + file.getName());
	if (file.getName().trim().toUpperCase().endsWith(".TEX"))
	{
	    Log.debug("studio", "tex matches");
	    final TexSourceFile sourceFile = new TexSourceFile(file.getName(), file.getAbsolutePath());
	    return new SingleFileProject(ide, file, sourceFile);
	}
	return null;
    }
}
