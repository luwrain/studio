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

final class TexAppearance extends EditUtils.DefaultEditAreaAppearance
{
    static private final String HOOK_NAME = "luwrain.studio.tex.appearance";

    TexAppearance(ControlContext context)
    {
	super(context);
    }

    @Override public void announceLine(int index, String line)
    {
	NullCheck.notNull(line, "line");
	/*
	try {
	    if (context.runHooks(HOOK_NAME + ".custom", new Object[]{new Integer(index), line}, Luwrain.HookStrategy.CHAIN_OF_RESPONSIBILITY))
		return;
	    if (context.runHooks(HOOK_NAME, new Object[]{new Integer(index), line}, Luwrain.HookStrategy.CHAIN_OF_RESPONSIBILITY))
		return;
	}
	catch(RuntimeException e)
	{
	    //FIXME:
	    context.say(context.getI18n().getExceptionDescr(e));
	    return;
	}
	*/
	if (line.trim().isEmpty())
	    context.setEventResponse(DefaultEventResponse.hint(line.isEmpty()?Hint.EMPTY_LINE:Hint.SPACES)); else
	    context.setEventResponse(DefaultEventResponse.text(context.getSpeakableText(line, Luwrain.SpeakableTextType.PROGRAMMING)));

    }
}
