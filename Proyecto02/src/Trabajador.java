import java.net.*;
import java.io.IOException;

public class Trabajador implements Runnable{

	private Colas<DatagramPacket> mensajes;
	private MulticastSocket socket;
	private int buffer_size;
    
	private InetAddress group;
	private int puertoDNS;
	private String dirDNS;


	public Trabajador(Colas<DatagramPacket> c,MulticastSocket s,int b){
		mensajes = c;
		socket = s;
		buffer_size = b;
	}

	public void run(){
	
		while(true){
		
			DatagramPacket res;

			DatagramPacket wrd = this.mensajes.removeElem();
			if(wrd!=null){
				Mensaje men = null;
		
				try{
					men = Mensajeria.decodePacket(wrd);
					System.out.println("Trabajador: "+men.getCommand());
					Thread.sleep(1000);
					Mensajeria.sendMessage(socket,wrd.getAddress(),wrd.getPort(),"SERVER");
					System.out.println("Trabajador: send");
				}catch(IOException e){
					e.printStackTrace();
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

} 
