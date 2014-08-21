/**Clase ServerInfo: Clase que especifica el conjunto de datos que cada servidor
 * tiene de los demas servidores
**/

import java.util.LinkedList;
import java.util.Queue;

public class ServerInfo{

	private String nombre;
	private String tipo;
	private Queue<Archivo> archivos;

	public ServerInfo(String n, String t){
		archivos = new LinkedList<Archivo>();
		nombre = n;
		tipo = t;
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

}
