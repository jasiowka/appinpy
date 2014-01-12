package pl.jasiowka.appinpy;

public interface Item extends LabeledElement {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    boolean isVisible();

    void setVisible(boolean enabled);

    ItemListener getListener();

    void setListener(ItemListener listener);

}
