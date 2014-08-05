/**
* @author Karen Troiano		09-10855
* @author Luis Miranda		10-10463
* @author Jose Montenegro	10-10469
*
* Test: Clase main (?)
*/

public class Test {

	public static int ocio = 0;
	
	public static void main(String args[]){
		
		Ready nuevo = new Ready();
		Colas waiting = new Colas();
		Ready ready = new Ready();
		Colas finished = new Colas();
		nuevo.parse("process_request_file.xml");
		Tick timer = new Tick(nuevo.size());

		System.out.println(nuevo.getQueue());
		
			
		Thread cpu = new Thread(new
		CPU(timer,ready,waiting,nuevo,finished,4),"CPU");
		
		Thread es1 = new Thread(new ES(timer,waiting,ready,finished),"ES1");
		Thread es2 = new Thread(new ES(timer,waiting,ready,finished),"ES2");
		cpu.start();
		es1.start();
		es2.start();
		
		while(finished.size() < timer.getMaxProc()) {
		    System.out.println("-------------- Tiempo " + timer.getTime() +" --------------");					
		    System.out.println("----------------------------------------\n");
		    timer.tick();

		}

		try{
			cpu.join();
			es1.join();
			es2.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		ocio = ((ocio * 100)/ timer.getTime());
		
		System.out.println("% Ocio de CPU: " + ocio + "%.");

		System.out.println("Tiempo promedio de ejecucion: " + 
		getAverageRunningTime(finished,timer));

		System.out.println("Tiempo promedio de espera: " +
		getAverageWaitTime(finished,timer));
		

	}

	public static int getAverageRunningTime(Colas f, Tick t){
		int totalTime = 0;

		for(Proceso p : f.getQueue())
		{
			totalTime += (p.getFinishTime() - p.getArrivalTime());
		}

		return totalTime/t.getMaxProc();
	}

	public static int getAverageWaitTime(Colas f, Tick t){
		int totalTime = 0;

		for(Proceso p: f.getQueue())
		{
			totalTime += p.getWaitTime();
		}

		return totalTime/t.getMaxProc();
	}

}
