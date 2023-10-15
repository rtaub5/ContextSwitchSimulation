import java.util.*;

public class Main {
    public static void main(String[] args)
    {
        SimProcessor processor = new SimProcessor();

        SimProcess processOne = new SimProcess(1, "Proc1", 100);
        SimProcess processTwo = new SimProcess(2, "Proc2", 150);
        SimProcess processThree = new SimProcess(3, "Proc3", 200);
        SimProcess processFour = new SimProcess(4, "Proc4", 400);
        SimProcess processFive = new SimProcess(5, "Proc5", 100);
        SimProcess processSix = new SimProcess(6, "Proc6", 150);
        SimProcess processSeven = new SimProcess(7, "Proc7", 100);
        SimProcess processEight = new SimProcess(8, "Proc8", 400);
        SimProcess processNine = new SimProcess(9, "Proc9", 100);
        SimProcess processTen = new SimProcess(10, "Proc10", 200);

        ProcessControlBlock pcbOne = new ProcessControlBlock(processOne);
        ProcessControlBlock pcbTwo = new ProcessControlBlock(processTwo);
        ProcessControlBlock pcbThree = new ProcessControlBlock(processThree);
        ProcessControlBlock pcbFour = new ProcessControlBlock(processFour);
        ProcessControlBlock pcbFive = new ProcessControlBlock(processFive);
        ProcessControlBlock pcbSix = new ProcessControlBlock(processSix);
        ProcessControlBlock pcbSeven = new ProcessControlBlock(processSeven);
        ProcessControlBlock pcbEight = new ProcessControlBlock(processEight);
        ProcessControlBlock pcbNine = new ProcessControlBlock(processNine);
        ProcessControlBlock pcbTen = new ProcessControlBlock(processTen);

        final int QUANTUM = 5;
        Collection<SimProcess> readyList = new LinkedList<SimProcess>();
        List<SimProcess> processList = new ArrayList<SimProcess>(Arrays.asList(processOne, processTwo, processThree, processFour, processFive, processSix, processSeven, processEight, processNine, processTen));
        List<ProcessControlBlock> pcbList = new ArrayList<ProcessControlBlock>(Arrays.asList(pcbOne, pcbTwo, pcbThree, pcbFour, pcbFive, pcbSix, pcbSeven, pcbEight, pcbNine, pcbTen));
        //readyList.add(processOne);
        readyList.add(processTwo);;
        readyList.add(processThree);
        readyList.add(processFour);
        readyList.add(processFive);
        readyList.add(processSix);
        readyList.add(processSeven);
        readyList.add(processEight);
        readyList.add(processNine);
        readyList.add(processTen);
        Collection<SimProcess> blockedList = new ArrayList<SimProcess>();

        int clock = 0;
        processor.setCurrentProcess(processOne);
        boolean contextSwitch = false;
        ProcessState previousState = ProcessState.READY;
        boolean finalProcess = false;

        for (int ix = 0; ix < 3000; ++ix)
        {
            if ((clock != QUANTUM) && (contextSwitch == false))
            {
                System.out.print("Step " + (ix + 1) + " ");
                ProcessState state = processor.executeNextInstruction();
                if (state == ProcessState.FINISHED)
                {
                    System.out.println("*** Process completed ***");
                    clock = 0;
                    contextSwitch = true;
                    previousState = ProcessState.FINISHED;
                }
                else if (state == ProcessState.BLOCKED)
                {
                    clock = 0;
                    contextSwitch = true;
                    previousState = ProcessState.BLOCKED;
                    blockedList.add(processor.getCurrentProcess());
                }
                else
                {
                    clock++;
                    contextSwitch = false;
                    previousState = ProcessState.READY;

                }
                if (readyList.isEmpty() && blockedList.isEmpty())
                {
                    finalProcess = true;
                }
            }
            else if ((clock == QUANTUM) && (contextSwitch == false))
            {
                contextSwitch = true;
            }
            else if (contextSwitch == true)
            {
                if ((readyList.isEmpty() == false))
                {
                    if (previousState == ProcessState.BLOCKED)
                    {
                        System.out.println("*** Process blocked ***");
                        // updating PCB of last process
                        int indexProcess = processList.indexOf(processor.getCurrentProcess());
                        ProcessControlBlock currPcb = pcbList.get(indexProcess);
                        currPcb.setRegisterOne(processor.getRegisterOne());
                        currPcb.setRegisterTwo(processor.getRegisterTwo());
                        currPcb.setRegisterThree(processor.getRegisterThree());
                        currPcb.setRegisterFour(processor.getRegisterFour());
                        currPcb.setCurrentInstruction(processor.getCurrentInstruction());
                        int currPcbInst = currPcb.getCurrentInstruction();
                        int currPcbRegOne = currPcb.getRegisterOne();
                        int currPcbRegTwo = currPcb.getRegisterTwo();
                        int currPcbRegThree = currPcb.getRegisterThree();
                        int currPcbRegFour = currPcb.getRegisterFour();


                       // blockedList.add(processor.getCurrentProcess());
                        System.out.println("Step " + (ix + 1) + " Context switch: Saving process: " + (indexProcess + 1) + " Instruction: " + currPcbInst);
                        System.out.println("\t\tR1: " + currPcbRegOne + " R2: " + currPcbRegTwo + " R3: " + currPcbRegThree + " R4: " + currPcbRegFour);

                        // put new process on processor using data from PCB

                            int newIndexProcess = processList.indexOf(((LinkedList<SimProcess>) readyList).removeFirst());
                            ProcessControlBlock newPcb = pcbList.get(newIndexProcess);
                            processor.setCurrentInstruction(newPcb.getCurrentInstruction());
                            processor.setCurrentProcess(newPcb.getCurrentProcess());
                            processor.setRegisterOne(newPcb.getRegisterOne());
                            processor.setRegisterTwo(newPcb.getRegisterTwo());
                            processor.setRegisterThree(newPcb.getRegisterThree());
                            processor.setRegisterFour(newPcb.getRegisterFour());
                            int newPcbInst = newPcb.getCurrentInstruction();
                            int newPcbRegOne = newPcb.getRegisterOne();
                            int newPcbRegTwo = newPcb.getRegisterTwo();
                            int newPcbRegThree = newPcb.getRegisterThree();
                            int newPcbRegFour = newPcb.getRegisterFour();
                            clock = 0;
                            contextSwitch = false;
                            System.out.println("Restoring process: " + (newIndexProcess + 1) + " Instruction: " + newPcbInst);
                            System.out.println("\t\tR1: " + newPcbRegOne + " R2: " + newPcbRegTwo + " R3: " + newPcbRegThree + " R4: " + newPcbRegFour);


                    }
                    else if (previousState == ProcessState.FINISHED)
                    {
                        // put new process on processor using data from PCB
                        int newIndexProcess = processList.indexOf(((LinkedList<SimProcess>) readyList).removeFirst());
                        ProcessControlBlock newPcb = pcbList.get(newIndexProcess);
                        processor.setCurrentInstruction(newPcb.getCurrentInstruction());
                        processor.setCurrentProcess(newPcb.getCurrentProcess());
                        processor.setRegisterOne(newPcb.getRegisterOne());
                        processor.setRegisterTwo(newPcb.getRegisterTwo());
                        processor.setRegisterThree(newPcb.getRegisterThree());
                        processor.setRegisterFour(newPcb.getRegisterFour());
                        int newPcbInst = newPcb.getCurrentInstruction();
                        int newPcbRegOne = newPcb.getRegisterOne();
                        int newPcbRegTwo = newPcb.getRegisterTwo();
                        int newPcbRegThree = newPcb.getRegisterThree();
                        int newPcbRegFour = newPcb.getRegisterFour();
                        clock = 0;
                        contextSwitch = false;
                        System.out.println("Step " + (ix + 1) + " Context switch: Restoring process: " + (newIndexProcess + 1) + " Instruction: " + newPcbInst);
                        System.out.println("\t\tR1: " + newPcbRegOne + " R2: " + newPcbRegTwo + " R3: " + newPcbRegThree + " R4: " + newPcbRegFour);

                    }
                    else if (clock == QUANTUM)
                    {
                        System.out.println("*** Quantum expired ***");
                        // updating PCB of last process
                        readyList.add(processor.getCurrentProcess());
                        int indexProcess = processList.indexOf(processor.getCurrentProcess());
                        ProcessControlBlock currPcb = pcbList.get(indexProcess);
                        currPcb.setRegisterOne(processor.getRegisterOne());
                        currPcb.setRegisterTwo(processor.getRegisterTwo());
                        currPcb.setRegisterThree(processor.getRegisterThree());
                        currPcb.setRegisterFour(processor.getRegisterFour());
                        currPcb.setCurrentInstruction(processor.getCurrentInstruction());
                        int currPcbInst = currPcb.getCurrentInstruction();
                        int currPcbRegOne = currPcb.getRegisterOne();
                        int currPcbRegTwo = currPcb.getRegisterTwo();
                        int currPcbRegThree = currPcb.getRegisterThree();
                        int currPcbRegFour = currPcb.getRegisterFour();

                        // put new process on processor using data from PCB
                        int newIndexProcess = processList.indexOf(((LinkedList<SimProcess>) readyList).removeFirst());
                        ProcessControlBlock newPcb = pcbList.get(newIndexProcess);
                        processor.setCurrentInstruction(newPcb.getCurrentInstruction());
                        processor.setCurrentProcess(newPcb.getCurrentProcess());
                        processor.setRegisterOne(newPcb.getRegisterOne());
                        processor.setRegisterTwo(newPcb.getRegisterTwo());
                        processor.setRegisterThree(newPcb.getRegisterThree());
                        processor.setRegisterFour(newPcb.getRegisterFour());
                        int newPcbInst = newPcb.getCurrentInstruction();
                        int newPcbRegOne = newPcb.getRegisterOne();
                        int newPcbRegTwo = newPcb.getRegisterTwo();
                        int newPcbRegThree = newPcb.getRegisterThree();
                        int newPcbRegFour = newPcb.getRegisterFour();


                        clock = 0;
                        contextSwitch = false;
                        System.out.println("Step " + (ix + 1) + " Context switch: Saving process: " + (indexProcess + 1) + " Instruction: " + currPcbInst);
                        System.out.println("\t\tR1: " + currPcbRegOne + " R2: " + currPcbRegTwo + " R3: " + currPcbRegThree + " R4: " + currPcbRegFour);
                        System.out.println("Restoring process: " + (newIndexProcess + 1) + " Instruction: " + newPcbInst);
                        System.out.println("\t\tR1: " + newPcbRegOne + " R2: " + newPcbRegTwo + " R3: " + newPcbRegThree + " R4: " + newPcbRegFour);
                    }
                }
                else
                {
                    if (finalProcess == true) {
                        if ((previousState == ProcessState.BLOCKED) && (contextSwitch == true)) {
                            System.out.println("*** Process blocked ***");
                            // updating PCB of last process
                            int indexProcess = processList.indexOf(processor.getCurrentProcess());
                            ProcessControlBlock currPcb = pcbList.get(indexProcess);
                            currPcb.setRegisterOne(processor.getRegisterOne());
                            currPcb.setRegisterTwo(processor.getRegisterTwo());
                            currPcb.setRegisterThree(processor.getRegisterThree());
                            currPcb.setRegisterFour(processor.getRegisterFour());
                            currPcb.setCurrentInstruction(processor.getCurrentInstruction());
                            int currPcbInst = currPcb.getCurrentInstruction();
                            int currPcbRegOne = currPcb.getRegisterOne();
                            int currPcbRegTwo = currPcb.getRegisterTwo();
                            int currPcbRegThree = currPcb.getRegisterThree();
                            int currPcbRegFour = currPcb.getRegisterFour();


                           // blockedList.add(processor.getCurrentProcess());
                            System.out.println("Step " + (ix + 1) + " Context switch: Saving process: " + (indexProcess + 1) + " Instruction: " + currPcbInst);
                            System.out.println("\t\tR1: " + currPcbRegOne + " R2: " + currPcbRegTwo + " R3: " + currPcbRegThree + " R4: " + currPcbRegFour);
                            clock = 0;
                            contextSwitch = true;
                        }
                        // System.out.println("Step " + (ix + 1) + " *** processor is idling ***");
                        // contextSwitch = true;

                        else if (previousState == ProcessState.FINISHED) {
                            finalProcess = false;
                        } else if (clock == QUANTUM) {
                            System.out.println("*** Quantum expired ***");
                            // updating PCB of last process
                            readyList.add(processor.getCurrentProcess());
                            int indexProcess = processList.indexOf(processor.getCurrentProcess());
                            ProcessControlBlock currPcb = pcbList.get(indexProcess);
                            currPcb.setRegisterOne(processor.getRegisterOne());
                            currPcb.setRegisterTwo(processor.getRegisterTwo());
                            currPcb.setRegisterThree(processor.getRegisterThree());
                            currPcb.setRegisterFour(processor.getRegisterFour());
                            currPcb.setCurrentInstruction(processor.getCurrentInstruction());
                            int currPcbInst = currPcb.getCurrentInstruction();
                            int currPcbRegOne = currPcb.getRegisterOne();
                            int currPcbRegTwo = currPcb.getRegisterTwo();
                            int currPcbRegThree = currPcb.getRegisterThree();
                            int currPcbRegFour = currPcb.getRegisterFour();
                            clock = 0;

                            System.out.println("Step " + (ix + 1) + " Context switch: Saving process: " + (indexProcess + 1) + " Instruction: " + currPcbInst);
                            System.out.println("\t\tR1: " + currPcbRegOne + " R2: " + currPcbRegTwo + " R3: " + currPcbRegThree + " R4: " + currPcbRegFour);
                            if (readyList.isEmpty() == false) {
                                // put new process on processor using data from PCB
                                int newIndexProcess = processList.indexOf(((LinkedList<SimProcess>) readyList).removeFirst());
                                ProcessControlBlock newPcb = pcbList.get(newIndexProcess);
                                processor.setCurrentInstruction(newPcb.getCurrentInstruction());
                                processor.setCurrentProcess(newPcb.getCurrentProcess());
                                processor.setRegisterOne(newPcb.getRegisterOne());
                                processor.setRegisterTwo(newPcb.getRegisterTwo());
                                processor.setRegisterThree(newPcb.getRegisterThree());
                                processor.setRegisterFour(newPcb.getRegisterFour());
                                int newPcbInst = newPcb.getCurrentInstruction();
                                int newPcbRegOne = newPcb.getRegisterOne();
                                int newPcbRegTwo = newPcb.getRegisterTwo();
                                int newPcbRegThree = newPcb.getRegisterThree();
                                int newPcbRegFour = newPcb.getRegisterFour();
                                System.out.println("Restoring process: " + (newIndexProcess + 1) + " Instruction: " + newPcbInst);
                                System.out.println("\t\tR1: " + newPcbRegOne + " R2: " + newPcbRegTwo + " R3: " + newPcbRegThree + " R4: " + newPcbRegFour);
                                contextSwitch = false;
                            } else {
                                contextSwitch = true;
                            }
                        }
                    }
                    else
                    {
                        System.out.println("Step " + (ix + 1) + " *** processor is idling ***");
                        contextSwitch = true;
                       // clock = 0;
                    }
                }
            }

            Random rand = new Random();
            if (blockedList.isEmpty() == false)
            {
                for (int jx = 0; jx < blockedList.size(); ++jx)
                {
                    int prob = rand.nextInt(101);
                    if (prob <= 30) {
                        SimProcess lastProcess = ((ArrayList<SimProcess>) blockedList).get(jx);
                        blockedList.remove(((ArrayList<SimProcess>) blockedList).get(jx));
                        readyList.add(lastProcess);
                    }
                }
            }
            System.out.println(readyList);
            System.out.println(blockedList);
        }



    }
}