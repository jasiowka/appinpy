package pl.jasiowka.appinpy;

public interface Menu extends LabeledElement {

    void addItem(RegularItem item);

    void addCheckItem(CheckItem checkItem);

    void addRadioGroup(RadioGroup radioGroup);

    void addSubMenu(Menu subMenu);

    void addSeparator();

}
