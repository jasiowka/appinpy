package pl.jasiowka.appinpy;

import javax.swing.ImageIcon;

public final class AppinpyFactory {

    public static AppIndicator createAppIndicator(ImageIcon icon) {
        if (AppIndicatorUnity.getIndicator() == null)
            AppIndicatorUnity.createIndicator(icon);
        return AppIndicatorUnity.getIndicator();
    }

    public static Menu createMenu(String text) {
        return new MenuUnity(text);
    }

    public static MenuItem createMenuItem(String text, ItemListener listener) {
        return new MenuItemUnity(text, listener);
    }

    public static MenuItem createCheckMenuItem(String text, ItemListener listener) {
        return new CheckMenuItemUnity(text, listener);
    }

    public static MenuItem createSeparatorMenuItem() {
        return new SeparatorMenuItemUnity();
    }

}
