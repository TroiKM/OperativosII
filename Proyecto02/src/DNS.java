//Servidor DNS

import java.net.*;
import java.io.*;

public class DNS implements Runnable{

	private InetAddress ipPrincipal;
	private boolean failed;
	private DatagramSocket socket;
	private int time;

	public DNS(int port){
		try{
			socket = new DatagramSocket(port);
			ipPrincipal = null;
			failed = true;
			time = 0;
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
				ans.getTime(), ans.getAttributes());
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	

	private Mensaje executeCommand(Mensaje m, InetAddress a){
		String c = m.getCommand();

		if(c.equals("WHO")){
			if(ipPrincipal == null){
				return new Mensaje("NOTHING",time);
			}else{
				return new Mensaje("OK",time,ipPrincipal);
			}
		}else if(c.equals("SERVER")){
			if(ipPrincipal == null){
				ipPrincipal = a;
				return new Mensaje("COORD",time);
			}else{
				return new Mensaje("OK",time,ipPrincipal);
			}
		}

		return new Mensaje("PING",time);
	}	

	public static void main(String args[]){
		new Thread(new DNS(1111)).start();
	}
}									
