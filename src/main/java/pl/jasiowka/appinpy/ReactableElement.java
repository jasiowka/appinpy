package pl.jasiowka.appinpy;

interface ReactableElement extends LabeledElement {

    ItemListener getListener();

    void setListener(ItemListener listener);

}
