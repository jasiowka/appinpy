package pl.jasiowka.appinpy;

public interface MenuItem extends Identity {

    boolean isCheckable();

    boolean isChecked();

    ItemListener getListener();

}
