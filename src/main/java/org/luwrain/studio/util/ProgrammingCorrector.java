
package org.luwrain.studio.util;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.MultilineEdit.ModificationResult;

public class ProgrammingCorrector extends EditUtils.EmptyCorrector
{
    public ProgrammingCorrector(MultilineEditCorrector basicCorrector)
    {
	super(basicCorrector);
    }

    public int getIndent(int lineIndex)
    {
	final int tabLen = 4;
	final String line = getLine(lineIndex);
	int res = 0;
	for(int i = 0;i < line.length() && Character.isWhitespace(line.charAt(i));i++)
	    if (line.charAt(i) == '\t')
		res += tabLen; else
		++res;
	return res;
    }

    public boolean deleteIndent(int lineIndex)
    {
	final String line = getLine(lineIndex);
	int pos = 0;
	while (pos < line.length() && Character.isWhitespace(line.charAt(pos)))
	    pos++;
	if (pos == 0)
	    return true;
	return basicCorrector.deleteRegion(0, lineIndex, pos, lineIndex).isPerformed();
    }

    public boolean addIndent(int lineIndex, int len)
    {
	if (len == 0)
	    return true;
	final int tabLen = 4;
	final StringBuilder b = new StringBuilder();
	final int tabCount = len / tabLen;
	for(int i = 0;i < tabCount;i++)
	    b.append('\t');
	final int spaceCount = len % tabLen;
	for(int i = 0;i < spaceCount;i++)
	    b.append(' ');
	return basicCorrector.putChars(0, lineIndex, new String(b)).isPerformed();
    }
}