package pl.jasiowka.appinpy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UnityRadioGroup extends PythonSkeleton implements RadioGroup {

    {
        loadPythonSnippet("menu_item_radio_create");
    }

    protected List<UnityRadioItem> items;
    protected UnityRadioItem checkedItem;

    UnityRadioGroup() {
        items = new ArrayList<UnityRadioItem>();
    }

    @Override
    public String getCode() {
        String allCode = null;
        if (items.size() > 0) {
            Map<String, String> replacements = new HashMap<String, String>();
            StringBuilder sbuf = new StringBuilder();
            String groupId = "None";
            for (int idx = 0; idx < items.size(); idx++) {
                UnityRadioItem item = items.get(idx);
                String code = item.getCode();
                if (idx == 0) {
                    replacements.put("groupId", groupId);
                    groupId = "self." + item.getId();
                    code = Utils.mergeCode(code, replacements, true);
                }
                sbuf.append(code + "\n");
            }
            replacements.put("groupId", groupId);
            allCode = Utils.mergeCode(sbuf.toString().replaceAll("\\s+$", ""), replacements, true);
        }
        return allCode;
    }

    @Override
    public String getMenuActionCode() {
        String allActionCode = null;
        StringBuilder sbuf = new StringBuilder();
        for (UnityRadioItem item : items) {
            String singleActionCode = item.getMenuActionCode();
            if (singleActionCode != null)
                sbuf.append(singleActionCode + "\n");
        }
        allActionCode = sbuf.toString();
        return allActionCode.length() > 0 ? allActionCode.replaceAll("\\s+$", "") : null;
    }

    @Override
    public String getJavaActionsCode() {
        String allJavaActionsCode = null;
        StringBuilder sbuf = new StringBuilder();
        for (UnityRadioItem item : items) {
            String singleJavaActionsCode = item.getJavaActionsCode();
            if (singleJavaActionsCode != null)
                sbuf.append(singleJavaActionsCode + "\n");
        }
        allJavaActionsCode = sbuf.toString();
        return allJavaActionsCode.length() > 0 ? allJavaActionsCode.replaceAll("\\s+$", "") : null;
    }

    @Override
    public List<RadioItem> getItems() {
        List<RadioItem> its = new ArrayList<RadioItem>();
        for (UnityRadioItem uri : items)
            its.add(uri);
        return its;
    }

    @Override
    public void addItem(RadioItem item) {
        if (item == null)
            throw new NullPointerException("Item can't be null");
        else {
            UnityRadioItem uri = (UnityRadioItem) item;
            //uri.setIndicator(indicator);
            uri.setGroup(this);
            items.add(uri);
        }
    }

    @Override
    public RadioItem getChecked() {
        return checkedItem;
    }

    public void checkItem(UnityRadioItem item) {
        if (items.contains(item)) {
            for (UnityRadioItem it : items) {
                if (it.equals(item)) {
                    it.setChecked(true);
                    checkedItem = it;
                }
                else
                    it.setChecked(false);
            }
        }
    }

}
