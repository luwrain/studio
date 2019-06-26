
package org.luwrain.controls;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.util.*;

import org.luwrain.controls.MultilineEdit2.ModificationResult;

    public class ScriptMultilineEditCorrector implements MultilineEditCorrector2
    {
	public String getLine(int index)
	{
	    return "";
	}

	public int getLineCount()
	{
	    return 0;
	}
	
	public int getHotPointX()
	{
	    return 0;
	}
	
	public int getHotPointY()
	{
	    return 0;
	}
	
	public String getTabSeq()
	{
	    return null;
	}
	
	public ModificationResult deleteChar(int pos, int lineIndex)
	{
	    return null;
	}
	

	public ModificationResult deleteRegion(int fromX, int fromY, int toX, int toY)
	{
	    return null;
	}

	public ModificationResult insertRegion(int x, int y, String[] lines)
	{
	    return null;
	}

	public ModificationResult putChars(int pos, int lineIndex, String str)
	{
	    return null;
	}

	public ModificationResult mergeLines(int firstLineIndex)
	{
	    return null;
	}

public ModificationResult splitLine(int pos, int lineIndex)
	{
	    return null;
	}

public ModificationResult doEditAction(TextEditAction action)
	{
	    return null;
	}
    }
