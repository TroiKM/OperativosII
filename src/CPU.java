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
  private Proceso running;
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
      while(finished.size() < timer.getMaxProc()){
	  System.out.println("CPU say: startJob...");
	  timer.startJob();
	  System.out.println("CPU say: done!\tnewToReady...");
	  newToReady();
	  System.out.println("CPU say: done!\tprocessReady...");
	  processReady();
	  System.out.println("CPU say: done!\tageProcesses...");
	  ageProcesses();
	  System.out.println("CPU say: done!\tendJob...");
	  timer.endJob();
	  System.out.println("CPU say: done!\n");

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
    if(!(ready.isEmpty() && running == null)){
      if(running == null){
        running = ready.removeElem();
      }
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

  public void ageProcesses(){
    for(Proceso p : ready.getQueue()){
      p.envejecer(1);
    }
  }

  private void endProcess(Proceso p){
    p.setFinishTime(timer.getTime());
    finished.addElem(p);
    running = null;
  }

  private void sendProcessToIO(Proceso p){
    p.setCurrentQuantum(quantum);
    p.setIOTime(5);
    p.resetEnvejecimiento();
    waiting.addElem(p);
    running = null;
  }

  private void reinsertProcess(Proceso p){
    p.envejecer(-2);
    p.setCurrentQuantum(quantum);
    ready.addElem(p);
    running = null;
  }
  
}
