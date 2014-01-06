package pl.jasiowka.appinpy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class PythonExecutor extends Thread {

    private ProcessBuilder processBuilder;
    private Process process;
    private List<String> commandLine;

    PythonExecutor(String filename) {
        commandLine = new ArrayList<String>();
        commandLine.add("python");
        commandLine.add(filename);
    }

    public void run() {
        try {
            processBuilder = new ProcessBuilder(commandLine);
            process = processBuilder.start();
            process.waitFor();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

}
