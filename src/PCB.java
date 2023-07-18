
public class PCB {
	
	
	public int programCounter;
	public int cyclesExecuted; //clock time elapsed since start
	public Process process;
	public int r1;
	public int r2;
	
	public PCB(Process p) {
		this.process = p;
	}
	
	public String getPCBData() {
		return "Program counter: " + this.programCounter + "\n" + "r1 data: " + this.r1 + "\nr2 data: " + this.r2 + "\n" + this.process.toString() + "\n";
	}
	
}
