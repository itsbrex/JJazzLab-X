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
package org.jjazz.cl_editorimpl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.jjazz.chordleadsheet.api.item.CLI_BarAnnotation;
import org.jjazz.chordleadsheet.api.item.CLI_Section;
import org.jjazz.chordleadsheet.api.item.CLI_Factory;
import org.jjazz.chordleadsheet.api.item.ChordLeadSheetItem;
import org.jjazz.harmony.api.Position;
import org.jjazz.quantizer.api.Quantization;
import org.jjazz.cl_editor.api.CL_Editor;
import org.jjazz.cl_editor.barrenderer.api.BarRenderer;
import org.jjazz.cl_editor.spi.BarRendererSettings;
import org.jjazz.itemrenderer.api.IR_AnnotationText;
import org.jjazz.itemrenderer.api.IR_AnnotationTextSettings;
import org.jjazz.itemrenderer.api.IR_Copiable;
import org.jjazz.itemrenderer.api.IR_Type;
import org.jjazz.itemrenderer.api.ItemRenderer;
import org.jjazz.itemrenderer.api.ItemRendererFactory;

/**
 * A BarRenderer to show an annotation.
 */
public class BR_Annotation extends BarRenderer implements ComponentListener
{
    /**
     * Special shared JPanel instances per groupKey, used to calculate the preferred size for a BarRenderer subclass.
     */
    private static final Map<Integer, PrefSizePanel> mapGroupKeyPrefSizePanel = new HashMap<>();

    private static final Dimension MIN_SIZE = new Dimension(10, 4);

    /**
     * The ItemRenderer to show the insertion point.
     */
    private ItemRenderer insertionPointRenderer;
    private int zoomVFactor = 50;

    private static final Logger LOGGER = Logger.getLogger(BR_Annotation.class.getSimpleName());

    @SuppressWarnings("LeakingThisInConstructor")
    public BR_Annotation(CL_Editor editor, int barIndex, BarRendererSettings settings, ItemRendererFactory irf, Object groupKey)
    {
        super(editor, barIndex, settings, irf, groupKey);


        // Our layout manager
        setLayout(new BorderLayout());


        // Explicity set the preferred size so that layout's preferredLayoutSize() is never called
        setPreferredSize(getPrefSizePanelSharedInstance().getPreferredSize());
        getPrefSizePanelSharedInstance().addComponentListener(this);
        setMinimumSize(MIN_SIZE);

    }

    public void setNbLines(int n)
    {
        for (ItemRenderer ir : getItemRenderers())
        {
            if (ir instanceof IR_AnnotationText irAt)
            {
                irAt.setNbLines(n);
            }
        }
        getPrefSizePanelSharedInstance().setNbLines(n);
    }

    /**
     * Overridden to unregister the pref size panel shared instance.
     */
    @Override
    public void cleanup()
    {
        super.cleanup();
        getPrefSizePanelSharedInstance().removeComponentListener(this);
        getEditor().removePropertyChangeListener(this);

        // Remove only if it's the last bar of the editor
        if (getEditor().getNbBarBoxes() == 1)
        {
            JDialog dlg = getFontMetricsDialog();
            dlg.remove(getPrefSizePanelSharedInstance());
            mapGroupKeyPrefSizePanel.remove(System.identityHashCode(getGroupKey()));
            getPrefSizePanelSharedInstance().cleanup();
        }
    }

    @Override
    public void moveItemRenderer(ChordLeadSheetItem<?> item)
    {
        throw new IllegalStateException("item=" + item);
    }

    /**
     *
     * @param cliSection Can be null
     */
    @Override
    public void setSection(CLI_Section cliSection)
    {
        // Nothing
    }

    @Override
    public void showInsertionPoint(boolean b, ChordLeadSheetItem<?> item, Position pos, boolean copyMode)
    {
        if (b)
        {
            if (insertionPointRenderer == null)
            {
                insertionPointRenderer = addItemRenderer(item);
                insertionPointRenderer.setSelected(true);
            }
            if (insertionPointRenderer instanceof IR_Copiable irc)
            {
                irc.showCopyMode(copyMode);
            }
        } else
        {
            removeItemRenderer(insertionPointRenderer);
            insertionPointRenderer = null;
        }

        if (insertionPointRenderer instanceof IR_Copiable irc)
        {
            irc.showCopyMode(copyMode);
        }
    }

    @Override
    public void showPlaybackPoint(boolean b, Position pos)
    {
        // Do nothing
    }

    /**
     * Vertical zoom factor.
     *
     * @param factor 0=min zoom (bird's view), 100=max zoom
     */
    @Override
    public void setZoomVFactor(int factor)
    {
        if (zoomVFactor == factor)
        {
            return;
        }
        // Forward to the shared panel instance
        getPrefSizePanelSharedInstance().setZoomVFactor(factor);

        // Apply to this BR_Sections object
        zoomVFactor = factor;
        for (ItemRenderer ir : getItemRenderers())
        {
            ir.setZoomFactor(factor);
        }
        revalidate();
        repaint();
    }

    @Override
    public int getZoomVFactor()
    {
        return zoomVFactor;
    }

    @Override
    public String toString()
    {
        return "BR_Annotation[" + getBarIndex() + "]";
    }

    @Override
    public boolean isRegisteredItemClass(ChordLeadSheetItem<?> item)
    {
        return item instanceof CLI_BarAnnotation;
    }

    @Override
    protected ItemRenderer createItemRenderer(ChordLeadSheetItem<?> item)
    {
        if (!isRegisteredItemClass(item))
        {
            throw new IllegalArgumentException("item=" + item);
        }
        assert item instanceof CLI_BarAnnotation;
        IR_AnnotationText irAt = (IR_AnnotationText) getItemRendererFactory()
                .createItemRenderer(IR_Type.BarAnnotationText, item, getSettings().getItemRendererSettings());
        irAt.setNbLines(getEditor().getBarAnnotationNbLines());
        return irAt;
    }

    /**
     * Overridden to draw an almost transparent background (component is not opaque).
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Color c = new Color(127, 127, 127, 30);
        g.setColor(c);
        g.fillRect(0, 0, getWidth(), getHeight());
    }


    //-----------------------------------------------------------------------
    // Implementation of the ComponentListener interface
    //-----------------------------------------------------------------------
    /**
     * Our reference panel size (so its prefSize also) has changed, update our preferredSize.
     *
     * @param e
     */
    @Override
    public void componentResized(ComponentEvent e)
    {
        setPreferredSize(getPrefSizePanelSharedInstance().getSize());
        revalidate();
        repaint();
    }

    @Override
    public void componentMoved(ComponentEvent e)
    {
        // Nothing
    }

    @Override
    public void componentShown(ComponentEvent e)
    {
        // Nothing
    }

    @Override
    public void componentHidden(ComponentEvent e)
    {
        // Nothing
    }


    // ---------------------------------------------------------------
    // Private functions
    // ---------------------------------------------------------------
    @Override
    public void setDisplayQuantizationValue(Quantization q)
    {
        // Do nothing
    }

    @Override
    public Quantization getDisplayQuantizationValue()
    {
        // Do nothing
        return null;
    }


    // ---------------------------------------------------------------
    // Private methods
    // ---------------------------------------------------------------
    /**
     * Get the PrefSizePanel shared instance between BR_Sections of same groupKey.
     *
     * @return
     */
    private PrefSizePanel getPrefSizePanelSharedInstance()
    {
        PrefSizePanel panel = mapGroupKeyPrefSizePanel.get(System.identityHashCode(getGroupKey()));
        if (panel == null)
        {
            panel = new PrefSizePanel(this);
            mapGroupKeyPrefSizePanel.put(System.identityHashCode(getGroupKey()), panel);
        }
        return panel;
    }

    // ---------------------------------------------------------------
    // Private classes
    // ---------------------------------------------------------------
    /**
     * A special shared JPanel instance used to calculate the preferred size for all BR_Annotations.
     * <p>
     * Add ItemRenderers with the tallest size. Panel is added to the "hidden" BarRenderer's JDialog to be displayable so that FontMetrics can be calculated
     * with a Graphics object.
     * <p>
     */
    static private class PrefSizePanel extends JPanel implements PropertyChangeListener
    {

        int zoomVFactor;
        final IR_AnnotationText ir;
        final IR_AnnotationTextSettings settings = IR_AnnotationTextSettings.getDefault();
        final BR_Annotation brAnnotation;

        public PrefSizePanel(BR_Annotation brAnnotation)
        {
            this.brAnnotation = brAnnotation;

            // FlowLayout sets children size to their preferredSize
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));


            // Listen to settings changes impacting ItemRenderers size
            // Required here because the dialog is displayable but NOT visible (see myRevalidate()).
            settings.addPropertyChangeListener(this);


            // Add the tallest possible items
            CLI_Factory clif = CLI_Factory.getDefault();
            ChordLeadSheetItem<?> item = clif.createBarAnnotation("LYRICS ALALOLALALA\nLINE2\nLINE3\nLINE4", 0);
            ItemRendererFactory irf = brAnnotation.getItemRendererFactory();
            ir = (IR_AnnotationText) irf.createItemRenderer(IR_Type.BarAnnotationText, item, brAnnotation.getSettings().getItemRendererSettings());
            ir.setNbLines(brAnnotation.getEditor().getBarAnnotationNbLines());
            add(ir);


            // Add the panel to a hidden dialog so it can be made displayable (getGraphics() will return a non-null value, so font-based sizes
            // can be calculated
            JDialog dlg = brAnnotation.getFontMetricsDialog();
            dlg.add(this);
            dlg.pack();    // Force all components to be displayable
        }

        public void cleanup()
        {
            settings.removePropertyChangeListener(this);
        }

        /**
         * Overridden to use our own calculation instead of using FlowLayout's preferredLayoutSize().
         *
         * @return
         */
        @Override
        public Dimension getPreferredSize()
        {
            // Get the max preferred height from ItemRenderers and the sum of their preferred width
            int irMaxHeight = 0;
            int irWidthSum = 0;
            Dimension pd = ir.getPreferredSize();
            irWidthSum += pd.width;
            if (pd.height > irMaxHeight)
            {
                irMaxHeight = pd.height;
            }

            int V_MARGIN = 1;    // Do not depend on zoomFactor     

            Insets in = getInsets();
            int pWidth = irWidthSum + 5 + in.left + in.right;
            int pHeight = irMaxHeight + 2 * V_MARGIN + in.top + in.bottom;

            return new Dimension(pWidth, pHeight);
        }

        public void setZoomVFactor(int vFactor)
        {
            if (zoomVFactor == vFactor)
            {
                return;
            }
            zoomVFactor = vFactor;
            ir.setZoomFactor(vFactor);
            forceRevalidate();
        }

        public void setNbLines(int n)
        {
            ir.setNbLines(n);
            forceRevalidate();
        }


        //-----------------------------------------------------------------------
        // Implementation of the PropertiesListener interface
        //-----------------------------------------------------------------------
        @Override
        public void propertyChange(PropertyChangeEvent e)
        {
            if (e.getSource() == settings)
            {
                if (e.getPropertyName().equals(IR_AnnotationTextSettings.PROP_FONT))
                {
                    forceRevalidate();
                }
            }
        }


        /**
         * Because dialog is displayable but not visible, invalidating a component is not enough to relayout everything.
         */
        private void forceRevalidate()
        {
            brAnnotation.getFontMetricsDialog().pack();
        }

    }

}
