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
    private MulticastSocket socket2;
    private MulticastSocket socket3;
<<<<<<< HEAD
	 private MulticastSocket commandSocket;
	private InetAddress group;
	private int puertoDNS;
	private InetAddress dirDNS;
	private int time;
	private int k;
	
    public Servidor(String n, int gPort, int gPort2, int gPort3,
		    String g, int dPort, String d, int kes, int cPort, String tipo){
		servers = new LinkedList<ServerInfo>();
		puertoDNS = dPort;
		time = 0;
		k = kes;

		try{
			dirDNS = InetAddress.getByName(d);
			socket = new MulticastSocket(gPort);
			socket2 = new MulticastSocket(gPort2);
			socket3 = new MulticastSocket(gPort3);
			commandSocket = new MulticastSocket(cPort);
			group = InetAddress.getByName(g);
			socket.joinGroup(group);
			socket2.joinGroup(group);
			socket3.joinGroup(group);
			
		}catch(IOException e){
			e.printStackTrace();
		}

		Colas<DatagramPacket> x = new Colas<DatagramPacket>();
		Colas<ServerInfo> y = new Colas<ServerInfo>();
				
		info = new ServerInfo(n,tipo,group, gPort, gPort2);
		System.out.println("My info: " + info);

      Thread ali = new Thread(new Alive(socket2),"Alive");
      Thread che = new Thread(new Check(x,socket3,y),"Check");
		Thread oye = new Thread(new Oyente(x,socket),"Oyente");
		Thread tra = new Thread(new
		Trabajador(x,socket,gPort,info,y,commandSocket,k), "Trabaj");

		oye.start();
		tra.start();
		ali.start();
		che.start();
		
		
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
	    Servidor s = new
		 Servidor("Name",Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2]),"224.0.0.1",1111,"localhost",Integer.parseInt(args[3]),Integer.parseInt(args[4]),
		 args[5]);
	}

}
