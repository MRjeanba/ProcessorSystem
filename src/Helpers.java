import java.io.File;
import java.io.FileNotFoundException;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Helpers {
	
	public static int[] transformToIntArr(LinkedList<Integer> t) {
		
		if (t.size()!=0) {
			int[] arr = new int[t.size()];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = t.get(i);
			}
			return arr;
		}
		int[] empty = new int[0];
		return empty;

	}
	
	public static String getFileContent(String fileName) {
		File processFile = new File(fileName);
		Scanner fileReader;
		String fileContent = "";
		try {
			fileReader = new Scanner(processFile);
			
			// dont get the first line since it is just attribute name of the data
			fileReader.nextLine();
			while (fileReader.hasNextLine()) {
				fileContent += fileReader.nextLine() + "\n";
			}
			fileReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Here is your file content :\n" + fileContent);
		
		return fileContent;
	}
	
	public static LinkedList<Process> initProcesses(String fileContent) {
		
		// first we convert the file content into an array where each index is a line (so a process)
		String[] arrayOfStringProcesses = fileContent.split("\n");
		String[] teString = arrayOfStringProcesses[0].split(",");
		LinkedList<Process> processList = new LinkedList<Process>();

		// for each index of the above array, we create a process and add it to a list
		for (int i = 0; i < arrayOfStringProcesses.length; i++) {
			int indexOfClosingBracket = arrayOfStringProcesses[i].indexOf("]");
			arrayOfStringProcesses[i] = arrayOfStringProcesses[i].substring(0, indexOfClosingBracket + 1) + "," +  arrayOfStringProcesses[i].substring(indexOfClosingBracket + 1);

			// variables of the process to fill in order to create the process
			int arrayNumber = 0;
			LinkedList<Integer> IORequestAtInstruction = new LinkedList<Integer>();
			LinkedList<Integer> IODevicesRequested = new LinkedList<Integer>();
			
			String[] stringArr = arrayOfStringProcesses[i].split(",");
			String id = stringArr[0];
			int nOfInstruct = Integer.parseInt(stringArr[1].trim());
			for (int j = 0; j < stringArr.length; j++) {
				
				if(stringArr[j].contains("[")) {
					
					String arrayBuilder = "";
					// up to the closing bracket, we fill the array IORequestAtInstruction of the process
					while (!stringArr[j].contains("]")) {
						arrayBuilder += stringArr[j].replace("[", "")+",";
						++j;
					}
					//arrayBuilder += stringArr[j].replace("]", "");
					arrayBuilder += stringArr[j];
					int index = arrayBuilder.indexOf("]");
					arrayBuilder = arrayBuilder.substring(0, index);
					++arrayNumber;
					arrayBuilder = arrayBuilder.trim();
					// array number is one, the we fill the IORequestAtInstruction array
					if(arrayNumber == 1) {
						if(arrayBuilder.length() != 1) {
							String[] IORequestAtInstructionString = arrayBuilder.split(",");
							
							for (int k = 0; k < IORequestAtInstructionString.length; k++) {
								System.out.println(IORequestAtInstructionString[k]);
								IORequestAtInstruction.add(Integer.parseInt(IORequestAtInstructionString[k].trim().replace("[", "")));
							}
							System.out.println(IORequestAtInstruction.toString());
						}
					} else if (arrayNumber == 2) {
						if(arrayBuilder.length() != 1) {
							String[] IODevicesRequestedString = arrayBuilder.split(",");
							for (int k = 0; k < IODevicesRequestedString.length; k++) {
								IODevicesRequested.add(Integer.parseInt(IODevicesRequestedString[k].trim().replace("[", "")));
							}
							System.out.println(IODevicesRequested.toString());

						}
						System.out.println("\n");
					}
					
				}
				

			}
			int[] IORequestAtInstr = transformToIntArr(IORequestAtInstruction);
			int[] IODevicesRequestedArr = transformToIntArr(IODevicesRequested);
			processList.add(new Process(id, nOfInstruct,IORequestAtInstr,IODevicesRequestedArr));

		}
		return processList;
	}
	
}
