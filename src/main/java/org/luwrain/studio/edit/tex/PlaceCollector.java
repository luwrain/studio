/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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

import java.util.*;
import java.io.*;

import org.apache.logging.log4j.*;

import groovy.lang.*;
import groovy.util.*;

import org.luwrain.core.*;

import static org.luwrain.util.ResourceUtils.*;

public final class PlaceCollector
{
    static private final Logger log = LogManager.getLogger();

    final String script;

    PlaceCollector()
    {
	try {
	    script = getStringResource(getClass(), "places.groovy");
	}
	catch(IOException ex)
	{
log.catching(ex);
	    throw new RuntimeException(ex);
	}
	
    }

    void collect(MutableMarkedLines lines)
    {
	final var res = Eval.me("{-> }");
	if (res == null)
	    throw new RuntimeException("The places collecting script doesn't give any result");
	if (!(res instanceof Closure))
	    throw new RuntimeException("The result of the places collecting script isn't a closure");

    }

}
