/**
 *E/S: Clase que representa al manejador del recursos
 *    de Entrada/Salida.
 *@param 
**/
public class ES implements Runnable {
  
	private Tick timer;
	private Colas waiting;
        private Ready ready;
  
	/**
	*Constructor de E/S. Corre El hilo
	*@param t: Tick a asignar al E/S.
	**/
	public ES(Tick t, Colas w, Ready r){
		
		timer = t;
		waiting = w;
		ready = r;
		new Thread(this,"ES").start();

	}

        /**
         *Run: Metodo de hilo
	**/
	public void run(){
 
     		while(true){
                  timer.startJob();
		  processWaiting();
                  timer.endJob();
		}
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
