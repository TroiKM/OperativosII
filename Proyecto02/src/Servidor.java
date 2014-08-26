/**Clase Servidor: Clase que especifica a un servidor de control de versiones
*@param tipo: Tipo del servidor. Puede ser pasivo, activo o principal
*@param info: Informacion del servidor
**/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.net.*;
import java.io.*;

public class Servidor{	

	private ServerInfo info;
	private Queue<ServerInfo> servers;
	private MulticastSocket socket;
	private InetAddress group;
	private int puertoDNS;
	private InetAddress dirDNS;
	private int time;
	
	public Servidor(String n, int gPort, String g, int dPort, String d){
		servers = new LinkedList<ServerInfo>();
		puertoDNS = dPort;
		time = 0;

		try{
			dirDNS = InetAddress.getByName(d);
			socket = new MulticastSocket(gPort);
			group = InetAddress.getByName(g);
			socket.joinGroup(group);
		}catch(IOException e){
			e.printStackTrace();
		}

		Colas<DatagramPacket> x = new Colas<DatagramPacket>();
		
		info = new ServerInfo(n,"Activo", group, gPort);
		System.out.println("My info: " + info);

		Thread oye = new Thread(new Oyente(x,socket),"Oyente");
		Thread tra = new Thread(new Trabajador(x,socket,gPort,info),"Trabaj");
		oye.start();
		tra.start();
		
		System.out.println("Starting the send");

		try{
		    Mensajeria.sendMessage(socket,dirDNS,puertoDNS,"SERVER",time);
		} catch(IOException e) {
			e.printStackTrace();
		}

		try{
		    oye.join();
		    tra.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]){
		Servidor s = new Servidor("Name",Integer.parseInt(args[0]),"224.0.0.1",1111,"localhost");
	}

}
