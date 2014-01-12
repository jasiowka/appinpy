package pl.jasiowka.appinpy;

import java.util.Vector;

import javax.swing.ImageIcon;

class Test {

    public static void main(String[] args) {

        ItemListener listener = new ItemListener() {
            @Override
            public void onItemSelection(ActionInfo info) {
                LabeledElement lel = (LabeledElement) info;
                System.out.println("You chose '" + lel.getLabel() + "'");
	        }
        };

        ImageIcon icon1 = new ImageIcon("/home/wallie/Desktop/img/icon_22x22.png");
        ImageIcon icon2 = new ImageIcon("/home/wallie/Desktop/img/apple22.png");
        UnityIndicator ind = (UnityIndicator) AppinpyFactory.createIndicator();
        ind.addIcon("ikonka1", icon1);
        ind.addIcon("ikonka2", icon2);
        ind.setActiveIcon("ikonka1");

        Menu menu = AppinpyFactory.createMenu("MainMenu");

        CheckItem item = AppinpyFactory.createCheckItem("Apple");
        item.setListener(listener);
        menu.addCheckItem(item);
        RegularItem item2 = AppinpyFactory.createItem("Strawberry");
        item2.setListener(listener);
        menu.addItem(item2);

            Menu submenu = AppinpyFactory.createMenu("Grapes");
            item2 = AppinpyFactory.createItem("White grapes");
            item2.setListener(listener);
            submenu.addItem(item2);
            item2 = AppinpyFactory.createItem("Red grapes");
            item2.setListener(listener);
            submenu.addItem(item2);

        menu.addSubMenu(submenu);
        item2 = AppinpyFactory.createItem("Banana");
        item2.setListener(listener);
        menu.addItem(item2);

            RadioGroup group = new UnityRadioGroup();
            RadioItem item11 = new UnityRadioItem("Radio1");
            item11.setListener(listener);
            group.addItem(item11);
            RadioItem item22 = new UnityRadioItem("Radio2");
            item22.setListener(listener);
            group.addItem(item22);
            RadioItem item33 = new UnityRadioItem("Radio3");
            item33.setListener(listener);
            group.addItem(item33);

        menu.addRadioGroup(group);
        menu.addSeparator();
        ind.setMenu(menu);

        //System.out.print(ind.getCode());
        //System.out.print(group.getActionCode());
        
        Vector<String> v = new Vector<String>();
        v.add("a");
        v.add("b");
        v.add("c");
        v.add("d");
        v.add("e");
        for (int i = 0; i < 5; i++) {
            System.out.print(v.get(0));
            v.remove(0);
        }
        
    }

}
