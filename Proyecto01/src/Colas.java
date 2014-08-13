/**
* @author Karen Troiano		09-10855
* @author Luis Miranda		10-10463
* @author Jose Montenegro	10-10469
*
* Colas: Clase que representa al monitor de colas.
*
* @param timer: Tick que representa el timer del CPU
*/

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Queue;

public class Colas
{
	
	Queue<Proceso> cola;
	
	/**
	* Constructor de Colas.
	*/
	public Colas() {
		cola = new LinkedList<Proceso>();
	}

    public synchronized Queue<Proceso> getQueue() {
    	return cola;
    }
	
    public synchronized void addElem(Proceso p){
		cola.add(p);
    } 
    
    public synchronized Proceso removeElem() {
		if(!cola.isEmpty()){
			return cola.remove();
		}else{
			return null;
		}
    }
    
    public synchronized Proceso peekElem(){
		return cola.peek(); //Nuevo
    }

    public synchronized boolean isEmpty(){
		return cola.isEmpty();
    }

    public synchronized int size(){
    	return cola.size();
    }

	public synchronized void envejecer(int e){
	 	for(Proceso p : cola){
			p.envejecer(e);
		}
	}

	public synchronized void incrementarEspera(){
		for(Proceso p : cola){
			p.incrementarEspera();
		}
	}

    
    
    
}
