import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosopher34 {
	private static long start_time;
	private static long current_time;
	private static Lock[] chopsticks;
	private static int choplength;
	private static boolean keep_running = true;
	private static Random rand = new Random();
	private static Lock priority = new ReentrantLock();
	private static ExecutorService executor = Executors.newCachedThreadPool();
	
	public static void main(String[] args){
		
		PhiloGenAndEx.runGenAndEx(Integer.parseInt(args[0]));
		
		/*Timing mechanism used to stop Philosophers 
		 * from thinking and eating.*/
		start_time = System.currentTimeMillis();
		current_time = System.currentTimeMillis();
		while((current_time - start_time) < 60000){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			current_time = System.currentTimeMillis();
		}
		
		keep_running = false;
	}
		
	/*Static philosopher and chop stick generator class*/
	public static class PhiloGenAndEx{
		private PhiloGenAndEx(){
		}
		
		/*Populate chopstick array with lock resources*/
		private static void genChopSticks(int chopCount){
			chopsticks = new Lock[chopCount];
			for(int i=0; i<chopCount; i++){
				chopsticks[i] = new ReentrantLock();
			}
			choplength = chopsticks.length;
		}
		
		/*Needs to run after genChopSticks*/
		/*Create user specified number of philosopher threads*/
		private static void genAndExecutePhilo(int philoCount){
			for(int i=0; i<philoCount; i++){
				executor.execute(new ThinkandEat(chopsticks[i], chopsticks[((i+1)% choplength + 1)-1], "Philo"+ i + "" + ((i+1)%choplength)));
			}
			executor.shutdown();
		}
		
		/*Start generating both chopsticks and philosopher threads*/
		public static void runGenAndEx(int chopAndPhiloCount){
			genChopSticks(chopAndPhiloCount);
			genAndExecutePhilo(chopAndPhiloCount);
		}
	}
	
	/*Philosopher calss*/
	public static class ThinkandEat implements Runnable{
		private Lock left, right;
		private String name;
		private int eat_count;
		
		public ThinkandEat(Lock left, Lock right, String name){
			this.left = left;
			this.right = right;
			this.name = name;
			this.eat_count = 0;
		}
		
		/*Keep eating and thinking until keep_running is no longer true*/
		public void run(){
			int priority_count = 0;
			while(keep_running){
				System.out.println(name + " is thinking");
				try{
					Thread.sleep(rand.nextInt(2000));
				}catch(InterruptedException ex){}
				
				System.out.println(name + " is attempting to eat");
				
				left.lock();
				System.out.println(name +" grabbed left chopstick");
				/*Added delay to show that, even in the worst case,
				 * deadlock does not occur with n philosophers*/ 
				try{Thread.sleep(2000);
				}catch(InterruptedException ex){
				}
				
				//Preemption
				if(!right.tryLock()){
					//Give priority to those who have waited for 5 iterations without eating
					if(priority_count == 5){
						System.out.println(name + " was given priority");
						priority.lock();
						right.lock();
					}else{
						try{
							System.out.println(name + " couldn't grab right chopstick.");
							System.out.println(name + " dropping left chopstick and restarting");
							left.unlock();
							priority_count++;
							Thread.sleep(rand.nextInt(4000));
							continue;
						}catch(InterruptedException ex){
							System.out.print(ex);
							continue;
						}
					}
				}
				priority_count = 0;
				System.out.println(name +" grabbed right chopstick");
				
				/*Eat for random amount of time between 0 and 2s
				 * then release all held resources*/
				try{
					Thread.sleep(rand.nextInt(2000));
					eat_count++;
				}catch(InterruptedException ex){
				}finally {
					left.unlock();
					System.out.println(name +" released left chopstick");
					
					right.unlock();
					System.out.println(name +" released right chopstick");
					
					try{
						priority.unlock();
					}catch(IllegalMonitorStateException ex){
					}
				}
			}
			System.out.println(name + " ate " + eat_count + " times.");
		}
	}
}
