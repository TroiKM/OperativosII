import java.net.*;
import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.LinkedList;
import java.util.Queue;

public class Trabajador implements Runnable{

	private Colas<DatagramPacket> mensajes;
	private MulticastSocket socket;
    
	private InetAddress group;
	private int puertoDNS;
	private InetAddress dirDNS;
	private int time;

	private Servicios serv;
	private int puerto;
	private ServerInfo info;
	private Queue<ServerInfo> servers = new LinkedList<ServerInfo>();;



	public Trabajador(Colas<DatagramPacket> c,MulticastSocket s, int p, ServerInfo i){
		mensajes = c;
		socket = s;
		time = 0;
		puerto = p;
		info = i;
	}

	public void run(){
	
		while(true){
		
			Mensaje men = null;
			time++;
			DatagramPacket wrd = this.mensajes.removeElem();
			
			if(wrd!=null){
		
				try{
					men = Mensajeria.decodePacket(wrd);
					time = Math.max(men.getTime(),time);
					time++;
					executeCommand(men);
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	private void executeCommand(Mensaje men){
		String com = men.getCommand();

		if(com.equals("COORD")){
			try{
				serv = new ServiciosImpl("localhost",String.valueOf(puerto));
				LocateRegistry.createRegistry(puerto);
				Naming.rebind("rmi://localhost:" + puerto + "/Servicios",serv);
			}catch(RemoteException e){
				e.printStackTrace();
			}catch(MalformedURLException e){
				e.printStackTrace();
			}
			System.out.println("Ahora soy el cordi");
		}else if ( com.equals("OK") ) {
			try{
				Mensajeria.sendMessage(this.socket,(InetAddress)men.getAttribute(0),(int)men.getAttribute(1),"SERVER",time, this.info );
			}catch(IOException e){
				e.printStackTrace();
			}
		}else if (com.equals("SERVER")) {
			ServerInfo i = (ServerInfo) men.getAttribute(0);
			this.servers.add(i);
			System.out.println(this.servers);
			try{
				for (ServerInfo inf: this.servers) {
					Mensajeria.sendMessage(this.socket,inf.getIP(),inf.getPuerto(),"NEWSERVER",time, this.servers);
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		} else if (com.equals("NEWSERVER")){
			this.servers = (Queue<ServerInfo>)men.getAttribute(0);
			System.out.println(this.servers);
		}

	}

} 
