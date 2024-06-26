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
package org.jjazz.yjzwizard;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jjazz.utilities.api.ResUtil;

public final class YjzCreatorVisualPanel3 extends JPanel
{

    /**
     * Creates new form YjzCreatorVisualPanel3
     */
    public YjzCreatorVisualPanel3()
    {
        initComponents();
    }

    @Override
    public String getName()
    {
        return ResUtil.getString(getClass(), "CREATION_PARAMETERS");
    }

    public void setNbMain(int nbMainA, int nbMainB, int nbMainC, int nbMainD)
    {
        spn_nbMainA.setValue(nbMainA);
        spn_nbMainB.setValue(nbMainB);
        spn_nbMainC.setValue(nbMainC);
        spn_nbMainD.setValue(nbMainD);
        spn_nbMainAStateChanged(null);
        spn_nbMainBStateChanged(null);
        spn_nbMainCStateChanged(null);
        spn_nbMainDStateChanged(null);
    }

    public void setIncludeIntroEndings(boolean b)
    {
        cb_includeIntroEndings.setSelected(b);
    }

    public boolean isIncludeIntroEndings()
    {
        return cb_includeIntroEndings.isSelected();
    }

    public void setIncludeFills(boolean b)
    {
        cb_includeFills.setSelected(b);
    }    
    
    public boolean isIncludeFills()
    {
        return cb_includeFills.isSelected();
    }

    /**
     *
     * @param c Must be 'A' 'B' 'C' or 'D'
     * @return Can be 0
     */
    public int getNbMain(char c)
    {
        int r;
        switch (c)
        {
            case 'A':
                r = (Integer) spn_nbMainA.getValue();
                break;
            case 'B':
                r = (Integer) spn_nbMainB.getValue();
                break;
            case 'C':
                r = (Integer) spn_nbMainC.getValue();
                break;
            case 'D':
                r = (Integer) spn_nbMainD.getValue();
                break;

            default:
                throw new IllegalArgumentException("c=" + c);   //NOI18N
        }
        return r;
    }

    public int getNbSrcPhrases()
    {
        return (Integer) spn_nbSrcPhrases.getValue();
    }

    public void setNbSrcPhrases(int n)
    {
        spn_nbSrcPhrases.setValue(n);
    }

    public void setExtendedFilename(String s)
    {
        lbl_yjzFilename.setText(s);
    }

    private void updateNbMainResultLabel(JLabel lbl, char c, Integer n)
    {
        StringBuilder sb = new StringBuilder();
        if (n > 0)
        {
            String strMain = "Main " + c + "-";
            sb.append(strMain).append("1");
            for (int i = 2; i <= n; i++)
            {
                sb.append(", ").append(strMain).append(i);
            }
        }
        lbl.setText(sb.toString() + "  ");    // trailing space for italics...
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane2 = new javax.swing.JScrollPane();
        helpTextArea1 = new org.jjazz.flatcomponents.api.HelpTextArea();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lbl_resultMainC = new javax.swing.JLabel();
        spn_nbMainA = new org.jjazz.flatcomponents.api.WheelSpinner();
        jLabel6 = new javax.swing.JLabel();
        spn_nbMainD = new org.jjazz.flatcomponents.api.WheelSpinner();
        spn_nbMainB = new org.jjazz.flatcomponents.api.WheelSpinner();
        jLabel4 = new javax.swing.JLabel();
        lbl_resultMainB = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        spn_nbMainC = new org.jjazz.flatcomponents.api.WheelSpinner();
        lbl_resultMainA = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lbl_resultMainD = new javax.swing.JLabel();
        lbl_mainAsize = new javax.swing.JLabel();
        lbl_mainBsize = new javax.swing.JLabel();
        lbl_mainCsize = new javax.swing.JLabel();
        lbl_mainDsize = new javax.swing.JLabel();
        cb_includeIntroEndings = new javax.swing.JCheckBox();
        cb_includeFills = new javax.swing.JCheckBox();
        spn_nbSrcPhrases = new org.jjazz.flatcomponents.api.WheelSpinner();
        jLabel16 = new javax.swing.JLabel();
        lbl_yjzFilename = new javax.swing.JLabel();

        jScrollPane2.setBorder(null);

        helpTextArea1.setColumns(20);
        helpTextArea1.setRows(5);
        helpTextArea1.setText(org.openide.util.NbBundle.getMessage(YjzCreatorVisualPanel3.class, "YjzCreatorVisualPanel3.helpTextArea1.text")); // NOI18N
        jScrollPane2.setViewportView(helpTextArea1);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(YjzCreatorVisualPanel3.class, "YjzCreatorVisualPanel3.jLabel1.text")); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(YjzCreatorVisualPanel3.class, "YjzCreatorVisualPanel3.jPanel1.border.title"))); // NOI18N

        lbl_resultMainC.setFont(lbl_resultMainC.getFont().deriveFont((lbl_resultMainC.getFont().getStyle() | java.awt.Font.ITALIC)));
        org.openide.awt.Mnemonics.setLocalizedText(lbl_resultMainC, "Main B-1"); // NOI18N

        spn_nbMainA.setModel(new javax.swing.SpinnerNumberModel(2, 0, 10, 1));
        spn_nbMainA.setColumns(2);
        spn_nbMainA.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                spn_nbMainAStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, "Main C"); // NOI18N

        spn_nbMainD.setModel(new javax.swing.SpinnerNumberModel(2, 0, 10, 1));
        spn_nbMainD.setColumns(2);
        spn_nbMainD.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                spn_nbMainDStateChanged(evt);
            }
        });

        spn_nbMainB.setModel(new javax.swing.SpinnerNumberModel(2, 0, 10, 1));
        spn_nbMainB.setColumns(2);
        spn_nbMainB.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                spn_nbMainBStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, "Main A"); // NOI18N

        lbl_resultMainB.setFont(lbl_resultMainB.getFont().deriveFont((lbl_resultMainB.getFont().getStyle() | java.awt.Font.ITALIC)));
        org.openide.awt.Mnemonics.setLocalizedText(lbl_resultMainB, "jLabel12"); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, "Main B"); // NOI18N

        spn_nbMainC.setModel(new javax.swing.SpinnerNumberModel(2, 0, 10, 1));
        spn_nbMainC.setColumns(2);
        spn_nbMainC.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                spn_nbMainCStateChanged(evt);
            }
        });

        lbl_resultMainA.setFont(lbl_resultMainA.getFont().deriveFont((lbl_resultMainA.getFont().getStyle() | java.awt.Font.ITALIC)));
        org.openide.awt.Mnemonics.setLocalizedText(lbl_resultMainA, "Unchanged"); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, "Main D"); // NOI18N

        lbl_resultMainD.setFont(lbl_resultMainD.getFont().deriveFont((lbl_resultMainD.getFont().getStyle() | java.awt.Font.ITALIC)));
        org.openide.awt.Mnemonics.setLocalizedText(lbl_resultMainD, "a long text aerkja eazeae jLabel13"); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lbl_mainAsize, "(8 beats)"); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lbl_mainBsize, "(8 beats)"); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lbl_mainCsize, "(4 beats)"); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lbl_mainDsize, "(8 beats)"); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(cb_includeIntroEndings, org.openide.util.NbBundle.getMessage(YjzCreatorVisualPanel3.class, "YjzCreatorVisualPanel3.cb_includeIntroEndings.text")); // NOI18N
        cb_includeIntroEndings.setToolTipText(org.openide.util.NbBundle.getMessage(YjzCreatorVisualPanel3.class, "YjzCreatorVisualPanel3.cb_includeIntroEndings.toolTipText")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(cb_includeFills, org.openide.util.NbBundle.getBundle(YjzCreatorVisualPanel3.class).getString("YjzCreatorVisualPanel3.cb_includeFills.text")); // NOI18N
        cb_includeFills.setToolTipText(org.openide.util.NbBundle.getBundle(YjzCreatorVisualPanel3.class).getString("YjzCreatorVisualPanel3.cb_includeFills.toolTipText")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_mainAsize)
                            .addComponent(lbl_mainBsize)
                            .addComponent(lbl_mainCsize)
                            .addComponent(lbl_mainDsize))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spn_nbMainA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spn_nbMainB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spn_nbMainC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spn_nbMainD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_resultMainB)
                            .addComponent(lbl_resultMainC)
                            .addComponent(lbl_resultMainD)
                            .addComponent(lbl_resultMainA)))
                    .addComponent(cb_includeIntroEndings)
                    .addComponent(cb_includeFills))
                .addGap(0, 224, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lbl_mainAsize)
                    .addComponent(spn_nbMainA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_resultMainA))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lbl_mainBsize)
                    .addComponent(spn_nbMainB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_resultMainB))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lbl_mainCsize)
                    .addComponent(spn_nbMainC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_resultMainC))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lbl_mainDsize)
                    .addComponent(spn_nbMainD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_resultMainD))
                .addGap(18, 18, 18)
                .addComponent(cb_includeFills)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cb_includeIntroEndings)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        spn_nbSrcPhrases.setModel(new javax.swing.SpinnerNumberModel(2, 1, 20, 1));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel16, org.openide.util.NbBundle.getMessage(YjzCreatorVisualPanel3.class, "YjzCreatorVisualPanel3.jLabel16.text")); // NOI18N
        jLabel16.setToolTipText(org.openide.util.NbBundle.getMessage(YjzCreatorVisualPanel3.class, "YjzCreatorVisualPanel3.jLabel16.toolTipText")); // NOI18N

        lbl_yjzFilename.setFont(lbl_yjzFilename.getFont().deriveFont(lbl_yjzFilename.getFont().getStyle() | java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(lbl_yjzFilename, "jLabel2"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(spn_nbSrcPhrases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_yjzFilename)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(lbl_yjzFilename))
                .addGap(27, 27, 27)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(spn_nbSrcPhrases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void spn_nbMainAStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_spn_nbMainAStateChanged
    {//GEN-HEADEREND:event_spn_nbMainAStateChanged
        updateNbMainResultLabel(lbl_resultMainA, 'A', (Integer) spn_nbMainA.getValue());
    }//GEN-LAST:event_spn_nbMainAStateChanged

    private void spn_nbMainBStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_spn_nbMainBStateChanged
    {//GEN-HEADEREND:event_spn_nbMainBStateChanged
        updateNbMainResultLabel(lbl_resultMainB, 'B', (Integer) spn_nbMainB.getValue());
    }//GEN-LAST:event_spn_nbMainBStateChanged

    private void spn_nbMainCStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_spn_nbMainCStateChanged
    {//GEN-HEADEREND:event_spn_nbMainCStateChanged
        updateNbMainResultLabel(lbl_resultMainC, 'C', (Integer) spn_nbMainC.getValue());
    }//GEN-LAST:event_spn_nbMainCStateChanged

    private void spn_nbMainDStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_spn_nbMainDStateChanged
    {//GEN-HEADEREND:event_spn_nbMainDStateChanged
        updateNbMainResultLabel(lbl_resultMainD, 'D', (Integer) spn_nbMainD.getValue());
    }//GEN-LAST:event_spn_nbMainDStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cb_includeFills;
    private javax.swing.JCheckBox cb_includeIntroEndings;
    private org.jjazz.flatcomponents.api.HelpTextArea helpTextArea1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_mainAsize;
    private javax.swing.JLabel lbl_mainBsize;
    private javax.swing.JLabel lbl_mainCsize;
    private javax.swing.JLabel lbl_mainDsize;
    private javax.swing.JLabel lbl_resultMainA;
    private javax.swing.JLabel lbl_resultMainB;
    private javax.swing.JLabel lbl_resultMainC;
    private javax.swing.JLabel lbl_resultMainD;
    private javax.swing.JLabel lbl_yjzFilename;
    private org.jjazz.flatcomponents.api.WheelSpinner spn_nbMainA;
    private org.jjazz.flatcomponents.api.WheelSpinner spn_nbMainB;
    private org.jjazz.flatcomponents.api.WheelSpinner spn_nbMainC;
    private org.jjazz.flatcomponents.api.WheelSpinner spn_nbMainD;
    private org.jjazz.flatcomponents.api.WheelSpinner spn_nbSrcPhrases;
    // End of variables declaration//GEN-END:variables

}
