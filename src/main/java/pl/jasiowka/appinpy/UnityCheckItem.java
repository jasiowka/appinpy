package pl.jasiowka.appinpy;

import java.util.HashMap;
import java.util.Map;
import pl.jasiowka.appinpy.UnityIndicator.ItemAction;

class UnityCheckItem extends PythonSkeleton implements CheckItem {

    {
        loadPythonSnippet("menu_item_check_create");
        loadPythonSnippet("menu_item_add_action");
        loadPythonSnippet("menu_item_append");
        loadPythonSnippet("menu_item_check_exec_action");
        loadPythonSnippet("java_actions");
        loadPythonSnippet("java_check_actions");
    }

    protected String label;
    protected boolean enabled;
    protected boolean visible;
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
        // send signal
        indicator.doItemAction(id, ItemAction.SET_LABEL, label);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        // send signal
        if (enabled)
            indicator.doItemAction(id, ItemAction.ENABLE, null);
        else
            indicator.doItemAction(id, ItemAction.DISABLE, null);
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
        // send signal
        if (visible)
            indicator.doItemAction(id, ItemAction.SHOW, null);
        else
            indicator.doItemAction(id, ItemAction.HIDE, null);
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
        // send signal
        if (checked)
            indicator.doItemAction(id, ItemAction.CHECK, null);
        else
            indicator.doItemAction(id, ItemAction.UNCHECK, null);
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
    public String getMenuActionCode() {
        String actionCode = null;
        if (listener != null) {
            Map<String, String> replacements = new HashMap<String, String>();
            replacements.put("itemId", id);
            actionCode = Utils.mergeCode(snippets.get("menu_item_check_exec_action"), replacements, true);
        }
        return actionCode;
    }

    @Override
    public String getJavaActionsCode() {
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("javaCheckActions", snippets.get("java_check_actions"));
        String all = Utils.mergeCode(snippets.get("java_actions"), replacements, true);
        replacements.put("itemId", id);
        return Utils.mergeCode(all, replacements, true);
    }

}
