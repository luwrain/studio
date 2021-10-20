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

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.*;

import org.luwrain.core.*;
import org.luwrain.studio.*;

final class CompilationCommandFactory
{
    ProxyExecutable newCompilationCommand(TexCompilation compilation, String commandName)
    {
	NullCheck.notNull(compilation, "compilation");
	NullCheck.notEmpty(commandName, "commandName");
	return (ProxyExecutable)(args)->{
	    final List<String> argsArray = new ArrayList<>();
	    for(Value v: args)
	    {
		if (v == null)
		    throw new NullPointerException(commandName + ": Args can't include nulls");
		argsArray.add(v.toString());
	    }
	    run(compilation, commandName, argsArray.toArray(new String[argsArray.size()]));
	    return null;
	};
    }

    private void run(TexCompilation compilation, String commandName, String[] args)
    {
	NullCheck.notNull(compilation, "compilation");
	NullCheck.notEmpty(commandName, "commandName");
	NullCheck.notNullItems(args, "args");
	final List<String> cmd = new ArrayList<>();
	cmd.add(commandName);
	cmd.addAll(Arrays.asList(args));
	try {
	final Process p = new ProcessBuilder(cmd).directory(compilation.getProjectDir()).start();
	}
	catch(IOException e)
	{
	    throw new CompilationException(e);
	}
    }
}
