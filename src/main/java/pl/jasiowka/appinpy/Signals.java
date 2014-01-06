package pl.jasiowka.appinpy;

public class Signals {

    public Integer itemSelection(String itemId) {
        //AppIndicatorUnity.getIndicator().itemSelectionSignal(itemId);
        return 0;
    }

    public Integer shutdown() {
        AppIndicatorUnity.getIndicator().shutdown();
        return 0;
    }

}
