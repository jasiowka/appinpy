package pl.jasiowka.appinpy;

import java.util.List;

public interface RadioGroup {

    void addItem(RadioItem item);

    List<RadioItem> getItems();

    RadioItem getChecked();

}
