
package org.luwrain.studio.syntax;

import org.luwrain.core.*;

public final class IndentUtils
{
    final SyntaxParams params;
    public IndentUtils(SyntaxParams params)
    {
	NullCheck.notNull(params, "params");
	this.params = params;
    }

public int getIndent(String line)
    {
	int res = 0;
	for(int i = 0;i < line.length() && Character.isWhitespace(line.charAt(i));i++)
	    if (line.charAt(i) == '\t')
		res += params.getTabLen(); else
		++res;
	return res;
    }
    }
