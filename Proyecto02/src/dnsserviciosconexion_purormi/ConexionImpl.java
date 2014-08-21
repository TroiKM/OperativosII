/**
* @author Karen Troiano		09-10855
* @author Luis Miranda		10-10463
* @author Jose Montenegro	10-10469
*
*/

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


public class ConexionImpl 
	extends	java.rmi.server.UnicastRemoteObject 
	implements Conexion {
	
	/**
	 * Variables globales.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Fin de las variables globaless.
	 */
	

	/**
	 * ServiciosImpl:
	 * 	Constructor de la clase.
	 * 	Se encarga de establecer el puerto del servicio
	 *  mediante el cual se conectara al servidor.
	 *   
	 */
	public ConexionImpl(String host, String puerto)
	throws java.rmi.RemoteException {
		super();
	}
	
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
	public String nuevoServidor() throws java.rmi.RemoteException {
		
		return "welp";
	}
	
}