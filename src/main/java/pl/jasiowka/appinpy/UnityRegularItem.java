package pl.jasiowka.appinpy;

import java.util.HashMap;
import java.util.Map;

import pl.jasiowka.appinpy.UnityIndicator.ItemAction;

class UnityRegularItem extends PythonSkeleton implements RegularItem {

    {
        loadPythonSnippet("menu_item_create");
        loadPythonSnippet("menu_item_add_action");
        loadPythonSnippet("menu_item_append");
        loadPythonSnippet("menu_item_exec_action");
        loadPythonSnippet("java_actions");
    }

    protected String label;
    protected boolean enabled;
    protected boolean visible;
    protected ItemListener listener;

    UnityRegularItem(String label) {
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
    public String getCode() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(snippets.get("menu_item_create") + "\n");
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
            actionCode = Utils.mergeCode(snippets.get("menu_item_exec_action"), replacements, true);
        }
        return actionCode;
    }

    @Override
    public String getJavaActionsCode() {
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("itemId", id);
        replacements.put("javaCheckActions", "");
        return Utils.mergeCode(snippets.get("java_actions"), replacements, true).replaceAll("\\s+$", "");
    }

}
