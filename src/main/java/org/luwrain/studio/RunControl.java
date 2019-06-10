
//LWR_API 1.0

package org.luwrain.studio;

public interface RunControl
{
    java.util.concurrent.Callable getCallableObj();
    boolean isContinuous();
}
