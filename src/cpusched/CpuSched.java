package cpusched;

import java.util.*;

public class CpuSched {

    public static CpuSched cpu = new CpuSched();
    public int numberOfProcesses; //number of processes
    public static List<Process> processes = new ArrayList<Process>();
    public static List<Process> processesPriority = new ArrayList<Process>();
    public static ArrayList<Integer> arrivalTime = new ArrayList<>(); // store arrival time input
    public static ArrayList<Integer> burstTime = new ArrayList<>(); //store burst time input 
    public static ArrayList<Integer> priority = new ArrayList<>(); // store priority input
    public String chosenAlgo,chosenPreEmptive;
    
    public boolean valueChecker = false;
    public int input;

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
        
        while (valueChecker == false) {
            System.out.print("Input no. of processes [2-9]:");
            numberOfProcesses = Integer.parseInt(scanner.nextLine());
            if ((numberOfProcesses > 1) && (numberOfProcesses < 10)) {
                break;
            }
            System.out.println("Invalid input, try again");
        }

        System.out.println("Input individual arrival time");
        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.println("AT" + (i + 1) + ": ");
            input = scanner.nextInt();
            arrivalTime.add(input);
        }

        System.out.println("Input individual burst time");
        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.println("BT" + (i + 1) + ": ");
            input = scanner.nextInt();
            burstTime.add(input);
        }
    }

    //user chooses which algorithm to use
    public void chooseAlgo() {
        
        boolean chosenProperly1=false;
                
        while(chosenProperly1==false){
            Scanner scanner = new Scanner(System.in);

            System.out.println("CPU Scheduling Algorithm\n"
                    + "[A] First-Come First-Served (FCFS)\n"
                    + "[B] Shortest Job First (SJF)\n"
                    + "[C] Priority \n"
                    + "[D] Round Robin\n"
                    + "[E] Exit");

            chosenAlgo = scanner.nextLine().toUpperCase();

            switch (chosenAlgo) {
                case "A":
                    cpu.FCFS();
                    chosenProperly1=true;
                    break;
                case "B":
                    cpu.STRF();
                    chosenProperly1=true;
                    break;
                case "C":
                    System.out.println("Input individual priority");

                    for (int i = 0; i < numberOfProcesses; i++) {
                        System.out.println("P" + (i + 1) + ": ");
                        input = scanner.nextInt();
                        priority.add(input);
                    }
                    boolean chosenProperly2=false;

                    while(chosenProperly2==false){

                        Scanner preemptiveOption = new Scanner(System.in);
                        System.out.println("CPU Scheduling Algorithm\n"
                        + "[A] Non-Preemptive Priority \n"
                        + "[B] Preemptive Priority \n");

                        chosenPreEmptive = preemptiveOption.nextLine().toUpperCase();

                        switch (chosenPreEmptive) {
                        case "A":
                            nonPreEmptivePriority();
                            chosenProperly2=true;
                            break;
                        case "B":
                            preEmptivePriority();
                            chosenProperly2=true;
                            break;
                        default:
                            System.out.println("Not in given options, choose again");
                        }
                    }
                    chosenProperly1=true;
                    break;
                case "D":
                    System.out.println("Input Time Quantum: ");
                    int timeQuantum = Integer.parseInt(scanner.nextLine());

                    cpu.roundRobin(timeQuantum);
                    chosenProperly1=true;
                    break;
                case "E":
                    System.out.println("Exiting Program");
                    chosenProperly1=true;
                    break;
                default:
                    System.out.println("Not in given options, choose again");

            }
        }
    }
    
    //First-Come First-Served Algorithm
    private void FCFS() {
        
        // Populate process list
        for (int i = 0; i < numberOfProcesses; i++) {
            processes.add(new Process((i + 1), arrivalTime.get(i), burstTime.get(i)));
        }

        //sorts and rearranges processes by arrival time
        //lambda expression, like a callable method but no need
        //to do the formalities that callable methods require
        Collections.sort(processes, (Process p1, Process p2) -> Integer.compare(p1.arrivalTime, p2.arrivalTime));
        
        // Calculate completion times, waiting times, and turnaround times
        int currentTime = 1;
        float totalWaitingTime = 0;
        float totalTurnaroundTime = 0;
        for (int i = 0; i < numberOfProcesses; i++) {
            Process currentProcess = processes.get(i);
            currentProcess.completionTime = currentTime + currentProcess.burstTime;
            currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
            currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
            totalWaitingTime += currentProcess.waitingTime;
            totalTurnaroundTime += currentProcess.turnaroundTime;
            currentTime = currentProcess.completionTime;
        }

        // Print results
        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.println(processes.get(i));
        }
        System.out.println("Average Waiting Time is: " + (totalWaitingTime / numberOfProcesses));
        System.out.println("Average Turnaround Time is: " + (totalTurnaroundTime / numberOfProcesses));
        
    }
    
    //shortest time remaining first (or preemptive SJF)
    private void STRF() { 
        
        //populate process list
        for (int i = 0; i < numberOfProcesses; i++) {
            processes.add(new Process((i + 1), arrivalTime.get(i), burstTime.get(i)));
        }

        int currentTime = 0; //keep track of times passed
        int completedProcesses = 0; //keep track of completed procesess
        float totalTurnaroundTime = 0;
        float totalWaitingTime = 0;
        
        while (completedProcesses < numberOfProcesses) {
            Process currentProcess = null;
            int shortestTimeLeft = Integer.MAX_VALUE; //default shortest time is max int value
            
            //pick process in ready state and with shortest time left
            for (int i = 0; i < numberOfProcesses; i++) {
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
        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.println(processes.get(i));
        }
        System.out.println("Average Waiting Time is: " + (totalWaitingTime / numberOfProcesses));
        System.out.println("Average Turnaround Time is: " + (totalTurnaroundTime / numberOfProcesses));

    }
     
    //Non-PreEmptive Priotity Algorithm
    private void nonPreEmptivePriority() {
        
    }

    //PreEmptive Priotity Algorithm
    private void preEmptivePriority() {
        // Create a copy of burstTime and priority lists
        List<Integer> remainingBurstTime = new ArrayList<>(burstTime);
        List<Integer> remainingPriority = new ArrayList<>(priority);

        int currentTime = 0;
        int completedProcesses = 0;
        float totalWaitingTime = 0;
        float totalTurnaroundTime = 0;




        int totalBurstTime=0;


        //Populate process list
        for (int i = 0; i < numberOfProcesses; i++) {
            processes.add(new Process((i + 1), arrivalTime.get(i), burstTime.get(i), priority.get(i)));
            processesPriority.add(new Process((i + 1), arrivalTime.get(i), burstTime.get(i), priority.get(i)));
            totalBurstTime += processes.get(i).burstTime;
        }

        ArrayList<Integer> burstList = new ArrayList<>();
        for (int i = 0; i < numberOfProcesses; i++) {
            int burstValue = processesPriority.get(i).burstTime;
            burstList.add(burstValue);
        }





        while (completedProcesses < numberOfProcesses) {
            int highestPriorityProcessIndex = -1;
            int highestPriority = Integer.MAX_VALUE;

            // Find the process with the highest priority that has arrived
            for (int i = 0; i < numberOfProcesses; i++) {
                if (arrivalTime.get(i) <= currentTime && remainingPriority.get(i) < highestPriority && remainingBurstTime.get(i) > 0) {
                    highestPriorityProcessIndex = i;
                    highestPriority = remainingPriority.get(i);
                }
            }

            if (highestPriorityProcessIndex == -1) {
                currentTime++;
                continue;
            }

            // Reduce the remaining burst time of the selected process
            remainingBurstTime.set(highestPriorityProcessIndex, remainingBurstTime.get(highestPriorityProcessIndex) - 1);
            System.out.println("Remaining: " + remainingBurstTime.get(highestPriorityProcessIndex));

            if (remainingBurstTime.get(highestPriorityProcessIndex) == 0) {
                completedProcesses++;
                int processCompletionTime = currentTime + 1;
                int processWaitingTime = processCompletionTime - arrivalTime.get(highestPriorityProcessIndex) - burstTime.get(highestPriorityProcessIndex);
                int processTurnaroundTime = processCompletionTime - arrivalTime.get(highestPriorityProcessIndex);

                totalWaitingTime += processWaitingTime;
                totalTurnaroundTime += processTurnaroundTime;

                // Print results for the completed process
                System.out.println("Process " + (highestPriorityProcessIndex + 1) + " - Completion Time: " + processCompletionTime
                        + " Waiting Time: " + processWaitingTime + " Turnaround Time: " + processTurnaroundTime);
            }
            currentTime++;
    }

    // Print average waiting time and average turnaround time
    System.out.println("Average Waiting Time: " + (totalWaitingTime / numberOfProcesses));
    System.out.println("Average Turnaround Time: " + (totalTurnaroundTime / numberOfProcesses));
}

    //Round-Robin Algorithm
    private void roundRobin(int timeQuantum) {
        // Populate process list
        for (int i = 0; i < numberOfProcesses; i++) {
            processes.add(new Process((i + 1), arrivalTime.get(i), burstTime.get(i)));
        }
        int currentTime = processes.get(0).arrivalTime;
        float totalTurnaroundTime = 0;
        float totalWaitingTime = 0;

        // Uses a Map to keep track of each process' burstTimes; more efficient and readable than Lists
        Map<Integer, Integer> burstTimeMap = new HashMap<>();
        // Uses Queues in managing the processes, given how sequential each element is taken from the List
        Queue<Process> completedProcesses = new LinkedList<>();
        Queue<Process> readyQueue = new LinkedList<>();
        List<Process> unfinishedProcesses = new ArrayList<>(processes);

        // Populates the burstTimeMap with every process, and their burstTime
        for (Process p : processes) {
            burstTimeMap.put(p.pid, p.burstTime);
        }

        readyQueue.add(unfinishedProcesses.get(0));

        while (unfinishedProcesses.size() > 0) {
            if (readyQueue.isEmpty()) {
                readyQueue.offer(unfinishedProcesses.get(0));
                assert readyQueue.peek() != null;
                currentTime = readyQueue.peek().arrivalTime;
            }

            Process currentProcess = readyQueue.poll();

            if (burstTimeMap.get(currentProcess.pid) <= timeQuantum) {
                int remainingT = burstTimeMap.get(currentProcess.pid);
                burstTimeMap.put(currentProcess.pid, 0);
                currentTime += remainingT;
            } else {
                burstTimeMap.put(currentProcess.pid, burstTimeMap.get(currentProcess.pid) - timeQuantum);
                currentTime += timeQuantum;
            }

            List<Process> currentProcessCycle = new ArrayList<>();
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p != currentProcess &&
                        !readyQueue.contains(p) && unfinishedProcesses.contains(p)) {
                    currentProcessCycle.add(p);
                }
            }

            readyQueue.addAll(currentProcessCycle);
            readyQueue.offer(readyQueue.poll());

            if (burstTimeMap.get(currentProcess.pid) == 0) {
                unfinishedProcesses.remove(currentProcess);
                readyQueue.remove(currentProcess);

                int turnAroundTime = currentTime - currentProcess.arrivalTime;
                int waitingTime = currentTime - currentProcess.arrivalTime - currentProcess.burstTime;

                currentProcess.turnaroundTime = turnAroundTime;
                currentProcess.waitingTime = waitingTime;
                completedProcesses.offer(currentProcess);
            }
        }

        for (int i = 0; i < numberOfProcesses; i++) {
            Process p = completedProcesses.poll();
            assert p != null;
            totalTurnaroundTime += p.turnaroundTime;
            totalWaitingTime += p.waitingTime;
        }

        //print results
        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.println(processes.get(i));
        }
        System.out.println("Average Waiting Time is: " + (totalWaitingTime / numberOfProcesses));
        System.out.println("Average Turnaround Time is: " + (totalTurnaroundTime / numberOfProcesses));

    }
    
}
