package pl.jasiowka.appinpy;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.webserver.WebServer;

public class RcpServer {

	public static void main(String[] args) {
		try {
			WebServer webServer = new WebServer(8000);
			XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
			PropertyHandlerMapping mapper = new PropertyHandlerMapping();
			Calculator calc = new Calculator();
			//SignalsDup sign = new SignalsDup();
			mapper.addHandler("Calculator", calc.getClass());
			//mapper.addHandler("SignalsDup", sign.getClass());
			xmlRpcServer.setHandlerMapping(mapper);
			webServer.start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
