public class Test {

  public static void main(String args[]){
    Colas nuevo = new Colas();
    Colas waiting = new Colas();
    Ready ready = new Ready();
    Colas finished = new Colas();
    nuevo.parse("../bash/process_request_file.xml");
    Tick timer = new Tick(nuevo.size());
    
     
    Thread cpu = new Thread(new CPU(timer,ready,waiting,nuevo,finished,4),"CPU");
    Thread es = new Thread(new ES(timer,waiting,ready,finished),"ES");
    cpu.start();
    es.start();

    while(finished.size() < timer.getMaxProc()){
	System.out.println("Tick say: tick...");
	timer.tick();
	System.out.println("Tick say: done!\n");
    }
    System.out.println("Terminado el timer en el tiempo " + timer.getTime());
    try{
      cpu.join();
      es.join();
    }catch(InterruptedException e){
      e.printStackTrace();
    }
    System.out.println("Terminado todo el proceso");
    for(Proceso p : finished.getQueue()){
      System.out.println(p);    
    }

  }
}
