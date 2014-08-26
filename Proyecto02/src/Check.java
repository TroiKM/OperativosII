import java.net.*;
import java.io.IOException;

public class Check implements Runnable{

    private Colas<DatagramPacket> mensajes;
    private MulticastSocket socket;
    private Colas<ServerInfo> servers;

    public Check(Colas<DatagramPacket> c, MulticastSocket s,
		 Colas<ServerInfo> ser){

	mensajes = c;
	socket = s;
	servers = ser;
    }

    public void run(){
	DatagramPacket rec = null;

	while(true){
	    try{
		for (ServerInfo inf: this.servers.getQueue()) {
		    Mensajeria.sendMessage(this.socket,inf.getIP(),
					   inf.getPuerto(),"STAYING",0);

		    rec = Mensajeria.receivePacket(this.socket);
		    Thread.sleep(500);
		    
		}
	    }catch(IOException e){
		e.printStackTrace();
	    }catch(InterruptedException e){
		e.printStackTrace();
	    }
	    
	}
    }

}
