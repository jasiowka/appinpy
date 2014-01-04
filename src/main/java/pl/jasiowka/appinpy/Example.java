package pl.jasiowka.appinpy;

public class Example {

    public static void main(String[] args) {
        AppIndicator ind = AppinpyElements.getAppIndicator();
        
        Menu menu = AppinpyElements.createMenu("MainMenu");
        
        MenuItem item = AppinpyElements.createMenuItem("pozycja pierwsza");
        item.setListener(new ItemListener() {
			@Override
			public void onItemSelect() {
				System.out.println("PIEEEERRRRRWWWSSZAAAA");
			}
        });
        menu.add(item);
        
        item = AppinpyElements.createMenuItem("pozycja druga");
        item.setListener(new ItemListener() {
			@Override
			public void onItemSelect() {
				System.out.println("DRRUUUUUUUUGGGGAAAA");
			}
        });
        menu.add(item);

        
        
        Menu submenu = AppinpyElements.createMenu("SubMenu");
        
        item = AppinpyElements.createMenuItem("sub1");
        item.setListener(new ItemListener() {
			@Override
			public void onItemSelect() {
				System.out.println("SUUUUB1");
			}
        });
        submenu.add(item);
        
        item = AppinpyElements.createMenuItem("sub2");
        item.setListener(new ItemListener() {
			@Override
			public void onItemSelect() {
				System.out.println("SUUUB2");
			}
        });
        submenu.add(item);
        
        
        menu.add(submenu);
        
        
        item = AppinpyElements.createMenuItem("pozycja trzecia");
        item.setListener(new ItemListener() {
			@Override
			public void onItemSelect() {
				System.out.println("TRZEEEEEECCCCIIIAAAAAA");
			}
        });
        menu.add(item);
        
        
        

        
        
        
        
        
        ind.setMenu(menu);
        
        ind.start();
    }

}
