import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.Random;

public class DiningPhilosopher {
	/*All shared chop stick resources*/
	private static Random rand = new Random();
	private static Lock chop1 = new ReentrantLock();
	private static Lock chop2 = new ReentrantLock();
	private static Lock chop3 = new ReentrantLock();
	private static Lock chop4 = new ReentrantLock();
	private static Lock chop5 = new ReentrantLock();
	
	public static void main(String[] args){
		ExecutorService executor = Executors.newCachedThreadPool();
		
		/*Creating and executing the philosophers*/
		executor.execute(new ThinkandEat(chop1, chop2, "Philo12"));
		executor.execute(new ThinkandEat(chop2, chop3, "Philo23"));
		executor.execute(new ThinkandEat(chop3, chop4, "Philo34"));
		executor.execute(new ThinkandEat(chop4, chop5, "Philo45"));
		executor.execute(new ThinkandEat(chop5, chop1, "Philo51"));
		
		executor.shutdown();
	}
	
	/*Philosopher Runnable class*/
	public static class ThinkandEat implements Runnable{
		private Lock left, right;
		private String name;
		
		public ThinkandEat(Lock left, Lock right, String name){
			this.left = left;
			this.right = right;
			this.name = name;
		}
		
		/*Process of thinking and eating*/
		public void run(){
			while(true){
				/*Think for random time between 0 and 2s*/
				System.out.println(name + " is thinking");
				try{
					Thread.sleep(rand.nextInt(2000));
				}catch(InterruptedException ex){}
				
				System.out.println(name + " is attempting to eat");
				left.lock();
				System.out.println(name +" grabbed left chopstick");
				//Added delay to show the deadlock that can occur with this specific implementation
				try{Thread.sleep(2000);
				}catch(InterruptedException ex){
				}
				
				right.lock();
				System.out.println(name +" grabbed right chopstick");
				
				/*Eat for random time between 0 and 2s*/
				try{
					Thread.sleep(rand.nextInt(2000));
				}catch(InterruptedException ex){
				}finally {
					left.unlock();
					System.out.println(name +" released left chopstick");
					
					right.unlock();
					System.out.println(name +" released right chopstick");
				}
			}
		}
	}
}