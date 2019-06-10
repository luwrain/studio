
package org.luwrain.app.studio;

import javax.script.*;

import org.luwrain.core.*;

final class ScriptExceptionWrapper
{
    final ScriptException ex;

    ScriptExceptionWrapper(ScriptException ex)
    {
	NullCheck.notNull(ex, "ex");
	this.ex = ex;
    }

    @Override public String toString()
    {
	return "Строка " + ex.getLineNumber() + ":" + ex.getMessage();//FIXME:
    }
}
