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

package org.luwrain.studio;

import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.script2.*;
import org.luwrain.script2.hooks.*;
import org.luwrain.util.*;

import org.luwrain.studio.backends.tex.TexProject;
import org.luwrain.studio.backends.tex.TexPresentationWizard;

import org.luwrain.studio.backends.ly.LyProject;
import org.luwrain.studio.backends.ly.LyPianoWizard;

public final class ProjectFactory
{
    static public final String
	TYPES_LIST_HOOK = "luwrain.studio.project.types",
	CREATE_HOOK = "luwrain.studio.project.create";

    private final IDE ide;
    private final Luwrain luwrain;

    public ProjectFactory(IDE ide)
    {
	NullCheck.notNull(ide, "ide");
	this.ide = ide;
	this.luwrain = ide.getLuwrainObj();
    }

    public Project load(File projFile) throws IOException
    {
	NullCheck.notNull(projFile, "projFile");
	final Project loader = readProjectKey(projFile);
	if (loader == null)
	    throw new IOException("No known keys in the file");
	final Project proj = loader.load(projFile);
	if (proj == null)
	    throw new IOException(projFile.getPath() + " doesn't contain proper  project structure");
	return proj;
    }

        private Project readProjectKey(File projFile) throws IOException
    {
	NullCheck.notNull(projFile, "projFile");
	final String text = FileUtils.readTextFileSingleString(projFile, "UTF-8");
	if (text.contains(TexProject.KEY))
	    return new TexProject();
		if (text.contains(LyProject.KEY))
	    return new LyProject();
		return null;
    }

    public Project create(String projType, File destDir) throws IOException
    {
	NullCheck.notEmpty(projType, "projType");
	NullCheck.notNull(destDir, "destDir");
switch(projType)
	{
	case "latex-presentation": {
	    final TexPresentationWizard w = new TexPresentationWizard(ide, destDir);
	    ide.showWizard(w);
	    luwrain.announceActiveArea();
	    return null;
	}

	    	case "lilypond-piano": {
	    final LyPianoWizard w = new LyPianoWizard(ide, destDir);
	    ide.showWizard(w);
	    luwrain.announceActiveArea();
	    return null;
	}
	default:
	    return null;
	}
    }

    public ProjectType[] getNewProjectTypes()
    {
	return new ProjectType[]{
	    new ProjectType("latex-presentation", 0, "Презентация TeX")
	};
    }
}
