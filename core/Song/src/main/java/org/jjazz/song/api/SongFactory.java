/*
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 *  Copyright @2019 Jerome Lelasseux. All rights reserved.
 *
 *  This file is part of the JJazzLab software.
 *   
 *  JJazzLab is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License (LGPLv3) 
 *  as published by the Free Software Foundation, either version 3 of the License, 
 *  or (at your option) any later version.
 *
 *  JJazzLab is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with JJazzLab.  If not, see <https://www.gnu.org/licenses/>
 * 
 *  Contributor(s): 
 */
package org.jjazz.song.api;

import com.google.common.base.Preconditions;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.logging.Logger;
import org.jjazz.harmony.api.TimeSignature;
import org.jjazz.chordleadsheet.api.ChordLeadSheet;
import org.jjazz.chordleadsheet.api.ChordLeadSheetFactory;
import org.jjazz.chordleadsheet.api.UnsupportedEditException;
import org.jjazz.chordleadsheet.api.item.CLI_Section;
import org.jjazz.songstructure.api.SongStructure;
import org.jjazz.songstructure.api.SongPart;
import org.jjazz.songstructure.api.SongStructureFactory;
import org.openide.util.Exceptions;

/**
 * Manage the creation and the registration of the songs.
 * <p>
 * All songs created by this factory are automatically registered. Registered songs are unregistered when song is closed.
 */
public class SongFactory implements PropertyChangeListener
{

    static private SongFactory INSTANCE;
    // Use WeakReference to avoid a memory leak if for some reason a closed song was not unregistered. Integer value is not used. 
    private final WeakHashMap<Song, Integer> songs;
    /**
     * Used to make sure we don't have the same name twice.
     */
    private static int counter = 1;

    private static final Logger LOGGER = Logger.getLogger(SongFactory.class.getSimpleName());

    static public SongFactory getInstance()
    {
        synchronized (SongFactory.class)
        {
            if (INSTANCE == null)
            {
                INSTANCE = new SongFactory();
            }
        }
        return INSTANCE;
    }

    private SongFactory()
    {
        songs = new WeakHashMap<>();
    }

    /**
     * All songs created by this object are automatically registered.
     *
     *
     * @return A list of the songs registered by this object.
     */
    public List<Song> getRegisteredSongs()
    {
        return new ArrayList<>(songs.keySet());
    }

    /**
     * Register a song if it was not created by the SongManager.
     *
     * @param sg
     */
    public void registerSong(Song sg)
    {
        if (songs.put(sg, 0) == null)
        {
            sg.addPropertyChangeListener(this);
        }
    }

    /**
     * Provide a new song name which is not used by any currently opened song.
     *
     * @param baseName Can't be blank
     * @return
     */
    public String getNewSongName(String baseName)
    {
        Preconditions.checkArgument(baseName != null && !baseName.isBlank(), "baseName=%s", baseName);
        String name = baseName + counter;
        while (!isSongNameUsed(name))
        {
            counter++;
            name = baseName + counter;
        }
        return name;
    }

    /**
     * Remove a song from the list returned by getRegisteredSong().
     *
     * @param song
     */
    public void unregisterSong(Song song)
    {
        songs.remove(song);
        song.removePropertyChangeListener(this);
    }

    /**
     * Find in the created song the first one which uses the specified SongStructure.
     *
     * @param sgs
     * @return
     */
    public Song findSong(SongStructure sgs)
    {
        Song res = null;
        for (Song song : songs.keySet())
        {
            if (song.getSongStructure() == sgs)
            {
                res = song;
                break;
            }
        }
        return res;
    }

    /**
     * Find in the created song the first one which uses the specified ChordLeadSheet.
     *
     * @param cls
     * @return
     */
    public Song findSong(ChordLeadSheet cls)
    {
        Song res = null;
        for (Song song : songs.keySet())
        {
            if (song.getChordLeadSheet() == cls)
            {
                res = song;
                break;
            }
        }
        return res;
    }

    /**
     * Create a Song from the specified chordleadsheet.
     *
     * @param name
     * @param cls
     * @return
     * @throws UnsupportedEditException Can happen if too many timesignature changes resulting in not enough Midi channels for the various rhythms.
     */
    public Song createSong(String name, ChordLeadSheet cls) throws UnsupportedEditException
    {
        if (name == null || name.isEmpty() || cls == null)
        {
            throw new IllegalArgumentException("name=" + name + " cls=" + cls);
        }
        Song song = new Song(name, cls);
        registerSong(song);
        return song;
    }

    /**
     * Create a Song from the specified chordleadsheet.
     *
     * @param name
     * @param cls
     * @param sgs  Must be kept consistent with cls changes (sgs.getParentChordLeadSheet() must be non null)
     * @return
     * @throws UnsupportedEditException Can happen if too many timesignature changes resulting in not enough Midi channels for the various rhythms.
     */
    public Song createSong(String name, ChordLeadSheet cls, SongStructure sgs) throws UnsupportedEditException
    {
        if (name == null || name.isEmpty() || cls == null || sgs == null)
        {
            throw new IllegalArgumentException("name=" + name + " cls=" + cls + " sgs=" + sgs);
        }
        Song song = new Song(name, cls, sgs);
        registerSong(song);
        return song;
    }

    /**
     * Create a 8-bar empty song with only the 4/4 initial Section named "A" and its corresponding SongPart.
     *
     * @param songName
     * @return
     */
    public Song createEmptySong(String songName)
    {
        return createEmptySong(songName, 8, "A", TimeSignature.FOUR_FOUR, null);
    }

    /**
     * Create an empty song with the specified parameters.
     * <p>
     *
     * @param songName        The name of the song
     * @param nbBars
     * @param initSectionName The name of the initial section
     * @param ts              The time signature of the initial section
     * @param initialChord    eg "Cm7". A string describing an initial chord to be put at the start of the song. If null no chord is inserted.
     * @return
     */
    public Song createEmptySong(String songName, int nbBars, String initSectionName, TimeSignature ts, String initialChord)
    {
        Objects.requireNonNull(songName);
        Objects.requireNonNull(initSectionName);
        Objects.requireNonNull(ts);
        Preconditions.checkArgument(nbBars > 0, "nbBars=%s", nbBars);

        ChordLeadSheetFactory clsf = ChordLeadSheetFactory.getDefault();
        ChordLeadSheet cls = clsf.createEmptyLeadSheet(initSectionName, ts, nbBars, initialChord);
        Song song = null;
        try
        {
            song = new Song(songName, cls);
        } catch (UnsupportedEditException ex)
        {
            // We should not be here
            throw new IllegalStateException("Unexpected 'UnsupportedEditException'.", ex);
        }
        int tempo = song.getSongStructure().getSongPart(0).getRhythm().getPreferredTempo();
        song.setTempo(tempo);
        song.setSaveNeeded(false);
        registerSong(song);
        return song;
    }

    public boolean isSongNameUsed(String name)
    {
        boolean b = true;
        for (Song sg : getRegisteredSongs())
        {
            if (sg.getName().equals(name))
            {
                b = false;
                break;
            }
        }
        return b;
    }

    /**
     * Return a deep copy of the specified song.
     * <p>
     * Copy the following variables: chordleadsheet, songStructure, name, tempo, comments, tags, user phrases, clientProperties.<br>
     * ChordLeadSheetItem's clientProperties are copied.<br>
     * Listeners or file are NOT copied. Returned song is not closed, even if the original song was.
     *
     * @param song
     * @param register If true register the created song
     * @return
     */
    @SuppressWarnings(
            {
                "unchecked"
            })
    public Song getCopy(Song song, boolean register)
    {
        if (song == null)
        {
            throw new IllegalArgumentException("song");
        }
        ChordLeadSheetFactory clsf = ChordLeadSheetFactory.getDefault();
        ChordLeadSheet newCls = clsf.getCopy(song.getChordLeadSheet());

        Song s = null;
        try
        {
            s = new Song(song.getName(), newCls);       // SongStructure and ChordLeadsheet will be linked
        } catch (UnsupportedEditException ex)
        {
            // Should not occur since it's a clone, ie already accepted edits
            throw new IllegalArgumentException("clone() failed. Song's name=" + song.getName(), ex);
        }
        s.setComments(song.getComments());
        s.setTempo(song.getTempo());
        s.setTags(song.getTags());


        // Clean the default songStructure
        SongStructure newSgs = s.getSongStructure();
        try
        {
            newSgs.removeSongParts(newSgs.getSongParts());
        } catch (UnsupportedEditException ex)
        {
            // Should not happen since it's a copy
            Exceptions.printStackTrace(ex);
        }

        // Recreate each SongPart copy
        var newSpts = new ArrayList<SongPart>();
        for (SongPart spt : song.getSongStructure().getSongParts())
        {
            CLI_Section newParentSection = newCls.getSection(spt.getParentSection().getData().getName());
            assert newParentSection != null :
                    "spt.getStartBarIndex()=" + spt.getStartBarIndex()
                    + " spt.getParentSection()=" + spt.getParentSection()
                    + "  newCls-sections=" + newCls.getItems(CLI_Section.class);
            SongPart sptCopy = spt.clone(spt.getRhythm(), spt.getStartBarIndex(), spt.getNbBars(), newParentSection);
            newSpts.add(sptCopy);
        }

        // Add new song parts in one shot to avoid issue if an AdaptedRhythm is used
        try
        {
            newSgs.addSongParts(newSpts);            // Can raise UnsupportedEditException
        } catch (UnsupportedEditException ex)
        {
            // Should never happen
            throw new IllegalArgumentException(
                    "getCopy() failed. Song's name=" + song.getName() + " newSgs=" + newSgs + " newSpts=" + newSpts, ex);
        }


        // Copy the user phrases
        for (String name : song.getUserPhraseNames())
        {
            try
            {
                s.setUserPhrase(name, song.getUserPhrase(name).clone());
            } catch (PropertyVetoException ex)
            {
                // Should never happen as it was OK for song
                Exceptions.printStackTrace(ex);
            }
        }


        // Copy client properties
        s.getClientProperties().set(song.getClientProperties());


        s.setSaveNeeded(false);
        if (register)
        {
            registerSong(s);
        }
        return s;
    }

    /**
     * Return a copy of the song where the SongStructure does NOT listen to the ChordLeadsheet changes.
     * <p>
     * WARNING: Because SongStructure and ChordLeadsheet are not linked, changing them might result in inconsistent states. This should be used only in special
     * cases.<p>
     * Copy the following variables: chordleadsheet, songStructure, name, tempo, comments, tags, user phrases. Listeners or file are NOT copied.
     *
     * @param song
     * @param register If true register the created song.
     * @return
     */
    @SuppressWarnings(
            {
                "unchecked"
            })
    public Song getCopyUnlinked(Song song, boolean register)
    {
        if (song == null)
        {
            throw new IllegalArgumentException("song");
        }
        ChordLeadSheet cls = ChordLeadSheetFactory.getDefault().getCopy(song.getChordLeadSheet());
        SongStructure ss = null;
        try
        {
            ss = SongStructureFactory.getDefault().createSgs(cls, false);     // Don't link sgs to cls.  Can raise UnsupportedEditException
            ss.removeSongParts(ss.getSongParts());


            // Get a copy for each SongPart
            var newSpts = new ArrayList<SongPart>();
            for (SongPart spt : song.getSongStructure().getSongParts())
            {
                String parentSectionName = spt.getParentSection().getData().getName();
                CLI_Section parentSectionCopy = cls.getSection(parentSectionName);
                SongPart sptCopy = spt.clone(spt.getRhythm(), spt.getStartBarIndex(), spt.getNbBars(), parentSectionCopy);
                newSpts.add(sptCopy);
            }


            // Add new song parts in one shot to avoid issue if an AdaptedRhythm is used      
            ss.addSongParts(newSpts);            // Can raise UnsupportedEditException     
        } catch (UnsupportedEditException ex)
        {
            throw new IllegalArgumentException("getCopyUnlinked() failed. Song's name=" + song.getName() + " ss=" + ss, ex);
        }

        // Now create the song copy
        Song s = new Song(song.getName(), cls, ss);
        s.setComments(song.getComments());
        s.setTempo(song.getTempo());
        s.setTags(song.getTags());
        for (String name : song.getUserPhraseNames())
        {
            try
            {
                s.setUserPhrase(name, song.getUserPhrase(name).clone());
            } catch (PropertyVetoException ex)
            {
                // Should never happen as it was OK for song
                Exceptions.printStackTrace(ex);
            }
        }


        s.setSaveNeeded(false);
        if (register)
        {
            registerSong(s);
        }
        return s;
    }

    // =================================================================================
    // PropertyChangeListener methods
    // =================================================================================    
    @Override
    public void propertyChange(PropertyChangeEvent e)
    {
        if (e.getSource() instanceof Song sg)
        {
            assert songs.keySet().contains(sg) : "song=" + sg + " songs=" + songs.keySet();
            if (e.getPropertyName().equals(Song.PROP_CLOSED))
            {
                unregisterSong(sg);
            }
        }
    }

    // =================================================================================
    // Private methods
    // =================================================================================
}
