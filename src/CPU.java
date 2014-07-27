/**
 *CPU: Clase que representa al manejador del CPU
 *@param timer: Tick que representa el timer del CPU
**/
public class CPU implements Runnable{
  
  private Tick timer;
  String name;

  /**
   *Constructor de CPU. Corre El hilo
   *@param t: Tick a asignar al CPU
  **/
  public CPU(Tick t, String n){
    timer = t;
    name = n;
    new Thread(this,"CPU").start();
  }

  /**
   *Run: Metodo de Hilo
  **/
  public void run(){
    while(true){
      System.out.println("Comenzando " + name);
      timer.startJob();
      System.out.println(name + " dice que el tiempo del timer es " +
                         timer.getTime());
      timer.endJob();
    }
  }

  public static void main(String args[]){
    Tick timer = new Tick();
    new CPU(timer,"CPU 1");
    new CPU(timer,"CPU 2");
    while(true){
      timer.tick();
    }
  }
}
