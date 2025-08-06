/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.studio.edit;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.controls.edit.MultilineEdit.ModificationResult;
//import org.luwrain.studio.util.*;

public class NewLineIndent extends EditAugmentationUtils
{
    public NewLineIndent(MultilineEdit.Model base)
    {
	super(base);
    }

            @Override public ModificationResult splitLine(int pos, int lineIndex)
    {
	final ModificationResult res = wrappedModel.splitLine(pos, lineIndex);
	if (!res.isPerformed())
	    return res;
	final int indent = getProperIndent(lineIndex + 1);
	if (!deleteIndent(lineIndex + 1))
	    return new ModificationResult(false);
	if (!addIndent(lineIndex + 1, indent))
	    return new ModificationResult(false);  
return res;
    }

    protected int getProperIndent(int lineIndex)
    {
	// By default using the indent of the previous line
	if (lineIndex <= 0 || lineIndex >= getLineCount())
	    return 0;
	return getIndent(lineIndex - 1);
    }
}
