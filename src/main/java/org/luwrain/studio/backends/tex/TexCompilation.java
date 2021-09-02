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
import org.luwrain.script.core.*;
import org.luwrain.studio.*;

final class TexCompilation
{
    private final IDE ide;
    private final TexProject proj;

    TexCompilation(IDE ide, TexProject proj)
    {
	NullCheck.notNull(ide, "ide");
	NullCheck.notNull(proj, "proj");
	this.ide = ide;
	this.proj = proj;
    }

    void build()
    {
	runCompilationScript();
    }

    private void runCompilationScript()
    {
	final ScriptCore scriptCore = new ScriptCore(ide.getLuwrainObj(), (bindings)->{
		final CompilationCommandFactory factory = new CompilationCommandFactory();
		bindings.putMember("latex", factory.newCompilationCommand(TexCompilation.this, "latex"));
	    });
    }

    File getProjectDir()
    {
	return new File("/tmp");
    }
}
