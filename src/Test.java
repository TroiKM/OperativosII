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
		
		Colas nuevo = new Colas();
		Colas waiting = new Colas();
		Ready ready = new Ready();
		Colas finished = new Colas();
		nuevo.parse("process_request_file.xml");
		Tick timer = new Tick(nuevo.size());

			
		Thread cpu = new Thread(new CPU(timer,ready,waiting,nuevo,finished,4),"CPU");
		Thread es = new Thread(new ES(timer,waiting,ready,finished),"ES");
		cpu.start();
		es.start();
		
		while(finished.size() < timer.getMaxProc()) {
			System.out.println("-------------- Tiempo " + timer.getTime() +" --------------");
			
			System.out.println("----------------------------------------\n");
			timer.tick();
		}

		try{
			cpu.join();
			es.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		ocio = ((ocio * 100)/ timer.getTime());
		
		System.out.println("Ocio de CPU: " + ocio + "%.");
		/*for(Proceso p : finished.getQueue()){
			System.out.println(p);    
		}*/

	}
}
