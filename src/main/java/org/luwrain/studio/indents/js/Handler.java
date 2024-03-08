
package org.luwrain.studio.indents.js;

import java.util.*;

public class Handler
{
    static private final String
	BLOCK = "BlockContext",
	CLASS_BODY = "ClassTailContext",
		CLASS_EL = "ClassElementContext",
	FIELD = "FieldDefinitionContext",
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
		System.out.println("#" + type + " "  + line + ", " + pos);
	while (lines.size() < line + 1)
	    lines.add(new LineInfo());

	final LineInfo lineInfo = lines.get(line);
	final StackItem newStackItem;
	switch(type)
	{
	case STATEMENT:
	case CLASS_EL:
	newStackItem = new StackItem(type, line, pos);
	break;
	default:
	    newStackItem = null;
	}

	//Registering the first element on the line
	if (lineInfo.firstType == null && (
					   type.equals(STATEMENT) ||
					   type.equals(CLASS_EL)
					   ))
	{
	    lineInfo.firstType = type;
	    lineInfo.firstStackItem = newStackItem;
	    if (!stack.isEmpty())
	    {
		final var tail = stack.getLast();
		lineInfo.calcIndent = tail.prevStatementIndent >= 0?tail.prevStatementIndent:(tail.pos + step);
		//Saving the current position for the next statement
		tail.prevStatementIndent = pos;
	    }
	}

	if (newStackItem != null)
	{
	    stack.add(newStackItem);
	    return;
	}

	//Opening bracket of a function
	if (type.equals(FUNC_BODY) && lineInfo.firstType == null && !stack.isEmpty())
	{
	    lineInfo.firstType = FUNC_BODY;
	    lineInfo.calcIndent = stack.getLast().pos;
	    return;
	}

	//Opening bracket inside of a function
	if (type.equals(BLOCK) && !stack.isEmpty())
	{
	    //If it starts on the same position as the top stack statement, popping it
	    final var tail = stack.getLast();
	    if (tail.line == line && tail.pos == pos)
	    {
		stack.pollLast();
		//The popped statement set the prevStatementIndent of the function statement, cleaning it
		if (stack.isEmpty())
		    throw new IllegalStateException("No statement of the function in the stack");
		stack.getLast().prevStatementIndent = -1;
		//If the removed item was the first on the line, updating the calcIndent
		if (lineInfo.firstStackItem != null &&lineInfo.firstStackItem.equals(tail))
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

	//	System.out.println("Closing " + type + " " + endLine + " " + endPos + ", " + startLine + " " + startPos);
	//Processing the closing brackets
	if (lineInfo.firstType == null && !stack.isEmpty() &&  (
								type.equals( FUNC_BODY) || type.equals(CLASS_BODY) ||
type.equals(BLOCK)
			 )) {
	    lineInfo.calcIndent = stack.getLast().pos;
	}

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

    //Better to rename to Statement
        static final class StackItem
    {
	final String type;
	final int line, pos;
	int prevStatementIndent = -1;
	StackItem(String type, int line, int pos)
	{
	    /*
	    if (!type.equals(STATEMENT))
		throw new IllegalArgumentException("type can be only the statement (" + type + ")");
	    */
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
