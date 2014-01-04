package pl.jasiowka.appinpy;

import javax.swing.ImageIcon;

public interface AppIndicator {

    public void setIcon(ImageIcon icon);

    public void setMenu(Menu menu);

    public void start();

    public void stop();

}
