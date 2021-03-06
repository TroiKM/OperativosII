/**Clase ServerInfo: Clase que especifica el conjunto de datos que cada servidor
 * tiene de los demas servidores
**/

import java.util.LinkedList;
import java.util.Queue;
import java.net.*;
import java.io.*;

public class ServerInfo implements Serializable{

	private String nombre;
	private String tipo;
	private Queue<Archivo> archivos;
	private InetAddress IP;
    private int puerto;
    private int puertoR;

	private int edad;

    public ServerInfo(String n, String t, InetAddress ip, int p,int pr){
		archivos = new LinkedList<Archivo>();
		nombre = n;
		tipo = t;
		edad = 1;
		IP = ip;
		puerto = p;
		puertoR = pr;
	}

	public String getNombre(){
		return nombre;
	}

	public String getTipo(){
		return tipo;
	}

	public void setTipo(String t){
		tipo = t;
	}

	public InetAddress getIP(){
		return IP;
	}
	
	public int getPuerto(){
		return puerto;
	}

    	public int getPuertoR(){
		return puertoR;
	}

	public void addArchivo(Archivo a){
		archivos.add(a);
	}

	public Queue<Archivo> getArchivos(){
		return archivos;
	}

	
	public String toString(){
		return "IP: " + this.IP + 
			" Puerto: " + this.puerto;
	};

}
