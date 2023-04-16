/*
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 *  Copyright @2019 Jerome Lelasseux. All rights reserved.
 *
 *  This file is part of the JJazzLabX software.
 *   
 *  JJazzLabX is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License (LGPLv3) 
 *  as published by the Free Software Foundation, either version 3 of the License, 
 *  or (at your option) any later version.
 *
 *  JJazzLabX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with JJazzLabX.  If not, see <https://www.gnu.org/licenses/>
 * 
 *  Contributor(s): 
 */
package org.jjazz.util.api;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.io.Serializable;

/**
 * String-value based and serializable ObservableProperties.
 */
public class StringProperties extends ObservableProperties<String> implements Serializable
{

    public StringProperties()
    {
    }

    public StringProperties(StringProperties sp)
    {
        super(sp);
    }

    public int getInt(String prop, int defaultValue)
    {
        String s = get(prop);
        int res = defaultValue;
        try
        {
            res = Integer.parseInt(s);
        } catch (NumberFormatException ex)
        {
            // Nothing
        }
        return res;
    }

    public void putInt(String prop, int value)
    {
        put(prop, String.valueOf(value));
    }

    public Color getColor(String prop, Color defaultValue)
    {
        Integer rgb = getInt(prop, defaultValue != null ? defaultValue.getRGB() : null);
        return rgb != null ? new Color(rgb) : null;
    }

    public void putColor(String prop, Color value)
    {
        putInt(prop, value != null ? value.getRGB() : null);
    }
}