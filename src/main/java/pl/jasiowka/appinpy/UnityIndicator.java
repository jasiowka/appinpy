package pl.jasiowka.appinpy;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.naming.InvalidNameException;
import javax.swing.ImageIcon;

import org.apache.commons.io.FileUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.webserver.WebServer;

class UnityIndicator extends PythonSkeleton implements Indicator {

    //final Logger logger = LoggerFactory.getLogger(AppIndicatorImpl.class);

    private String tmpFolderPath;
    private String tmpFolder;
    private String activeIcon;
    private UnityMenu menu;
    private Signals signals;
    private WebServer rpcServer;
    private XmlRpcClient rpcClient;
    private PythonExecutor executor;
    private String quitText;
    private ItemListener quitListener;
    private boolean working;
    private Set<String> icons;

    {
        loadPythonSnippet("base");
    }

    private UnityIndicator() {
        //logger.debug("Temperature set to {}. Old temperature was {}.");
        //logger.info("Temperature has risen above 50 degrees.");
        tmpFolderPath = "/tmp";
        tmpFolder = "/.appinpy";
        createDeployDir();
        //this.icon = icon;//loadIcon("apple22");
        signals = new Signals();
        quitText = "Quit";
        working = false;
        icons = new HashSet<String>();
    }

    public static void createIndicator() {
        if (indicator == null)
            indicator = new UnityIndicator();
    }

    public static UnityIndicator getIndicator() {
        return indicator;
    }

    @Override
    public void addIcon(String name, ImageIcon icon) {
        if (name == null)
            throw new NullPointerException("Icon's name can't be null");
        // TODO else if (!Utils.isCorrectIdentifier(name))
        //    throw new InvalidNameException("");
        else if (icon == null)
            throw new NullPointerException("Can't add null ImageIcon");
        else if (icons.add(name))
            exportIcon(name, icon);
    }

    @Override
    public void setActiveIcon(String name) {
        try {
            if (icons.contains(name)) {
                activeIcon = name;
                if (working) {
                    Vector<Object> params = new Vector<Object>();
                    params.add(activeIcon);
                    rpcClient.execute("changeIcon", params);
                }
            }
        }
        catch (XmlRpcException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setMenu(Menu menu) {
        if (menu != null) {
            if (menu instanceof UnityMenu) {
                this.menu = (UnityMenu) menu;
            }
        }
    }

    @Override
    public void start() {
    	working = true;
        deployPython();
        String exePath = tmpFolderPath + tmpFolder + "/appindicator_script.py";
        startRpcServer();
        createRpcClient();
        executor = new PythonExecutor(exePath);
        executor.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (working)
                    UnityIndicator.this.stop();
            }
        });
    }

    @Override
    public void stop() {
        try {
        	shutdown(); // it also sets working=false
            Vector<Object> params = new Vector<Object>();
            rpcClient.execute("shutdown", params);
        }
        catch (XmlRpcException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCode() {
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("parentObjectId", "self.ind");
        if (menu == null)
            menu = (UnityMenu) AppinpyFactory.createMenu("MainMenu");
        String completeMenu = Utils.mergeCode(menu.getCode(), replacements, true);
        
        replacements.put("menu", completeMenu);
        replacements.put("mainMenuId", menu.getId());
        replacements.put("actions", menu.getActionCode());
        replacements.put("quitText", quitText);
        replacements.put("startIcon", activeIcon);
        //StringBuffer sbuf = new StringBuffer();
//        for (PythonCodeGen item : menu.getItems()) {
//            if (item.getListener() != null) {
//                replacements.put("itemId", item.getId());
//                sbuf.append(AppinpyUtils.mergeCode(snippets.get("action_exec"), replacements, true) + "\n");
//            }
//        }
        //replacements.put("actions", sbuf.toString());
        return Utils.mergeCode(snippets.get("base"), replacements, true);
        //return menu.getCode() + "\n";
    }

    @Override
    public String getActionCode() {
        return null;
    }

    @Override
    public void setQuitLabel(String text) {
        if (text != null)
            this.quitText = quitText;
    }

    @Override
    public void setQuitListener(ItemListener listener) {
        this.quitListener = listener;
    }

    //private ImageIcon loadIcon(String imageName) {
    //    String resourcePath = "images/" + imageName + ".png";
    //    URL is = getClass().getClassLoader().getResource(resourcePath);
    //    return new ImageIcon(is);
    //}

    private void createDeployDir() {
        try {
            // remove old one and create a new temporary folder
            File deployFolder = new File(tmpFolderPath + tmpFolder);
            FileUtils.deleteDirectory(deployFolder);
            boolean result = deployFolder.mkdir();
            System.out.println("dir created: " + result);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deployPython() {
        try {
            // remove old one and create a new temporary folder
            //File deployFolder = new File(tmpFolderPath + tmpFolder);
            //FileUtils.deleteDirectory(deployFolder);
            //boolean result = deployFolder.mkdir();
            // export python script
            FileUtils.writeStringToFile(new File(tmpFolderPath + tmpFolder + "/appindicator_script.py"), getCode());
            // export icon file
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportIcon(String name, ImageIcon icon) {
        try {
    	// export icon file
        BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        icon.paintIcon(null, g, 0,0);
        g.dispose();
        File iconFile = new File(tmpFolderPath + tmpFolder + "/" + name + ".png");
        ImageIO.write(bi, "png", iconFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void itemSelectionSignal(String itemId, boolean checked) {
        List<ReactableElement> items = menu.getActionItems();
        for (ReactableElement rel : items) {
            PythonSkeleton ps = (PythonSkeleton) rel;
            if (ps.getId().equals(itemId)) {
                if (rel instanceof CheckItem) {
                    UnityCheckItem uci = (UnityCheckItem) rel;
                    uci.setChecked(checked);
                }
                else if (rel instanceof UnityRadioItem) {
                    UnityRadioItem uri = (UnityRadioItem) rel;
                    UnityRadioGroup urg = (UnityRadioGroup) uri.getGroup();
                    urg.checkItem(uri);
                }
                UnityActionInfo ai = new UnityActionInfo();
                ai.setSource(rel);
                rel.getListener().onItemSelection(ai);
            }
        }
    }

    private void shutdown() {
        working = false;
        rpcServer.shutdown();
        if (quitListener != null) {
            UnityActionInfo ai = new UnityActionInfo();
            quitListener.onItemSelection(ai);
        }
    }

    public static class Signals {

	    public Integer itemSelection(String itemId, boolean checked) {
	        UnityIndicator.getIndicator().itemSelectionSignal(itemId, checked);
	        return 0;
	    }

	    public Integer shutdown() {
	        UnityIndicator.getIndicator().shutdown();
	        return 0;
	    }

	}
    
    private void startRpcServer() {
        try {
            rpcServer = new WebServer(8000);
            XmlRpcServer xmlRpcServer = rpcServer.getXmlRpcServer();
            PropertyHandlerMapping mapper = new PropertyHandlerMapping();
            mapper.addHandler("Signals", signals.getClass());
            xmlRpcServer.setHandlerMapping(mapper);
            rpcServer.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createRpcClient() {
        try {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		    config.setServerURL(new URL("http://127.0.0.1:8003"));
		    rpcClient = new XmlRpcClient();
		    rpcClient.setConfig(config);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
