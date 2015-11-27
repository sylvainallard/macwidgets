package com.explodingpixels.macwidgets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;

import com.explodingpixels.macwidgets.plaf.PreferencesTabBarButtonUI;
import com.explodingpixels.macwidgets.plaf.UnifiedToolbarButtonUI;
import com.explodingpixels.painter.ButtonStatePainter;
import com.explodingpixels.painter.CompoundPainter;
import com.explodingpixels.painter.MacWidgetsPainter;
import com.explodingpixels.painter.RectanglePainter;
import com.explodingpixels.swingx.EPButton;
import com.explodingpixels.util.Retina;
import com.explodingpixels.util.RetinaIcon;
import com.explodingpixels.widgets.PopdownButton;
import com.explodingpixels.widgets.PopupMenuCustomizer;
import java.awt.Image;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import sun.awt.image.MultiResolutionImage;

/**
 * A factory for creating Mac-style buttons.
 */
public class MacButtonFactory {

    private static JLabel lblDisabledGenerator = new JLabel();

    // Unified toolbar button methods. ////////////////////////////////////////////////////////////
    public static AbstractButton makeUnifiedToolBarButton(AbstractButton button) {
        button.setUI(new UnifiedToolbarButtonUI());
        return button;
    }

    public static AbstractButton makePreferencesTabBarButton(AbstractButton button) {
        button.setUI(new PreferencesTabBarButtonUI());
        return button;
    }

    // Gradient button methods. ///////////////////////////////////////////////////////////////////
    static final Color GRADIENT_BUTTON_BORDER_COLOR = new Color(190, 190, 190);

    private static URL GRADIENT_BACKGROUND_URL
            = ComponentBottomBar.class.getResource(
                    "/com/explodingpixels/macwidgets/images/component_status_bar_shiny_background_no_border.png");

//    static final ImagePainter GRADIENT_BUTTON_IMAGE_PAINTER =
//            new ImagePainter(GRADIENT_BACKGROUND_URL);
    static final RectanglePainter GRADIENT_BUTTON_IMAGE_PAINTER = new RectanglePainter(Color.decode("#f2f2f2"));
    private static final MacWidgetsPainter<Component> PRESSED_AND_SELECTED_GRADIENT_PAINTER
            = new CompoundPainter<Component>(GRADIENT_BUTTON_IMAGE_PAINTER,
                    new RectanglePainter(new Color(0, 0, 0, 89)));

    private static final ButtonStatePainter<AbstractButton> GRADIENT_BUTTON_PAINTER
            = new ButtonStatePainter<AbstractButton>(
                    GRADIENT_BUTTON_IMAGE_PAINTER,
                    GRADIENT_BUTTON_IMAGE_PAINTER,
                    PRESSED_AND_SELECTED_GRADIENT_PAINTER,
                    PRESSED_AND_SELECTED_GRADIENT_PAINTER);

    /**
     * Returns a {@link com.explodingpixels.painter.MacWidgetsPainter} that
     * paints a gradient matching Apple's depiction of a "Gradient style"
     * button, depicted
     * <a href="http://developer.apple.com/technotes/tn2007/tn2196.html#BUTTONS">here</a>.
     *
     * @return a {@code Painter} that paints an Apple style gradient-button
     * background.
     */
    public static MacWidgetsPainter<Component> getGradientButtonPainter() {
        return GRADIENT_BUTTON_IMAGE_PAINTER;
    }

    /**
     * Creates an Apple style gradient button using the given {@link Icon}. The
     * given {@link ActionListener} will be notified when the button's action
     * fires.
     *
     * @param icon the {@code Icon} to use in the button.
     * @param actionListener the {@code ActionListener} to notify when the
     * created button is pressed.
     * @return a gradient-style button.
     */
    public static JComponent createGradientButton(
            Icon icon, ActionListener actionListener) {

        EPButton button = new EPButton(icon);
        button.addActionListener(actionListener);

        button.setBackgroundPainter(GRADIENT_BUTTON_PAINTER);
        initGradientButton(button);
        button.setPressedIcon(icon);

        return button;
    }

    public static Icon validateDisabledIco(AbstractButton button) {
        ButtonModel model = button.getModel();
        if (Retina.hasRetinaDisplay() && !model.isEnabled() && button.getIcon() != null && !(button.getDisabledIcon() instanceof RetinaIcon)) {
            Image img = ((ImageIcon) button.getIcon()).getImage();
            if (img instanceof MultiResolutionImage) {
                int w = button.getIcon().getIconWidth() * 2;
                int h = button.getIcon().getIconHeight() * 2;
                Image retinaImage = ((MultiResolutionImage) img).getResolutionVariant(w, h);
                if (retinaImage != null) {
                    lblDisabledGenerator.setIcon(new ImageIcon(retinaImage));
                    ImageIcon disIcon = (ImageIcon) lblDisabledGenerator.getDisabledIcon();
                    button.setDisabledIcon(new RetinaIcon(disIcon.getImage()));
                }
            }
        }
        return button.getDisabledIcon();
    }

    /**
     * Creates an Apple style gradient button using the given text. The given
     * {@link ActionListener} will be notified when the button's action fires.
     *
     * @param text the text to use in the button.
     * @param actionListener the {@code ActionListener} to notify when the
     * created button is pressed.
     * @return a gradient-style button.
     */
    public static JComponent createGradientButton(
            String text, ActionListener actionListener) {

        EPButton button = new EPButton(text);
        button.addActionListener(actionListener);

        button.setBackgroundPainter(GRADIENT_BUTTON_PAINTER);
        initGradientButton(button);
        return button;
    }

    /**
     * Creates an Apple style gradient pop-down button using the given
     * {@link Icon}. The given {@link PopupMenuCustomizer} will be notified just
     * prior to the pop-down menu being shown, and can thus add appropriate menu
     * items.
     *
     * @param icon the {@code Icon} to use in the button.
     * @param popupMenuCustomizer the {@code PopupMenuCustomizer} to be notified
     * just prior to the popup menu being shown.
     * @return a gradient-style pop-down menu.
     */
    public static PopdownButton createGradientPopdownButton(
            Icon icon, PopupMenuCustomizer popupMenuCustomizer) {

        PopdownButton popdownButton
                = new PopdownButton(icon, popupMenuCustomizer);
        popdownButton.setBackgroundPainter(GRADIENT_BUTTON_PAINTER);
        initGradientButton(popdownButton.getComponent());
        popdownButton.setPressedIcon(icon);
        
        return popdownButton;
    }

    private static void initGradientButton(JComponent button) {
        button.setBorder(BorderFactory.createLineBorder(GRADIENT_BUTTON_BORDER_COLOR));
        button.setPreferredSize(new Dimension(30, 22));
    }

}
