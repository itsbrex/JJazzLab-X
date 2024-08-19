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
package org.jjazz.chordinspector;

import java.util.logging.Logger;
import org.jjazz.utilities.api.ResUtil;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays the chord viewers.
 */
@ConvertAsProperties(
        dtd = "-//org.jjazz.chordinspector//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ChordInspectorTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "jlnavigator", openAtStartup = false, position = 20)
@ActionID(category = "Window", id = "org.jjazz.chordinspector.ChordInspectorTopComponent")
@ActionReference(path = "Menu/Tools", position = 10)
@TopComponent.OpenActionRegistration(displayName = "#CTL_ChordInspector", preferredID = "ChordInspectorTopComponent"
)
public final class ChordInspectorTopComponent extends TopComponent
{

    private static final Logger LOGGER = Logger.getLogger(ChordInspectorTopComponent.class.getSimpleName());
    private final ChordInspectorPanel editor;
    private ChordListener chordListener;

    public ChordInspectorTopComponent()
    {
        setName(ResUtil.getString(getClass(), "CTL_ChordInspector"));
        setToolTipText(ResUtil.getString(getClass(), "CTL_ChordInspectorDesc"));
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        // putClientProperty(TopComponent.PROP_SLIDING_DISABLED, Boolean.TRUE);


        initComponents();


        editor = new ChordInspectorPanel();
        add(editor);
    }

    @Override
    public void componentClosed()
    {
        chordListener.cleanup();
        chordListener = null;
    }

    @Override
    public void componentOpened()
    {
        chordListener = new ChordListener(editor);
    }

    /**
     *
     * @return Can be null
     */
    static public ChordInspectorTopComponent getInstance()
    {
        return (ChordInspectorTopComponent) WindowManager.getDefault().findTopComponent("ChordInspectorTopComponent");
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