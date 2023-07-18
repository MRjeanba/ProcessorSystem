import java.util.LinkedList;
import java.util.Queue;

public class IODevice {

	// The IO request takes 5 unit times to be fulfilled.
	// Maybe do a loop mod 5, once 5 unit of time are finished, pop the top of the waiting queue, change its state, and wait again 5 s for the next...
	public Queue<Process> waitingQueue;
	
	public Process finisheRequest;
	
	
	public IODevice() {
		this.waitingQueue = new LinkedList<>();
	}
	
	/**
	 * Add the given process to the waiting queue
	 * @param process the current process asking for the IO device
	 */
	public void requestIO(Process process){
		waitingQueue.add(process);
		++process.ioRequestNumber;
	}
	
	public String getWaitingQueueContent() {
		String waitingQueueContent = "";
		
		if (!this.waitingQueue.isEmpty()) {
			for (Process process : waitingQueue) {
				waitingQueueContent += process.id+"\n";
			}
			waitingQueueContent += "_________________________________________";
			return waitingQueueContent;
		}
		return "Nothing in the waiting queue now...\n_________________________________________";
	}
}
