
package org.luwrain.studio.indents.js;

import java.util.*;

public class Handler
{
    static private final String
	FUNC_EXP = "FunctionExpressionContext",
		FUNC_DECL = "FunctionDeclarationContext",
	FUNC_BODY = "FunctionBodyContext",
	SOURCE_EL = "SourceElementContext";

static final class StackItem
{
    final String type;
    final int line, pos;
    StackItem(String type, int line, int pos)
    {
	this.type = type;
	this.line = line;
	this.pos = pos;
    }
}

    static final class LineInfo
    {
	String type = null;
	int calcIndent = 0;
    }

    final LinkedList<StackItem> stack = new LinkedList<>();
    final List<LineInfo> lines = new ArrayList<>();
    int step = 4;

    void beginBlock(String type, int line, int pos)
    {
	//System.out.println("#" + type + " "  + line + ", " + pos);
	while (lines.size() < line + 1)
	    lines.add(new LineInfo());
	final LineInfo lineInfo = lines.get(line);
	if (type.equals(SOURCE_EL) && lineInfo.type == null)
	{
	    lineInfo.type = type;
	    if (!stack.isEmpty())
		lineInfo.calcIndent = stack.getLast().pos + step;
	}

	if (type.equals(SOURCE_EL))
	    stack.add(new StackItem(type, line, pos));
    }

    void endBlock(String type, int line, int pos)
    {
		while (lines.size() < line + 1)
	    lines.add(new LineInfo());
	final LineInfo lineInfo = lines.get(line);

	
	if (type == FUNC_BODY && lineInfo.type == null && !stack.isEmpty())
	{
	    lineInfo.calcIndent = stack.getLast().pos;
	}


	    	    	    //Popping the closing entity from the stack
	    if (stack.isEmpty())
		return;

	final var tail = stack.getLast();
	if (tail.type.equals(type))
	    stack.pollLast();
    }

    public int getCalculatedIndent(int line)
    {
	if (line < 0 || line >= lines.size())
	    return 0;
	return lines.get(line).calcIndent;
    }
}
