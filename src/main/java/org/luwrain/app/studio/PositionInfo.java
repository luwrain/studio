/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.app.studio;

import org.luwrain.core.*;

final class PositionInfo
    {
	final String fileName;
	final int lineNum;
	final int colNum;
	PositionInfo(String fileName, int lineNum, int colNum)
	{
	    NullCheck.notNull(fileName, "fileName");
	    if (lineNum <= 0)
		throw new IllegalArgumentException("lineNum (" + lineNum + ") must be greater than zero");
	    this.fileName = fileName;
	    this.lineNum = lineNum;
	    this.colNum = colNum;
	}
    }
