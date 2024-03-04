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

package org.luwrain.studio.indents.js;

import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.atn.*;

import org.luwrain.core.*;
import org.luwrain.antlr.js.*;

public final class Parser
{
    void parse(String[] lines)
    {
	final String lineSep = System.lineSeparator();
	final StringBuilder text = new StringBuilder();
	final JavaScriptLexer lexer = new JavaScriptLexer(CharStreams.fromString(new String(text)));
	final CommonTokenStream tokens = new CommonTokenStream(lexer);
	final JavaScriptParser parser = new JavaScriptParser(tokens);
	parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
	final ParseTree tree = parser.program();
	final ParseTreeWalker walker = new ParseTreeWalker();
	final Listener listener = new Listener(){
	    };
	walker.walk(listener, tree);
    }
    }
