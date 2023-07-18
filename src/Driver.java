import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.Scanner;

public class Driver {

	public static String fileName = "Processes.txt";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String fileContentString = Helpers.getFileContent(fileName);
		LinkedList<Process> processes =  Helpers.initProcesses(fileContentString);
		
		// Initialize the two IO devices:
		IODevice IO1 = new IODevice();
		IODevice IO2 = new IODevice();
		
		// Initialize the single processor system and load the processes to the system
		CPU processorSystem = new CPU(IO1, IO2);
		processorSystem.loadProcesses(processes);
		
		processorSystem.run();
		
	}

}
