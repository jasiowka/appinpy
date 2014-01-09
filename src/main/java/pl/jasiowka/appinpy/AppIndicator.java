package pl.jasiowka.appinpy;

public interface AppIndicator extends Identity {

    void setMenu(Menu menu);

    void setQuitText(String text);

    void setQuitListener(ItemListener listener);

    void start();

    void stop();

}
