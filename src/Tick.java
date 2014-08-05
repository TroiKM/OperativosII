/**
* @author Karen Troiano		09-10855
* @author Luis Miranda		10-10463
* @author Jose Montenegro	10-10469
*
* Clase Tick: Representa al monitor de los ticks
*
* @param time: Tiempo que lleva el manejador
*/

public class Tick{
  
	private static final int MAX_DEVICES = 3;
	private int time;
	private boolean ok_to_tick;
	private boolean ok_to_run;
	private int finished;
	private int numProc;

	/**
	* Constructor del Tick
	* @return Objeto Tick. Inicializa el thread del reloj
	*/
	public Tick(int n){
		time = 0;
		ok_to_tick = false;
		ok_to_run = true;
		finished = MAX_DEVICES;
		numProc = n;
		//finished = 0;
	}

	/**
	* getTime: Obtiene el tiempo del manejador
	* @return Tiempo actual
	*/
	public synchronized int getTime(){
		return time;
	}

	/**
	* Tick: Hace el tick
	*/
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
		//    finished = 0;
		finished = MAX_DEVICES;
		notifyAll();
	}

	/**
	* startJob: Inicializa un manejador
	*/
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
	*/
	public synchronized void endJob(){
		finished--;
		if(finished == 0)
		{
			ok_to_tick = true;
			ok_to_run = false;
			notifyAll();
		}else
		{
			try{
				wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		
	// if(finished != MAX_DEVICES){
	//   while(finished != MAX_DEVICES){
	//     try{
	//       wait();
	//     }catch(InterruptedException e){
	//       e.printStackTrace();
	//     }
	//   }
		
	// }else{
	//   ok_to_tick = true;
	//   ok_to_run = false;
		
	//   notifyAll();
	// }
	}
	
	public int getMaxProc(){
		return numProc;
	}
	
}
