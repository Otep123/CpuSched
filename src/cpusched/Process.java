package cpusched;

class Process {

    int pid;
    int arrivalTime;
    int burstTime;
    int priority;
    int waitingTime;
    int turnaroundTime;
    int timeLeft;
    int completionTime;
   
    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid; //process ID
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.waitingTime = 0;
        this.timeLeft = burstTime;
        this.completionTime = 0;
    }

    public Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.waitingTime = 0;
        this.timeLeft = burstTime;
        this.completionTime = 0;
    }

    @Override
    public String toString() {
        return "Process{" + "pid=" + pid
                + ", Arrival Time=" + arrivalTime
                + ", Burst Time=" + burstTime
                + ", Waiting Time=" + waitingTime
                + ", Turnaround Time=" + turnaroundTime + '}';
    }

}
