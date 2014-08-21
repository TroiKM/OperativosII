//Servidor DNS

import java.net.*;
import java.io.*;

public class DNS implements Runnable{

	private static final int BUFFER_SIZE = 256;

	private String ipPrincipal;
	private Boolean failed;
	private DatagramSocket socket;

	public DNS(int port){
		try{
			socket = new DatagramSocket(port);
			ipPrincipal = "localhost";
			failed = true;
		}catch (SocketException e){
			e.printStackTrace();
		}
	}

	public void run(){
		while(true){
			byte[] buf = new byte[BUFFER_SIZE];
			DatagramPacket p = new DatagramPacket(buf,buf.length);

			try{
				System.out.println("Listening");
				socket.receive(p);
			}catch(IOException e){
				e.printStackTrace();
			}

			System.out.println("Message received");
			String received = new String(p.getData(),0,p.getLength());
			String response;
			DatagramPacket resPacket;

			if(received.startsWith("WHO")){
			System.out.println("Ip: " +ipPrincipal);
				respondIP(p);
			}else if(received.startsWith("FAILED")){
				failed = true;
				ipPrincipal = null;
				respondIP(p);
			}else if(received.startsWith("SERVER")){
				respondCoord(p);
			}
		}
	}

	private void respondIP(DatagramPacket p){
		DatagramPacket resPacket;
		byte[] buf = new byte[BUFFER_SIZE];

		if(ipPrincipal == null){
			String response = "NOTHING";
			buf = response.getBytes();
		}else{
			System.out.println("Ip: " +ipPrincipal);
			buf = ipPrincipal.getBytes();
		}
		resPacket = new DatagramPacket(buf,buf.length,p.getAddress(),p.getPort());

		try{
			socket.send(resPacket);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void respondCoord(DatagramPacket p){
		DatagramPacket resPacket;
		byte[] buf = new byte[BUFFER_SIZE];
		String response;

		if(failed){
			response = "COORD";
			ipPrincipal = p.getAddress().toString();
			failed = false;
		}else{
			response = "OK";
		}
		buf = response.getBytes();
		resPacket = new DatagramPacket(buf,buf.length,p.getAddress(),p.getPort());

		try{
			socket.send(resPacket);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public static void main(String args[]){
		new Thread(new DNS(1111)).start();
	}
}									
