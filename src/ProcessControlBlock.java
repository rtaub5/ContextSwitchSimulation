public class ProcessControlBlock
{
    private SimProcess currentProcess;
    private int currentInstruction;
    private int registerOne;
    private int registerTwo;
    private int registerThree;
    private int registerFour;
    public ProcessControlBlock(SimProcess currProcess)
    {
        currentProcess = currProcess;
    }
    public void setCurrentInstruction(int currInst)
    {
        currentInstruction = currInst;
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
    public SimProcess getCurrentProcess()
    {
        return currentProcess;
    }
    public int getCurrentInstruction()
    {
        return currentInstruction;
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
}
