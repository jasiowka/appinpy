package pl.jasiowka.appinpy;

import java.util.HashMap;
import java.util.Map;

class CheckMenuItemUnity extends MenuItemUnity {

    {
        loadPythonSnippet("menu_item_check_create");
        loadPythonSnippet("menu_item_check_exec_action");
    }

    CheckMenuItemUnity(String text, ItemListener listener) {
        super(text, listener);
    }

    @Override
    public String getCode() {
        // we build code from loaded snippets
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(snippets.get("menu_item_check_create") + "\n");
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
        return AppinpyUtils.mergeCode(snippets.get("menu_item_check_exec_action"), replacements, true);
    }

    @Override
    public boolean isCheckable() {
        return true;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

}
