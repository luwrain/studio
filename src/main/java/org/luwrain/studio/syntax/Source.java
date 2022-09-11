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

package org.luwrain.studio.syntax;

import java.io.*;
import java.util.*;

import org.luwrain.core.*;

public class Source
{
    String content = "";

    public Source(String content)
    {
	NullCheck.notNull(content, "content");
	this.content = content;
    }

    public String getText()
    {
	return content;
    }
}
