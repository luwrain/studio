
package org.luwrain.app.studio;

import java.io.*;
import java.util.*;
import java.net.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.util.*;

final class Actions
{
    private final Luwrain luwrain;
    private final Base base;
    private final Strings strings;

    final Layouts layouts;
    final Conversations conv;

    Actions(Base base, Layouts layouts)
    {
	NullCheck.notNull(base, "base");
	NullCheck.notNull(layouts, "layouts");
	this.luwrain = base.luwrain;
	this.base = base;
	this.strings = base.strings;
	this.conv = new Conversations(luwrain, strings);
	this.layouts = layouts;
    }

    /*
    boolean onOpenProject(TreeArea treeArea)
    {
	NullCheck.notNull(treeArea, "treeArea");
	final File projFile = conv.openProject();
	if (projFile == null)
	    return true;
	final Project proj;
	try {
	    proj = ProjectFactory.load(projFile);
	}
	catch(IOException e)
	{
	    luwrain.message(luwrain.i18n().getExceptionDescr(e), Luwrain.MessageType.ERROR);
	    return true;
	}
	base.activateProject(proj);
	treeArea.refresh();
	return true;
    }
    */


    boolean onRun(NavigationArea outputArea)
    {
	NullCheck.notNull(outputArea, "outputArea");
	/*
	if (base.openedEditing != null)
	{
	    try {
		FileUtils.writeTextFileMultipleStrings(base.openedEditing.getFile(), base.fileText.getLines(), Base.CHARSET, null);
	    }
	    catch(IOException e)
	    {
		luwrain.message(luwrain.i18n().getExceptionDescr(e), Luwrain.MessageType.ERROR);
		return true;
	    }
	}
	try {
	    return base.runProject(()->outputArea.redraw());
	}
	catch(IOException e)
	{
	    luwrain.message(luwrain.i18n().getExceptionDescr(e));
	    return true;
	}
return false;
	*/
	return false;
    }

    boolean onSaveEdit()
    {
	/*
	if (base.openedEditing == null)
	    return false;
	try {
	FileUtils.writeTextFileMultipleStrings(base.openedEditing.getFile(), base.fileText.getLines(), Base.CHARSET, null);
	luwrain.message(strings.fileSavedSuccessfully(), Luwrain.MessageType.OK);	    
	}
	catch(IOException e)
	{
	    luwrain.message(luwrain.i18n().getExceptionDescr(e), Luwrain.MessageType.ERROR);
	}
	*/
	return true;
    }

    boolean onOutputClick(int lineIndex, EditArea editArea)
    {
	final Base.PositionInfo posInfo = base.getCompilationOutputPositionInfo(lineIndex);
	if (posInfo == null)
	    return false;
	//FIXME:ensure the corresponding file is opened
	editArea.setHotPoint(posInfo.colNum > 0?posInfo.colNum:0, posInfo.lineNum - 1);
	luwrain.setActiveArea(editArea);
	return true;
    }
}
