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

import static org.luwrain.studio.edit.tex.Hooks.*;

final class TexEditing extends TextEditingBase
{
        final EditSpellChecking spellChecking;

    TexEditing(IDE ide, File file) throws IOException
    {
	super(ide, file);
	this.spellChecking =new EditSpellChecking(ide.getLuwrainObj());
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
	return Part.actions(
			    Part.action("Добавить слайд", new InputEvent('f', EnumSet.of(Modifiers.ALT, Modifiers.SHIFT)), this::addFrame),
			    Part.action("Добавить ненумерованный список", new InputEvent('u', EnumSet.of(Modifiers.ALT, Modifiers.SHIFT)), this::addItemize),
			    Part.action("Добавить нумерованный список", new InputEvent('o', EnumSet.of(Modifiers.ALT, Modifiers.SHIFT)), this::addEnumerate),
			    Part.action("Добавить элемент списка", new InputEvent('i', EnumSet.of(Modifiers.ALT, Modifiers.SHIFT)), this::addItem),
			    Part.action(getAppearance().indent?"Отключить чтение отступов":"Включить чтение отступов", new InputEvent(InputEvent.Special.F5, EnumSet.of(Modifiers.ALT, Modifiers.SHIFT)), this::toggleIndent)
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

    @Override protected TexAppearance getAppearance()
    {
	return (TexAppearance)super.getAppearance();
    }
}
