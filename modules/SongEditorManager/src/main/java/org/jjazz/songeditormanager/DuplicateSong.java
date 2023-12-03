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
package org.jjazz.songeditormanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import org.jjazz.chordleadsheet.api.item.CLI_Section;
import org.jjazz.song.api.Song;
import org.jjazz.song.api.SongFactory;
import org.jjazz.songeditormanager.api.SongEditorManager;
import org.jjazz.cl_editor.api.CL_Editor;
import org.jjazz.cl_editor.api.CL_EditorTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;

@ActionID(category = "File", id = "org.jjazz.actions.DuplicateSong")
@ActionRegistration(displayName = "#CTL_DuplicateSong", lazy = true)
@ActionReferences(
        {
            @ActionReference(path = "Menu/Edit", position = 2115),
            @ActionReference(path = "Actions/CL_EditorTopComponent", position = 110),
            @ActionReference(path = "Actions/RL_EditorTopComponent", position = 110),
            @ActionReference(path = "Shortcuts", name = "D-D")
        })
public final class DuplicateSong implements ActionListener
{

    /**
     * Used to make sure we don't have the same name twice.
     */
    private static int counter = 1;
    final private Song song;

    public DuplicateSong(Song sg)
    {
        song = sg;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        SongFactory sf = SongFactory.getInstance();
        Song newSong = sf.getCopy(song, true);
        newSong.setName(song.getName() + " Copy" + counter);
        newSong.setSaveNeeded(false);
        SongEditorManager sm = SongEditorManager.getInstance();
        sm.showSong(newSong, false, false);     //  Posts an EDT task to create the editors              
        counter++;

        SwingUtilities.invokeLater(() -> sm.copySongEditorSettings(song, newSong));    
    }
}