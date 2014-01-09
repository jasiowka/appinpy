package pl.jasiowka.appinpy;

import javax.swing.ImageIcon;

public interface Indicator extends Element {

    void addIcon(String name, ImageIcon icon);

    void setActiveIcon(String name);

    void setMenu(Menu menu);

    void setQuitLabel(String label);

    void setQuitListener(ItemListener listener);

    void start();

    void stop();

}
