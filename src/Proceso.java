import java.util.ArrayList;
import java.util.Comparator;

/**
 *Proceso: Clase que representa los procesos
 *@param priority: Entero que representa la prioridad
 *@param arrivalTime: Tiempo de llegada del proceso
 *@param resourceUse: Lista de enteros que representa los usos de CPU que hace
 *       este proceso. Entre usos de CPU hay usos de IO
 **/

public class Proceso implements Comparator<Proceso>, Comparable<Proceso> {

    private static int numberProcess = 0;
    private int PID;
    private int priority;
    private int envejecimiento;
    private int arrivalTime;
    private int finishTime;
    private int IOTime;
    private ArrayList<Integer> resourceUse;
    private int currentQuantum;

    /**
     *Constructor de Proceso
     *@param p: Prioridad
     *@param t: Tiempo de llegada
     *@return Objeto Proceso con usoRecursos inicializado y vacio
     **/ 

    public Proceso(int p, int t){
	PID = numberProcess;
	priority = p;
	arrivalTime = t;
        envejecimiento = 0;
        IOTime = 0;
        currentQuantum = 0;
	resourceUse = new ArrayList<Integer>();
	numberProcess++;
    }

    /**
     *getPriority: Getter de prioridad
     *@return Prioridad del proceso
     **/
    public int getPriority(){
	return priority;
    }

    /**
     *getArrivalTime: Getter de arrivalTime
     *@return Tiempo de llegada del proceso
     **/
    public int getArrivalTime(){
	return arrivalTime;
    }

    /**
     *insertUse: Inserta un uso de CPU a la lista de uso
     *@param use: Entero que especifica tiempo de uso de CPU
     **/
    public void insertUse(Integer use){
	resourceUse.add(use);
    }

    /**
     *removeUse: Remueve el primer uso de la lista y lo devuelve
     *@return Primer uso de la lista
     **/
    public int removeUse(){
	if(resourceUse.isEmpty()){
	    System.out.println("ERROR: La lista esta vacia");
	    return 0;
	}else{
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

    /**
     *toString: Retorna una representacion de string del proceso
     *@return String con la informacion del proceso
     **/
    public String toString(){
	return "Proceso " + Integer.toString(this.PID)
	    +":\n\tPriority: " + Integer.toString(getPriority())
	    + "\n\tArrivalTime: " + Integer.toString(getArrivalTime()) 
	    + "\n\tResources: " + resourceUse.toString();
  }
  
  /**
  */
  @Override
   public int compareTo(Proceso p){
      return (new Integer(p.priority + p.envejecimiento))
             .compareTo(new Integer(this.priority + this.envejecimiento));
   }

   public int compare(Proceso p, Proceso p1){
      return p1.priority + p1.envejecimiento - p.priority - p.envejecimiento;
   }
  

}
