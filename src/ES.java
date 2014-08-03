/**
 *E/S: Clase que representa al manejador del recursos
 *    de Entrada/Salida.
 *@param 
**/
public class ES implements Runnable {
  
	private Tick timer;
	private Colas waiting;
        private Ready ready;
	private Colas finished;
  
	/**
	*Constructor de E/S. Corre El hilo
	*@param t: Tick a asignar al E/S.
	**/
	public ES(Tick t, Colas w, Ready r, Colas f){
		
		timer = t;
		waiting = w;
		ready = r;
 		finished = f;

	}

        /**
         *Run: Metodo de hilo
	**/
	public void run(){
 
	    while(finished.size() < timer.getMaxProc()){
		System.out.println("E/S say: startJob...");
		timer.startJob();
		System.out.println("E/S say: done!\tprocessWaiting...");
		processWaiting();
		System.out.println("E/S say: done!\tendJob...");
		timer.endJob();
		System.out.println("E/S say: done!\n");
			  
	     }
	     System.out.println("ES acaba su corrida");
	}

	/**
	 *ProcessWaiting: Procesa la cola de waiting en un solo tick
	**/
	public void processWaiting(){

		if(!waiting.isEmpty()){
                  Proceso temp = waiting.peekElem();
		  temp.setIOTime(temp.getIOTime()-1);
                  if(temp.getIOTime() <= 0){
                  	ready.addElem(temp);
			waiting.removeElem();
  		  }
                }
	}
}
