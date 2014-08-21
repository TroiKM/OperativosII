import java.net.*;

public class Trabajador implements Runnable{

    private Colas<DatagramPacket> mensajes;

    public Trabajador(Colas<DatagramPacket> c){
	mensajes = c;
    }

    public void run(){
	while(true){
	    
	    DatagramPacket wrd = this.mensajes.removeElem();
	    if(wrd!=null){
		String s = new String(wrd.getData(),0,wrd.getLength());
		System.out.println("Trabajador: "+s);
	    }
	}
    }
    
    public static void main(String args[]){

	Colas<DatagramPacket> x = new Colas<DatagramPacket>();

	Oyente o = new Oyente(x);
	Trabajador t = new Trabajador(x);

	Thread oye = new Thread(o,"Oyente");
	Thread tra = new Thread(t,"Trabaj");

	oye.start();
	tra.start();
    }
    	    
}
