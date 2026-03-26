
package org.luwrain.studio.edit.tex;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlaceCollectorTest
{
    private PlaceCollector c = null;

    
    @Test public void simple()
    {
	c.collect(null);
    }

    @BeforeEach void createPlaceCollector()
    {
	c = new PlaceCollector();
    }
}
