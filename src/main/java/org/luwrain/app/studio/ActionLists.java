
package org.luwrain.app.studio;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;

class ActionLists
{
    private final Strings strings;
    private final Base base;

    ActionLists(Strings strings, Base base)
    {
	NullCheck.notNull(strings, "strings");
	NullCheck.notNull(base, "base");
	this.strings = strings;
	this.base = base;
    }

    private List<Action> commonActions()
    {
	final List<Action> res = new LinkedList();
	res.add(new Action("open-project", strings.actionOpenProject()));
	if (base.getProject() != null && base.getProject().getBuildFlavors().length > 0)
	    res.add(new Action("build", strings.actionBuild()));
	if (base.getProject() != null)
	    res.add(new Action("run", strings.actionRun(), new KeyboardEvent(KeyboardEvent.Special.F9, EnumSet.of(KeyboardEvent.Modifiers.ALT))));
	return res;
    }

    Action[] getTreeActions()
    {
	final List<Action> res = commonActions();
	return res.toArray(new Action[res.size()]);
    }

    Action[] getEditActions()
    {
	final List<Action> res = commonActions();
	return res.toArray(new Action[res.size()]);
    }

    Action[] getOutputActions()
    {
	final List<Action> res = commonActions();
	return res.toArray(new Action[res.size()]);
    }
}
