/**
* @author Karen Troiano		09-10855
* @author Yeiker Vazquez	09-10882
* @grupo  15
*
* Archivo: Atenticacion.java
*
* Descripcion: Interfaz para el programa principal del
* servidor de autenticacion.
*/

public interface Autenticacion extends java.rmi.Remote {
	
	/*
     * autenticarUsuario:
     * 	Se encarga de verificar que el nombre y clave pasados
     * 	como argumento estan en el archivo de usuarios con
     * 	acceso al servidor de archivos.
     * 
     * @param 	Nombre del usuario que se quiere autenticar.
     * @param 	Clave del usuario que se quiere autenticar.
     * @throws 	java.rmi.RemoteException.
     * @return 	True si el usuario con su clave se encuentra en
     * 			el archivo de usuarios permitidos, False en caso
     * 			contrario.
     */
	public Boolean autenticarUsuario(String nombre, String clave)
			throws java.rmi.RemoteException;

}
