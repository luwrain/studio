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

package org.luwrain.studio.backends;

import java.io.*;
import java.util.*;

import com.google.gson.*;

import org.luwrain.core.*;

import org.luwrain.studio.backends.tex.TexProject;
import org.luwrain.studio.backends.tex.TexFolder;

public final class NewProjectCommands
{
    static public Command[] get()
    {
	return new Command[]{

	    new Command(){
		@Override public String getName() { return "studio-new-project-tex"; }
		@Override public void onCommand(Luwrain luwrain)
		{
		    NullCheck.notNull(luwrain, "luwrain");
		    final String dir = luwrain.getActiveAreaAttr(Luwrain.AreaAttr.DIRECTORY);
		    if (dir == null || dir.isEmpty())
		    {
			luwrain.playSound(Sounds.ERROR);
			return;
		    }
		    final File file = new File(new File(dir), "tex-project.lwrproj");
		    final TexProject proj = new TexProject();
		    proj.setProjName("New TeX project");
		    final TexFolder folder = new TexFolder();
		    folder.setName("Root folder");
		    folder.setSubfolders(new ArrayList());
		    folder.setSourceFiles(new ArrayList());
		    proj.setRootFolder(folder);
		    try {
			try (final BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
			    final Gson gson = new Gson();
			    gson.toJson(proj, w);
			    w.flush();
			}
			luwrain.playSound(Sounds.OK);
		    }
		    catch(IOException e)
		    {
			luwrain.crash(e);
		    }
		}
	    }
	};
    }
}
