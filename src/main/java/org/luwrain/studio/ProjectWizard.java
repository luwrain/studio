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
import java.util.*;
import org.apache.logging.log4j.*;

import groovy.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.app.base.*;
import org.luwrain.studio.proj.main.*;

import org.luwrain.controls.wizard.*;
import org.luwrain.studio.proj.main.*;

import static org.luwrain.util.FileUtils.*;
import static org.luwrain.util.ResourceUtils.*;
import static org.luwrain.studio.syntax.tex.TexUtils.*;
import static org.luwrain.core.Settings.*;
import static org.luwrain.core.NullCheck.*;

public final class ProjectWizard extends LayoutBase
{
    static private final Logger log = LogManager.getLogger();

    final IDE ide;
    final File destDir;
    final WizardArea wizardArea;
    final WizardGroovyController controller;

    ProjectWizard(IDE ide, File destDir, String scriptName) throws IOException
    {
	super(ide.getAppBase());
	this.ide = ide;
	this.destDir = destDir;
	wizardArea = new WizardArea(getControlContext());
	wizardArea.setAreaName("Новый проект");
	final var persInfo = createPersonalInfo(getLuwrain().getRegistry());
	final var values = new HashMap<String, String>();
	values.put("authors", persInfo.getFullName(""));
	controller = new WizardGroovyController(getLuwrain(), wizardArea){
		public void setValue(String name, String value)
		{
		    		    notEmpty(name, "name");
		    values.put(name, value);
		}
		public String getValue(String name)
		{
		    notEmpty(name, "name");
		    final var res = values.get(name);
		    return res != null?res:"";
		}
		public void writeFile(String fileName, List<String> lines)
		{
		    notEmpty(fileName, "fileName");
		    notNull(lines, "lines");
		    try {
		    writeTextFileMultipleStrings(new File(destDir, fileName), lines.toArray(new String[lines.size()]), "UTF-8", null);
		    }
		    catch(IOException ex)
		    {
			log.catching(ex);
			throw new RuntimeException(ex);
		    }
		}
		public void finish(String fileName, ProjectImpl proj)
		{
		    final var file = new File(destDir, fileName);
		    proj.setProjectFile(file);
		    proj.save();
		    ide.loadProject(file);
		}
	    };
	Eval.me("wizard", controller, getStringResource(this.getClass(), scriptName));
	setAreaLayout(wizardArea, null);
    }
    }
