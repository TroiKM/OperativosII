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

		if(args.length != 3){
			System.out.println("Uso: java Test <Archivo> <Quantum> <TiempoIO>");
			System.exit(0);
		}

		int quantum = 0;
		int IOTime = 0;

      try{
			quantum = Integer.parseInt(args[1]);
			IOTime = Integer.parseInt(args[2]);
		}catch(NumberFormatException e){
			System.out.println("El quantum o el tiempo de IO introducidos no son numeros");
			System.exit(1);
		}

		if(!(quantum > 0 && IOTime > 0)){
			System.out.println("Tiempo de quantum o IO invalidos");
			System.exit(1);
		}
		
		Ready nuevo = new Ready();
		Colas waiting = new Colas();
		Ready ready = new Ready();
		Colas finished = new Colas();
		nuevo.parse(args[0]);
		Tick timer = new Tick(nuevo.size());

		System.out.println(nuevo.getQueue());
		
			
		Thread cpu = new Thread(new
					CPU(timer,ready,waiting,nuevo,finished,quantum),"CPU0");
		Thread cpu1 = new Thread(new
					CPU(timer,ready,waiting,nuevo,finished,quantum),"CPU1");
		Thread cpu2 = new Thread(new
					CPU(timer,ready,waiting,nuevo,finished,quantum),"CPU2");
		Thread cpu3 = new Thread(new
					CPU(timer,ready,waiting,nuevo,finished,quantum),"CPU3");


		Thread es = new Thread(new ES(timer,waiting,ready,finished,IOTime),"ES");
		
		cpu.setName("CPU0");
		cpu1.setName("CPU1");
		cpu2.setName("CPU2");
		cpu3.setName("CPU3");
		
		cpu.start();
		cpu1.start();
		cpu2.start();
		// cpu3.start();
			
		es.start();
		
		while(finished.size() < timer.getMaxProc()) {
		    System.out.println("-------------- Tiempo " + timer.getTime() +" --------------");					
		    System.out.println("----------------------------------------\n");
		    timer.tick();

		}

		try{
			cpu.join();
			cpu1.join();
			cpu2.join();
			es.join();
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
