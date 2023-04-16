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

import java.awt.Color;
import org.jjazz.ui.ss_editor.api.SS_ContextActionSupport;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.jjazz.ui.ss_editor.api.SS_SelectionUtilities;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.jjazz.songstructure.api.SongPart;
import org.jjazz.ui.cl_editor.api.CL_Editor;
import org.jjazz.ui.cl_editor.api.CL_EditorTopComponent;
import org.jjazz.ui.colorsetmanager.api.ColorSetManager;
import org.jjazz.ui.ss_editor.api.SS_ContextActionListener;
import org.jjazz.util.api.ResUtil;
import org.openide.util.actions.Presenter;

@ActionID(category = "JJazz", id = "org.jjazz.ui.ss_editor.actions.setparentsectioncolor")
@ActionRegistration(displayName = "#CTL_SetParentSectionColor", lazy = false)
@ActionReferences(
        {
            @ActionReference(path = "Actions/SongPart", position = 1400)
        })
public class SetParentSectionColor extends AbstractAction implements ContextAwareAction, SS_ContextActionListener, Presenter.Popup
{

    private ColorMenu menu;
    private Lookup context;
    private SS_ContextActionSupport cap;
    private String undoText = ResUtil.getString(getClass(), "CTL_SetParentSectionColor");
    private static final Logger LOGGER = Logger.getLogger(SetParentSectionColor.class.getSimpleName());

    public SetParentSectionColor()
    {
        this(Utilities.actionsGlobalContext());
    }

    private SetParentSectionColor(Lookup context)
    {
        this.context = context;
        cap = SS_ContextActionSupport.getInstance(this.context);
        cap.addListener(this);
        putValue(NAME, undoText);
        selectionChange(cap.getSelection());
    }

    @Override
    public Action createContextAwareInstance(Lookup context)
    {
        return new SetParentSectionColor(context);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // Not used
    }

    @Override
    public void selectionChange(SS_SelectionUtilities selection)
    {
        boolean b = selection.isSongPartSelected();
        LOGGER.log(Level.FINE, "selectionChange() b={0}", b);
        setEnabled(b);
    }


    // ============================================================================================= 
    // Presenter.Popup implementation
    // =============================================================================================      
    @Override
    public JMenuItem getPopupPresenter()
    {
        if (menu == null)
        {
            menu = new ColorMenu(undoText);
        }
        return menu;
    }

    // ============================================================================================= 
    // Private methods
    // =============================================================================================    
    private void setColorOfSelectedSpts(Color c)
    {
        SS_SelectionUtilities selection = cap.getSelection();
        List<SongPart> spts = selection.getIndirectlySelectedSongParts();
        CL_Editor clEditor = CL_EditorTopComponent.get(selection.getModel().getParentChordLeadSheet()).getEditor();
        for (var spt : spts)
        {
            var parentSection = spt.getParentSection();
            clEditor.setSectionColor(parentSection, c);
        }
    }

    // ============================================================================================= 
    // Private class
    // =============================================================================================    
    private class ColorMenu extends JMenu implements PropertyChangeListener
    {

        public ColorMenu(String title)
        {
            super(title);

            prepareMenu();
            ColorSetManager.getDefault().addPropertyChangeListener(this);
        }

        @Override
        public void propertyChange(PropertyChangeEvent e)
        {
            // We may be outside the EDT
            if (e.getSource() == ColorSetManager.getDefault())
            {
                if (e.getPropertyName().equals(ColorSetManager.PROP_REF_COLOR_CHANGED))
                {
                    org.jjazz.ui.utilities.api.Utilities.invokeLaterIfNeeded(() -> prepareMenu());
                }
            }
        }


        private void prepareMenu()
        {
            removeAll();

            ColorSetManager csm = ColorSetManager.getDefault();

            for (final Color c : csm.getReferenceColors())
            {
                JMenuItem mi = new JMenuItem("    ");
                mi.setEnabled(true);
                mi.setOpaque(true);
                mi.setBackground(c);
                mi.addActionListener(ae -> setColorOfSelectedSpts(c));
                add(mi);
            }
        }
    }
}