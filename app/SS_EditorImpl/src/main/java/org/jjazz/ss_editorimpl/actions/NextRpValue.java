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
package org.jjazz.ss_editorimpl.actions;

import org.jjazz.ss_editor.api.SS_ContextActionSupport;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.ACCELERATOR_KEY;
import static javax.swing.Action.NAME;
import javax.swing.KeyStroke;
import org.jjazz.rhythm.api.RhythmParameter;
import org.jjazz.ss_editor.api.SS_SelectionUtilities;
import org.jjazz.songstructure.api.SongPartParameter;
import org.jjazz.undomanager.api.JJazzUndoManagerFinder;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.jjazz.songstructure.api.SongStructure;
import org.jjazz.songstructure.api.SongPart;
import org.jjazz.ss_editor.api.SS_ContextActionListener;
import static org.jjazz.uiutilities.api.UIUtilities.getGenericControlKeyStroke;
import org.jjazz.utilities.api.ResUtil;
import org.jjazz.rhythm.api.RpEnumerable;

@ActionID(category = "JJazz", id = "org.jjazz.ss_editorimpl.actions.nextrpvalue")
@ActionRegistration(displayName = "#CTL_NextRpValue", lazy = false)
@ActionReferences(
        {
            @ActionReference(path = "Actions/RhythmParameter", position = 400),
        })
public final class NextRpValue extends AbstractAction implements ContextAwareAction, SS_ContextActionListener
{

    public static final KeyStroke KEYSTROKE = getGenericControlKeyStroke(KeyEvent.VK_UP);
    private Lookup context;
    private SS_ContextActionSupport cap;
    private String undoText = ResUtil.getString(getClass(), "CTL_NextRpValue");
    private static final Logger LOGGER = Logger.getLogger(NextRpValue.class.getSimpleName());

    public NextRpValue()
    {
        this(Utilities.actionsGlobalContext());
    }

    public NextRpValue(Lookup context)
    {
        this.context = context;
        cap = SS_ContextActionSupport.getInstance(this.context);
        cap.addListener(this);
        putValue(NAME, ResUtil.getString(getClass(), "CTL_NextRpValue"));                          // For popupmenu display only
        putValue(ACCELERATOR_KEY, KEYSTROKE);    // For popupmenu display only
    }

    @SuppressWarnings(
            {
                "unchecked", "rawtypes"
            })
    @Override
    public void actionPerformed(ActionEvent e)
    {
        SS_SelectionUtilities selection = cap.getSelection();
        SongStructure sgs = selection.getModel();
        assert sgs != null : "selection=" + selection;
        LOGGER.log(Level.FINE, "actionPerformed() sgs={0} selection={1}", new Object[]
        {
            sgs, selection
        });
        JJazzUndoManagerFinder.getDefault().get(sgs).startCEdit(undoText);
        for (SongPartParameter sptp : selection.getSelectedSongPartParameters())
        {
            RhythmParameter rp = sptp.getRp();
            if (rp instanceof RpEnumerable)
            {
                SongPart spt = sptp.getSpt();
                Object newValue = ((RpEnumerable) rp).getNextValue(spt.getRPValue(rp));
                sgs.setRhythmParameterValue(spt, rp, newValue);
            }
        }
        JJazzUndoManagerFinder.getDefault().get(sgs).endCEdit(undoText);
    }

    @Override
    public void selectionChange(SS_SelectionUtilities selection)
    {
        boolean b = selection.isEnumerableRhythmParameterSelected();
        setEnabled(b);
        LOGGER.log(Level.FINE, "selectionChange() b={0}", b);
    }

    @Override
    public Action createContextAwareInstance(Lookup context)
    {
        return new NextRpValue(context);
    }
}
