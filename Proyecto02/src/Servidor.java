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

	private static final int BUFFER_SIZE = 256;

	private ServerInfo info;
	private Queue<ServerInfo> servers;
	private MulticastSocket socket;
	private InetAddress group;
	private int puertoDNS;
	private String dirDNS;
	//private Servicios s;

	public Servidor(String n, int gPort, String g, int dPort, String d){
		servers = new LinkedList<ServerInfo>();
		info = new ServerInfo(n,"Activo");
		puertoDNS = dPort;
		dirDNS = d;

		try{
			socket = new MulticastSocket(gPort);
			group = InetAddress.getByName(g);
			socket.joinGroup(group);
		}catch(IOException e){
			e.printStackTrace();
		}

		Colas<DatagramPacket> x = new Colas<DatagramPacket>();
		
		Thread oye = new Thread(new Oyente(x,socket,BUFFER_SIZE),"Oyente");
		Thread tra = new Thread(new Trabajador(x,socket,BUFFER_SIZE),"Trabaj");
		oye.start();
		tra.start();
		
		System.out.println("Starting the send");
		byte[] buf = new byte[BUFFER_SIZE];
		String i = "SERVER";
		buf = i.getBytes();

		try{
			DatagramPacket init = new
			DatagramPacket(buf,buf.length,InetAddress.getByName(dirDNS),puertoDNS);
			socket.send(init);
			System.out.println("Servidor: send");
			buf = new byte[BUFFER_SIZE];
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
		Servidor s = new Servidor("Name",2222,"224.0.0.1",1111,"localhost");
	}

}