/**
 *CPU: Clase que representa al manejador del CPU
 *@param timer: Tick que representa el timer del CPU
**/
public class CPU implements Runnable{
  
  private Tick timer;
  private Colas waiting;
  private Colas nuevo;
  private Ready ready;
  private Colas finished;
  private int quantum;

  /**
   *Constructor de CPU. Corre El hilo
   *@param t: Tick a asignar al CPU
  **/
  public CPU(Tick t, Ready r, Colas w, Colas n, Colas f, int q){
    timer = t;
    ready = r;
    waiting = w;
    nuevo = n;
    finished = f;
    quantum = q;
    new Thread(this,"CPU").start();
  }

  /**
   *Run: Metodo de Hilo
  **/
  public void run(){
    while(true){
      timer.startJob();
      newToReady();
      processReady();
      ageProcesses();
      timer.endJob();
    }
  }

  public void newToReady(){
    boolean hayMas = true;
    while(!nuevo.isEmpty() && hayMas){
      Proceso temp = nuevo.peekElem();
      if(temp.getArrivalTime() >= timer.getTime()){
        temp.setCurrentQuantum(quantum);
        ready.addElem(temp);
        nuevo.removeElem();
      }else{
        hayMas = false;
      }
    }
  }

  public void processReady(){
    if(!ready.isEmpty()){
      Proceso running = ready.peekElem();
      running.setFirstUse(running.getFirstUse() - 1);
      if(running.getFirstUse() <= 0){
        running.removeFirstUse();
        if(running.useEmpty()){
          running.setFinishTime(timer.getTime());
          finished.addElem(running);
        }else{
          running.setCurrentQuantum(quantum);
          running.setIOTime(5);
          running.resetEnvejecimiento();
          waiting.addElem(running);
        }
        ready.removeElem();
      }else if(running.getCurrentQuantum() <= 0){
        running.envejecer(-2);
        ready.removeElem();
        ready.addElem(running);
      }
    }
  }

  public void ageProcesses(){
    for(Proceso p : ready.getQueue()){
      p.envejecer(1);
    }
  }
      
}
