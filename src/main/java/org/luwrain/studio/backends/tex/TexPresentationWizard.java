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
import com.google.gson.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.app.base.*;
import org.luwrain.util.*;

import org.luwrain.controls.WizardArea.Frame;
import org.luwrain.controls.WizardArea.WizardValues;

import org.luwrain.app.studio.Strings;

public final class TexPresentationWizard extends LayoutBase
{
    final AppBase<Strings> app;
    final File destDir;
    final WizardArea wizardArea;

    private String title = "", author = "", date = "";
    private List<String> frames = new ArrayList();

    public TexPresentationWizard(AppBase<Strings> app, File destDir)
    {
	super(app);
	NullCheck.notNull(destDir, "destDir");
	this.app = app;
	this.destDir = destDir;
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
	this.title = input.getText(0).trim();
	this.author = input.getText(1).trim();
	this.date = input.getText(2).trim();
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
	if (input.getText(0).trim().isEmpty())
	{
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
	frames.add(input.getText(0));
	wizardArea.show(newFrameTitle());
	return true;
    }

    private void save() throws IOException
    {
	final StringBuilder body = new StringBuilder();
	for(String f: frames)
	    body.append("\\\\begin{frame}").append(System.lineSeparator())
	    .append("  \\\\frametitle{").append(Utils.escapeTex(f)).append("}").append(System.lineSeparator()).append(System.lineSeparator())
	    .append("\\\\end{frame}").append(System.lineSeparator()).append(System.lineSeparator());
	final StringBuilder b = new StringBuilder();
	try (final BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("presentation-ru.tex"), "UTF-8"))) {
	    String line = r.readLine();
	    while (line != null)
	    {
		b.append(line).append(System.lineSeparator());
		line = r.readLine();
	    }
	}
	final String text = new String(b)
	.replaceAll("LWR_STUDIO_TITLE", Utils.escapeTex(this.title))
	.replaceAll("LWR_STUDIO_AUTHOR", Utils.escapeTex(this.author))
	.replaceAll("LWR_STUDIO_DATE", Utils.escapeTex(this.date))
	.replaceAll("LWR_STUDIO_BODY", new String(body).trim());
	final TexProject proj = new TexProject();
	proj.setProjName(this.title);
	final TexFolder folder = new TexFolder();
	folder.setName("Презентация Tex");
	folder.setSubfolders(new ArrayList());
	folder.setSourceFiles(Arrays.asList(new TexSourceFile("main.tex", "main.tex")));
	proj.setRootFolder(folder);
	final Gson gson = new Gson();
	final File projFile = new File(destDir, "presentation.lwrproj");
	final File mainFile = new File(destDir, "main.tex");
	FileUtils.writeTextFileSingleString(mainFile, text, "UTF-8");
	FileUtils.writeTextFileSingleString(projFile, gson.toJson(proj), "UTF-8");
    }
}
