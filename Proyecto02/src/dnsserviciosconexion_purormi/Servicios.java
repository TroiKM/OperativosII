/**
* @author Karen Troiano		09-10855
* @author Luis Miranda		10-10463
* @author Jose Montenegro	10-10469
*
* Archivo: Servicios.java
*
* Descripcion: Interfaz para el programa principal del
* servidor de de archivos.
*/

public interface Servicios
	extends java.rmi.Remote {
	
	/** 
	 * iniciarSesion:
	 * 	Funcion encargada de autenticar el usuario 
	 * 	y agregar al log este inicio de sesion.
	 * 
	 * @param 	Nombre del usuario a entrar.
	 * @param	Clave del usuario a entrar.
	 * @param	Opcion para identificar si se registrara en 
	 * 			el log o no.
	 * @return	Devuelve true si fue exitosa la conexion, false
	 * 			en caso de error de autentificacion.
	 */
	public Boolean iniciarSesion(String nombre, String clave, int i)
			throws java.rmi.RemoteException;
	
	/** 
	 * cerrarSesion:
	 * 	Funcion encargada de autenticar el usuario 
	 * 	y agregar al log este cierre de sesion.
	 * 
	 * @param 	Nombre del usuario a salir.
	 * @param	Clave del usuario a salir.
	 * @return	Devuelve true si fue exitosa la salida, false
	 * 			en caso de error de autentificacion.
	 */
	public Boolean cerrarSesion(String nombre, String clave)
			throws java.rmi.RemoteException;
	
	/** 
	 * listarArchivosEnServidor:
	 * 	Funcion encargada de buscar si un archivo 
	 * 	pertenece a la lista de archivos locales 
	 * 	del servidor.
	 * 
	 * 	A su vez se encarga de mostrar 
	 * 	la lista de archivos del servidor.
	 * 
	 * @param 	Nombre del usuario a autenticar.
	 * @param	Clave del usuario a autenticar.
	 * @param	Nombre del archivo a ser buscado.
	 * @return	Devuelve la lista si fue exitosa la conexion, "false"
	 * 			en caso de error de autentificacion y "no" en caso de
	 * 			no haber encontrado el archivo en la lista de archivos
	 * 			en el servidor.
	 */
	public String listarArchivosEnServidor(String nombre, String clave, String nombreArchivo)
	throws java.rmi.RemoteException;
	
	
	/** 
	 * subirArchivo:
	 * 	Funcion encargada de subir un archivo 
	 * 	al servidor.
	 * 
	 * @param 	Nombre del usuario a autenticar.
	 * @param	Clave del usuario a autenticar.
	 * @param	Nombre del archivo a ser agregado.
	 * @param	Arreglo de bytes que contienen la informacion del archivo.
	 * @return	Devuelve "false" en caso de error de autentificacion y 
	 * 			un mensaje de exito en caso de exito y otro mensaje en 
	 * 			caso de error.
	 */
	public String subirArchivo(String nombre, String clave, String nombreArchivo, byte[] bytesArchivo)
		throws java.rmi.RemoteException;
	
	/** 
	 * bajarArchivo:
	 * 	Funcion encargada de bajar un archivo 
	 * 	del servidor.
	 * 
	 * @param 	Nombre del usuario a autenticar.
	 * @param	Clave del usuario a autenticar.
	 * @param	Nombre del archivo a ser bajado.
	 * @return	Devuelve null en caso de error y un arreglo de bytes con
	 * 			el contenido del archivo.
	 */
	public byte[] updateArchivos(String nombre, String clave)
		throws java.rmi.RemoteException;
	
	/** 
	 * borrarArchivo:
	 * 	Funcion encargada de borrar un archivo 
	 * 	del servidor.
	 * 
	 * @param 	Nombre del usuario a autenticar.
	 * @param	Clave del usuario a autenticar.
	 * @param	Nombre del archivo a ser borrado.
	 * @return	Devuelve "false" en caso de error de autentificacion y 
	 * 			un mensaje de exito en caso de exito y otro mensaje en 
	 * 			caso de error.
	 */
	public String borrarArchivo(String nombre, String clave, String nombreArchivo)
		throws java.rmi.RemoteException;
	
		
}