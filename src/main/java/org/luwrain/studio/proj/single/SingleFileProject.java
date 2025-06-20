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

import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.studio.*;

import org.luwrain.studio.edit.tex.*;

import static org.luwrain.core.NullCheck.*;

public final class SingleFileProject implements Project
{
    static private final Logger log = LogManager.getLogger();

    final IDE ide;
    final File projDir, sourceFile;

    private final Part mainPart;

    public SingleFileProject(IDE ide, File sourceFile)
    {
	notNull(ide, "ide");
	notNull(sourceFile, "sourceFile");
	this.ide = ide;
	this.sourceFile = sourceFile;
		this.projDir = this.sourceFile.getParentFile();
		if (sourceFile.getName().toUpperCase().endsWith(".TEX"))
		{
		    log.trace("Creating a single file project: dir=" + sourceFile.getParentFile().getAbsolutePath() + ", file=" + sourceFile.getName());
final var p = new TexSourceFile(sourceFile.getName(), sourceFile.getName());
p.init(this, ide);
this.mainPart = p;
		} else
		    throw new IllegalArgumentException("Unable to choose proper project type: " + sourceFile.getName());
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
}
