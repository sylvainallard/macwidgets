package com.explodingpixels.macwidgets.plaf;

import com.explodingpixels.macwidgets.MacButtonFactory;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

import com.explodingpixels.macwidgets.MacColorUtils;
import com.explodingpixels.macwidgets.MacFontUtils;
import javax.swing.Icon;
import sun.swing.SwingUtilities2;

public class UnifiedToolbarButtonUI extends BasicButtonUI {

    private static final Color PRESSED_BUTTON_MASK_COLOR = new Color(0, 0, 0, 128);
    protected Icon icoPressed, icoRollover, icoRollSel, icoSelected;

    @Override
    protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);

        // TODO save original values.

        b.setHorizontalTextPosition(AbstractButton.CENTER);
        b.setVerticalTextPosition(AbstractButton.BOTTOM);
        b.setIconTextGap(0);
        b.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        b.setOpaque(false);
        b.setFocusable(false);
        // TODO make the font derivation more robust.
        b.setFont(MacFontUtils.DEFAULT_BUTTON_FONT);
        this.setIcons(b.getPressedIcon(), b.getRolloverIcon(), b.getRolloverSelectedIcon(), b.getSelectedIcon());
    }

    @Override
    protected void uninstallDefaults(AbstractButton b) {
        super.uninstallDefaults(b);
        // TODO implement.
    }

    @Override
    protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {

        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();

        // create a graphics context from the buffered image.
        Graphics2D graphics = (Graphics2D) g;
        // paint the icon into the buffered image.
        if (!model.isEnabled()) {
            MacButtonFactory.validateDisabledIco(b);
            Icon ico = b.getDisabledIcon();
            if(ico != null){
                ico.paintIcon(c, graphics, iconRect.x, iconRect.y);
            }
        }else if( icoPressed != null && model.isPressed() ) {
            icoPressed.paintIcon(c, graphics, iconRect.x, iconRect.y);
            return; //dont render the dark background if there is a pressed Icon
        } else if( icoRollSel != null && model.isRollover() && model.isSelected() ) {
            icoRollSel.paintIcon(c, graphics, iconRect.x, iconRect.y);
        } else if( icoRollover != null && model.isRollover() ) {
            icoRollover.paintIcon(c, graphics, iconRect.x, iconRect.y);
        } else if( icoSelected != null && model.isSelected() ) {
            icoSelected.paintIcon(c, graphics, iconRect.x, iconRect.y);
        } else {
            b.getIcon().paintIcon(c, graphics, iconRect.x, iconRect.y);
        }
        
        if (model.isArmed()) {
            graphics.setColor(PRESSED_BUTTON_MASK_COLOR);
            // fill a rectangle with the mask color.
            graphics.fillRect(0, 0, c.getWidth(), c.getHeight());
        }       

    }
    
    

    @Override
    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
        MacFontUtils.enableAntialiasing((Graphics2D) g);
        Graphics2D graphics = (Graphics2D) g.create();

        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        FontMetrics fm = c.getFontMetrics(c.getFont());

        // 1) Draw the emphasis text.
        graphics.setColor(model.isArmed()
                ? MacColorUtils.EMPTY_COLOR
                : EmphasizedLabelUI.DEFAULT_EMPHASIS_COLOR);
        SwingUtilities2.drawStringUnderlineCharAt(c, graphics, text, -1,
                textRect.x, textRect.y + 1 + fm.getAscent());

        // 2) Draw the text.
        graphics.setColor(model.isEnabled()
                ? EmphasizedLabelUI.DEFAULT_FOCUSED_FONT_COLOR
                : EmphasizedLabelUI.DEFAULT_DISABLED_FONT_COLOR);
        SwingUtilities2.drawStringUnderlineCharAt(c, graphics, text, -1,
                textRect.x, textRect.y + fm.getAscent());

        graphics.dispose();
    }
    
    public void setIcons( Icon icoPressed, Icon icoRollover, Icon icoRollSel, Icon icoSelected ) {
        this.icoPressed = icoPressed;
        this.icoRollover = icoRollover;
        this.icoRollSel = icoRollSel;
        this.icoSelected = icoSelected;
    }
}
