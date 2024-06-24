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
//import com.google.gson.*;
//import static java.util.regex.Matcher.*;

import groovy.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.app.base.*;
import org.luwrain.studio.proj.main.*;

//import org.luwrain.controls.WizardArea.Frame;
//import org.luwrain.controls.WizardArea.WizardValues;
import org.luwrain.controls.wizard.*;

import static org.luwrain.util.FileUtils.*;
import static org.luwrain.util.ResourceUtils.*;
import static org.luwrain.studio.syntax.tex.TexUtils.*;

public final class ProjectWizard extends LayoutBase
{
    final IDE ide;
    final File destDir;
    final WizardArea wizardArea;
    final WizardGroovyController controller;

    ProjectWizard(IDE ide, File destDir, String scriptName) throws IOException
    {
	super(ide.getAppBase());
	this.ide = ide;
	//	this.app = ide.getAppBase();
	this.destDir = destDir;
	wizardArea = new WizardArea(getControlContext());
	wizardArea.setAreaName("Новый проект");
	controller = new WizardGroovyController(getLuwrain(), wizardArea);
	Eval.me("wizard", controller, getStringResource(this.getClass(), scriptName));
	setAreaLayout(wizardArea, null);
    }
    }
