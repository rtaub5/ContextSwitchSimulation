import java.util.Random;

public class SimProcessor {
    private SimProcess currentProcess;
    private int registerOne;
    private int registerTwo;
    private int registerThree;
    private int registerFour;
    private static int currentInstruction = 0;

   /* public SimProcessor(SimProcess currProcess, int regOne, int regTwo, int regThree, int regFour, int currInst)
    {
        currentProcess = currProcess;
        registerOne = regOne;
        registerTwo = regTwo;
        registerThree = regThree;
        registerFour = regFour;
        currentInstruction = currInst;
    }*/

    public void setCurrentProcess(SimProcess currProcess)
    {
        currentProcess = currProcess;
    }
    public void setRegisterOne(int regOne)
    {
        registerOne = regOne;
    }
    public void setRegisterTwo(int regTwo)
    {
        registerTwo = regTwo;
    }
    public void setRegisterThree(int regThree)
    {
        registerThree = regThree;
    }
    public void setRegisterFour(int regFour)
    {
        registerFour = regFour;
    }
    public void setCurrentInstruction(int currInst)
    {
        currentInstruction = currInst;
    }

    public SimProcess getCurrentProcess()
    {
        return currentProcess;
    }

    public int getRegisterOne()
    {
        return registerOne;
    }
    public int getRegisterTwo()
    {
        return registerTwo;
    }
    public int getRegisterThree()
    {
        return registerThree;
    }
    public int getRegisterFour()
    {
        return registerFour;
    }
    public int getCurrentInstruction()
    {
        return currentInstruction;
    }

    public ProcessState executeNextInstruction()
    {
        Random rand = new Random();
        ProcessState state = currentProcess.execute(currentInstruction);
        currentInstruction ++;
        registerOne = rand.nextInt();
        registerTwo = rand.nextInt();
        registerThree = rand.nextInt();
        registerFour = rand.nextInt();
        return state;
    }


}
