/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.explodingpixels.util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author jeanfelix
 */
public class RetinaIcon extends ImageIcon {
    
    public RetinaIcon() {
    }

    public RetinaIcon(Image image) {
        super(image);
    }

    public int getIconWidth() {
        if (Retina.hasRetinaDisplay()) {
            return (int)(super.getIconWidth() / Retina.getRetinaScaleX());
        }
        return super.getIconWidth();
    }

    /**
     * Gets the height of the icon.
     *
     * @return the height in pixels of this icon
     */
    public int getIconHeight() {
        if (Retina.hasRetinaDisplay()) {
            return (int)(super.getIconHeight() / Retina.getRetinaScaleY());
        }
        return super.getIconHeight();
    }

    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        if (Retina.hasRetinaDisplay()) {
            Graphics2D g2 = (Graphics2D) g.create();
            Retina.scaleDownGraphics2D(g2);
            super.paintIcon(c, g2, (int)(x * Retina.getRetinaScaleX()), (int)(y * Retina.getRetinaScaleY()));
            g2.dispose();
        } else {
            super.paintIcon(c, g, x, y);
        }
    }

}
