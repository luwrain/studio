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

package org.luwrain.studio;

import java.io.*;

import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;

/** Editing session of a source file for the case when it's a text file. */
public interface TextEditing extends Editing
{
    EditArea.Params getEditParams(ControlContext context);
    void onNewHotPoint(int hotPointX, int hotPointY);
}
