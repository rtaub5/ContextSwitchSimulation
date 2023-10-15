import  java.util.*;

public class SimProcess {
    private int pid;
    private String procName;
    private int totalInstructions;

    public SimProcess(int p, String pName, int totInt)
    {
        pid = p;
        procName = pName;
        totalInstructions = totInt;
    }

    public ProcessState execute(int i)
    {
        Random rand = new Random();
        System.out.println("Proc " + procName + ", PID: " + pid + " executing instruction: " + i);
        if (i >= totalInstructions)
        {
            return ProcessState.FINISHED;
        }
        else
        {
             int prob = rand.nextInt(101);
             if (prob <= 15)
             {
                 return ProcessState.BLOCKED;
             }
             else
             {
                 return ProcessState.READY;
             }
        }
    }


}

