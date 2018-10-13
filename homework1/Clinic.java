package homework1;

public class Clinic {
    private int state;
    protected Clinic(){
        state = 0;
    }
    protected void setState(boolean b){
        if(b) state = 1;
        else state = 0;
    }
    protected int getState(){
        return state;
    }
}
