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
package org.jjazz.helpers.midiwizard;

import javax.sound.midi.MidiDevice;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import org.jjazz.util.ResUtil;

public final class MidiWizardVisualPanel_SfWin_2 extends JPanel
{

    private MidiDevice vmsDevice = null;

    /**
     * Creates new form StartupWizardVisualPanel1
     */
    public MidiWizardVisualPanel_SfWin_2()
    {
        initComponents();
    }

    @Override
    public String getName()
    {
        return ResUtil.getString(getClass(), "MidiWizardVisualPanel_SfWin_2.Name");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        ta_notUserOnlyToGetFont = new org.jjazz.ui.utilities.WizardTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        editorPane = new javax.swing.JEditorPane();
        editorPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE); // To make setFont work
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        ta_notUserOnlyToGetFont.setEditable(false);
        ta_notUserOnlyToGetFont.setColumns(20);
        ta_notUserOnlyToGetFont.setTabSize(2);

        jScrollPane2.setBackground(null);
        jScrollPane2.setBorder(null);

        editorPane.setEditable(false);
        editorPane.setBackground(null);
        editorPane.setBorder(null);
        editorPane.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
        editorPane.setFont(ta_notUserOnlyToGetFont.getFont());
        editorPane.setText(org.openide.util.NbBundle.getMessage(MidiWizardVisualPanel_SfWin_2.class, "MidiWizardVisualPanel_SfWin_2.editorPane.text")); // NOI18N
        editorPane.addHyperlinkListener(new javax.swing.event.HyperlinkListener()
        {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt)
            {
                editorPaneHyperlinkUpdate(evt);
            }
        });
        jScrollPane2.setViewportView(editorPane);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jjazz/helpers/midiwizard/resources/VirtualMidiSynthConfig1.png"))); // NOI18N
        jPanel1.add(jLabel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ta_notUserOnlyToGetFont, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 577, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ta_notUserOnlyToGetFont, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void editorPaneHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt)//GEN-FIRST:event_editorPaneHyperlinkUpdate
    {//GEN-HEADEREND:event_editorPaneHyperlinkUpdate
        if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
        {
            org.jjazz.util.Utilities.openInBrowser(evt.getURL(), false);
        }
    }//GEN-LAST:event_editorPaneHyperlinkUpdate

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane editorPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private org.jjazz.ui.utilities.WizardTextArea ta_notUserOnlyToGetFont;
    // End of variables declaration//GEN-END:variables
}
