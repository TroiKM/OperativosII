import java.util.ArrayList;

/**
 *Proceso: Clase que representa los procesos
 *@param priority: Entero que representa la prioridad
 *@param arrivalTime: Tiempo de llegada del proceso
 *@param resourceUse: Lista de enteros que representa los usos de CPU que hace
 *       este proceso. Entre usos de CPU hay usos de IO
**/

public class Proceso {

  private int priority;
  private int arrivalTime;
  private ArrayList<Integer> resourceUse;

  /**
   *Constructor de Proceso
   *@param p: Prioridad
   *@param t: Tiempo de llegada
   *@return Objeto Proceso con usoRecursos inicializado y vacio
  **/ 

  public Proceso(int p, int t){
    priority = p;
    arrivalTime = t;
    resourceUse = new ArrayList<Integer>();
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
}
