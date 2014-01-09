package pl.jasiowka.appinpy;

import java.util.HashMap;
import java.util.Map;

class UnitySeparatorItem extends PythonSkeleton implements Element {

    {
        loadPythonSnippet("menu_item_sep_create");
        loadPythonSnippet("menu_item_add_action");
        loadPythonSnippet("menu_item_append");
        loadPythonSnippet("menu_item_check_exec_action");
    }

    UnitySeparatorItem() {
    }

    @Override
    public String getCode() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(snippets.get("menu_item_sep_create") + "\n");
        sbuf.append(snippets.get("menu_item_append"));
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("itemId", id);
        return Utils.mergeCode(sbuf.toString(), replacements, true);
    }

    @Override
    public String getActionCode() {
        return null;
    }

}
