
package org.luwrain.app.studio ;

import org.luwrain.core.*;
import org.luwrain.controls.*;

final class EditCorrectorWrapper implements MultilineEditCorrector
{
    private MultilineEditCorrector activatedCorrector = null;
    private MultilineEditCorrector wrappedCorrector = null;

    void setActivatedCorrector(MultilineEditCorrector corrector)
    {
	NullCheck.notNull(corrector, "corrector");
	this.activatedCorrector = corrector;
    }

    void deactivateCorrector()
    {
	this.activatedCorrector = null;
    }

    void setWrappedCorrector(MultilineEditCorrector corrector)
    {
	NullCheck.notNull(corrector, "corrector");
	this.wrappedCorrector = corrector;
    }

    MultilineEditCorrector getWrappedCorrector()
    {
	return wrappedCorrector;
    }

    @Override public int getLineCount()
    {
	if (activatedCorrector != null)
	    return activatedCorrector.getLineCount();
	return wrappedCorrector.getLineCount();
    }

    @Override public String getLine(int index)
    {
	if (activatedCorrector != null)
	    return activatedCorrector.getLine(index);
	return wrappedCorrector.getLine(index);
    }

    @Override public int getHotPointX()
    {
	if (activatedCorrector != null)
	    return activatedCorrector.getHotPointX();
	return wrappedCorrector.getHotPointX();
    }

    @Override public int getHotPointY()
    {
	if (activatedCorrector != null)
	    return activatedCorrector.getHotPointY();
	return wrappedCorrector.getHotPointY();
    }

    @Override public String getTabSeq()
    {
	if (activatedCorrector != null)
	    return activatedCorrector.getTabSeq();
	return wrappedCorrector.getTabSeq();
    }

    @Override public char deleteChar(int pos, int lineIndex)
    {
	if (activatedCorrector != null)
	    return activatedCorrector.deleteChar(pos, lineIndex);
	return wrappedCorrector.deleteChar(pos, lineIndex);
    }

    @Override public boolean deleteRegion(int fromX, int fromY, int toX, int toY)
    {
	if (activatedCorrector != null)
	    return activatedCorrector.deleteRegion(fromX, fromY, toX, toY);
	return wrappedCorrector.deleteRegion(fromX, fromY, toX, toY);
    }

    @Override public boolean insertRegion(int x, int y, String[] lines)
    {
	NullCheck.notNullItems(lines, "lines");
	if (activatedCorrector != null)
	    return activatedCorrector.insertRegion(x, y, lines);
	return wrappedCorrector.insertRegion(x, y, lines);
    }

    @Override public boolean insertChars(int pos, int lineIndex, String str)
    {
	NullCheck.notNull(str, "str");
	if (activatedCorrector != null)
	    return activatedCorrector.insertChars(pos, lineIndex, str);
	    return wrappedCorrector.insertChars(pos, lineIndex, str);
    }

    @Override public boolean mergeLines(int firstLineIndex)
    {
	if (activatedCorrector != null)
	    return activatedCorrector.mergeLines(firstLineIndex);
	    return wrappedCorrector.mergeLines(firstLineIndex);
    }

    @Override public String splitLine(int pos, int lineIndex)
    {
	if (activatedCorrector != null)
	    return activatedCorrector.splitLine(pos, lineIndex);
	return wrappedCorrector.splitLine(pos, lineIndex);
    }

    @Override public void doDirectAccessAction(DirectAccessAction action)
    {
	if (activatedCorrector != null)
	    activatedCorrector.doDirectAccessAction(action); else
	    wrappedCorrector.doDirectAccessAction(action);
    }
}
