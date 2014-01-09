package pl.jasiowka.appinpy;

import java.util.HashMap;
import java.util.Map;

class MenuItemUnity extends PythonSnippet implements PythonCode, MenuItem {

    protected String id;
    protected String text;
    protected ItemListener listener;
    protected boolean checked;

    {
        loadPythonSnippet("menu_item_create");
        loadPythonSnippet("menu_item_add_action");
        loadPythonSnippet("menu_item_append");
        loadPythonSnippet("menu_item_exec_action");
    }

    MenuItemUnity(String text, ItemListener listener) {
        id = "item" + Sequence.next();
        this.text = text;
        this.listener = listener;
        checked = false;
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
    public ItemListener getListener() {
        return listener;
    }

    @Override
    public boolean isCheckable() {
        return false;
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
