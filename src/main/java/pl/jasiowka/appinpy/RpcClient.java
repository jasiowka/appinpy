package pl.jasiowka.appinpy;

import java.net.URL;
import java.util.Vector;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class RpcClient {

	public static void main(String args[]) {
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL("http://127.0.0.1:8003"));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);
			Vector<Object> params = new Vector<Object>();
			//params.addElement(new String("I am a confidant guy"));
			System.out.println("sfds");
			int result = (int) client.execute("shutdown", params);
			System.out.println("sfdsfdsfd");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
