package pl.jasiowka.appinpy;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Go {

    public static void main(String[] args) {
        try {
    	// build my command as a list of strings
    	List<String> command = new ArrayList<String>();
    	command.add("python");
    	command.add("/home/wallie/indykator.py");
    	//command.add("/var/tmp");

    	// execute my command
    	SystemCommandExecutor commandExecutor = new SystemCommandExecutor(command);
    	int result = commandExecutor.executeCommand();

    	// get the output from the command
    	String stdout = commandExecutor.getStandardOutputFromCommand();
    	String stderr = commandExecutor.getStandardErrorFromCommand();
    	
    	//commandExecutor.eval("echo 'kupa'");
    	commandExecutor.eval("strwe");

    	// print the output from the command
    	//System.out.println("STDOUT");
    	System.out.println(stdout);
    	//System.out.println("STDERR");
    	//System.out.println(stderr);
    	
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
