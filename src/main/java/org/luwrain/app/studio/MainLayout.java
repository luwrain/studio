
package org.luwrain.app.studio;

import java.util.*;
import java.io.*;

import org.luwrain.base.*;
import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.template.*;

public final class MainLayout extends LayoutBase
{
    private final App app;
    private TreeArea treeArea = null;
    private Area workArea = null;
    private NavigationArea outputArea = null;

    MainLayout(App app)
    {
	NullCheck.notNull(app, "app");
	this .app = app;

 	this.treeArea = new TreeArea(createTreeParams()){
		@Override public boolean onInputEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (app.onInputEvent(this, event))
			return true;
		    return super.onInputEvent(event);
		}
		@Override public boolean onSystemEvent(EnvironmentEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (app.onSystemEvent(this, event))
			return true;
		    			return super.onSystemEvent(event);
		}
		@Override public Action[] getAreaActions()
		{
		    return new Action[0];
		}
	    };
	this.outputArea = new NavigationArea(new DefaultControlContext(app.getLuwrain())) {
		final Lines outputModel = null;//app.getOutputModel();
		@Override public boolean onInputEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (app.onInputEvent(this, event))
			return true;
		    return super.onInputEvent(event);
		}
		@Override public boolean onSystemEvent(EnvironmentEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (app.onSystemEvent(this, event))
			return true;
			return super.onSystemEvent(event);
		    }
		@Override public Action[] getAreaActions()
		{
		    return new Action[0];
		}
		@Override public int getLineCount()
		{
		    return outputModel.getLineCount();
		}
		@Override public String getLine(int index)
		{
		    return outputModel.getLine(index);
		}
		@Override public String getAreaName()
		{
		    return app.getStrings().outputAreaName();
		}
	    };
    }

    private Area createWorkArea(Editing editing)
    {
	NullCheck.notNull(editing, "editing");
	if (!(editing instanceof TextEditing))
	    return null;
	final TextEditing textEditing = (TextEditing)editing;
	final EditArea.Params editParams = textEditing.getEditParams(new DefaultControlContext(app.getLuwrain()));
	return new EditArea(editParams) {
	    @Override public boolean onInputEvent(KeyboardEvent event)
	    {
		NullCheck.notNull(event, "event");
		return super.onInputEvent(event);
	    }
	    @Override public boolean onSystemEvent(EnvironmentEvent event)
	    {
		NullCheck.notNull(event, "event");
		if (event.getType() != EnvironmentEvent.Type.REGULAR)
		    return super.onSystemEvent(event);
		switch(event.getCode())
		{
		    /*
		case SAVE:
		    return actions.onSaveEdit();
		    */
		default:
		    return super.onSystemEvent(event);
		}
	    }
	    @Override public Action[] getAreaActions()
	    {
		return null;
	    }
	};
    }

        private TreeArea.Params createTreeParams()
    {
	final TreeArea.Params params = new TreeArea.Params();
	params.context = new DefaultControlContext(app.getLuwrain());
	params.model = new CachedTreeModel(new TreeModel());
	params.name = app.getStrings().treeAreaName();
	//	params.clickHandler = clickHandler;
	return params;
    }

        boolean onTreeClick(Object obj)
    {
	
	NullCheck.notNull(obj, "obj");
	/*
	    if (!(obj instanceof Part))
		return false;
	    final Part part = (Part)obj;
	    final Editing editing;
	    try {
	    editing = part.startEditing();
	    }
	    catch(IOException e)
	    {
		luwrain.message(luwrain.i18n().getExceptionDescr(e));
		return true;
	    }
	    if (editing == null)
		return false;
	    return layouts.editing(editing);
	*/
    return false;
    }



    

            private final class TreeModel implements CachedTreeModelSource
    {
	@Override public Object getRoot()
	{
	    return app.getTreeRoot();
	}
	@Override public Object[] getChildObjs(Object obj)
	{
	    NullCheck.notNull(obj, "obj");
	    if (app.getProject() == null || !(obj instanceof Part))
		return new Object[0];
	    final Part part = (Part)obj;
	    final Part[] res = part.getChildParts();
	    if (res == null)
		return new Object[0];
	    for(int i = 0;i < res.length;i++)
		if (res[i] == null)
		    return new Object[0];
	    return res;
	    	}
    }


    

}
