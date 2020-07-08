
package org.luwrain.studio.backends.java;

import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import org.luwrain.core.*;
import org.luwrain.antlr.java.*;

public final class Parser
{
    private List<ClassPart> classes = new LinkedList();
    private String namingContext = "";

    void parse(String text)
    {
	NullCheck.notNull(text, "text");
	final JavaLexer lexer = new JavaLexer(CharStreams.fromString(text));
	final CommonTokenStream tokens = new CommonTokenStream(lexer);
final JavaParser parser = new JavaParser(tokens);
final ParseTree tree = parser.compilationUnit();
final ParseTreeWalker walker = new ParseTreeWalker();
final ParsingListener listener = new ParsingListener(){
	    @Override public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) 
	{
	    classes.add(new ClassPart(namingContext + ctx.normalClassDeclaration().identifier().Identifier().toString()));
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
