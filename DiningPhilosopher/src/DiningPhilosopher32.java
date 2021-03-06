import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.Random;

public class DiningPhilosopher32 {
	/*All shared chop stick resources*/
	private static Random rand = new Random();
	private static boolean keep_running = true;
	private static long start_time;
	private static long current_time;
	private static Lock chop1 = new ReentrantLock();
	private static Lock chop2 = new ReentrantLock();
	private static Lock chop3 = new ReentrantLock();
	private static Lock chop4 = new ReentrantLock();
	private static Lock chop5 = new ReentrantLock();
	/*Eating resource to ensure to two philosophers eat at once*/
	private static Lock eating = new ReentrantLock();
	
	public static void main(String[] args){
		ExecutorService executor = Executors.newCachedThreadPool();
		
		/*Creating and executing the philosophers*/
		executor.execute(new ThinkandEat(chop1, chop2, "Philo12"));
		executor.execute(new ThinkandEat(chop2, chop3, "Philo23"));
		executor.execute(new ThinkandEat(chop3, chop4, "Philo34"));
		executor.execute(new ThinkandEat(chop4, chop5, "Philo45"));
		executor.execute(new ThinkandEat(chop5, chop1, "Philo51"));
		
		executor.shutdown();
		
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
		
	/*Philosopher Runnable class*/
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
		
		/*Process of thinking and eating*/
		public void run(){
			while(keep_running){
				/*Think for random time between 0 and 2s*/
				System.out.println(name + " is thinking");
				try{
					Thread.sleep(rand.nextInt(2000));
				}catch(InterruptedException ex){}
				
				System.out.println(name + " is attempting to eat");
				
				eating.lock();
				
				left.lock();
				System.out.println(name +" grabbed left chopstick");
				//Added delay to show that deadlock no longer occurs with this implementation
				try{Thread.sleep(2000);
				}catch(InterruptedException ex){
				}
				
				right.lock();
				System.out.println(name +" grabbed right chopstick");
				
				/*Eat for random time between 0 and 2s*/
				try{
					Thread.sleep(rand.nextInt(2000));
					eat_count++;
				}catch(InterruptedException ex){
				}finally {
					left.unlock();
					System.out.println(name +" released left chopstick");
					
					right.unlock();
					System.out.println(name +" released right chopstick");
					
					eating.unlock();
					System.out.println(name +" done eating!");
				}
			}
			System.out.println(name + " ate " + eat_count + " times.");
		}
	}
}