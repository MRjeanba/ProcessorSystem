import java.util.Arrays;

public class Process {
	
	public String state;
	public String id;
	public int numberOfInstructions;
	public int[] IORequestAtInstruction;
	public int[] IODevicesRequested;
	public PCB pcb;
	public int ioRequestNumber;
	
	public Process(String id,int nOfInstructions, int[] instructionsRequest, int[] requestedDevices) {
		this.state = "ready";
		this.id = id;
		this.numberOfInstructions = nOfInstructions;
		this.IORequestAtInstruction = instructionsRequest;
		this.IODevicesRequested = requestedDevices;
		this.ioRequestNumber = 0;//IORequestAtInstruction.length;
		this.pcb = new PCB(this);
	}
	
	/**
	 * Help to not write a long dot notation object (process.pcb.programCounter)
	 * @return the current instruction at which the process is at
	 */
	public int getCurrentInstruction() {
		return this.pcb.programCounter;
	}
	
	/**
	 * Get the instruction at which the next IO request will be executed
	 * @return the instruction number when a new IO request should be executed
	 */
	public int getIORequestInstructionNumber() {
		
		if(this.ioRequestNumber >= this.IORequestAtInstruction.length) {
			return -1;
		}
		return this.IORequestAtInstruction.length == 0 ?  -1 :  this.IORequestAtInstruction[this.ioRequestNumber];
	}
	
	/**
	 * 
	 * @return which IO device is being called (1 or 2)
	 */
	public int getIODeviceRequested() {
		return this.IODevicesRequested[this.ioRequestNumber];
	}
	
	/**
	 * This function has to be called on the running process from the CPU at each tick time, it updates the instructions executed
	 * @param clockTimeElapsed the number of instructions the process has executed on the CPU.
	 */
	public void incrementInstruction(int clockTimeElapsed) {
		this.pcb.programCounter++;
		this.pcb.cyclesExecuted = clockTimeElapsed; //clockTimeElapsed + 1 % 2 == 0 ? 0 : 1;
	}
	
	
	public void stateToWaiting() {
		this.state = "waiting";

	}
	
	
	public void stateToRunning() {
		this.state = "running";
	}
	
	
	public void stateToReady() {
		this.state = "ready";
	}
	
	
	public void stateToTerminated() {
		this.state = "terminated";
	}
	
	@Override
	public String toString() {
		return "Process informations: \nid: " + this.id + "\nInstructions: " + this.numberOfInstructions + "\nArray of instructions: " + Arrays.toString(this.IORequestAtInstruction) +
				"\nDevices Requested: " + Arrays.toString(this.IODevicesRequested) + "\nState:" + this.state; 
	}
}
