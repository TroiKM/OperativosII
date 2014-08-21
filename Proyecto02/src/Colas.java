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

public class Colas<E>
{
	
    Queue<E> cola;
	
    /**
     * Constructor de Colas.
     */
    public Colas() {
	cola = new LinkedList<E>();
    }

    public synchronized Queue<E> getQueue() {
    	return cola;
    }
	
    public synchronized void addElem(E p){
	cola.add(p);
    } 
    
    public synchronized E removeElem() {
	if(!cola.isEmpty()){
	    return cola.remove();
	}else{
	    return null;
	}
    }
    
    public synchronized E peekElem(){
	return cola.peek(); //Nuevo
    }

    public synchronized boolean isEmpty(){
	return cola.isEmpty();
    }

    public synchronized int size(){
    	return cola.size();
    }
        
    
}
