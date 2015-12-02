/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.explodingpixels.util;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.lang.reflect.Field;
import javax.swing.AbstractButton;
import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import sun.awt.image.MultiResolutionImage;

/**
 *
 * @author jeanfelix
 */
public class Retina {

    private static boolean retina = testRetinaDisplay();

    public static boolean hasRetinaDisplay() {
        return retina;
    }

    public static BufferedImage createBufferedImage(int w, int h, boolean alpha) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        if (retina) {
            w *= 2;
            h *= 2;
        }
        // Create an image that does not support transparency
        return gc.createCompatibleImage(w, h, alpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE);
    }

    public static void scaleUpGraphics2D(Graphics2D g2d) {
        if (retina) {
            g2d.scale(2, 2);
        }
    }

    public static void scaleDownGraphics2D(Graphics2D g2d) {
        if (retina) {
            g2d.scale(0.5, 0.5);
        }
    }

    private static boolean testRetinaDisplay() {
        boolean isRetina = false;
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        try {
            Field field = graphicsDevice.getClass().getDeclaredField("scale");
            if (field != null) {
                field.setAccessible(true);
                Object scale = field.get(graphicsDevice);
                if (scale instanceof Integer && ((Integer) scale) == 2) {
                    isRetina = true;
                }
            }
        } catch (Exception e) {
        }
        return isRetina;
    }

    private static final GrayFilter DEFAULT_GRAY_FILTER = new GrayFilter(true, 25);

    public static ImageIcon createPressedIcon(ImageIcon icon) {
        if (icon instanceof RetinaIcon) {
            //L&F: retina Icon are not well transformed into pressed icon 
            //let's force a nice one
            ImageProducer prod = new FilteredImageSource(icon.getImage().getSource(), DEFAULT_GRAY_FILTER);
            return new RetinaIcon(Toolkit.getDefaultToolkit().createImage(prod));
        }
        return null;
    }

    private static JLabel lblDisabledGenerator = new JLabel();

    public static Icon createDisabledIcon(AbstractButton button) {
        Image img = ((ImageIcon) button.getIcon()).getImage();
        if (img instanceof MultiResolutionImage) {
            int w = button.getIcon().getIconWidth() * 2;
            int h = button.getIcon().getIconHeight() * 2;
            Image retinaImage = ((MultiResolutionImage) img).getResolutionVariant(w, h);
            if (retinaImage != null) {
                lblDisabledGenerator.setIcon(new ImageIcon(retinaImage));
                ImageIcon disIcon = (ImageIcon) lblDisabledGenerator.getDisabledIcon();
                return new RetinaIcon(disIcon.getImage());
            }
        }
        return null;
    }
}
