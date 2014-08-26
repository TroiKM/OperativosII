import java.net.*;
import java.io.*;
import java.util.Queue;
import java.util.LinkedList;

class Mensajeria{
	public static final int BUFFER_SIZE = 2048;

   public static void sendMessage(DatagramSocket s, InetAddress address, 
	int port, String com,int t, Object...atr) throws IOException{

		Mensaje men = new Mensaje(com,t,atr);
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(men);

		byte[] res = b.toByteArray();
		DatagramPacket toSend = new DatagramPacket(res,res.length,address,port);
		s.send(toSend);
	}

	public static DatagramPacket receivePacket(DatagramSocket s) 
	throws IOException{
		byte[] b = new byte[BUFFER_SIZE];
		DatagramPacket rec = new DatagramPacket(b,b.length);
		s.receive(rec);
		return rec;
	}

	public static Mensaje decodePacket(DatagramPacket p) throws IOException{
		ByteArrayInputStream in = new ByteArrayInputStream(p.getData());
		ObjectInputStream o = new ObjectInputStream(in);
		Mensaje res = null;
		
		try{
			res = (Mensaje) o.readObject();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return res;
	}
	
	public static void broadcast(DatagramSocket s,Queue<ServerInfo> se,String com,int t, Object...atr){
		System.out.println(se);
		try{
			for (ServerInfo inf: se) {
				Mensajeria.sendMessage(s,inf.getIP(),inf.getPuerto(),com,t, atr);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	

}
