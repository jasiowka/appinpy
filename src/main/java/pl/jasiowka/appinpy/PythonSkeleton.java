package pl.jasiowka.appinpy;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

abstract class PythonSkeleton implements Element {

    protected static Map<String, String> snippets;
    protected String id;
    protected static UnityIndicator indicator;

    {
        if (snippets == null)
            snippets = new HashMap<String, String>();
    }

    PythonSkeleton() {
        id = Sequence.next();
    }

    public String getId() {
        return id;
    }

    public abstract String getCode();

    public abstract String getMenuActionCode();

    public abstract String getJavaActionsCode();

    protected void loadPythonSnippet(String snippetName) {
        if ((snippetName != null) && (!snippets.containsKey(snippetName))) {
            try {
                String resourcePath = "python/v27/" + snippetName + ".py";
                URL is = getClass().getClassLoader().getResource(resourcePath);
                snippets.put(snippetName, IOUtils.toString(is).trim());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
