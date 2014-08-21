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
	byte[] buf;
	DatagramPacket rec;
	
	while(true){

	    buf = new byte[this.buffer_size];
	    rec = new DatagramPacket(buf,buf.length);

	    try{
		this.socket.receive(rec);
		System.out.println("Oyente: receive");

	    } catch(IOException e) {
		e.printStackTrace();
	    }

	    this.mensajes.addElem(rec);
			    		
	}
	
    }

    // public static void main(String args[]){

    // 	Colas<DatagramPacket> x = new Colas<DatagramPacket>();
	
    // 	Oyente o = new Oyente(x);
	
    // 	o.run();

    // 	System.out.println(o.mensajes.getQueue());
		
    // }
}
