package pl.jasiowka.appinpy;

//import javax.swing.ImageIcon;

public interface AppIndicator {

    //void setIcon(ImageIcon icon);

    void setMenu(Menu menu);

    void setQuitText(String text);

    void setQuitListener(ItemListener listener);

    void start();

    void stop();

}
