/**
* @author Karen Troiano		09-10855
* @author Luis Miranda		10-10463
* @author Jose Montenegro	10-10469
*
* Proceso: Clase que representa los procesos
* 
* @param priority: Entero que representa la prioridad
* @param arrivalTime: Tiempo de llegada del proceso
* @param resourceUse: Lista de enteros que representa los usos de CPU que hace
*       este proceso. Entre usos de CPU hay usos de IO
*/

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Queue;
import java.util.LinkedList;

public class Proceso implements Comparator<Proceso>, Comparable<Proceso> {

    private static int numberProcess = 0;
    public static Queue<Proceso> all = new LinkedList<Proceso>();

    private int PID;
	private int priority;
	private int envejecimiento;
	private int arrivalTime;
    private int finishTime;
	private int IOTime;
    private int waitTime;
    private ArrayList<Integer> resourceUse;
    private int currentQuantum;
    private String state;
    
	/**
	* Constructor de Proceso
	* @param p: Prioridad
	* @param t: Tiempo de llegada
	* @return Objeto Proceso con usoRecursos inicializado y vacio
	*/ 
	public Proceso(int p, int t){
		PID = numberProcess;
		priority = p;
		arrivalTime = t;
		envejecimiento = 0;
		IOTime = 0;
		currentQuantum = 0;
		waitTime = 0;
		resourceUse = new ArrayList<Integer>();
		numberProcess++;
		state = "Nuevo";
		all.add(this);
	}

	/**
	* getPriority: Getter de prioridad
	* @return Prioridad del proceso
	*/
	public int getPriority(){
		return priority;
	}

	/**
	* getArrivalTime: Getter de arrivalTime
	* @return Tiempo de llegada del proceso
	*/
	public int getArrivalTime(){
		return arrivalTime;
	}

	/**
	* insertUse: Inserta un uso de CPU a la lista de uso
	* @param use: Entero que especifica tiempo de uso de CPU
	*/
	public void insertUse(Integer use){
		resourceUse.add(use);
	}

	/**
	* removeUse: Remueve el primer uso de la lista y lo devuelve
	* @return Primer uso de la lista
	**/
	public int removeUse(){
		if(resourceUse.isEmpty()){
			System.out.println("ERROR: La lista esta vacia");
			return 0;
		} else {
			int res = resourceUse.get(0);
			resourceUse.remove(0);
			return res;
		}
	}

	public void setFinishTime(int f){
		this.finishTime = f;
	}

	public int getFinishTime(){
		return finishTime;
	}

	public int getIOTime(){
		return IOTime;
	}

	public void setIOTime(int t){
		IOTime = t;
	}

    public String getState(){
	return state;
    }

    public void setState(String t){
	state = t;
    }             	   


	public int getCurrentQuantum(){
		return currentQuantum;
	}

	public void setCurrentQuantum(int i){
		currentQuantum = i;
	} 

	public int getFirstUse(){
		return resourceUse.get(0);
	}

	public void setFirstUse(int u){
		resourceUse.set(0,u);
	}

	public boolean useEmpty(){
		return resourceUse.isEmpty();
	}

	public void removeFirstUse(){
		resourceUse.remove(0);
	}

	public void setEnvejecimiento(int e){
		envejecimiento = e;
	}

	public void resetEnvejecimiento(){
		envejecimiento = 0;
	}

	public void envejecer(int e){
		envejecimiento += e;
	}

	public void incrementarEspera(){
		++waitTime;
	}

	public int getWaitTime(){
		return waitTime;
	}

	/**
	* toString: Retorna una representacion de string del proceso
	* @return String con la informacion del proceso
	**/
	public String toString(){
	
		String print = "\n" + Integer.toString(this.PID) + "\t|"
		+ "\t"+this.state;
		
		if (this.state.compareTo("Final")==0) 
		{
			return "\n" + Integer.toString(this.PID) + "\t|"
		    + "\t\t"+this.finishTime;
		}
		
		return print;
	}
	
    @Override
    public int compareTo(Proceso p){
	if(p.state.compareTo("Nuevo")==0){
	    return (new Integer(this.arrivalTime))
		.compareTo(new Integer(p.arrivalTime));
	}else{
	    return (new Integer(p.priority + p.envejecimiento))
		.compareTo(new Integer(this.priority + this.envejecimiento));
	}
	
    }
    
    public int compare(Proceso p, Proceso p1){
		if(p.state.compareTo("Nuevo")==0){
			return p1.arrivalTime - p.arrivalTime;
		}else{
			return p1.priority + p1.envejecimiento - p.priority - p.envejecimiento;
		}
    }
}
