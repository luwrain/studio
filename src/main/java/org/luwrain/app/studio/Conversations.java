
package org.luwrain.app.studio;

import java.io.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;

class Conversations
{
    private final Luwrain luwrain;
    private final Strings strings;

    Conversations(Luwrain luwrain, Strings strings)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
	this.luwrain = luwrain;
	this.strings = strings;
    }

    File openProject()
    {
	return Popups.path(luwrain, strings.openProjectPopupName(), strings.openProjectPopupPrefix(),
			   (fileToCheck,announce)->{
			       return true;
			   });
    }
    }
