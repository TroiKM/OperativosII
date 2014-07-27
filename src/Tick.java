/**
 *Clase Tick: Representa al monitor de los ticks
 *@param time: Tiempo que lleva el manejador
**/
public class Tick{
  
  private static final int MAX_DEVICES = 2;

  private int time;
  private boolean ok_to_tick;
  private boolean ok_to_run;
  private int finished;

  /**
   *Constructor del Tick
   *@return Objeto Tick. Inicializa el thread del reloj
  **/
  public Tick(){
    time = 0;
    finished = 0;
    ok_to_tick = false;
    ok_to_run = true;
  }
  
  /**
   *getTime: Obtiene el tiempo del manejador
   *@return Tiempo actual
  **/
  public synchronized int getTime(){
    return time;
  }

  /**
   *Tick: Hace el tick
  **/
  public synchronized void tick(){
    while(!ok_to_tick){
      try{
        wait();
      }catch(IllegalMonitorStateException | InterruptedException e){
        e.printStackTrace();
        System.exit(-1);
      }
    }
    time++;
    ok_to_run = true;
    ok_to_tick = false;
    finished = 0;
    notifyAll();
  }

  /**
   *startJob: Inicializa un manejador
  **/
  public synchronized void startJob(){
    while(!ok_to_run){
      try{
        wait();
      }catch(InterruptedException e){
        e.printStackTrace();
      }
    }
  }

  /**
   *endJob: Termina un manejador
  **/
  public synchronized void endJob(){
    finished++;
    while(finished != MAX_DEVICES){
      try{
        wait();
      }catch(InterruptedException e){
        e.printStackTrace();
      }
    }
    if(!ok_to_tick){
      ok_to_tick = true;
      ok_to_run = false;
      notifyAll();
    }
  }

}
