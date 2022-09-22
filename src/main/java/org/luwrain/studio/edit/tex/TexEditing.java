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

package org.luwrain.studio.edit.tex;

import java.io.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.core.events.InputEvent.Modifiers;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.studio.util.*;
import org.luwrain.script.controls.*;
import org.luwrain.app.base.*;
import org.luwrain.studio.edit.*;
import org.luwrain.nlp.*;
import org.luwrain.util.*;


import static org.luwrain.popups.Popups.*;
import static org.luwrain.studio.edit.tex.Hooks.*;
import static org.luwrain.studio.Part.*;

final class TexEditing extends TextEditingBase
{
        final EditSpellChecking spellChecking;
    final Strings strings;

    TexEditing(IDE ide, File file) throws IOException
    {
	super(ide, file);
	this.spellChecking =new EditSpellChecking(ide.getLuwrainObj());
	this.strings = (Strings)ide.getLuwrainObj().i18n().getStrings(Strings.NAME);
    }

    @Override public EditArea.Params getEditParams(ControlContext context)
    {
	final EditArea.Params params = new EditArea.Params();
	params.context = context;
		params.name = file.getName();
		params.content = this.content;
		params.appearance = new TexAppearance(context, this.content);
	params.inputEventListeners = new ArrayList<>(Arrays.asList(createEditAreaInputEventHook()));
	params.changeListeners = new ArrayList<>(Arrays.asList(spellChecking));
	params.editFactory = (editParams)->{
	    final MultilineEditCorrector base = (MultilineEditCorrector)editParams.model;
	    editParams.model = new EditCorrectorHooks(ide.getScriptCore(), new TexNewLineIndent(base), HOOK_EDIT);
	    setEdit(new MultilineEdit(editParams), base);
	    return getEdit();
	};
	return params;
    }

    @Override public Part.Action[] getActions()
    {
	//FIXME: Strings
	return actions(
		       action(strings.actAddFrame(), new InputEvent('f', EnumSet.of(Modifiers.ALT, Modifiers.SHIFT)), this::addFrame),
		       action(strings.actAddUnordered(), new InputEvent('u', EnumSet.of(Modifiers.ALT, Modifiers.SHIFT)), this::addItemize),
		       action(strings.actAddOrdered(), new InputEvent('o', EnumSet.of(Modifiers.ALT, Modifiers.SHIFT)), this::addEnumerate),
		       action(strings.actAddListItem(), new InputEvent('i', EnumSet.of(Modifiers.ALT, Modifiers.SHIFT)), this::addItem),
		       action(getAppearance().indent?strings.actDisableIndentSpeaking():strings.actEnableIndentSpeaking(), new InputEvent(InputEvent.Special.F5, EnumSet.of(Modifiers.ALT, Modifiers.SHIFT)), this::toggleIndent),
		       		       action(strings.actSuggestCorrection(), new InputEvent('s', EnumSet.of(InputEvent.Modifiers.ALT, InputEvent.Modifiers.SHIFT)), this::suggestCorrection),
		       action(strings.actReplace(), new InputEvent('r', EnumSet.of(InputEvent.Modifiers.ALT, InputEvent.Modifiers.SHIFT)), this::replace),
		       action(strings.actGotoLine(), new InputEvent('g', EnumSet.of(InputEvent.Modifiers.ALT, InputEvent.Modifiers.SHIFT)), this::gotoLine)
		       );
    }

    private boolean enableIndent()
    {
	getEdit().getMultilineEditAppearance();
	return false;
    }

    private boolean addFrame(IDE ide)
    {
	NullCheck.notNull(ide, "ide");
	if (!insertText(new String[]{
		    "\\begin{frame}",
		    "  \\frametitle{}",
		    "\\end{frame}"
		}))
	    return false;
	ide.getLuwrainObj().message("frame", Luwrain.MessageType.OK);
	return true;
    }

    private boolean addItemize(IDE ide)
    {
	NullCheck.notNull(ide, "ide");
	if (!insertText(new String[]{
		    "\\begin{itemize}",
		    "\\item{}",
		    "\\end{itemize}"
		}))
	    return false;
	ide.getLuwrainObj().message("Itemize", Luwrain.MessageType.OK);
	return true;
    }

    private boolean addEnumerate(IDE ide)
    {
	NullCheck.notNull(ide, "ide");
	if (!insertText(new String[]{
		    "\\begin{enumerate}",
		    "\\item{}",
		    "\\end{enumerate}"
		}))
	    return false;
	ide.getLuwrainObj().message("Enumerate", Luwrain.MessageType.OK);
	return true;
    }

    private boolean addItem(IDE ide)
    {
	if (!insertText("\\item{}"))
	    return false;
	ide.getLuwrainObj().message("item", Luwrain.MessageType.OK);
	return true;
    }

    private boolean toggleIndent(IDE ide)
    {
	final boolean
	oldState = getAppearance().indent,
	newState = !oldState;
	getAppearance().indent = newState;
	ide.getLuwrainObj().message(newState?"Включено чтение отступов":"Отключено чтение отступов", Luwrain.MessageType.OK);
	return true;
    }

    private boolean replace(IDE ide)
    {
	final String replaceExp = textNotEmpty(ide.getLuwrainObj(), "Замена", "Заменить:", "");//FIXME:
	if (replaceExp == null)
	    return true;
	final String replaceWith = text(ide.getLuwrainObj(), "Замена", "Заменить на:", "");//FIXME:
		if (replaceWith == null)
		    return true;
		try {
		super.replaceStr(replaceExp, replaceWith);
		}
		catch(java.util.regex.PatternSyntaxException e)
		{
		    ide.getLuwrainObj().message("Введённая строка для замены не является корректным регулярным выражением", Luwrain.MessageType.ERROR);
		}
		return true;
    }

        private boolean gotoLine(IDE ide)
    {
	final String lineNumStr = textNotEmpty(ide.getLuwrainObj(), "Перейти на строку", "Номер строки:", "");//FIXME:
	if (lineNumStr == null)
	    return true;
	final int lineNum;
	try {
	    lineNum = Integer.parseInt(lineNumStr);
	}
	catch(NumberFormatException e)
	{
	    ide.getLuwrainObj().message("Введённое значение не является допустимым номером строки", Luwrain.MessageType.ERROR);
	    return true;
	}
	if (lineNum <= 0 || lineNum> this.content.getLineCount())
	{
	    ide.getLuwrainObj().message("Введённое выражение выходит за границы допустимых номеров строки", Luwrain.MessageType.ERROR);
	    return true;
	}
	return true;
    }

    private boolean suggestCorrection(IDE ide)
    {
	final String word = new TextFragmentUtils(this.content).getWord(getHotPointX(), getHotPointY());
	if (word == null)
	    return false;
	final List<String> suggestions = spellChecking.getSpellChecker().suggestCorrections(word);
	if (suggestions != null)
	    if (suggestions == null || suggestions.isEmpty())
		return false;
	final String correction = (String)fixedList(ide.getLuwrainObj(), strings.suggestCorrectionsPopupName(), suggestions.toArray(new String[suggestions.size()]));
	if (correction == null)
	    return true;
	this.content.update((lines)->{
		final String newLine = new TextFragmentUtils(lines).replaceWord(getHotPointX(), getHotPointY(), correction);
		lines.setLine(getHotPointY(), newLine);
	    });
	return true;
    }

    @Override protected TexAppearance getAppearance()
    {
	return (TexAppearance)super.getAppearance();
    }
}
