/**
* @author Karen Troiano		09-10855
* @author Luis Miranda		10-10463
* @author Jose Montenegro	10-10469
*
* DNS.java: Contiene el programa principal del
* servidor de archivos.
*/

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class DNS {

	/**
	 * Variables globales.
	 */
	public static String host = "localhost";
	public static String puerto = "1111";
	/**
	 * Interfaz que permite la invocacion remota
	 *   los metodos del servidor.
	 **/
	public static Conexion c;
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
	public DNS() {
		try {
			c = new ConexionImpl(host, puerto);
			
			LocateRegistry.createRegistry(Integer.parseInt(puerto));
			
			Naming.rebind("rmi://localhost:" + puerto + "/Conexion", c);
			
		}
		catch (Exception e) {
			System.out.println ("Fallas conectandose al servidor DNS.");
			System.exit(0);
		}
		
	}
	
	/**
	 * main:
	 * 	Funcion principal del s_rmifs.
	 * 
	 * @param Argumentos enviados.
	 * 
	 */
	public static void main(String args[]) {
		
		int argv = args.length;
		/** 
		 * Variable que verifica de que no se
		 *  repita algun parametro en la invocacion
		 *  del programa cliente.
		 */
		 
		Boolean[] opciones = { false, false };

		if (argv != 4) {
			System.out.println ("Sintaxis de invocacion incorrecta.");
			System.out.println("\nSintaxis de invocacion: ");
			System.out.println ("java DNS -h host -p puerto");
			System.exit(0);
		} else {
			for(int j = 0; j < argv; j = j + 2){
				if(args[j].equals("-h") && !opciones[0]){
					host = args[j+1];
					opciones[0] = true;
					
				}else if(args[j].equals("-p") && !opciones[1]){
					puerto = args[j+1];
					opciones[1] = true;
					
				} else {
					System.out.println("HOLA");
					System.out.println ("Sintaxis de invocacion incorrecta.");
					System.out.println("\nSintaxis de invocacion: ");
					System.out.println ("java DNS -h host -p puerto");
					System.exit(0);
				}
			}
		}
		
		if (!(opciones[0] && opciones[1])) {
				System.out.println ("java Cliente -s servidor -p puerto"); 
				System.exit(0);
		} 
		
		/* RMI */
		new DNS();
		
		System.out.println("Servidor up!");
		
		return;
	}
	
}