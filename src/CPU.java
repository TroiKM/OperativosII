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
  }

  /**
   *Run: Metodo de Hilo
  **/
  public void run(){
    while(!(waiting.isEmpty() && ready.isEmpty() && nuevo.isEmpty())){
      timer.startJob();
      newToReady();
      processReady();
      ageProcesses();
      timer.endJob();
    }
    System.out.println("CPU acaba su corrida");
  }

  public void newToReady(){
    boolean hayMas = true;
    while(!nuevo.isEmpty() && hayMas){
      Proceso temp = nuevo.peekElem();
      if(timer.getTime() >= temp.getArrivalTime()){
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
      if(running.useEmpty()){
        endProcess(running);
      }else{
        running.setFirstUse(running.getFirstUse() - 1);
        running.setCurrentQuantum(running.getCurrentQuantum() - 1);
        if(running.getFirstUse() <= 0){
          running.removeFirstUse();
          if(running.useEmpty()){
            endProcess(running);
          }else{
            sendProcessToIO(running);
          }
        }else if(running.getCurrentQuantum() <= 0){
          reinsertProcess(running);
        }
      }
    }
  }

  public void ageProcesses(){
    for(Proceso p : ready.getQueue()){
      p.envejecer(1);
    }
  }

  private void endProcess(Proceso p){
    p.setFinishTime(timer.getTime());
    finished.addElem(p);
    ready.removeElem();
  }

  private void sendProcessToIO(Proceso p){
    p.setCurrentQuantum(quantum);
    p.setIOTime(5);
    p.resetEnvejecimiento();
    waiting.addElem(p);
    ready.removeElem();
  }

  private void reinsertProcess(Proceso p){
    p.envejecer(-2);
    ready.removeElem();
    ready.addElem(p);
  }
  
}
