
package org.luwrain.app.studio;

import java.util.*;
import org.apache.commons.vfs2.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;

class ActionList
{private final Strings strings;

    ActionList(Strings strings)
    {
	NullCheck.notNull(strings, "strings");
	this.strings = strings;
    }

    Action[] getPanelAreaActions()
    {
	return new Action[0];
    }
}
