package pl.jasiowka.appinpy;

public final class AppinpyFactory {

    public static Indicator createIndicator() {
        if (UnityIndicator.getIndicator() == null)
            UnityIndicator.createIndicator();
        return UnityIndicator.getIndicator();
    }

    public static Menu createMenu(String text) {
        return new UnityMenu(text);
    }

    public static Item createItem(String label) {
        return new UnityItem(label);
    }

    public static CheckItem createCheckItem(String label) {
        return new UnityCheckItem(label);
    }

    public static RadioItem createRadioItem(String label) {
        return new UnityRadioItem(label);
    }

    public static RadioGroup createRadioGroup() {
        return new UnityRadioGroup();
    }

}
