import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.Random;

public class DiningPhilosopher {
	private static Random rand = new Random();
	private static Lock chop1 = new ReentrantLock();
	private static Lock chop2 = new ReentrantLock();
	private static Lock chop3 = new ReentrantLock();
	private static Lock chop4 = new ReentrantLock();
	private static Lock chop5 = new ReentrantLock();

	
	private static void main(String[] args){
		ExecutorService executor = Executors.newCachedThreadPool();
	}
	
	private static class Philo12 extends Thread{
		
		public void ThinkandEat(){
			System.out.println("Philo12 is thinking");
			try{
				Thread.sleep(rand.nextInt(2000));
			}catch(InterruptedException ex){}
			
			System.out.println("Philo12 is attempting to eat");
			chop1.lock();
			System.out.println("Philo12 grabbed chop1");
			
			chop2.lock();
			System.out.println("Philo12 grabbed chop2");
			
			try{
				Thread.sleep(rand.nextInt(2000));
			}catch(InterruptedException ex){
			}finally {
				chop1.unlock();
				System.out.println("Philo12 released chop1");
				
				chop2.unlock();
				System.out.println("Philo12 released chop2");
			}
			
		}
		
		public void run(){
			this.ThinkandEat();
		}
	}
	
	private static class Philo23 extends Thread{
		
		public void ThinkandEat(){
			System.out.println("Philo23 is thinking");
			try{
				Thread.sleep(rand.nextInt(2000));
			}catch(InterruptedException ex){}
			
			System.out.println("Philo23 is attempting to eat");
			chop2.lock();
			System.out.println("Philo23 grabbed chop2");
			
			chop3.lock();
			System.out.println("Philo23 grabbed chop3");
			
			try{
				Thread.sleep(rand.nextInt(2000));
			}catch(InterruptedException ex){
			}finally {
				chop2.unlock();
				System.out.println("Philo23 released chop2");
				
				chop3.unlock();
				System.out.println("Philo23 released chop3");
			}
			
		}
		
		public void run(){
			this.ThinkandEat();
		}
	}

	private static class Philo34 extends Thread{
		
		public void ThinkandEat(){
			System.out.println("Philo34 is thinking");
			try{
				Thread.sleep(rand.nextInt(2000));
			}catch(InterruptedException ex){}
			
			System.out.println("Philo34 is attempting to eat");
			chop3.lock();
			System.out.println("Philo34 grabbed chop3");
			
			chop4.lock();
			System.out.println("Philo12 grabbed chop4");
			
			try{
				Thread.sleep(rand.nextInt(2000));
			}catch(InterruptedException ex){
			}finally {
				chop3.unlock();
				System.out.println("Philo34 released chop3");
				
				chop4.unlock();
				System.out.println("Philo34 released chop4");
			}
			
		}
		
		public void run(){
			this.ThinkandEat();
		}
	}

	private static class Philo45 extends Thread{
		
		public void ThinkandEat(){
			System.out.println("Philo45 is thinking");
			try{
				Thread.sleep(rand.nextInt(2000));
			}catch(InterruptedException ex){}
			
			System.out.println("Philo45 is attempting to eat");
			chop4.lock();
			System.out.println("Philo45 grabbed chop4");
			
			chop5.lock();
			System.out.println("Philo45 grabbed chop5");
			
			try{
				Thread.sleep(rand.nextInt(2000));
			}catch(InterruptedException ex){
			}finally {
				chop4.unlock();
				System.out.println("Philo45 released chop4");
				
				chop5.unlock();
				System.out.println("Philo45 released chop5");
			}
			
		}
		
		public void run(){
			this.ThinkandEat();
		}
	}

	private static class Philo51 extends Thread{
		
		public void ThinkandEat(){
			System.out.println("Philo51 is thinking");
			try{
				Thread.sleep(rand.nextInt(2000));
			}catch(InterruptedException ex){}
			
			System.out.println("Philo51 is attempting to eat");
			chop5.lock();
			System.out.println("Philo51 grabbed chop5");
			
			chop1.lock();
			System.out.println("Philo51 grabbed chop1");
			
			try{
				Thread.sleep(rand.nextInt(2000));
			}catch(InterruptedException ex){
			}finally {
				chop5.unlock();
				System.out.println("Philo51 released chop5");
				
				chop1.unlock();
				System.out.println("Philo51 released chop1");
			}
			
		}
		
		public void run(){
			this.ThinkandEat();
		}
	}
}

