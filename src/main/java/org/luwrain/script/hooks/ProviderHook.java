/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.script.hooks;

import java.util.concurrent.atomic.*;

import org.luwrain.core.*;
import org.luwrain.script.*;

public class ProviderHook
{
    protected final Luwrain luwrain;

    public ProviderHook(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }

    public Object run(String hookName, Object[] args)
    {
	NullCheck.notEmpty(hookName, "hookName");
	NullCheck.notNullItems(args, "args");
	final AtomicReference res = new AtomicReference();
	luwrain.xRunHooks(hookName, (hook)->{
		try {
		    final Object obj = hook.run(args);
		    if (obj == null)
			return Luwrain.HookResult.CONTINUE;
		    res.set(obj);
		    return Luwrain.HookResult.BREAK;
		}
		catch(RuntimeException e)
		{
		    res.set(e);
		    return Luwrain.HookResult.BREAK;
		}
	    });
	if (res.get() == null)
	    return null;
	if (res.get() instanceof RuntimeException)
	    throw (RuntimeException)res.get();
	return res.get();
    }
}
