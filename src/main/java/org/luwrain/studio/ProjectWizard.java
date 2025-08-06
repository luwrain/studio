/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

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

import org.luwrain.controls.*;
import org.luwrain.io.json.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.wizard.*;
import org.luwrain.studio.proj.main.*;
import org.luwrain.app.studio.Strings;

import static java.util.Objects.*;
import static org.luwrain.util.FileUtils.*;
import static org.luwrain.util.ResourceUtils.*;

public final class ProjectWizard extends LayoutBase
{
    static private final Logger log = LogManager.getLogger();

    final IDE ide;
    final File destDir;
    final WizardArea wizardArea;
    final WizardGroovyController controller;

    ProjectWizard(IDE ide, Strings strings, File destDir, String scriptName)
    {
	super(ide.getAppBase());
	this.ide = ide;
	this.destDir = destDir;
	wizardArea = new WizardArea(getControlContext());
	wizardArea.setAreaName("Новый проект");
	var persInfo = getLuwrain().loadConf(PersonalInfo.class);
	if (persInfo == null)
	    persInfo = new PersonalInfo();
	final var values = new HashMap<String, String>();
	values.put("authors", requireNonNullElse(persInfo.getFullName(), ""));
	controller = new WizardGroovyController(getLuwrain(), wizardArea){
		public void setValue(String name, String value)
		{
		    values.put(name, value);
		}
		public String getValue(String name)
		{
		    final var res = values.get(name);
		    return res != null?res:"";
		}
		public void writeFile(String fileName, List<String> lines)
		{
		    try {
			writeTextFileMultipleStrings(new File(destDir, fileName), lines.toArray(new String[lines.size()]), "UTF-8", null);
		    }
		    catch(IOException ex)
		    {
			log.error(ex);
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
		public Strings getStrings()
		{
		    return strings;
		}
	    };
	try {
	    Eval.me("wizard", controller, getStringResource(this.getClass(), scriptName));
	}
	catch(IOException ex)
	{
	    log.error("Unable to run project wizard script", ex);
	    throw new RuntimeException(ex);
	}
	setAreaLayout(wizardArea, null);
    }
}
