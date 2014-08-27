import java.net.*;
import java.io.IOException;
import java.util.Queue;
import java.util.LinkedList;

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
	    LinkedList<ServerInfo> dummy = new LinkedList<ServerInfo>(this.servers.getQueue());
	    try{
		for (ServerInfo inf: dummy) {
		    System.out.println("Check: send a "+inf.getPuertoR());
		    Mensajeria.sendMessage(this.socket,inf.getIP(),
					   inf.getPuertoR(),"STAYING",0);
		    rec = Mensajeria.receivePacket(this.socket);
		    Thread.sleep(1000);		    
		}
	    }catch(IOException e){
		e.printStackTrace();
	    }catch(InterruptedException e){
		e.printStackTrace();
	    }
	    
	}
    }

}
