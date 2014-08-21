/** 
* @author Karen Troiano		09-10855
* @author Luis Miranda		10-10463
* @author Jose Montenegro	10-10469
*
* Clase Servidor: Clase que especifica a un servidor de 
*	control de versiones
* 
* @param tipo: Tipo del servidor. Puede ser pasivo, 
	activo o principal
* @param info: Informacion del servidor
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Servidor{

	private ServerInfo info;
	private Queue<ServerInfo> servers;
	
	public static String host = "localhost";
	public static String puerto = "5555";
	
	/**
	 * Interfaz que permite la invocacion remota
	 *   los metodos del servidor.
	 **/
	public static Servicios c;
	/**
	 * Fin de las variables globales.
	 */

	/**
	 * s_rmisf:
	 * 	Constructor de la clase.
	 * 	Se encarga de establecer el puerto del servicio
	 *  mediante el cual los clientes se conectaran.
	 *   
	 */
	public Servidor() {
		try {
			c = new ServiciosImpl(host, puerto);
			
			LocateRegistry.createRegistry(Integer.parseInt(puerto));
			
			Naming.rebind("rmi://localhost:" + puerto + "/Servicios", c);
			
		}
		catch (Exception e) {
			System.out.println ("Fallas conectandose.");
			System.exit(0);
		}
	}
	
	
	public static void main(String args[]) {
		/* RMI */
		new Servidor();
		
		System.out.println("Server up!");
	}
	
}
