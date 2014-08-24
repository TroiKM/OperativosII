import java.net.*;
import java.io.IOException;


public class Oyente implements Runnable{

	private Colas<DatagramPacket> mensajes;
	private MulticastSocket socket;
	private int buffer_size;
        
	public Oyente(Colas<DatagramPacket> c, MulticastSocket s, int b){
		mensajes = c;
		socket = s;
		buffer_size = b;
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
