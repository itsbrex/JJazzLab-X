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
package org.jjazz.pianoroll;

import java.awt.BorderLayout;
import javax.swing.SwingUtilities;
import org.jjazz.flatcomponents.api.CollapsiblePanel;
import org.jjazz.pianoroll.api.PianoRollEditor;

/**
 * The left side panel.
 */
public class SidePanel extends javax.swing.JPanel
{

    /**
     * A property change event is fired when one the collapsiblePanel state has changed.
     */
    public static final String PROP_COLLAPSED_STATE = "propCollapsedState";
    private final QuantizePanel quantizePanel;
    private final GhostPhrasesPanel ghostPhrasesPanel;
    private final PianoRollEditor editor;

    public SidePanel(PianoRollEditor editor)
    {
        this.editor = editor;


        initComponents();

        // Quantize CollapsiblePanel
        cpan_quantize.getContentPane().setLayout(new BorderLayout());
        quantizePanel = new QuantizePanel(editor);
        cpan_quantize.getContentPane().add(quantizePanel, BorderLayout.CENTER);


        // Ghost phrases CollapsiblePanels                
        cpan_showTracks.getContentPane().setLayout(new BorderLayout());
        ghostPhrasesPanel = new GhostPhrasesPanel(this.editor.getGhostPhrasesModel());
        cpan_showTracks.getContentPane().add(ghostPhrasesPanel, BorderLayout.CENTER);


        // Fire an event when CollapsiblePanel state changes
        cpan_quantize.addPropertyChangeListener(CollapsiblePanel.PROP_COLLAPSED,
                e -> SwingUtilities.invokeLater(() -> firePropertyChange(PROP_COLLAPSED_STATE, false, true)));
        cpan_showTracks.addPropertyChangeListener(CollapsiblePanel.PROP_COLLAPSED,
                e -> SwingUtilities.invokeLater(() -> firePropertyChange(PROP_COLLAPSED_STATE, false, true)));

    }

    public void cleanup()
    {
        quantizePanel.cleanup();

    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        cpan_quantize = new org.jjazz.flatcomponents.api.CollapsiblePanel();
        cpan_showTracks = new org.jjazz.flatcomponents.api.CollapsiblePanel();

        cpan_quantize.setTitleComponentText(org.openide.util.NbBundle.getMessage(SidePanel.class, "SidePanel.cpan_quantize.titleComponentText")); // NOI18N

        cpan_showTracks.setTitleComponentText(org.openide.util.NbBundle.getMessage(SidePanel.class, "SidePanel.cpan_showTracks.titleComponentText")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cpan_quantize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(cpan_showTracks, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cpan_quantize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cpan_showTracks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(88, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jjazz.flatcomponents.api.CollapsiblePanel cpan_quantize;
    private org.jjazz.flatcomponents.api.CollapsiblePanel cpan_showTracks;
    // End of variables declaration//GEN-END:variables
}
