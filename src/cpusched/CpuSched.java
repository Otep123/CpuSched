package cpusched;

import java.util.*;

public class CpuSched {

    public static CpuSched cpu = new CpuSched();
    public int n; //number of processes
    public static List<Process> processes = new ArrayList<Process>();
    public static ArrayList<Integer> arrivalTime = new ArrayList<>(); // store arrival time input
    public static ArrayList<Integer> burstTime = new ArrayList<>(); //store burst time input 
    public static ArrayList<Integer> priority = new ArrayList<>(); // store priority input
    public String chosenAlgo;

    public static void main(String[] args) {

        cpu.inputPrompt();
        System.out.println("Arrival time: " + arrivalTime.toString());
        System.out.println("Burst time: " + burstTime.toString());
        cpu.chooseAlgo();
    }

    public CpuSched() {

    }

    //prompt user input
    public void inputPrompt() {

        Scanner scanner = new Scanner(System.in);
        boolean valueChecker = false;
        int input;

        while (valueChecker == false) {
            System.out.print("Input no. of processes [2-9]:");
            n = Integer.valueOf(scanner.nextLine());
            if ((n > 1) && (n < 10)) {
                break;
            }
            System.out.println("Invalid input, try again");
        }

        System.out.println("Input individual arrival time");
        for (int i = 0; i < n; i++) {
            System.out.println("AT" + (i + 1) + ": ");
            input = scanner.nextInt();
            arrivalTime.add(input);
        }

        System.out.println("Input individual burst time");
        for (int i = 0; i < n; i++) {
            System.out.println("BT" + (i + 1) + ": ");
            input = scanner.nextInt();
            burstTime.add(input);
        }
    }

    //user chooses which algorithm to use
    public void chooseAlgo() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("CPU Scheduling Algorithm\n"
                + "[A] Shortest Job First (SJF)\n"
                + "[B] Round Robin \n"
                + "[C] Priority\n"
                + "[E] Exit");

        chosenAlgo = scanner.nextLine().toUpperCase();

        switch (chosenAlgo) {
            case "A":
                cpu.STRF();
                break;
            case "B":
                cpu.roundRobin();
                break;
            case "C":
                cpu.priority();
                break;
            case "D":
                System.out.println("Exiting Program");
                break;
            default:
                System.out.println("Not in given options");

        }
    }
    
    //shortest time remaining first (or preemptive SJF)
    private void STRF() { 
        
        //populate process list
        for (int i = 0; i < n; i++) {
            processes.add(new Process((i + 1), arrivalTime.get(i), burstTime.get(i)));
        }

        int currentTime = 0; //keep track of times passed
        int completedProcesses = 0; //keep track of completed procesess
        float totalTurnaroundTime = 0;
        float totalWaitingTime = 0;
        
        while (completedProcesses < n) {
            Process currentProcess = null;
            int shortestTimeLeft = Integer.MAX_VALUE; //default shortest time is max int value
            
            //pick process in ready state and with shortest time left
            for (int i = 0; i < n; i++) { 
                Process p = processes.get(i);
                if (p.arrivalTime <= currentTime && p.timeLeft < shortestTimeLeft && p.timeLeft > 0) {
                    shortestTimeLeft = p.timeLeft;
                    currentProcess = p;
                }
            }

            if (currentProcess == null) { //if no process executing
                currentTime++;
            } else {
                currentProcess.timeLeft--;
                if (currentProcess.timeLeft == 0) {
                    completedProcesses++;
                    currentProcess.completionTime = currentTime + 1;
                    processes.get(currentProcess.pid - 1).waitingTime = currentProcess.completionTime - currentProcess.arrivalTime - currentProcess.burstTime;
                    processes.get(currentProcess.pid - 1).turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                    totalWaitingTime = totalWaitingTime + currentProcess.completionTime - currentProcess.arrivalTime - currentProcess.burstTime;
                    totalTurnaroundTime = totalTurnaroundTime + currentProcess.completionTime - currentProcess.arrivalTime;
                }
                currentTime++;
            }
        }

        //print results
        for (int i = 0; i < n; i++) {
            System.out.println(processes.get(i));
        }
        System.out.println("Average Waiting Time is: " + (totalWaitingTime / n));
        System.out.println("Average Turnaround Time is: " + (totalTurnaroundTime / n));

    }

    private void roundRobin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void priority() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
