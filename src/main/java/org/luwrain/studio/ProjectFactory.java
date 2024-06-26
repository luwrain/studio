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

package org.luwrain.studio;

import java.io.*;

import org.luwrain.core.*;
import org.luwrain.util.*;

import org.luwrain.studio.backends.tex.TexProject;
import org.luwrain.studio.proj.wizards.*;

import static org.luwrain.studio.ProjectType.*;
import static org.luwrain.core.NullCheck.*;

public final class ProjectFactory
{
    private final IDE ide;
    private final Luwrain luwrain;

    public ProjectFactory(IDE ide)
    {
	notNull(ide, "ide");
	this.ide = ide;
	this.luwrain = ide.getLuwrainObj();
    }

    public ProjectType[] getNewProjectTypes()
    {
	return new ProjectType[]{
	    new ProjectType("latex-presentation", 0, "Презентация TeX"),
	    	    new ProjectType(TEX_ARTICLE, 0, "Статья TeX"),
	    new ProjectType("lilypond-piano", 0, "Фортепианная пьеса Lilypond"),
	};
    }

    /*
    public Project load(File projFile) throws IOException
    {
	final Project loader = readProjectKey(projFile);
	if (loader == null)
	    throw new IOException("No known keys in the file");
	final Project proj = loader.load(projFile, ide);
	if (proj == null)
	    throw new IOException(projFile.getPath() + " doesn't contain proper  project structure");
	return proj;
    }
    */

    private Project readProjectKey(File projFile) throws IOException
    {
	final String text = FileUtils.readTextFileSingleString(projFile, "UTF-8");
	if (text.contains(TexProject.KEY))
	    return new TexProject();
	return null;
    }

    public void create(String projType, File destDir)
    {
	try {
	switch(projType)
	{
	case TEX_PRESENTATION: {
	    final TexPresentation w = new TexPresentation(ide, destDir);
	    ide.showWizard(w);
	    luwrain.announceActiveArea();
	    return;
	}
	    	case TEX_ARTICLE: {
		    final var w = new ProjectWizard(ide, destDir, "newLatexArticle.groovy");
	    ide.showWizard(w);
	    luwrain.announceActiveArea();
	    return;
	}
	default:
	    throw new IllegalArgumentException("Unknown project type: " + projType);
	}
    }
    catch(IOException e)
    {
	throw new RuntimeException(e);
    }
    }
}
