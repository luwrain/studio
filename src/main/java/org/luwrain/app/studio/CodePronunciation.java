
package org.luwrain.app.studio;

import org.luwrain.core.*;

final class CodePronunciation
{
    private final Luwrain luwrain;
    private final Strings strings;

    CodePronunciation(Luwrain luwrain, Strings strings)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
	this.luwrain = luwrain;
	this.strings = strings;
    }

    void announceLine(String line)
    {
	if (line == null || line.isEmpty())
	{
	    luwrain.setEventResponse(DefaultEventResponse.hint(Hint.EMPTY_LINE));
	    return;
	}
	final int indentLen = getIndent(line);
	final String indent = (indentLen > 0)?strings.codeIndent("" + indentLen):"";
	if (line.trim().startsWith("//"))
	{
	    luwrain.speak(indent + " " + strings.codeComments() + " " + line.trim().substring(2));
	    return;
	}
	if (line.trim().equals("{"))
	{
	    luwrain.speak(indent + " " + strings.codeBlockBegin());
	    return;
	}
	if (line.trim().equals("}"))
	{
	    luwrain.speak(indent + " " + strings.codeBlockEnd());
	    return;
	}
	luwrain.speak(indent + " " + luwrain.getSpokenText(line.trim(), Luwrain.SpokenTextType.PROGRAMMING));
    }

    private int getIndent(String line)
    {
	NullCheck.notNull(line, "line");
	int res = 0;
	for(int i = 0;i < line.length();++i)
	    switch(line.charAt(i))
	    {
	    case ' ':
		++res;
		break;
	    case '\t':
		res += 8;
		break;
	    default:
		return res;
	    }
	return res;
    }
}
