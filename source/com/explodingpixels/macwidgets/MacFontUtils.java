package com.explodingpixels.macwidgets;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.UIManager;

public class MacFontUtils {

    public static Font getFont() {
        return UIManager.getFont("Table.font");
    }

    public static Font getFontBold() {
        return UIManager.getFont("Label.font").deriveFont(Font.BOLD);
    }
    		
    public static void enableAntialiasing(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
}
