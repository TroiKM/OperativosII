import java.net.*;
import java.io.*;

class Mensajeria{
	public static final int BUFFER_SIZE = 256;

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

}
