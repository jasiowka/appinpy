package pl.jasiowka.appinpy;

import java.util.HashMap;
import java.util.Map;

class MenuItemUnity extends PythonSnippet implements PythonCode, MenuItem {

    private String id;
    private String text;
    private ItemListener listener;

    {
        loadPythonSnippet("menu_item_create");
        loadPythonSnippet("menu_item_add_action");
        loadPythonSnippet("menu_item_append");
        loadPythonSnippet("menu_item_exec_action");
    }

    MenuItemUnity(String text) {
        id = "item" + Sequence.next();
        this.text = text;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getCode() {
        // we build code from loaded snippets
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(snippets.get("menu_item_create") + "\n");
        if (listener != null)
            sbuf.append(snippets.get("menu_item_add_action") + "\n");
        sbuf.append(snippets.get("menu_item_append") + "\n");
        // and then insert values into code
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("itemId", id);
        replacements.put("itemText", text);
        return AppinpyUtils.mergeCode(sbuf.toString(), replacements, true);
    }

    @Override
    public String getActionCode() {
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("itemId", id);
        return AppinpyUtils.mergeCode(snippets.get("menu_item_exec_action"), replacements, true);
    }

    @Override
    public void setListener(ItemListener listener) {
        this.listener = listener;
    }

    @Override
    public ItemListener getListener() {
        return listener;
    }

}
