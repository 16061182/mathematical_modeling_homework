package homework1;

import java.util.Scanner;
public class Scheduler {
    protected static double time = 0;//时间轴
    protected static double ave_arrivetime;//平均到达时间
    protected static double ave_servetime;//平均服务时间
    protected static int num;//顾客数目
    protected static int lenth_limit;//队列最大长度
    protected static double total_of_delays = 0;//总延迟
    protected static int number_of_delays = 0;//被延迟的客户数
    protected static double total_of_server = 0;//服务器状态面积

    protected static Queue queue1 = new Queue();//等待队列
    protected static Queue queue2 = new Queue();//候选等待队列

    protected static Clinic clinic = new Clinic();



    public static void input(){
        Scanner in = new Scanner(System.in);
        System.out.print("Please input ave_arrivetime : ");
        ave_arrivetime = in.nextDouble();
        System.out.print("Please input ave_servetime : ");
        ave_servetime = in.nextDouble();
        System.out.print("Please input num : ");
        num = in.nextInt();
        System.out.print("Please input lenth_limit : ");
        lenth_limit = in.nextInt();
    }

    public static double poisson(){//泊松分布
        double L = Math.exp(0-ave_arrivetime),k = 0,p = 1;
        do{
            k += 1;
            p *= Math.random();
        }while(p > L);
        return k-1;
    }

    public static double exponent() {//指数分布return -(1/ave_servetime)*Math.log(Math.random());}
        double lambda = 1 / ave_servetime;
        double u = Math.random();
        int x = 0;
        double amd = 0;
        while(u >= amd){
            x += 1;
            amd = 1 - Math.exp(-1.0 * lambda * x);
        }
        return x;
    }

    public static void produce(){//产生num个顾客
         //new Customer(poisson(),exponent());
        for(int i=0;i<num;i++){
            Customer c = new Customer(poisson(),exponent());
            queue2.push(c);
            int j = i + 1;
            System.out.println("NO."+ j + " " + c.getArrivetime() + " " + c.getServetime());
        }
    }

    public static void schedule(){
        double nextarrive = Math.pow(2,32)-1;
        double nextleave = Math.pow(2,32)-1;
        int alreadyserved = 0;
        int alreadyarrived = 0;//用来输出
        double gap;
        produce();
        Customer c = queue2.pop();
        nextarrive = time + c.getArrivetime();
        while(alreadyserved < num){
            if(nextarrive >= 120 && nextleave >= 120) { //break;//超出时间限制
                gap = 120 - time;
                time = 120;
                total_of_delays += gap * queue1.getNum();//更新总延迟
                total_of_server += gap * clinic.getState();//更新状态面积
                break;
            }
            if(nextarrive < nextleave){//下一个事件是到达事件，执行这个事件
                alreadyarrived ++;
                gap = nextarrive - time;
                time = nextarrive;
                System.out.println("Customer No." + alreadyarrived + " arriving at time " + time);
                total_of_delays += gap * queue1.getNum();//更新总延迟
                total_of_server += gap * clinic.getState();//更新状态面积
                if(clinic.getState() == 0){//诊所空闲，直接服务
                    nextleave = time + c.getServetime();
                    clinic.setState(true);
                }
                else{//诊所忙碌
                    if(!queue1.push(c)){
                        nextarrive = nextleave;//如果队列已满无法入队，令下次到达时间为下次离队时间
                        alreadyarrived -- ;//用于输出
                        continue;
                    }
                    else{
                        number_of_delays++;//进入队列，等待人数+1
                    }
                }
                //更新nextarrive和Customer c
                c = queue2.pop();
                if (c != null) {
                    nextarrive = time + c.getArrivetime();
                }
                else nextarrive = Math.pow(2,32) - 1;
            }//if
            else{//下一个事件是离开事件，执行这个事件
                alreadyserved ++;
                gap = nextleave - time;
                time = nextleave;
                System.out.println("Customer No." + alreadyserved + " leaving at time " + time);
                total_of_delays += gap * queue1.getNum();//更新总延迟
                total_of_server += gap * clinic.getState();//更新状态面积
                Customer m = queue1.pop();
                if(m == null){//队列为空
                    clinic.setState(false);
                    nextleave = Math.pow(2,32) - 1;//下次离开时间未知
                }
                else{
                    clinic.setState(true);
                    nextleave = time + m.getServetime();
                }

            }
        }


    }
    public static void main(String args[]){
        input();
        queue1.setLenth_limit(lenth_limit);
        queue2.setLenth_limit(500);
        schedule();
        System.out.println("总延迟" + total_of_delays);
        System.out.println("状态面积" + total_of_server);
        System.out.println("被延迟客户" + number_of_delays);
        System.out.println("时间" + time);
        System.out.println("Average total of customers in queue : " +  total_of_delays/time );//队列平均顾客数=队列面积/当前时间
        System.out.print("Average waiting time : ");
        if(number_of_delays == 0) System.out.println("0");
        else System.out.println(total_of_delays/number_of_delays);//平均等待时间=队列面积/被延迟的客户数
        System.out.println("Server utilization : " + total_of_server/time);//服务器利用率=服务器状态面积/当前时间

        System.exit(0);
    }

}
