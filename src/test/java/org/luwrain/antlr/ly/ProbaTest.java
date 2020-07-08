
package org.luwrain.antlr.ly;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
    
import org.junit.*;

import org.luwrain.core.*;

public class ProbaTest extends Assert
{
    @Test public void simple() throws Exception
    {
	LilypondLexer l = new LilypondLexer(CharStreams.fromString(""));

	CommonTokenStream tokens = new CommonTokenStream(l);
LilypondParser p = new LilypondParser(tokens);
ParseTree tree = null;//p.compilationUnit();
ParseTreeWalker walker = new ParseTreeWalker();
LilypondListener listener = null;// new UppercaseMethodListener();

walker.walk(listener, tree);

//assertThat(listener.getErrors().size(), is(1));
//assertThat(listener.getErrors().get(0),
//  is("Method DoSomething is uppercased!"));


	    }
    }
    
