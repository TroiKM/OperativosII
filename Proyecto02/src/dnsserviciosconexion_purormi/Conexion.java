/**
* @author Karen Troiano		09-10855
* @author Luis Miranda		10-10463
* @author Jose Montenegro	10-10469
*
* Conexion.java: Interfaz para el DNS del programa.
*/

public interface Conexion
	extends java.rmi.Remote {
	
	public String nuevoServidor() throws java.rmi.RemoteException;
	
}