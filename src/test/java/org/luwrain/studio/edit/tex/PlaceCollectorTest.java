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
