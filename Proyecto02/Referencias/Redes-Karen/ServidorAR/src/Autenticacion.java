/**
* @author Karen Troiano		09-10855
* @author Yeiker Vazquez	09-10855
* @grupo  15
*
* Archivo: Atenticacion.java
*
* Descripcion: Interfaz para el programa principal del
* servidor de autentificacion.
*/

public interface Autenticacion extends java.rmi.Remote {
	
	public Boolean autenticarUsuario(String nombre, String clave)
			throws java.rmi.RemoteException;

}
