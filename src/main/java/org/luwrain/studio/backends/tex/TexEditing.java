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

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.core.events.InputEvent.Modifiers;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.studio.util.*;
import org.luwrain.app.base.*;

final class TexEditing extends TextEditingBase
{
    TexEditing(File file) throws IOException
    {
	super(file);
    }

    @Override public Part.Action[] getActions()
    {
	return Part.actions(
			    Part.action("Добавить слайд", new InputEvent('f', EnumSet.of(Modifiers.ALT, Modifiers.SHIFT)), this::addFrame),
			    Part.action("Добавить ненумерованный список", new InputEvent('u', EnumSet.of(Modifiers.ALT, Modifiers.SHIFT)), this::addItemize),
			    Part.action("Добавить ненумерованный список", new InputEvent('o', EnumSet.of(Modifiers.ALT, Modifiers.SHIFT)), this::addEnumerate),
			    Part.action("Добавить элемент списка", new InputEvent('i', EnumSet.of(Modifiers.ALT, Modifiers.SHIFT)), this::addItem)
			    );
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
	NullCheck.notNull(ide, "ide");
	if (!insertText("\\item{}"))
	    return false;
	ide.getLuwrainObj().message("item", Luwrain.MessageType.OK);
	return true;
    }

    @Override public EditArea.Params getEditParams(ControlContext context)
    {
	NullCheck.notNull(context, "context");
	final EditArea.Params params = new EditArea.Params();
	params.context = context;
	params.content = content;
	params.appearance = new TexAppearance(context);
	params.editFactory = (editParams)->{
	    final MultilineEditCorrector bottomAugmentation = (MultilineEditCorrector)editParams.model;
editParams.model = new TexNewLineIndent(bottomAugmentation);
	    setEdit(new MultilineEdit(editParams), bottomAugmentation);
	    return getEdit();
	};
	params.name = file.getName();
	return params;
    }
}
