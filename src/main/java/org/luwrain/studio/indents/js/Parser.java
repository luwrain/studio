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
    final Handler handler;
    final String text;

    public Parser(Handler handler, List<String> lines)
    {
	final String lineSep = System.lineSeparator();
	this.handler = handler;
	this.text = lines.stream().reduce("", (a, b)->{ return a + (a.isEmpty()?"":lineSep) + b;});
    }

    void parse()
    {
	final var lexer = new JavaScriptLexer(CharStreams.fromString(text));
	final var tokens = new CommonTokenStream(lexer);
	final var parser = new JavaScriptParser(tokens);
	parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
	final var tree = parser.program();
	final var walker = new ParseTreeWalker();
	final var listener = new JavaScriptParserBaseListener(){
		@Override public void enterEveryRule(ParserRuleContext c)  
		{
		    handler.beginBlock(c.getClass().getSimpleName(), c.getStart().getLine(), c.getStart().getCharPositionInLine());
		}
		@Override public void exitEveryRule(ParserRuleContext c)  
		{
		    handler.endBlock(c.getClass().getSimpleName(), c.getStop().getLine(), c.getStop().getCharPositionInLine());
		}
	    };
	walker.walk(listener, tree);
    }
}
