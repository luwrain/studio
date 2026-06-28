
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
	final var listener = new JavaParserBaseListener(){
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
		    /*
		    		    JavaParser.PackageNameContext c = ctx.packageName();
		    String s = "";
		    while (c != null)
		    {
			s = c.identifier().Identifier().toString() + "." + s;
			c = c.packageName();
		    }
		    //		    namingContext = s;
		    */
		}
	    };
	walker.walk(listener, tree);
    }
    }
