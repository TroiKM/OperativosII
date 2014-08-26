import java.net.*;
import java.io.IOException;


public class Alive implements Runnable{

	private Colas<DatagramPacket> mensajes;
	private MulticastSocket socket;
        
	public Alive(Colas<DatagramPacket> c, MulticastSocket s){
		mensajes = c;
		socket = s;
	}
    
	public void run(){
		DatagramPacket rec = null;
		
		while(true){
			try{
				rec = Mensajeria.receivePacket(socket);
				System.out.println("Oyente: receive");
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			this.mensajes.addElem(rec);
		}
	}

}
