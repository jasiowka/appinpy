package pl.jasiowka.appinpy;

import java.util.HashMap;
import java.util.Map;

class SeparatorMenuItemUnity extends PythonSnippet implements PythonCode, MenuItem {

    private String id;

    {
        loadPythonSnippet("menu_item_sep_create");
        loadPythonSnippet("menu_item_append");
    }

    SeparatorMenuItemUnity() {
        id = "item" + Sequence.next();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public String getCode() {
        // we build code from loaded snippets
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(snippets.get("menu_item_sep_create") + "\n");
        sbuf.append(snippets.get("menu_item_append") + "\n");
        // and then insert values into code
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("itemId", id);
        return AppinpyUtils.mergeCode(sbuf.toString(), replacements, true);
    }

    @Override
    public String getActionCode() {
        return null;
    }

    @Override
    public void setListener(ItemListener listener) {
    }

    @Override
    public ItemListener getListener() {
        return null;
    }

}
