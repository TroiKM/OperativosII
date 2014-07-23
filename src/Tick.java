/**
 *Clase Tick: Representa al manejador de los ticks
 *@param time: Tiempo que lleva el manejador
**/
public class Tick{
  
  private int time;

  /**
   *Constructor del Tick
   *@return Objeto Tick
  **/
  public Tick(){
    time = 0;
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
  public void tick(Boolean ok_to_tick, Boolean ok_to_run){
    try{
      ok_to_tick.wait();
    }catch(IllegalMonitorStateException | InterruptedException e){
      e.printStackTrace();
      System.exit(-1);
    }
    time++;
    ok_to_run.notifyAll();
  }

  public static void main(String args[]){
    Tick tick = new Tick();
    Boolean a = new Boolean(true);
    Boolean b = new Boolean(false);
    tick.tick(a, b);
    System.out.println(tick.getTime());
  }
}
