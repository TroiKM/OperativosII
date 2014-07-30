/**
 *Colas: Clase que representa al monitor de colas.
 *@param timer: Tick que representa el timer del CPU
**/

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Colas{
	
	Queue<Proceso> cola;
	
	/**
	* Constructor de Colas.
	*/
	public Colas(){
		cola = new LinkedList<Proceso>();
	}
	
	public synchronized void addElem(Proceso p){
		cola.add(p);
	} 
    
    public synchronized Proceso removeElem() {
		return cola.remove();
    }
    
    public synchronized Proceso peekElem(){
		return cola.peek(); //Nuevo
    }

    public synchronized boolean isEmpty(){
		return cola.isEmpty();
    }
    
}
