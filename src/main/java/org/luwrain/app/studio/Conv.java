/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.app.studio;

import java.io.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;

import static org.luwrain.popups.Popups.*;

class Conv
{
    enum SavingType {SAVE, NOT_SAVE, CANCEL};

    private final Luwrain luwrain;
    private final Strings strings;

    Conv(App app)
    {
	NullCheck.notNull(app, "app");
	this.luwrain = app.getLuwrain();
	this.strings = app.getStrings();
    }

    File newProjectDir() { return existingDir(luwrain, "Каталог нового проекта:"); }
    File openProject() { return existingFile(luwrain, strings.openProjectPopupPrefix()); }

        SavingType unsavedChanges()
    {
	final YesNoPopup popup = new YesNoPopup(luwrain, "Несохранённые изменения", "Вы хотите сохранить изменения?", true, Popups.DEFAULT_POPUP_FLAGS);//FIXME:
	luwrain.popup(popup);
	if (popup.wasCancelled())
	    return SavingType.CANCEL;
	return popup.result()?SavingType.SAVE:SavingType.NOT_SAVE;
    }
    }
