/**
 *Colas: Clase que representa al monitor de colas.
 *@param timer: Tick que representa el timer del CPU
**/

import java.util.PriorityQueue;

public class Ready extends Colas{
	
	/**
	* Constructor de Ready.
	*/
	public Ready(){
		cola = new PriorityQueue<Proceso>();
	}
	
	public static void main(String args[]){
		Ready r = new Ready();
		for(int i=0;i<100;++i){
			r.addElem(new Proceso(i,0));
		}
		for(int i=0;i<100;++i){
			System.out.println(r.removeElem());
		}
	}
	
	
}