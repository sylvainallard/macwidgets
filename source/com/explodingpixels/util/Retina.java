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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.MultiResolutionImage;
import java.lang.reflect.Field;
import javax.swing.AbstractButton;
import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author jeanfelix
 */
public class Retina {

    private static boolean retina = testRetinaDisplay();
    private static double scaleX = -1;
    private static double scaleY = -1;

    public static boolean hasRetinaDisplay() {
        return retina;
    }
    
    public static double getRetinaScaleX(){
        if(scaleX == -1)
        {
            testRetinaDisplay();
        }
        return scaleX == -1 ? 1 : scaleX;
    }
    
    public static double getRetinaScaleY(){
        if(scaleY == -1)
        {
            testRetinaDisplay();
        }
        return scaleY == -1 ? 1 : scaleY;
    }

    public static BufferedImage createBufferedImage(int w, int h, boolean alpha) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        if (retina) {
            w *= getRetinaScaleX();
            h *= getRetinaScaleY();
        }
        // Create an image that does not support transparency
        return gc.createCompatibleImage(w, h, alpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE);
    }

    public static void scaleUpGraphics2D(Graphics2D g2d) {
        if (retina) {
            g2d.scale(getRetinaScaleX(), getRetinaScaleY());
        }
    }

    public static void scaleDownGraphics2D(Graphics2D g2d) {
        if (retina) {
            g2d.scale(1.0/getRetinaScaleX(), 1.0/getRetinaScaleY());
        }
    }

    private static boolean testRetinaDisplay() {
        boolean isRetina = false;
        try {
            final GraphicsConfiguration gfxConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            final AffineTransform transform = gfxConfig.getDefaultTransform();
            scaleX = transform.getScaleX();
            scaleY = transform.getScaleY();
            isRetina = !transform.isIdentity();
        } catch (Exception e) {
            System.err.println("Retina check failed:" + e);
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
        if (retina && img instanceof MultiResolutionImage) {
            int w = (int)(button.getIcon().getIconWidth() * getRetinaScaleX());
            int h = (int)(button.getIcon().getIconHeight() * getRetinaScaleY());
            Image retinaImage = ((MultiResolutionImage) img).getResolutionVariant(w, h);
            if (retinaImage != null) {
                lblDisabledGenerator.setIcon(new ImageIcon(retinaImage));
                ImageIcon disIcon = (ImageIcon) lblDisabledGenerator.getDisabledIcon();
                return new RetinaIcon(disIcon.getImage());
            }
        }else if(button.getIcon() instanceof RetinaIcon){
            lblDisabledGenerator.setIcon(button.getIcon());
            ImageIcon disIcon = (ImageIcon) lblDisabledGenerator.getDisabledIcon();
            return new RetinaIcon(disIcon.getImage());
        }
        return null;
    }
}
