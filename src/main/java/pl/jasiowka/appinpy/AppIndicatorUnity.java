package pl.jasiowka.appinpy;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.FileUtils;
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
    private WebServer webServer;
    private PythonExecutor executor;

    {
        loadPythonSnippet("base");
    }

    private AppIndicatorUnity() {
        //logger.debug("Temperature set to {}. Old temperature was {}.");
        //logger.info("Temperature has risen above 50 degrees.");
        id = "appindicator" + Sequence.next();
        text = "AppIndicator";
        tmpFolderPath = "/tmp";
        tmpFolder = "/.appinpy";
        icon = loadIcon("apple22");
        signals = new Signals();
    }

    public static AppIndicatorUnity getIndicator() {
        if (indicator == null)
            indicator = new AppIndicatorUnity();
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

//    public void itemSelectionSignal(String itemId) {
//        List<MenuItemUnity> items = menu.getItems();
//        for (MenuItemUnity item : items) {
//            if (item.getId().equals(itemId)) {
//                item.getListener().onItemSelect();
//            }
//        }
//    }

    public void shutdown() {
    	webServer.shutdown();
    }

    public void setIcon(ImageIcon icon) {
        //
    }

    public void setMenu(Menu menu) {
        if (menu != null) {
            if (menu instanceof MenuUnity) {
                this.menu = (MenuUnity) menu;
            }
        }
    }

    public void start() {
        deployPython();
        String exePath = tmpFolderPath + tmpFolder + "/appindicator_script.py";
        startRpcServer();
        executor = new PythonExecutor(exePath);
        executor.start();
        //Shell.execute(new String[] {"python", exePath});
    }

    public void stop() {
        //
    }

    @Override
    public String getCode() {
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("parentObjectId", "self.ind");
        String completeMenu = AppinpyUtils.mergeCode(menu.getCode(), replacements, true);
        
        replacements.put("menu", completeMenu);
        replacements.put("mainMenuId", menu.getId());
        replacements.put("actions", menu.getActionCode());
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

    private void startRpcServer() {
        try {
            webServer = new WebServer(8000);
            XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
            PropertyHandlerMapping mapper = new PropertyHandlerMapping();
            mapper.addHandler("Signals", signals.getClass());
            xmlRpcServer.setHandlerMapping(mapper);
            webServer.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRpcServer() {
        webServer.shutdown();
    }

    @Override
    public String getActionCode() {
        return null;
    }

}
