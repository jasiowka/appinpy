package pl.jasiowka.appinpy;

import java.util.HashMap;
import java.util.Map;

class UnityCheckItem extends PythonSkeleton implements CheckItem {

    {
        loadPythonSnippet("menu_item_check_create");
        loadPythonSnippet("menu_item_add_action");
        loadPythonSnippet("menu_item_append");
        loadPythonSnippet("menu_item_check_exec_action");
    }

    protected String label;
    protected ItemListener listener;
    protected boolean checked;

    UnityCheckItem(String label) {
        setLabel(label);
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = Utils.fit(label);
        // TODO send signal
    }

    @Override
    public ItemListener getListener() {
        return listener;
    }

    @Override
    public void setListener(ItemListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        // TODO send signal
    }

    @Override
    public String getCode() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(snippets.get("menu_item_check_create") + "\n");
        if (listener != null)
            sbuf.append(snippets.get("menu_item_add_action") + "\n");
        sbuf.append(snippets.get("menu_item_append"));
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("itemId", id);
        replacements.put("itemText", label);
        return Utils.mergeCode(sbuf.toString(), replacements, true);
    }

    @Override
    public String getActionCode() {
        String actionCode = null;
        if (listener != null) {
            Map<String, String> replacements = new HashMap<String, String>();
            replacements.put("itemId", id);
            actionCode = Utils.mergeCode(snippets.get("menu_item_check_exec_action"), replacements, true);
        }
        return actionCode;
    }

}
