import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

public class CPU {
	
	// need to simulate the execution of processes on a single processor system. Each process
	// takes 2 time units on the CPU (%2)
	
	public Queue<Process> readyQueue;
	public Queue<Process> waitingQueue;
	public Queue<Process> terminatedQueue;
	public IODevice io1;
	public IODevice io2;
	public int io1Counter;
	public int io2Counter;
	public int cpuCounter;
	public int r1;
	public int r2;
	public Process runningProcess;
	
	public CPU(IODevice io1, IODevice io2) {
		this.io1 = io1;
		this.io2 = io2;
		this.readyQueue = new LinkedList<Process>();
		this.waitingQueue = new LinkedList<Process>();
		this.terminatedQueue = new LinkedList<Process>();
	}

	// Load the processes to the ready queue
	public void loadProcesses(LinkedList<Process> p) {
		
		for (Process process : p) {
			this.readyQueue.add(process);
		}
		
	}
	
	public void run() {
		int min = 1;
		int max = 70;
		this.runningProcess = readyQueue.poll();
		
		// while we still have process waiting in these queues, the CPU should not stop
		while (!(readyQueue.isEmpty() && waitingQueue.isEmpty() && io1.waitingQueue.isEmpty() && io2.waitingQueue.isEmpty() && this.runningProcess == null) ) {
			System.out.println("cpu counter: " + cpuCounter );
			
			if (runningProcess == null && !readyQueue.isEmpty()) {
				readyToRunning();
			}
			
			if (runningProcess == null && !waitingQueue.isEmpty()) {
				manageIOQueues();
				System.out.println(printSystemInfo());
				if((cpuCounter + 1) % 2 == 0) {
					cpuCounter = 0;
				}else {
					++cpuCounter;
				}
				continue;
				
			}

			// executing an instruction:
			r1 = ThreadLocalRandom.current().nextInt(min, max + 1);
			r2 = ThreadLocalRandom.current().nextInt(min, max + 1);
			int sumExecution = r1 + r2;
			this.runningProcess.pcb.r1 = r1;
			this.runningProcess.pcb.r2 = r2;
			
			if(runningProcess.getCurrentInstruction() >= runningProcess.numberOfInstructions) {
				runningToTerminated();
				continue;
			}
			
			// check if the running process has to perform an IO request
			if (runningProcess.getCurrentInstruction() == runningProcess.getIORequestInstructionNumber()) {
				// we find at which IO device the request is made
				int device = runningProcess.getIODeviceRequested();	
				
				switch (device){
					
					case 1: {
						io1.requestIO(runningProcess);
						runningProcess.incrementInstruction(cpuCounter);
						runningToWaiting();
						break;
					}
					case 2:{
						io2.requestIO(runningProcess);
						runningProcess.incrementInstruction(cpuCounter);
						runningToWaiting();
						break;
					}
					default:{
						System.out.println("Something unexpected happend, CPU class, line 44 switch statement");
						break;
					}
					
				}
				manageIOQueues();
				System.out.println(printSystemInfo());
				if((cpuCounter + 1) % 2 == 0) {
					cpuCounter = 0;
				}else {
					++cpuCounter;
				}
				
				continue;
				
			}
			
			// Check if the IO waiting Queues are empty or not
			manageIOQueues();
			
			if((cpuCounter + 1) % 2 == 0) {
				runningProcess.incrementInstruction(cpuCounter);
				System.out.println(printSystemInfo());
				runningToReady();
				readyToRunning();
				cpuCounter = 0;

			} else {
				runningProcess.incrementInstruction(cpuCounter);
				System.out.println(printSystemInfo());
				cpuCounter++;
			}
			


			
			
			
		}
		
		String terminatedQueueContent = "";
		for (Process process : terminatedQueue) {
			terminatedQueueContent += process.pcb.getPCBData();
		}
		
		System.out.println("Content of the terminated queue: \n" + terminatedQueueContent);
	}
	
	/**
	 * Move the top item of the ready queue at the running process position
	 */
	public void readyToRunning() {
		try {
			runningProcess = readyQueue.poll();
			runningProcess.stateToRunning();

		} catch (Exception e) {
			System.out.println("Nothing in the ready queue...");
			return;
		}
	}
	
	public void manageIOQueues() {
					// Check if the IO waiting Queues are empty or not
					if(!(io1.waitingQueue.isEmpty()) || !(io2.waitingQueue.isEmpty())){
						
						// check if the 5 time units are fulfilled for the IODevice 1
						if ((io1Counter + 1) % 5 == 0) {
							System.out.println("Io request successfull on IO1");
							Process temProcess = io1.waitingQueue.poll();
							temProcess.stateToReady();
							this.readyQueue.add(this.waitingQueue.poll());
							this.io1Counter = 0;
						}
						// check if the 5 time units are fulfilled for the IODevice 2
						if ((io2Counter + 1)  % 5 == 0) {
							System.out.println("Io request successfull on IO2");
							Process temProcess = io2.waitingQueue.poll();
							temProcess.stateToReady();
							this.readyQueue.add(this.waitingQueue.poll());
							this.io2Counter = 0;
						}
						
						// then we check if the IO waiting queues are not empty, if they're not empty we increment their counter
						if (!io1.waitingQueue.isEmpty()) {
							++io1Counter;
						}
						if (!io2.waitingQueue.isEmpty()) {
							++io2Counter;
						}
					}
					
	}
	
	/**
	 * Move the running process to the ready queue
	 */
	public void runningToReady() {
		runningProcess.stateToReady();
		readyQueue.add(runningProcess);
		runningProcess = null;
	}
	
	/**
	 * Move the running process to the waiting queue
	 */
	public void runningToWaiting() {
		runningProcess.stateToWaiting();
		waitingQueue.add(runningProcess);
		this.runningProcess = null;
	}
	
	public void runningToTerminated() {
		runningProcess.stateToTerminated();
		terminatedQueue.add(runningProcess);
		this.runningProcess = null;
	}
	
	/**
	 * Move the top element of the waiting queue to the ready queue
	 */
	public void waitingToReady() {
		try {
			Process tempProcess = waitingQueue.poll();
			tempProcess.stateToReady();
			readyQueue.add(tempProcess);
		} catch (Exception e) {
			System.out.println("Nothing in the CPU waiting queue");
		}

	}
	
	public String getReadyQueueItems() {
		String readyQueueContent = "";
		
		if (!this.readyQueue.isEmpty()) {
			for (Process process : readyQueue) {
				readyQueueContent += "process id: " + process.id + "\n";
			}
			return readyQueueContent;
		}
		return "Nothing in the ready queue now...";
	}
	
	public String cpuOutput() {
		String processInfo = Objects.equals(this.runningProcess, null) ? "no processes running now" : this.runningProcess.id;
		
		return "CPU output informations:\n" + "Process being executed now: \n" + processInfo + "\nContent of the ready queue: \n" + getReadyQueueItems()+
				"\n_________________________________________";
	}
	
	public String getProcessesPCBData() {
		String pCBDataString = "PCB data of all the processes in the system:\n\n";
		
		for (Process process : readyQueue) {
			pCBDataString += process.pcb.getPCBData() + "\n";
		}
		for (Process process : waitingQueue) {
			pCBDataString += process.pcb.getPCBData() + "\n";
		}
		pCBDataString += runningProcess != null ? runningProcess.pcb.getPCBData() : "";
		
		pCBDataString += "_________________________________________";
		return pCBDataString;
	}
	
	public String printSystemInfo() {
		return("\nIO1 counter: " + io1Counter + "\nIO2 counter: " + io2Counter + "\n\n" + cpuOutput() + "\n\n" + "Content of the IO device 1: \n" + this.io1.getWaitingQueueContent() + "\n" + 
				"Content of the IO device 2: \n" + this.io2.getWaitingQueueContent() + "\n\n" + getProcessesPCBData() +  "\n\n"
				);
	}
	
	
}
