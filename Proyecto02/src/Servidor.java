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

		System.out.println("Starting the send");
		byte[] buf = new byte[BUFFER_SIZE];
		String i = "SERVER";
		buf = i.getBytes();

		try{
			DatagramPacket init = new
				DatagramPacket(buf,buf.length,InetAddress.getByName(dirDNS),puertoDNS);
			socket.send(init);

			System.out.println("Sent");
			buf = new byte[BUFFER_SIZE];
			DatagramPacket rec = new DatagramPacket(buf,buf.length);
			socket.receive(rec);
			i = new String(rec.getData(),0,rec.getLength());
			System.out.println("Received " + i);
		}catch(IOException e){
			e.printStackTrace();
		}

	}

	public static void main(String args[]){
		Servidor s = new Servidor("Name",2222,"224.0.0.1",1111,"localhost");
	}

}
