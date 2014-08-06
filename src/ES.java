/**
* @author Karen Troiano		09-10855
* @author Luis Miranda		10-10463
* @author Jose Montenegro	10-10469
*
* E/S: Clase que representa al manejador del recursos
*	de Entrada/Salida.
* 
*/

public class ES implements Runnable {
  
	private Tick timer;
	private Colas waiting;
	private Ready ready;
	private Colas finished;
	private int IOTime;
  
	/**
	* Constructor de E/S. Corre El hilo
	* @param t: Tick a asignar al E/S.
	**/
	public ES(Tick t, Colas w, Ready r, Colas f, int i){
		
		timer = t;
		waiting = w;
		ready = r;
 		finished = f;
		IOTime = i;

	}

	/**
	* Run: Metodo de hilo
	*/
	public void run(){
//	    while(finished.size() < timer.getMaxProc())
	    while(timer.getEnd())
	    {
			timer.startJob();
			processWaiting();
			timer.endJob(-1,finished);
	    }
	}

	/**
	 * ProcessWaiting: Procesa la cola de waiting en un solo tick
	*/
	public void processWaiting(){

		if(!waiting.isEmpty())
		{
			Proceso temp = waiting.peekElem();
			if(temp.getIOTime() == -1) temp.setIOTime(IOTime);
			temp.setIOTime(temp.getIOTime()-1);
			
			if(temp.getIOTime() <= 0)
			{
				temp.setState("Listo");
				ready.addElem(temp);
				waiting.removeElem();
			}
		}
		waiting.incrementarEspera();
	}

}
