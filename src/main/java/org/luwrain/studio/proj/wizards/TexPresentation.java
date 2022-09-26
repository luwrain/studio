/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.studio.proj.wizards;

import java.io.*;
import java.util.*;
import com.google.gson.*;
import static java.util.regex.Matcher.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.app.base.*;
import org.luwrain.studio.proj.main.*;

import org.luwrain.controls.WizardArea.Frame;
import org.luwrain.controls.WizardArea.WizardValues;

import static org.luwrain.util.FileUtils.*;
import static org.luwrain.util.ResourceUtils.*;
import static org.luwrain.studio.syntax.tex.TexUtils.*;

public final class TexPresentation extends LayoutBase
{
    static private final String
	MAIN_FILE_NAME = "main.tex",
	PROJ_FILE_NAME = "presentation.lwrproj";

    
    private final IDE ide;
    private final AppBase<org.luwrain.app.studio.Strings> app;
    private final Strings strings;
    private final File destDir;
    private final WizardArea wizardArea;

    private String title = "", author = "", date = "";
    private List<String> frames = new ArrayList<>();

    public TexPresentation(IDE ide, File destDir)
    {
	super(ide.getAppBase());
	this.ide = ide;
	this.app = ide.getAppBase();
	this.strings = (Strings)ide.getLuwrainObj().i18n().getStrings(Strings.NAME);
	this.destDir = destDir;
	this.wizardArea = new WizardArea(getControlContext());
	this.wizardArea.setAreaName(strings.texPresentationTitle());
	final Frame greeting = wizardArea.newFrame()
	.addText(strings.texPresentationGreeting())
	.addInput(strings.texPresentationInputTitle(), "")
	.addInput(strings.texPresentationInputAuthor(), ide.getSett().getPersonalName(""))
	.addInput(strings.texPresentationInputDate(), "")
	.addClickable(strings.next(), (input)-> {return onGreetingClick(input);});
	wizardArea.show(greeting);
	setAreaLayout(wizardArea, null);
    }

    private boolean onGreetingClick(WizardValues input)
    {
	if (input.getText(0).trim().isEmpty())
	{
	    ide.getLuwrainObj().message(strings.titleCannotBeEmpty(), Luwrain.MessageType.ERROR);
	    return true;
	}
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
	    .append("  \\\\frametitle{").append(escapeTex(f)).append("}").append(System.lineSeparator()).append(System.lineSeparator())
	    .append("\\\\end{frame}").append(System.lineSeparator()).append(System.lineSeparator());
	final String text = getStringResource(this.getClass(), "presentation-ru.tex")
	.replaceAll("LWR_STUDIO_TITLE", quoteReplacement(escapeTex(this.title)))
	.replaceAll("LWR_STUDIO_AUTHOR", quoteReplacement(escapeTex(this.author)))
	.replaceAll("LWR_STUDIO_DATE", quoteReplacement(escapeTex(this.date)))
	.replaceAll("LWR_STUDIO_BODY", new String(body).trim());
		final ProjectImpl proj = new ProjectImpl();
	proj.setProjName(this.title);
	final Folder folder = new Folder();
	folder.setName("Презентация Tex");
	folder.setSubfolders(Arrays.asList());
	folder.setTexFiles(Arrays.asList(new org.luwrain.studio.edit.tex.TexSourceFile(MAIN_FILE_NAME, MAIN_FILE_NAME)));
	proj.setRootFolder(folder);
	final Gson gson = new Gson();
	final File projFile = new File(destDir, PROJ_FILE_NAME);
	final File mainFile = new File(destDir, "main.tex");
	writeTextFileSingleString(mainFile, text, "UTF-8");
	writeTextFileSingleString(projFile, gson.toJson(proj), "UTF-8");
	ide.loadProject(projFile);
    }
}
