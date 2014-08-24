import java.net.*;
import java.io.IOException;

public class Trabajador implements Runnable{

	private Colas<DatagramPacket> mensajes;
	private MulticastSocket socket;
    
	private InetAddress group;
	private int puertoDNS;
	private InetAddress dirDNS;


	public Trabajador(Colas<DatagramPacket> c,MulticastSocket s){
		mensajes = c;
		socket = s;
	}

	public void run(){
	
		while(true){
		
			Mensaje men = null;

			DatagramPacket wrd = this.mensajes.removeElem();
			if(wrd!=null){
		
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
