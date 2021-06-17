package com.explodingpixels.macwidgets;

import java.awt.Image;
import java.awt.image.BaseMultiResolutionImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class MacIcons {

    public static final ImageIcon PLUS =                  readBundledIcon("add", "macwidgets");
    public static final ImageIcon MINUS =                 readBundledIcon("delete", "macwidgets");
    public static final ImageIcon GEAR =                  readBundledIcon("gear", "macwidgets");
    public static final ImageIcon DELETE =                readBundledIcon("delete", "macwidgets");
    public static final ImageIcon SPLITTER_HANDLE =       readBundledIcon("splitter_handle", "macwidgets");
    public static final ImageIcon CLOSE =                 readBundledIcon("close", "macwidgets");
    public static final ImageIcon CLOSE_HOVER =           readBundledIcon("close_hover", "macwidgets");
    public static final ImageIcon CLOSE_PRESSED =         readBundledIcon("close_pressed", "macwidgets");
    public static final ImageIcon RESIZE =                readBundledIcon("resize_corner_dark", "macwidgets");
    public static final ImageIcon COLLAPSED =             readBundledIcon("nav_right", "macwidgets");
    public static final ImageIcon COLLAPSED_WHITE =       readBundledIcon("nav_right_white", "macwidgets");
    public static final ImageIcon EXPANDED =              readBundledIcon("nav_down", "macwidgets");
    public static final ImageIcon EXPANDED_WHITE =        readBundledIcon("nav_down_white", "macwidgets");
    public static final ImageIcon WIDGET_CLOSE =          readBundledIcon("close", "widget");
    public static final ImageIcon WIDGET_CLOSE_OVER =     readBundledIcon("close_over", "widget");
    public static final ImageIcon WIDGET_CLOSE_PRESSED =  readBundledIcon("close_pressed", "widget");
    public static final ImageIcon WIDGET_CLOSE_UNSELECTED=readBundledIcon("close_unselected", "widget");
  
   
   private static final ImageIcon readBundledIcon(String imageName, String bundle) {
       List<Image> imgList = new ArrayList<Image>();
       try {
        imgList.add(ImageIO.read(MacIcons.class.getResource("/com/explodingpixels/"+bundle+"/images/"+imageName+".png")));
        URL url2x = MacIcons.class.getResource("/com/explodingpixels/"+bundle+"/images/"+imageName+"@2x.png");
        if(url2x != null) {
            imgList.add(ImageIO.read(url2x));
        }
        return new ImageIcon(new BaseMultiResolutionImage(imgList.toArray(new Image[0]))); 
       }catch(Exception e) {
           System.err.println("MacIcons missing" + imageName);
           return null;
       }
       
   }
}
