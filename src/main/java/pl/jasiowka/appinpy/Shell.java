package pl.jasiowka.appinpy;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Piotr Jasiowka
 * @TODO być może chodzi o czas pomiędzy wykonaniem kolejnych komend. może
 *       trzeba trochę poczekać?
 */
public class Shell {
	
	public static String execute(String[] commandArray) {

        Process proc = null;
        int inBuffer, errBuffer;
        int result = 0;
        StringBuffer outputReport = new StringBuffer();
        StringBuffer errorBuffer = new StringBuffer();

        try {
            proc = Runtime.getRuntime().exec(commandArray);
        } catch (IOException e) {
            return "";
        }
        try {
            result = proc.waitFor();
        } catch (InterruptedException e) {
            return "";
        }
        if (proc != null && null != proc.getInputStream()) {
            InputStream is = proc.getInputStream();
            InputStream es = proc.getErrorStream();
            OutputStream os = proc.getOutputStream();

            try {
                while ((inBuffer = is.read()) != -1) {
                    outputReport.append((char) inBuffer);
                }

                while ((errBuffer = es.read()) != -1) {
                    errorBuffer.append((char) errBuffer);
                }

            } catch (IOException e) {
                return "";
            }
            try {
                is.close();
                is = null;
                es.close();
                es = null;
                os.close();
                os = null;
            } catch (IOException e) {
                return "";
            }

            proc.destroy();
            proc = null;
        }


        if (errorBuffer.length() > 0) {
            //logger.error("could not finish execution because of error(s).");
            //logger.error("*** Error : " + errorBuffer.toString());
            return "";
        }


        return outputReport.toString();
    }
	
	
	public static void main(String[] args) {
		
		//tu można wysłać sygnał rpc do pythona, żeby usunął ikonę i zakończył działanie
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		    public void run() {
		        System.out.println("end");
		    }
		}));
		
		//execute(new String[] {"python", "/home/wallie/indykator.py"});
	}
	

//	private List<String> shellCommand(String command) throws IOException {
//		List<String> output = new ArrayList<String>();
//		Process proc = Runtime.getRuntime().exec(command);
//		BufferedInputStream buffer = new BufferedInputStream(
//				proc.getInputStream());
//		BufferedReader commandOutput = new BufferedReader(
//				new InputStreamReader(buffer));
//		String line = null;
//		while ((line = commandOutput.readLine()) != null)
//			output.add(line);
//		commandOutput.close();
//		return output;
//	}

}