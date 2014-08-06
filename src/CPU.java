/**
 * @author Karen Troiano		09-10855
 * @author Luis Miranda		10-10463
 * @author Jose Montenegro	10-10469
 *
 * CPU: Clase que representa al manejador del CPU
 *
 * @param timer: Tick que representa el timer del CPU
 */

public class CPU implements Runnable{

    private Tick timer;
    private Colas waiting;
    private Colas nuevo;
    private Ready ready;
    private Colas finished;
    private Proceso running;
    private int quantum;
    
    private int position;
    private int numb;

    private static int num_cpu = 0;
    private static int num = 0;

        
    /**
     * Constructor de CPU. Corre El hilo
     * @param t: Tick a asignar al CPU
     */
    public CPU(Tick t, Ready r, Colas w, Colas n, Colas f, int q)
	{
	    timer = t;
	    ready = r;
	    waiting = w;
	    nuevo = n;
	    finished = f;
	    quantum = q;
	    running = null;
	    numb = num;
	    num++;
	    
	}
    
	
    /**
     * Run: Metodo de Hilo
     */
    public void run()
	{

	    while(finished.size() < timer.getMaxProc())
//	    do
	    {
//		System.out.println(this.numb + ":waiting" );
		timer.startJob();
		addCounter();
		newToReady();
		getRunning();
		processReady();
		ageProcesses();
		lessCounter();
//		System.out.println(this.numb + ":lessCounter" );
		timer.endJob();
	    };
	    

	    System.out.println(this.numb + ":end" );
		
	}

    public synchronized void newToReady()
	{
	    if(this.position==0){
		
		boolean hayMas = true;
		
		while(!nuevo.isEmpty() && hayMas)
		{
		    Proceso temp = nuevo.peekElem();
			
		    if(timer.getTime() >= temp.getArrivalTime())
		    {
			temp.setCurrentQuantum(quantum);
			temp.setState("Listo");
			ready.addElem(temp);
			nuevo.removeElem();
		    } else {
			hayMas = false;
		    }
		} 
	    }

	}

    public synchronized void addCounter()
	{
	    this.position = num_cpu;
	    num_cpu++;
	}
    
    
    public synchronized void lessCounter()
	{
	    num_cpu--;
	    this.position = 0;
	}

    public synchronized void getRunning()
	{
	    if(!ready.isEmpty() && this.running==null)
	    {
		this.running = ready.removeElem();
		if(this.running!=null){
		    this.running.setState("CPU");
		}
		
	    }
	}
    
        
    public void processReady()
	{
	    System.out.println(this.numb + ": " +running);
	    
	    if(this.running!=null){
				
		this.running.setFirstUse(this.running.getFirstUse() - 1);
		this.running.setCurrentQuantum(this.running.getCurrentQuantum() - 1);
			
		if(this.running.getFirstUse() < 0)
		{
		    this.running.removeFirstUse();
		    if(this.running.useEmpty())
		    {
			endProcess(this.running);
		    } else {
			sendProcessToIO(this.running);
		    }
		} 
		else if(this.running.getCurrentQuantum() <= 0) {
		    reinsertProcess(this.running);
		}
	    } else {
		++Test.ocio;
	    }
	}

    public synchronized void ageProcesses()
	{
	    if(this.position==0){
		ready.envejecer(1);
		ready.incrementarEspera();
	    }
	    
	}

    private void endProcess(Proceso p)
	{
	    p.setState("Final");
	    p.setFinishTime(timer.getTime());
	    finished.addElem(p);
	    running = null;

	}

    private void sendProcessToIO(Proceso p)
	{
	    p.setCurrentQuantum(quantum);
	    p.setIOTime(-1);
	    p.resetEnvejecimiento();
	    p.setState("Bloqueado");				
	    waiting.addElem(p);
	    running = null;
	}

    private void reinsertProcess(Proceso p)
	{
	    p.envejecer(-2);
	    p.setCurrentQuantum(quantum);
	    p.setState("Listo");
	    ready.addElem(p);
	    running = null;
	}

}
