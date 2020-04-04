
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

    File newProjectDir()
    {
	return Popups.existingDir(luwrain, "Создание проекта", "Каталог для нового проекта:");
    }

    File openProject()
    {
	return Popups.existingFile(luwrain, strings.openProjectPopupName(), strings.openProjectPopupPrefix(), new String[0]);
	    }
    }
