
package org.luwrain.studio.indents.js;

import java.util.*;

public class Handler
{
    static private final String
	BLOCK = "BlockContext",
	FUNC_EXP = "FunctionExpressionContext",
	FUNC_DECL = "FunctionDeclarationContext",
	FUNC_BODY = "FunctionBodyContext",
	SOURCE_EL = "SourceElementContext",
	STATEMENT = "StatementContext";

    final LinkedList<StackItem> stack = new LinkedList<>();
    final List<LineInfo> lines = new ArrayList<>();
    int step = 4;

    void beginBlock(String type, int line, int pos)
    {
	//	System.out.println("#" + type + " "  + line + ", " + pos);
	while (lines.size() < line + 1)
	    lines.add(new LineInfo());

	final LineInfo lineInfo = lines.get(line);
	final StackItem newStackItem = new StackItem(type, line, pos);

	//Registering the first element on the line
	if (type.equals(STATEMENT) && lineInfo.firstType == null)
	{
	    lineInfo.firstType = type;
	    lineInfo.firstStackItem = newStackItem;
	    if (!stack.isEmpty())
		lineInfo.calcIndent = stack.getLast().pos + step;
	}

	if (type.equals(STATEMENT))
	{
	    stack.add(newStackItem);
	    System.out.println("Adding " + type + " " + line + " " + pos);
	}

	if (type.equals(BLOCK) && !stack.isEmpty())
		{
		    //If it starts on the same position as the top stack item, which is supposed to be a statement, popping it
		    final var stackTop = stack.getLast();
		    if (stackTop.line == line && stackTop.pos == pos)
		    {
			stack.pollLast();
			System.out.println("Replacing with block " + line + " " + pos);
		    //If the removed item was the first on the line, updating the calcIndent
			if (lineInfo.firstStackItem != null &&lineInfo.firstStackItem.equals(stackTop) && !stack.isEmpty())
			{
			    lineInfo.calcIndent = stack.getLast().pos;
			    lineInfo.firstStackItem = null;
			}
		    }
		}
    }

    void endBlock(String type, int endLine, int endPos, int startLine, int startPos)
    {
		while (lines.size() < endLine + 1)
	    lines.add(new LineInfo());
	final LineInfo lineInfo = lines.get(endLine);

	//Processing the closing brackets
	if ((type == FUNC_BODY ||
	     type.equals(BLOCK)) &&
	    lineInfo.firstType == null && !stack.isEmpty())
	{
	    System.out.println("Closing " + type + " " + stack.size());
	    System.out.println("" + stack.getLast().line + " " + stack.getLast().pos);
	    lineInfo.calcIndent = stack.getLast().pos;
	}

	/*
	if (type.equals(STATEMENT))
	    System.out.println("Removing statement " + line + " " + pos);
	*/


	    	    	    //Popping the closing entity from the stack
	    if (stack.isEmpty())
		return;

	final var tail = stack.getLast();
	if (tail.type.equals(type) && tail.line == startLine && tail.pos == startPos)
	    stack.pollLast();
    }

    public int getCalculatedIndent(int line)
    {
	if (line < 0 || line >= lines.size())
	    return 0;
	return lines.get(line).calcIndent;
    }

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
	@Override public boolean equals(Object o)
	{
	    if (o instanceof StackItem s)
		return type.equals(s.type) && line == s.line && pos == s.pos;
	    return false;
	}
    }

    static final class LineInfo
    {
	String firstType = null; //The type of the first element on the line
	StackItem firstStackItem = null; //The stack item for the first element on the line
	int calcIndent = 0;

    }

}
