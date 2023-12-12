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
package org.jjazz.easyreader;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import org.jjazz.activesong.api.ActiveSongManager;
import org.jjazz.cl_editor.api.CL_Editor;
import org.jjazz.cl_editor.api.CL_EditorTopComponent;
import org.jjazz.song.api.Song;
import org.jjazz.utilities.api.ResUtil;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays the Easy Reader
 */
@ConvertAsProperties(
        dtd = "-//org.jjazz.easyreader//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "EasyReaderTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "jlnavigator", openAtStartup = false, position = 25)
@ActionID(category = "Window", id = "org.jjazz.chordinspector.EasyReaderTopComponent")
@ActionReference(path = "Menu/Tools", position = 20)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_EasyReader",
        preferredID = "EasyReaderTopComponent"
)
public final class EasyReaderTopComponent extends TopComponent implements PropertyChangeListener
{

    private static final Logger LOGGER = Logger.getLogger(EasyReaderTopComponent.class.getSimpleName());
    private final EasyReaderPanel editor;

    public EasyReaderTopComponent()
    {
        setName(ResUtil.getString(getClass(), "CTL_EasyReader"));
        setToolTipText(ResUtil.getString(getClass(), "CTL_EasyReaderDesc"));
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        // putClientProperty(TopComponent.PROP_SLIDING_DISABLED, Boolean.TRUE);


        initComponents();


        editor = new EasyReaderPanel();
        add(editor);


    }

    @Override
    public void componentClosed()
    {
        editor.cleanup();
        var asm = ActiveSongManager.getInstance();
        asm.removePropertyListener(this);

    }

    @Override
    public void componentOpened()
    {
        // Keep editor up to date
        var asm = ActiveSongManager.getInstance();
        editor.setModel(asm.getActiveSong());
        asm.addPropertyListener(this);
    }

    /**
     *
     * @return Can be null
     */
    static public EasyReaderTopComponent getInstance()
    {
        return (EasyReaderTopComponent) WindowManager.getDefault().findTopComponent("EasyReaderTopComponent");
    }

    // ======================================================================
    // PropertyChangeListener interface
    // ======================================================================   
    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        if (evt.getSource() == ActiveSongManager.getInstance())
        {
            if (evt.getPropertyName().equals(ActiveSongManager.PROP_ACTIVE_SONG))
            {
                Song song = (Song) evt.getNewValue();
                var tc = CL_EditorTopComponent.get(song.getChordLeadSheet()); // Might be null in the case of non-editable song, eg such as the temporary one created by RhythmSelectionDialog for rhythm preview
                if (tc != null)
                {
                    
                    editor.setModel(song);
                }
            }
        }
    }

    // ===========================================================================
    // Private methods
    // ===========================================================================
    void writeProperties(java.util.Properties p)
    {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p)
    {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        setMinimumSize(new java.awt.Dimension(50, 50));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
