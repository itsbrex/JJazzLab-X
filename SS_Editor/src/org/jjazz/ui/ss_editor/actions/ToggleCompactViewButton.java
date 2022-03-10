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
package org.jjazz.ui.ss_editor.actions;

import static com.google.common.base.Preconditions.checkNotNull;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.Icon;
import org.jjazz.song.api.Song;
import org.jjazz.ui.flatcomponents.api.FlatToggleButton;
import static org.jjazz.ui.ss_editor.actions.ToggleCompactView.PROP_COMPACT_VIEW_MODE;
import static org.jjazz.ui.ss_editor.actions.ToggleCompactView.PROP_COMPACT_VIEW_MODE_VISIBLE_RPS;
import org.jjazz.ui.ss_editor.api.SS_Editor;

/**
 * The toggle button associated to the ToggleCompactView action.
 * <p>
 */
public class ToggleCompactViewButton extends FlatToggleButton implements PropertyChangeListener
{


    private final Song song;
    private final SS_Editor editor;
    private static final Logger LOGGER = Logger.getLogger(ToggleCompactViewButton.class.getName());


    public ToggleCompactViewButton(SS_Editor editor, ToggleCompactView toggleAction)
    {
        checkNotNull(editor);

        this.editor = editor;
        this.song = editor.getSongModel();
        this.song.addPropertyChangeListener(this);


        // Clicking the button triggers the action
        this.addActionListener(evt -> toggleAction.actionPerformed(evt));


        // Init UI from action properties 
        // NB: action should be ideally a BooleanStateAction to use FlatToggleButton.setAction(), but BooleanStateAction generates
        // an error when several instances are created from the same class (which is needed since there is 1 button per song)
        setIcon((Icon) toggleAction.getValue(Action.SMALL_ICON));
        setSelectedIcon((Icon) toggleAction.getValue(Action.LARGE_ICON_KEY));
        setToolTipText((String) toggleAction.getValue(Action.SHORT_DESCRIPTION));


        setSelected(ToggleCompactView.isSongInCompactViewMode(song));


        updateEditor();

    }


    // ======================================================================
    // PropertyChangeListener interface
    // ======================================================================
    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        if (evt.getSource() == song)
        {
            if (evt.getPropertyName().equals(Song.PROP_CLOSED))
            {
                song.removePropertyChangeListener(this);

            } else if (evt.getPropertyName().equals(PROP_COMPACT_VIEW_MODE))
            {
                // View mode has changed update the buttons and the editor
                boolean b = ToggleCompactView.isSongInCompactViewMode(song);
                setSelected(b);
                updateEditor();

            } else if (evt.getPropertyName().startsWith(PROP_COMPACT_VIEW_MODE_VISIBLE_RPS))
            {
                // The list of the visible RPs in compact mode has changed
                if (ToggleCompactView.isSongInCompactViewMode(song))
                {
                    updateEditor();
                }
            }
        }
    }


    // ======================================================================
    // Private methods
    // ======================================================================
    /**
     * Update the editor to show/hide RPs depending on the view mode.
     */
    private void updateEditor()
    {
        boolean b = ToggleCompactView.isSongInCompactViewMode(song);
        var allRhythms = song.getSongStructure().getUniqueRhythms(false, true);

        for (var r : allRhythms)
        {
            if (b)
            {
                // Compact mode
                editor.setVisibleRps(r, ToggleCompactView.getCompactViewModeVisibleRPs(song, r));
            } else
            {
                // Full mode
                editor.setVisibleRps(r, r.getRhythmParameters());
            }
        }
    }


}
