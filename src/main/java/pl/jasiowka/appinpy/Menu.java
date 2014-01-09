package pl.jasiowka.appinpy;

public interface Menu extends Identity {

    void add(MenuItem menuItem);

    void add(Menu menu);

}
