import java.net.*;
import java.io.IOException;

public class Trabajador implements Runnable{

    private Colas<DatagramPacket> mensajes;
    private MulticastSocket socket;
    private int buffer_size;
    
    private InetAddress group;
    private int puertoDNS;
    private String dirDNS;


    public Trabajador(Colas<DatagramPacket> c,MulticastSocket s,int b){
	mensajes = c;
	socket = s;
	buffer_size = b;
    }

    public void run(){
	while(true){
	    byte[] buf;
	    DatagramPacket res;

	    DatagramPacket wrd = this.mensajes.removeElem();
	    if(wrd!=null){
		String s = new String(wrd.getData(),0,wrd.getLength());
		System.out.println("Trabajador: "+s);

		s = "SERVER";

		buf = new byte[this.buffer_size];
		buf = s.getBytes();
		res = new DatagramPacket(buf,buf.length,
					 wrd.getAddress(),wrd.getPort());
		
		try{
		    Thread.sleep(1000);
		    this.socket.send(res);
		    System.out.println("Trabajador: send");
		}catch(IOException e){
		    e.printStackTrace();
		} catch(InterruptedException e) {
		    e.printStackTrace();
		}

	    }
	    
	}
    }
    
    // public static void main(String args[]){

    // 	Colas<DatagramPacket> x = new Colas<DatagramPacket>();

    // 	Oyente o = new Oyente(x);
    // 	Trabajador t = new Trabajador(x);

    // 	Thread oye = new Thread(o,"Oyente");
    // 	Thread tra = new Thread(t,"Trabaj");

    // 	oye.start();
    // 	tra.start();
    // }
    	    
}
