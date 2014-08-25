import java.net.*;
import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Trabajador implements Runnable{

	private Colas<DatagramPacket> mensajes;
	private MulticastSocket socket;
    
	private InetAddress group;
	private int puertoDNS;
	private InetAddress dirDNS;
	private int time;

	private Servicios serv;
	private int puerto;


	public Trabajador(Colas<DatagramPacket> c,MulticastSocket s, int p){
		mensajes = c;
		socket = s;
		time = 0;
		puerto = p;
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
		}else{
			return;
		}

	}


} 
