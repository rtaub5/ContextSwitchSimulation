import java.util.*;

public class Main2
{
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
        Collection<SimProcess> readyList = new LinkedList<SimProcess>(Arrays.asList(processTwo, processThree, processFour, processFive, processSix, processSeven, processEight, processNine, processTen));
        List<SimProcess> processList = new ArrayList<SimProcess>(Arrays.asList(processOne, processTwo, processThree, processFour, processFive, processSix, processSeven, processEight, processNine, processTen));
        List<ProcessControlBlock> pcbList = new ArrayList<ProcessControlBlock>(Arrays.asList(pcbOne, pcbTwo, pcbThree, pcbFour, pcbFive, pcbSix, pcbSeven, pcbEight, pcbNine, pcbTen));
        Collection<SimProcess> blockedList = new ArrayList<SimProcess>();

        int clock = 0;
        ProcessState state = ProcessState.READY;
        processor.setCurrentProcess(processOne);
        boolean contextSwitch = false;
        boolean finalProcess = false;
        int idle = 0;

        for (int ix = 0; ix < 3000; ++ix)
        {
            if ((clock != QUANTUM) && (contextSwitch == false))
            {
                System.out.print("Step " + (ix + 1) + " ");
                state = processor.executeNextInstruction();
                clock = getClock(state, clock);
                contextSwitch = setContextSwitch(state);
                if (state == ProcessState.BLOCKED)
                {
                    if ((readyList.isEmpty()) && (blockedList.isEmpty()))
                    {
                        blockLastProcess(processList, processor,  pcbList,  ix, blockedList);
                        finalProcess = true;
                    }
                }
            }
            else if ((!readyList.isEmpty()))
            {
                if (state == ProcessState.BLOCKED)
                {
                    if (finalProcess == false)
                    {
                        updateCurrentPcb(processList, processor, pcbList, ix);
                        blockedList.add(processor.getCurrentProcess());
                    }
                    setNewProcess(processList, readyList, pcbList, processor);
                    clock = 0;
                    contextSwitch = false;
                }
                else if (state == ProcessState.FINISHED)
                {
                    setNewProcess(processList, readyList, pcbList, processor);
                    clock = 0;
                    contextSwitch = false;
                }
                else if (clock == QUANTUM)
                {
                    System.out.println("*** Quantum expired ***");
                    readyList.add(processor.getCurrentProcess());
                    updateCurrentPcb(processList, processor, pcbList, ix);
                    setNewProcess(processList, readyList, pcbList, processor);
                    clock = 0;
                    contextSwitch = false;
                }
            }
            else if ((clock == QUANTUM) && (readyList.isEmpty()) && (blockedList.isEmpty()))
            {
                System.out.println("*** Quantum expired ***");
                readyList.add(processor.getCurrentProcess());
                updateCurrentPcb(processList, processor, pcbList, ix);
                setNewProcess(processList, readyList, pcbList, processor);
                clock = 0;
                contextSwitch = false;
            }
           else
          {
               System.out.println("Step " + (ix + 1) + " *** processor is idling ***");
          }

            unblockProcesses(blockedList, readyList);
        }
    }


//  methods used in main

    public static int getClock(ProcessState state, int clock)
    {
        if (state == ProcessState.FINISHED)
        {
            System.out.println("*** Process completed ***");
            clock = 0;
        }
        else if (state == ProcessState.BLOCKED)
        {
            clock = 0;
            System.out.println("*** Process blocked ***");
        }
        else
        {
            clock++;
        }
        return clock;
    }


    public static boolean setContextSwitch(ProcessState state)
    {
        boolean contextSwitch;
        if ((state == ProcessState.FINISHED) || (state == ProcessState.BLOCKED))
        {
            contextSwitch = true;
        }
        else
        {
            contextSwitch = false;
        }
        return contextSwitch;
    }

    public static void updateCurrentPcb(List<SimProcess> processList, SimProcessor processor, List<ProcessControlBlock> pcbList, int ix)
    {
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

        System.out.println("Step " + (ix + 1) + " Context switch: Saving process: " + (indexProcess + 1) + " Instruction: " + currPcbInst);
        System.out.println("\t\tR1: " + currPcbRegOne + " R2: " + currPcbRegTwo + " R3: " + currPcbRegThree + " R4: " + currPcbRegFour);
    }

    public static void setNewProcess(List<SimProcess> processList, Collection<SimProcess> readyList, List<ProcessControlBlock> pcbList, SimProcessor processor)
    {
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
    }

    public static void blockLastProcess(List<SimProcess> processList, SimProcessor processor, List<ProcessControlBlock> pcbList, int ix, Collection<SimProcess> blockedList)
    {
        updateCurrentPcb(processList, processor, pcbList, ix);
        blockedList.add(processor.getCurrentProcess());
        System.out.println("blocked list " + blockedList);
    }

    public static void unblockProcesses(Collection<SimProcess> blockedList, Collection<SimProcess> readyList)
    {
        if (!blockedList.isEmpty())
        {
            Random rand = new Random();
            for (int jx = 0; jx < blockedList.size(); ++jx)
            {
                int prob = rand.nextInt(101);
                if (prob <= 30)
                {
                    SimProcess lastProcess = ((ArrayList<SimProcess>) blockedList).get(jx);
                    blockedList.remove(((ArrayList<SimProcess>) blockedList).get(jx));
                    readyList.add(lastProcess);
                }
            }
        }
    }

}
