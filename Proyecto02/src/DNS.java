//Servidor DNS

import java.net.*;
import java.io.*;

public class DNS implements Runnable{

	private static final int BUFFER_SIZE = 256;

	private InetAddress ipPrincipal;
	private Boolean failed;
	private DatagramSocket socket;

	public DNS(int port){
		try{
			socket = new DatagramSocket(port);
			ipPrincipal = null;
			failed = true;
		}catch (SocketException e){
			e.printStackTrace();
		}
	}

	public void run(){
		try{
			while(true){
				System.out.println("Listening...");
				DatagramPacket p = Mensajeria.receivePacket(socket);
				System.out.println("Packet received");

				Mensaje men = Mensajeria.decodePacket(p);
				System.out.println("Packet decoded");
				System.out.println("Got message with command " + men.getCommand());

				Mensaje ans = executeCommand(men,p.getAddress());
				System.out.println("Sending back message with command " +
				ans.getCommand());

				Mensajeria.sendMessage(socket,p.getAddress(),p.getPort(),ans.getCommand(),
				ans.getAttributes());
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	

	private Mensaje executeCommand(Mensaje m, InetAddress a){
		String c = m.getCommand();

		if(c == "WHO"){
			if(ipPrincipal == null){
				return new Mensaje("NOTHING");
			}else{
				return new Mensaje("OK",ipPrincipal);
			}
		}else if(c == "SERVER"){
			if(ipPrincipal == null){
				ipPrincipal = a;
				return new Mensaje("COORD");
			}else{
				return new Mensaje("OK",ipPrincipal);
			}
		}

		return new Mensaje("PING");
	}	

	public static void main(String args[]){
		new Thread(new DNS(1111)).start();
	}
}									
