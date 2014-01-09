package pl.jasiowka.appinpy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UnityMenu extends PythonSkeleton implements Menu {

    private String label;
    private List<Element> items;

    {
        loadPythonSnippet("menu_base");
        loadPythonSnippet("submenu_base");
    }

    UnityMenu(String label) {
        this.label = label;
        items = new ArrayList<Element>();
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = Utils.fit(label);
    }

    @Override
    public void addItem(Item item) {
        if (item == null)
            throw new NullPointerException("Can't add null MenuItem");
        else if (item instanceof UnityItem)
            items.add((Element) item);
        else
            throw new ClassCastException("UnityMenu.addItem() needs UnityItem class instance passed as a parameter");
    }

    @Override
    public void addCheckItem(CheckItem checkItem) {
        if (checkItem == null)
            throw new NullPointerException("Can't add null CheckItem");
        else if (checkItem instanceof CheckItem)
            items.add((Element) checkItem);
        else
            throw new ClassCastException("UnityMenu.addCheckItem() needs UnityCheckItem class instance passed as a parameter");
    }

    @Override
    public void addRadioGroup(RadioGroup radioGroup) {
        if (radioGroup == null)
            throw new NullPointerException("Can't add null RadioGroup");
        else if (radioGroup instanceof UnityRadioGroup)
            items.add((Element) radioGroup);
        else
            throw new ClassCastException("UnityMenu.addRadioGroup() needs UnityRadioGroup class instance passed as a parameter");
    }

    @Override
    public void addSubMenu(Menu subMenu) {
        if (subMenu == null)
            throw new NullPointerException("Can't add null Submenu");
        else if (subMenu instanceof UnityMenu)
            items.add((Element) subMenu);
        else
            throw new ClassCastException("UnityMenu.addSubMenu() needs UnityMenu class instance passed as a paramenter");
    }

    @Override
    public void addSeparator() {
        UnitySeparatorItem usi = new UnitySeparatorItem();
        items.add((Element) usi);
    }

    public List<ReactableElement> getActionItems() {
        List<ReactableElement> litems = new ArrayList<ReactableElement>();
        for (Element item : items) {
            if (item instanceof ReactableElement) {
            	ReactableElement rel = ((ReactableElement) item);
                if (rel.getListener() != null);
                    litems.add(rel);
            }
            else if (item instanceof UnityRadioGroup) {
                UnityRadioGroup urg = (UnityRadioGroup) item;
                for (RadioItem ri : urg.getItems()) {
                    if (ri.getListener() != null)
                        litems.add(ri);
                }
            }
            else if (item instanceof UnityMenu) {
                UnityMenu um = ((UnityMenu) item);
                litems.addAll(um.getActionItems());
            }
        }
        return litems;
    }

    @Override
    public String getCode() {
    	StringBuilder sbuf = new StringBuilder();
        Map<String, String> replacements = new HashMap<String, String>();
        /* iterate through all menu items */
        for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
            Element item = items.get(itemIndex);
            /* when normal item */
            if (item instanceof UnityItem) {
            	UnityItem ui = (UnityItem) item;
                sbuf.append(ui.getCode() + "\n");
            }
            /* when checkitem */
            else if (item instanceof UnityCheckItem) {
            	UnityCheckItem uci = (UnityCheckItem) item;
                sbuf.append(uci.getCode() + "\n");
            }
            /* when separator */
            else if (item instanceof UnitySeparatorItem) {
            	UnitySeparatorItem usi = (UnitySeparatorItem) item;
                sbuf.append(usi.getCode() + "\n");
            }
            /* when radiogroup */
            else if (item instanceof UnityRadioGroup) {
                UnityRadioGroup urg = (UnityRadioGroup) item;
                sbuf.append(urg.getCode() + "\n");
            }
            /* when submenu */
            else if (item instanceof UnityMenu) {
                UnityMenu um = (UnityMenu) item;
                replacements.put("menuId", um.getId());
                replacements.put("menuText", um.getLabel());
                replacements.put("subMenuId", "item" + Sequence.next());
                replacements.put("parentMenuId", id);
                String subMenuBase = Utils.mergeCode(snippets.get("submenu_base"), replacements, true);
                sbuf.append(um.getCode() + "\n");
                sbuf.append(subMenuBase + "\n");
            }
        }
        replacements = new HashMap<String, String>();
        replacements.put("parentMenuId", id);
        String items = Utils.mergeCode(sbuf.toString(), replacements, true);

        replacements = new HashMap<String, String>();
        replacements.put("items", items.replaceAll("\\s+$", ""));
        replacements.put("menuId", id);
        return Utils.mergeCode(snippets.get("menu_base"), replacements, true);
    }

    @Override
    public String getActionCode() {
        String allActionCode = null;
        StringBuilder sbuf = new StringBuilder();
        for (Element item : items) {
            PythonSkeleton ps = (PythonSkeleton) item;
            String singleActionCode = ps.getActionCode();
            if (singleActionCode != null)
                sbuf.append(singleActionCode + "\n");
        }
        allActionCode = sbuf.toString();
        return allActionCode.length() > 0 ? allActionCode.replaceAll("\\s+$", "") : null;
    }

}
