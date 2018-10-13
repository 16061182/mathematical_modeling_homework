package homework1;

public class Customer {
    private double arrivetime;
    private double servetime;
    public Customer(double a,double b){
        arrivetime = a;
        servetime = b;
    }
    protected void setArrivetime(double d){
        this.arrivetime = d;
    }
    protected double getArrivetime(){
        return this.arrivetime;
    }
    protected void setServetime(double d){
        this.servetime = d;
    }
    protected double getServetime(){
        return this.servetime;
    }
}
