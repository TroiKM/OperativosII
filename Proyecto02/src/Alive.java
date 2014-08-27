import java.net.*;
import java.io.IOException;


public class Alive implements Runnable{

    private MulticastSocket socket;
        
    public Alive(MulticastSocket s){
	
	socket = s;
	
    }
    
    public void run(){
	DatagramPacket rec = null;
		
	while(true){
	    try{
		System.out.println("Alive: listen");
		rec = Mensajeria.receivePacket(this.socket);
		System.out.println("Alive: recive");
		Mensajeria.sendMessage(this.socket,rec.getAddress(),
				       rec.getPort(),"ALIVE",0);
		
	    } catch(IOException e) {
		e.printStackTrace();
	    }
			
	}
    }

}
