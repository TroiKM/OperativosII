/**
* @author Karen Troiano		09-10855
* @author Luis Miranda		10-10463
* @author Jose Montenegro	10-10469
*
* Test: Clase main (?)
*/

import java.util.Queue;
import java.util.LinkedList;

public class Test {
	
	public static void main(String args[]){

		if(args.length != 4){
			System.out.println("Uso: java Test <Archivo> <Quantum> <TiempoIO> <NumeroCPUs>");
			System.exit(0);
		}

		int quantum = 0;
		int IOTime = 0;
		int numCPUs = 0;

      try{
			quantum = Integer.parseInt(args[1]);
			IOTime = Integer.parseInt(args[2]);
			numCPUs = Integer.parseInt(args[3]);
		}catch(NumberFormatException e){
			System.out.println("El quantum, tiempo de IO o numero de CPUs introducidos no son numeros");
			System.exit(1);
		}

		if(!(quantum > 0 && IOTime > 0 && numCPUs > 0)){
			System.out.println("Tiempo de quantum,IO o CPU invalidos");
			System.exit(1);
		}
		
		Ready nuevo = new Ready();
		Colas waiting = new Colas();
		Ready ready = new Ready();
		Colas finished = new Colas();
		nuevo.parse(args[0]);
		Tick timer = new Tick(nuevo.size(),numCPUs);

		System.out.println(nuevo.getQueue());

      Queue<CPU> cpus = new LinkedList<CPU>();
		Queue<Thread> cpThreads = new LinkedList<Thread>();
		for(int i=0;i<numCPUs;++i){
			CPU temp = new CPU(timer,ready,waiting,nuevo,finished,quantum);
			Thread c = new Thread(temp,"CPU" + i);
			cpus.add(temp);
			cpThreads.add(c);
			c.start();
		}	

		Thread es = new Thread(new ES(timer,waiting,ready,finished,IOTime),"ES");
		es.start();
		
		while(finished.size() < timer.getMaxProc()) {
		    timer.tick();
		}

		try{
			for(Thread t : cpThreads){
				t.join();
			}
			es.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		for(CPU c : cpus){
			c.printIdlePercentage();
		}

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
