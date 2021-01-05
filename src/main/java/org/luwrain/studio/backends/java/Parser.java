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

package org.luwrain.studio.backends.java;

import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.atn.*;

import org.luwrain.core.*;
import org.luwrain.antlr.java.*;

public final class Parser
{
    private List<ClassPart> classes = new LinkedList();
    private String namingContext = "";

    void parse(String[] lines)
    {
	NullCheck.notNullItems(lines, "lines");
	final String lineSep = System.lineSeparator();
	final StringBuilder text = new StringBuilder();
	for(String s: lines)
	    text.append(s).append(lineSep);
	final JavaLexer lexer = new JavaLexer(CharStreams.fromString(new String(text)));
	final CommonTokenStream tokens = new CommonTokenStream(lexer);
	final JavaParser parser = new JavaParser(tokens);
	parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
	final ParseTree tree = parser.compilationUnit();
	final ParseTreeWalker walker = new ParseTreeWalker();
	final ParsingListener listener = new ParsingListener(){
		@Override public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) 
		{
		    if (ctx.normalClassDeclaration() != null)
		    {
		    classes.add(new ClassPart(namingContext + ctx.normalClassDeclaration().identifier().Identifier().toString()));
		    }
		}
		@Override public void enterPackageDeclaration(JavaParser.PackageDeclarationContext ctx) 
		{
		    JavaParser.PackageNameContext c = ctx.packageName();
		    String s = "";
		    while (c != null)
		    {
			s = c.identifier().Identifier().toString() + "." + s;
			c = c.packageName();
		    }
		    namingContext = s;
		}
	    };
	walker.walk(listener, tree);
    }

    public ClassPart[] getClasses()
    {
	return classes.toArray(new ClassPart[classes.size()]);
    }
    }
