
package org.luwrain.app.studio;

import java.util.*;
import java.io.*;

import org.luwrain.base.*;
import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.template.*;

final class NewProjectLayout extends LayoutBase implements ListArea.ClickHandler
{
    private final App app;
    private final ListArea newProjectArea;

    NewProjectLayout(App app)
        {
	    NullCheck.notNull(app, "app");
	    this.app = app;
	    this.newProjectArea = new ListArea(createParams()){
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
		    };
	}

    	        @Override public boolean onListClick(ListArea listArea,int index,Object obj)
    {
	if (obj == null || !(obj instanceof ProjectType))
	    return false;
	/*
	final File destDir = actions.conv.newProjectDir();
	final ProjectType projType = (ProjectType)obj;
	if (destDir == null)
	    return true;
	final ProjectFactory factory = new ProjectFactory(luwrain);
	final Project proj;
	try {
proj = factory.create(projType.getId(), destDir);
	}
	catch(IOException e)
	{
	    luwrain.message(luwrain.i18n().getExceptionDescr(e), Luwrain.MessageType.ERROR);
	    return true;
	}
	if (proj != null)
	    onNewProject(proj);
	*/
	return true;
    }

private ListArea.Params createParams()
    {
	final ListArea.Params params = new ListArea.Params();
	params.context = new DefaultControlContext(app.getLuwrain());
	params.model = new ListUtils.FixedModel(new ProjectFactory(app.getLuwrain()).getNewProjectTypes());
	params.appearance = new ListUtils.DefaultAppearance(params.context){
		@Override public void announceItem(Object item, Set<Flags> flags)
		{
		    NullCheck.notNull(item, "item");
		    NullCheck.notNull(flags, "flags");
		    app.getLuwrain().setEventResponse(DefaultEventResponse.listItem(app.getLuwrain().getSpeakableText(item.toString(), Luwrain.SpeakableTextType.NATURAL)));
		}
	    };
	params.name = "Новый проект";//FIXME:
	return params;
    }
}
