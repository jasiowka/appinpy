package pl.jasiowka.appinpy;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.FileUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.webserver.WebServer;

class AppIndicatorUnity extends PythonSnippet implements PythonCode, AppIndicator {

    //final Logger logger = LoggerFactory.getLogger(AppIndicatorImpl.class);
    private String id;
    private String text;
	private static AppIndicatorUnity indicator;
    private String tmpFolderPath;
    private String tmpFolder;
    private ImageIcon icon;
    private MenuUnity menu;
    private Signals signals;
    private WebServer rpcServer;
    private XmlRpcClient rpcClient;
    private PythonExecutor executor;
    private String quitText;
    private ItemListener quitListener;
    private boolean working;

    {
        loadPythonSnippet("base");
    }

    private AppIndicatorUnity(ImageIcon icon) {
        //logger.debug("Temperature set to {}. Old temperature was {}.");
        //logger.info("Temperature has risen above 50 degrees.");
        id = "appindicator" + Sequence.next();
        text = "AppIndicator";
        tmpFolderPath = "/tmp";
        tmpFolder = "/.appinpy";
        this.icon = icon;//loadIcon("apple22");
        signals = new Signals();
        quitText = "Quit";
        working = false;
    }

    public static void createIndicator(ImageIcon icon) {
        if (indicator == null)
            indicator = new AppIndicatorUnity(icon);
    }

    public static AppIndicatorUnity getIndicator() {
        return indicator;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    //@Override
    //public void setIcon(ImageIcon icon) {
        //try {
        //    this.icon = icon;
        //    /* if appinpy is working, then redeploy the new icon and send python signal to change it */
        //    if (working) {
        //        exportIcon(icon);
        //        Vector<Object> params = new Vector<Object>();
        //        rpcClient.execute("changeIcon", params);
        //    }
        //}
        //catch (XmlRpcException e) {
        //    e.printStackTrace();
        //}
    //}

    @Override
    public void setMenu(Menu menu) {
        if (menu != null) {
            if (menu instanceof MenuUnity) {
                this.menu = (MenuUnity) menu;
            }
        }
    }

    @Override
    public void start() {
        deployPython();
        String exePath = tmpFolderPath + tmpFolder + "/appindicator_script.py";
        startRpcServer();
        createRpcClient();
        executor = new PythonExecutor(exePath);
        executor.start();
        working = true;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (working)
                    AppIndicatorUnity.this.stop();
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
            menu = (MenuUnity) AppinpyFactory.createMenu("MainMenu");
        String completeMenu = AppinpyUtils.mergeCode(menu.getCode(), replacements, true);
        
        replacements.put("menu", completeMenu);
        replacements.put("mainMenuId", menu.getId());
        replacements.put("actions", menu.getActionCode());
        replacements.put("quitText", quitText);
        //StringBuffer sbuf = new StringBuffer();
//        for (PythonCodeGen item : menu.getItems()) {
//            if (item.getListener() != null) {
//                replacements.put("itemId", item.getId());
//                sbuf.append(AppinpyUtils.mergeCode(snippets.get("action_exec"), replacements, true) + "\n");
//            }
//        }
        //replacements.put("actions", sbuf.toString());
        return AppinpyUtils.mergeCode(snippets.get("base"), replacements, true);
        //return menu.getCode() + "\n";
    }

    @Override
    public String getActionCode() {
        return null;
    }

    @Override
    public void setQuitText(String text) {
        if (text != null)
            this.quitText = quitText;
    }

    @Override
    public void setQuitListener(ItemListener listener) {
        this.quitListener = listener;
    }

    private ImageIcon loadIcon(String imageName) {
        String resourcePath = "images/" + imageName + ".png";
        URL is = getClass().getClassLoader().getResource(resourcePath);
        return new ImageIcon(is);
    }

    private void deployPython() {
        try {
            // remove old one and create a new temporary folder
            File deployFolder = new File(tmpFolderPath + tmpFolder);
            FileUtils.deleteDirectory(deployFolder);
            boolean result = deployFolder.mkdir();
            // export python script
            FileUtils.writeStringToFile(new File(tmpFolderPath + tmpFolder + "/appindicator_script.py"), getCode());
            // export icon file
            exportIcon(icon);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportIcon(ImageIcon icon) {
        try {
    	// export icon file
        BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        icon.paintIcon(null, g, 0,0);
        g.dispose();
        File iconFile = new File(tmpFolderPath + tmpFolder + "/icon.png");
        ImageIO.write(bi, "png", iconFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void itemSelectionSignal(String itemId, boolean checked) {
        List<MenuItemUnity> items = menu.getActionItems();
        for (MenuItemUnity item : items) {
            if (item.getId().equals(itemId)) {
                ActionInfoUnity ai = new ActionInfoUnity();
                // nie trzeba ustawiać checkable
                item.setChecked(checked);
                ai.setSource(item);
                item.getListener().onItemSelection(ai);
            }
        }
    }

    private void shutdown() {
        working = false;
        rpcServer.shutdown();
        if (quitListener != null) {
            ActionInfoUnity ai = new ActionInfoUnity();
            quitListener.onItemSelection(ai);
        }
    }

    public static class Signals {

	    public Integer itemSelection(String itemId, boolean checked) {
	        AppIndicatorUnity.getIndicator().itemSelectionSignal(itemId, checked);
	        return 0;
	    }

	    public Integer shutdown() {
	        AppIndicatorUnity.getIndicator().shutdown();
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
