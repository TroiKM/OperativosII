import java.net.*;
import java.io.IOException;

public class Trabajador implements Runnable{

	private Colas<DatagramPacket> mensajes;
	private MulticastSocket socket;
    
	private InetAddress group;
	private int puertoDNS;
	private InetAddress dirDNS;
	private int time;


	public Trabajador(Colas<DatagramPacket> c,MulticastSocket s){
		mensajes = c;
		socket = s;
		time = 0;
	}

	public void run(){
	
		while(true){
		
		    Mensaje men = null;
		    
		    System.out.println("Time: "+this.time);
		    		    
			time++;
			DatagramPacket wrd = this.mensajes.removeElem();
			
			if(wrd!=null){
		
				try{
					men = Mensajeria.decodePacket(wrd);
					time = Math.max(men.getTime(),time);

					time++;
					System.out.println("Trabajador: "+men.getCommand()+"\tTime: "+this.time);
					time++;
					Thread.sleep(1000);
					Mensajeria.sendMessage(socket,wrd.getAddress(),wrd.getPort(),"SERVER",time);
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
