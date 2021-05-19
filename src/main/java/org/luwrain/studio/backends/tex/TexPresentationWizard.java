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

package org.luwrain.studio.backends.tex;

import java.io.*;
import java.util.*;
import com.google.gson. annotations.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.app.base.*;

import org.luwrain.controls.WizardArea.Frame;
import org.luwrain.controls.WizardArea.WizardValues;

import org.luwrain.app.studio.Strings;

public final class TexPresentationWizard extends LayoutBase
{
    final AppBase<Strings> app;
    final WizardArea wizardArea;

    private String title = "", author = "", date = "";
    private List<String> frames = new ArrayList();

    public TexPresentationWizard(AppBase<Strings> app)
    {
	super(app);
	this.app = app;
	this.wizardArea = new WizardArea(getControlContext());
	final Frame greeting = wizardArea.newFrame()
	.addText("Новая презентация")
	.addInput("Название:", "")
		.addInput("Автор:", "")
			.addInput("Дата:", "")
	.addClickable("Далее", (input)-> {return onGreetingClick(input);});
	wizardArea.show(greeting);
	setAreaLayout(wizardArea, null);
    }

    	private boolean onGreetingClick(WizardValues input)
	{
	    NullCheck.notNull(input, "input");
		    wizardArea.show(newFrameTitle());
	    return true;
	}

    private Frame newFrameTitle()
    {
	final Frame res = wizardArea.newFrame()
	.addText("Введите заглавие нового слайда.");
	for(String s: frames)
	    res.addClickable(s, (input)->{ return false; });
	res.addInput("Заглавие нового слайда:", "");
	res.addClickable("Добавить новый слайд или завершить создание презентации", (input)->{ return addFrame(input); });
	return res;
    }

    private boolean addFrame(WizardValues input)
    {
	frames.add(input.getText(0));
	wizardArea.show(newFrameTitle());
	
	return true;
    }
}
