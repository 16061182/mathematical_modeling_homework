package homework1;

public class Queue {
    private Customer [] queue;//顾客队列
    private int lenth_limit;//
    private int num ;
    private int head ;
    private int rear ;
    public Queue(){
        queue = new Customer [500];
        num = 0;
        head = 0;
        rear = 0;
    }
    protected void setLenth_limit(int n){
        lenth_limit = n;
    }
    protected boolean push(Customer c){
        if(num < lenth_limit){
            queue[rear++] = c;
            num++;
            return true;
        }
        return false;
    }
    protected Customer pop(){
        if(num >0){
            num--;
            return queue[head++];
        }
        return null;
    }
    protected int getNum(){
        return num;
    }
}
