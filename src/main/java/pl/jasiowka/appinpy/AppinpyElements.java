package pl.jasiowka.appinpy;

public class AppinpyElements {

    public static AppIndicator getAppIndicator() {
        return AppIndicatorUnity.getIndicator();
    }

    public static Menu createMenu(String text) {
        return new MenuUnity(text);
    }

    public static MenuItem createMenuItem(String text) {
        return new MenuItemUnity(text);
    }

}
