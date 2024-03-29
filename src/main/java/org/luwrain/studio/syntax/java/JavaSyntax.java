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

package org.luwrain.studio.syntax.java;

import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.atn.*;

import org.luwrain.core.*;
import org.luwrain.antlr.java.*;
import org.luwrain.antlr.java.JavaParser.*;
import org.luwrain.studio.syntax.SpanTree;
import org.luwrain.studio.syntax.SpanTree.*;

public final class JavaSyntax
{
    public interface Source
    {
	String getText();
    }

    public final Source source;
    public final SpanTree spanTree = new SpanTree();
    public JavaSyntax(Source source)
    {
	this.source = source;
    }

    void parse()
    {
	final JavaLexer lexer = new JavaLexer(CharStreams.fromString(source.getText()));
	final CommonTokenStream tokens = new CommonTokenStream(lexer);
	final JavaParser parser = new JavaParser(tokens);
	parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
	final ParseTree tree = parser.compilationUnit();
	final ParseTreeWalker walker = new ParseTreeWalker();
	final JavaListener listener = new JavaBaseListener(){
		//enterMethodBody
		//enterConstructorBody
		//enterEnumBody
		//enterInterfaceBody
		//enterLambdaBody
		//enterClassBody
		@Override public void enterClassBody(ClassBodyContext c)
		{
		    final Span span = spanTree.addSpan();
		    span.setFromPos(c.getStart().getStartIndex());
		    span.setToPos(c.getStop().getStartIndex());
		}
		@Override public void exitClassBody(ClassBodyContext c) { spanTree.pop(); }
		@Override public void enterBlock(BlockContext c)
		{
		    final Span span = spanTree.addSpan();
		    		    span.setFromPos(c.getStart().getStartIndex());
		    span.setToPos(c.getStop().getStartIndex());
		}
		@Override public void exitBlock(BlockContext c) { spanTree.pop(); }
		@Override public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) 
		{
		    if (ctx.normalClassDeclaration() != null)
		    {
			//		    classes.add(new ClassPart(namingContext + ctx.normalClassDeclaration().identifier().Identifier().toString()));
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
		    //		    namingContext = s;
		}
	    };
	walker.walk(listener, tree);
    }
    }
