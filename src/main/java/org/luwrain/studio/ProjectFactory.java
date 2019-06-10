/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>

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

//LWR_API 1.0

package org.luwrain.studio;

import java.io.*;

import org.luwrain.core.*;
import org.luwrain.studio.backends.js.JsProject;
import org.luwrain.studio.backends.js.JsProjectLoader;
import org.luwrain.studio.backends.tex.TexProjectLoader;

public final class ProjectFactory
{
    static public Project load(File projFile) throws IOException
    {
	NullCheck.notNull(projFile, "projFile");
	final JsProjectLoader jsProjectLoader = new JsProjectLoader();
	final JsProject jsProj = jsProjectLoader.load(projFile);
	return jsProj;
	/*
	final TexProjectLoader texLoader = new TexProjectLoader();
	return texLoader.load(projFile);
	*/
    }
}
