/**
 *E/S: Clase que representa al manejador del recursos
 *    de Entrada/Salida.
 *@param 
**/
public class ES implements Runnable {
  
	private Tick timer;
	private Colas waiting;
        private Ready ready;
        private Colas nuevo;
  
	/**
	*Constructor de E/S. Corre El hilo
	*@param t: Tick a asignar al E/S.
	**/
	public ES(Tick t, Colas w, Ready r, Colas n){
		
		timer = t;
		waiting = w;
		ready = r;
                nuevo = n;

	}

        /**
         *Run: Metodo de hilo
	**/
	public void run(){
 
     		while(!(waiting.isEmpty() && ready.isEmpty() && nuevo.isEmpty())){
                  timer.startJob();
		  processWaiting();
                  timer.endJob();
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
                  if(temp.getIOTime() == 0){
                  	ready.addElem(temp);
			waiting.removeElem();
  		  }
                }
	}
}
