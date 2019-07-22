
package org.luwrain.app.studio;

import java.util.*;

import com.vk.api.sdk.objects.wall.WallPost;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import com.vk.api.sdk.objects.wall.PostType;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.core.queries.*;
import org.luwrain.controls.*;

class NewProjectArea extends ListArea
{
    private final Luwrain luwrain;
    private final Strings strings;
    private final Base base;
    private final Actions actions;

    NewProjectArea(Base base, Actions actions)
    {
	super(createParams(base.luwrain, base.strings, base));
	this.luwrain = base.luwrain;
	this.strings = base.strings;
	this.base = base;
	this.actions = actions;
    }

    @Override public boolean onInputEvent(KeyboardEvent event)
    {
	NullCheck.notNull(event, "event");
	if (event.isSpecial() && !event.isModified())
	    switch(event.getSpecial())
	    {
	    case ESCAPE:
		base.closeApp();
		return true;
	    }
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
	case ACTION:
	    if (ActionEvent.isAction(event, "delete"))
	    {
		final Object selected = selected();
		if (selected == null || !(selected instanceof WallPostFull))
		    return false;
		//FIXME:confirmation
		if (!actions.onWallDelete((WallPostFull)selected, ()->{
			    refresh();
			    luwrain.onAreaNewBackgroundSound(this);
			    luwrain.playSound(Sounds.OK);
			}, ()->luwrain.onAreaNewBackgroundSound(this)))
		    return false;
		luwrain.onAreaNewBackgroundSound(this);
		return true;
	    }
	    return super.onSystemEvent(event);
	    */
	case CLOSE:
	    base.closeApp();
	    return true;
	default:
	    return super.onSystemEvent(event);
	}
    }

    @Override public Action[] getAreaActions()
    {
	return new Action [0];
	//	return actions.lists.getWallActions();
    }

    static private ListArea.Params createParams(Luwrain luwrain, Strings strings, Base base)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
	NullCheck.notNull(base, "base");
	final ListArea.Params params = new ListArea.Params();
	params.context = new DefaultControlContext(luwrain);
	params.model = new ListUtils.FixedModel(base.getNewProjectTypes());
	params.appearance = new ListUtils.DefaultAppearance(params.context);
	params.name = "Новый проект";//FIXME:
	return params;
    }
}
