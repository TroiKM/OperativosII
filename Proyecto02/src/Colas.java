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

public class Colas<E>{
	
	Queue<E> cola;
	int elem;
    	
	/**
	* Constructor de Colas.
	*/
	public Colas() {
		cola = new LinkedList<E>();
		elem = 0;
	}

	public synchronized Queue<E> getQueue() {
		return cola;
	}

    public synchronized void setQueue(Queue<E> s) {
	this.cola = s;
    }
	
	public synchronized void addElem(E p){
		cola.add(p);
		this.elem++;
		if(this.elem == 1)
			notifyAll();
	} 
    
	public synchronized E removeElem() {
		if(this.elem == 0)
			try{
				wait();
			}catch(IllegalMonitorStateException | InterruptedException e){
				e.printStackTrace();
				System.exit(-1);
		}
		this.elem--;
		return cola.remove();
	}
    
	public synchronized E peekElem(){
		return cola.peek();
	}

	public synchronized boolean isEmpty(){
		return cola.isEmpty();
	}

	public synchronized int size(){
		return cola.size();
	}
        
}
