import java.net.*;

public class Oyente implements Runnable{

    private Colas<DatagramPacket> mensajes;
        
    public Oyente(Colas<DatagramPacket> c){
	mensajes = c;
    }
    
    public void run(){
	while(true){
	    for(int i=0;i<10;i++){
		System.out.println("Oyente: "+i);

		byte[] buf = new byte[8];
		buf = (Integer.toString(i)).getBytes();
		
		this.mensajes.addElem(
		    new DatagramPacket(buf,buf.length));
		
	    }
	    try{
		System.out.println("Oyente: ZZZ");
		Thread.sleep(1000);
	    }catch(InterruptedException ex){
		Thread.currentThread().interrupt();
	    }
	    		
	}
	
    }

    public static void main(String args[]){

	Colas<DatagramPacket> x = new Colas<DatagramPacket>();
	
	Oyente o = new Oyente(x);
	
	o.run();

	System.out.println(o.mensajes.getQueue());
		
    }
}
