
package org.luwrain.studio.backends.ly;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.MultilineEdit.ModificationResult;
import org.luwrain.studio.util.*;

final class LyCorrector extends ProgrammingCorrector
{
    LyCorrector(MultilineEditCorrector basicCorrector)
    {
	super(basicCorrector);
    }

            @Override public ModificationResult splitLine(int pos, int lineIndex)
    {
	final ModificationResult res = basicCorrector.splitLine(pos, lineIndex);
	if (!res.isPerformed())
	    return res;
	final int indent = getIndent(lineIndex);
	if (!deleteIndent(lineIndex + 1))
	    return new ModificationResult(false);
	if (!addIndent(lineIndex + 1, indent))
	    return new ModificationResult(false);  
return res;
    }

        @Override public ModificationResult putChars(int pos, int lineIndex, String str)
    {
	return basicCorrector.putChars(pos, lineIndex, str);
    }
}
