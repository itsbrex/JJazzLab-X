/*
 * 
 *   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 *   Copyright @2019 Jerome Lelasseux. All rights reserved.
 * 
 *   This file is part of the JJazzLab software.
 *    
 *   JJazzLab is free software: you can redistribute it and/or modify
 *   it under the terms of the Lesser GNU General Public License (LGPLv3) 
 *   as published by the Free Software Foundation, either version 3 of the License, 
 *   or (at your option) any later version.
 * 
 *   JJazzLab is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *  
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with JJazzLab.  If not, see <https://www.gnu.org/licenses/>
 *  
 *   Contributor(s): 
 * 
 */
package org.jjazz.test.walkingbass;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import org.jjazz.rhythmmusicgeneration.api.SimpleChordSequence;

/**
 * Which WbpSources (various sizes) cover which bars of a SimpleChordSequence.
 */
class WbpSourceTiling
{

    private final SimpleChordSequence simpleChordSequence;
    private final Object[] wbpSources; // a cell can contain: null (unset), a WbpSource instance, or an Integer (normally 1 or 3) for the subsequent bars covered by the WbpSource instance.
    private final int barOffset;
    private final Map<WbpSource, Integer> mapWbpSourceIndex;
    private static final Logger LOGGER = Logger.getLogger(WbpSourceTiling.class.getSimpleName());

    public WbpSourceTiling(SimpleChordSequence scs)
    {
        this.simpleChordSequence = scs;
        wbpSources = new Object[scs.getBarRange().size()];
        barOffset = scs.getBarRange().from;
        mapWbpSourceIndex = new HashMap<>();
    }

    public SimpleChordSequence getSimpleChordSequence()
    {
        return simpleChordSequence;
    }

    /**
     * Set the WbpSource for specified bar.
     * <p>
     *
     * @param bar
     * @param wbpSource Can not be null
     * @throws IllegalArgumentException If wbpSource will overwrite existing WbpSource(s).
     */
    public void set(int bar, WbpSource wbpSource)
    {
        Objects.requireNonNull(wbpSource);
        Preconditions.checkArgument(simpleChordSequence.getBarRange().contains(bar), "bar=%s", bar);

        int index = bar - barOffset;
        int size = wbpSource.getBarRange().size();
        if (!isFree(bar, size))
        {
            throw new IllegalArgumentException("Can not set " + wbpSource + " at bar " + bar + ": there are already 1 or more WbpSources on these bars");
        }

        wbpSources[index] = wbpSource;
        mapWbpSourceIndex.put(wbpSource, index);
        for (int i = 1; i < size; i++)
        {
            wbpSources[index + i] = Integer.valueOf(i);
        }

    }

    /**
     * Remove wbpSource from this tiling.
     *
     * @param wbpSource
     * @return True if wbpSource was actually removed.
     */
    public boolean remove(WbpSource wbpSource)
    {
        int bar = getStartBar(wbpSource);
        if (bar == -1)
        {
            return false;
        }
        int index = bar - barOffset;
        int size = wbpSource.getBarRange().size();
        for (int i = 0; i < size; i++)
        {
            wbpSources[index + i] = null;
        }
        mapWbpSourceIndex.remove(wbpSource);
        return true;
    }

    /**
     * Get all the WbpSources ordered by start bar.
     *
     * @param size Return only WbpSources of this size. If -1, return all WbpSources.
     * @return
     */
    public List<WbpSource> getWbpSources(int size)
    {
        List<WbpSource> res = new ArrayList<>();
        for (int i = 0; i < wbpSources.length; i++)
        {
            var wbpSource = getWbpSourceOrNull(i);
            if (wbpSource != null && (size == -1 || wbpSource.getBarRange().size() == size))
            {
                res.add(wbpSource);
            }
        }
        return res;
    }


    /**
     * Get the WbpSource covering the specified bar.
     *
     * @param bar
     * @return Might be null. Note that returned WbpSource might start before bar.
     */
    public WbpSource getWbpSource(int bar)
    {
        Preconditions.checkArgument(simpleChordSequence.getBarRange().contains(bar), "bar=%s", bar);
        int index = bar - barOffset;
        WbpSource res = getWbpSourceOrNull(index);
        Integer intValue;
        if (res == null && (intValue = getIntValueOrNull(index)) != null)
        {
            res = (WbpSource) wbpSources[index - intValue];
        }
        return res;
    }

    /**
     * Get the start bar of wbpSource.
     *
     * @param wbpSource
     * @return -1 wbpSource is not used.
     */
    public int getStartBar(WbpSource wbpSource)
    {
        Integer index = mapWbpSourceIndex.get(wbpSource);
        return index == null ? -1 : index + barOffset;
    }

    public boolean isCompletlyTiled()
    {
        for (var wbp : wbpSources)
        {
            if (wbp == null)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the start bar indexes of zones not covered by a WbpSource.
     *
     * @param zoneSize Typically 1, 2 or 4 bars.
     * @return
     */
    public List<Integer> getUntiledZonesStartBarIndexes(int zoneSize)
    {
        List<Integer> res = new ArrayList<>();
        for (int bar = barOffset; bar < wbpSources.length; bar++)
        {
            if (isFree(bar, zoneSize))
            {
                res.add(bar);
            }
        }
        return res;
    }

    /**
     * Select WbpSources from wbpSrcArray to tile this object.
     * <p>
     * Tile strategy is to place the most compatible WbpSource first, then continue with remaining WbpSources, until it's not possible to tile any remaining
     * WbpSource from wbpSrcArray.<p>
     *
     * @param wbpSrcArray Must be constructed on the same SimpleChordSequence
     */
    public void tileMostCompatibleFirst(WbpSourceArray wbpSrcArray)
    {
        Preconditions.checkArgument(wbpSrcArray.getSimpleChordSequence() == simpleChordSequence, "simpleChordSequence=%s", simpleChordSequence);

        int wbpSrcSize = wbpSrcArray.getWbpSourceSize();

        // Sort WbpSources per score (descending)
        var wbpsOrderedByScore = new ArrayList<>(wbpSrcArray.getWbpSources());
        wbpsOrderedByScore.sort((wbp1, wbp2) -> Float.compare(wbpSrcArray.getCompatibilityScore(wbp2), wbpSrcArray.getCompatibilityScore(wbp1)));


        for (var wbpSource : wbpsOrderedByScore)
        {
            int bar = wbpSrcArray.getStartBar(wbpSource);
            if (isFree(bar, wbpSrcSize))
            {
                set(bar, wbpSource);
            }
        }

    }

    /**
     * Select WbpSources from wbpSrcArray to tile this object.
     * <p>
     * Tile strategy is to maximize the number of tiled WbpSources.
     *
     * @param wbpSrcArray
     */
    public void tileMaximizeNbWbpSources(WbpSourceArray wbpSrcArray)
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String toString()
    {
        return toString(barOffset, wbpSources);
    }

    protected static String toString(int barOffset, Object[] objects)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < objects.length; i++)
        {
            Object o = objects[i];
            String s = "null";
            if (o instanceof WbpSource wbps)
            {
                s = wbps.toLongString();
            } else if (o instanceof Integer in)
            {
                s = "WbpSource idem " + (in + 1);
            }
            sb.append("bar ").append(i + barOffset).append(": ").append(s).append("\n");
        }
        return sb.toString();
    }

    // =================================================================================================================
    // Private methods
    // =================================================================================================================

    private WbpSource getWbpSourceOrNull(int index)
    {
        if (wbpSources[index] instanceof WbpSource wbps)
        {
            return wbps;
        } else
        {
            return null;
        }
    }

    private Integer getIntValueOrNull(int index)
    {
        if (wbpSources[index] instanceof Integer in)
        {
            return in;
        } else
        {
            return null;
        }
    }


    private boolean isFree(int bar, int nbBars)
    {
        Preconditions.checkArgument(bar >= barOffset, "bar=%d barOffset=%d", bar, barOffset);
        int index = bar - barOffset;
        for (int i = 0; i < nbBars; i++)
        {
            if (index + i >= wbpSources.length || wbpSources[index + i] != null)
            {
                return false;
            }
        }
        return true;
    }


}
