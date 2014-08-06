/**
* @author Karen Troiano		09-10855
* @author Luis Miranda		10-10463
* @author Jose Montenegro	10-10469
*
* Clase Tick: Representa al monitor de los ticks
*
* @param time: Tiempo que lleva el manejador
*/

import java.util.Scanner;

public class Tick{
  
  private int MAX_DEVICES;
  private int time;
  private boolean ok_to_tick;
  private boolean ok_to_run;
  private int finished;
  private int numProc;
  private boolean ok_to_while;
    
	/**
	 * Constructor del Tick
	 * @return Objeto Tick. Inicializa el thread del reloj
	 **/
	public Tick(int n, int c){
		time = 0;
		ok_to_tick = false;
		ok_to_run = true;
		MAX_DEVICES = c;
		finished = MAX_DEVICES;
		numProc = n;
		ok_to_while = true;
	}

	public boolean getEnd(){
		return ok_to_while;
	}
    
    
	/**
	 * getTime: Obtiene el tiempo del manejador
	 * @return Tiempo actual
	 **/
	public synchronized int getTime(){
		return time;
	}

	/**
	 * Tick: Hace el tick
	 **/
	public synchronized void tick(){
		while(!ok_to_tick){
			try{
				wait();
			}catch(IllegalMonitorStateException | InterruptedException e){
				e.printStackTrace();
				System.exit(-1);
			}
		}

		time++;
		ok_to_run = true;
		ok_to_tick = false;
		// finished = 0;
		finished = MAX_DEVICES;
		
		if ( ((time % 5) == 0) ){
			System.out.println("-------------- Tiempo " + time +" --------------");
			System.out.println("Proceso\t|\tEstado");
			System.out.println(Proceso.all);
			System.out.println("Press enter to continue...");
			Scanner keyboard = new Scanner(System.in);
			keyboard.nextLine();
		}
		notifyAll();
	}

	/**
	 * startJob: Inicializa un manejador
	 **/
	public synchronized void startJob(){
		while(!ok_to_run){
			try{
				wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * endJob: Termina un manejador
	 **/
	public synchronized void endJob(int i, Colas owari){
		finished--;
		
		if(finished == 0)
		{
			ok_to_tick = true;
			ok_to_run = false;
			ok_to_while = owari.size() < this.getMaxProc();
			
			notifyAll();
		}else{
			try{
				wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	public int getMaxProc(){
		return numProc;
	}

	public synchronized void  endAll(){
		finished = 0;
		notifyAll();
	}

}
