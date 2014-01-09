package pl.jasiowka.appinpy;

import java.util.HashMap;
import java.util.Map;

class UnityRadioItem extends PythonSkeleton implements RadioItem {

    {
        loadPythonSnippet("menu_item_radio_create");
        loadPythonSnippet("menu_item_add_action");
        loadPythonSnippet("menu_item_append");
        loadPythonSnippet("menu_item_radio_exec_action");
    }

    protected String label;
    protected ItemListener listener;
    protected boolean checked;
    protected UnityRadioGroup group;

    UnityRadioItem(String label) {
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
    public void check() {
        if (group == null)
            throw new NullPointerException("Item not assigned to group, can't change 'checked' property");
        else if (!checked) {
            group.checkItem(this);
            // TODO send signal
        }
    }

    /**
     * This can be used only by RadioGroup !!
     * @param checked
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String getCode() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(snippets.get("menu_item_radio_create") + "\n");
        if (listener != null)
            sbuf.append(snippets.get("menu_item_add_action") + "\n");
        sbuf.append(snippets.get("menu_item_append"));
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("itemId", id);
        replacements.put("itemLabel", label);
        return Utils.mergeCode(sbuf.toString(), replacements, true);
    }

    @Override
    public String getActionCode() {
        String actionCode = null;
        if (listener != null) {
            Map<String, String> replacements = new HashMap<String, String>();
            replacements.put("itemId", id);
            actionCode = Utils.mergeCode(snippets.get("menu_item_radio_exec_action"), replacements, true);
        }
        return actionCode;
    }

    @Override
    public RadioGroup getGroup() {
        return group;
    }

    public void setGroup(UnityRadioGroup group) {
        this.group = group;
    }

}