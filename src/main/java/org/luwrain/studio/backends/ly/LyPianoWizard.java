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

package org.luwrain.studio.backends.ly;

import java.io.*;
import java.util.*;
import com.google.gson.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.app.base.*;
import org.luwrain.util.*;

import org.luwrain.controls.WizardArea.Frame;
import org.luwrain.controls.WizardArea.WizardValues;

import org.luwrain.app.studio.Strings;

public final class LyPianoWizard extends LayoutBase
{
    private final IDE ide;
    final AppBase<Strings> app;
    final File destDir;
    final WizardArea wizardArea;

    private String title = "", author = "";
    private List<String> frames = new ArrayList<>();

    public LyPianoWizard(IDE ide, File destDir)
    {
	super(ide.getAppBase());
	NullCheck.notNull(destDir, "destDir");
	this.ide = ide;
	this.app = ide.getAppBase();
	this.destDir = destDir;
	this.wizardArea = new WizardArea(getControlContext());
	final Frame greeting = wizardArea.newFrame()
	.addText("Новая фортепианная пьеса")
	.addInput("Название:", "")
	.addInput("Автор:", "")
	.addClickable("Далее", (input)-> {return onGreetingClick(input);});
	wizardArea.show(greeting);
	setAreaLayout(wizardArea, null);
    }

    private boolean onGreetingClick(WizardValues input)
    {
	NullCheck.notNull(input, "input");
	this.title = input.getText(0).trim();
	this.author = input.getText(1).trim();
	    try {
		save();
		return true;
	    }
	    catch(IOException e)
	    {
		app.getLuwrain().crash(e);
		return true;
	    }
	}

    private void save() throws IOException
    {

    }
}
