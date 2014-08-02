public class Test {

  public static void main(String args[]){
    Tick timer = new Tick();
    Colas nuevo = new Colas();
    Colas waiting = new Colas();
    Colas finished = new Colas();
    Ready ready = new Ready();
   
    // Proceso p1 = new Proceso(10,2);
    // p1.insertUse(20);
    // Proceso p2 = new Proceso(50,2);
    // p2.insertUse(20);
    // p2.insertUse(30);
    // nuevo.addElem(p1);
    // nuevo.addElem(p2);

    nuevo.parse("bash/process_request_file.xml");
     
    Thread cpu = new Thread(new CPU(timer,ready,waiting,nuevo,finished,4),"CPU");
    Thread es = new Thread(new ES(timer,waiting,ready,nuevo),"ES");
    cpu.start();
    es.start();

    while(!(waiting.isEmpty() && ready.isEmpty() && nuevo.isEmpty())){
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
  }
}
