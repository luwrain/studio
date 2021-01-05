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

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.MultilineEdit.ModificationResult;
import org.luwrain.studio.util.*;

final class Corrector extends ProgrammingCorrector
{
    Corrector(MultilineEditCorrector basicCorrector)
    {
	super(basicCorrector);
    }

            @Override public ModificationResult splitLine(int pos, int lineIndex)
    {
	final ModificationResult res = basicCorrector.splitLine(pos, lineIndex);
	if (!res.isPerformed())
	    return res;
	final int indent = getIndent(lineIndex);
	if (!deleteIndent(lineIndex + 1))
	    return new ModificationResult(false);
	if (!addIndent(lineIndex + 1, indent))
	    return new ModificationResult(false);  
return res;
    }

        @Override public ModificationResult putChars(int pos, int lineIndex, String str)
    {
	return basicCorrector.putChars(pos, lineIndex, str);
    }
}
