package pl.jasiowka.appinpy;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

class PythonSnippet {

    protected static Map<String, String> snippets;

    {
        if (snippets == null)
            snippets = new HashMap<String, String>();
    }

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
