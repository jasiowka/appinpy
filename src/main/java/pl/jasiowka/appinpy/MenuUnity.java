package pl.jasiowka.appinpy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MenuUnity extends PythonSnippet implements PythonCode, Menu {

    private String id;
    private String text;
    private List<PythonCode> items;

    {
        loadPythonSnippet("menu_base");
        loadPythonSnippet("submenu_base");
    }

    MenuUnity(String text) {
        id = "menu" + Sequence.next();
        this.text = text;
        items = new ArrayList<PythonCode>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    //public List<MenuItemUnity> getItems() {
    //    return new ArrayList<MenuItemUnity>();
        //return items;
    //}

    @Override
    public void add(MenuItem menuItem) {
        if (menuItem != null) {
            if (menuItem instanceof MenuItemUnity) {
                items.add((PythonCode) menuItem);
            }
        }
    }

    @Override
    public void add(Menu menu) {
        if (menu != null) {
            if (menu instanceof MenuUnity) {
                items.add((PythonCode) menu);
            }
        }
    }

    @Override
    public String getCode() {
    	StringBuilder sbuf = new StringBuilder();
        Map<String, String> replacements = new HashMap<String, String>();
        // iterate through all menu items
        for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
            PythonCode item = items.get(itemIndex);
            /* when normal item */
            if (item instanceof MenuItemUnity) {
                sbuf.append(item.getCode() + "\n");
            }
            /* when submenu */
            else if (item instanceof MenuUnity) {
                replacements.put("menuId", item.getId());
                replacements.put("menuText", item.getText());
                replacements.put("subMenuId", "item" + Sequence.next());
                replacements.put("parentMenuId", id);
                String subMenuBase = AppinpyUtils.mergeCode(snippets.get("submenu_base"), replacements, true);
                sbuf.append(item.getCode() + "\n");
                sbuf.append(subMenuBase + "\n");
            }
        }
        replacements = new HashMap<String, String>();
        replacements.put("parentMenuId", id);
        //replacements.put("parentObjectId", id);
        String items = AppinpyUtils.mergeCode(sbuf.toString(), replacements, true);

        replacements = new HashMap<String, String>();
        replacements.put("items", items);
        replacements.put("menuId", id);
        return AppinpyUtils.mergeCode(snippets.get("menu_base"), replacements, true);
    }

    @Override
    public String getActionCode() {
    	StringBuilder sbuf = new StringBuilder();
        for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
        	PythonCode item = items.get(itemIndex);
            sbuf.append(item.getActionCode() + "\n");
        }
        return sbuf.toString();
    }

}
