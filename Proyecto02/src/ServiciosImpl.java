/**
* @author Karen Troiano		09-10855
* @author Luis Miranda		10-10463
* @author Jose Montenegro	10-10469
*
* AutenticacionImpl: Implementacion de la interfaz para el programa 
* principal del servidor de archivos (Servicios.java).
*/

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


public class ServiciosImpl 
	extends	java.rmi.server.UnicastRemoteObject 
	implements Servicios {
	
	/**
	 * Variables globales.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Fin de las variables globaless.
	 */
	
	private Trabajador trab;
	private int k;

	/**
	 * ServiciosImpl:
	 * 	Constructor de la clase.
	 * 	Se encarga de establecer el puerto del servicio
	 *  mediante el cual se conectara al servidor.
	 *   
	 */
	public ServiciosImpl(String host, String puerto, Trabajador t, int kes)
	throws java.rmi.RemoteException {
		super();
		trab = t;
		k = kes;
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
	public String listarArchivosEnServidor(String nombreArchivo)
	throws java.rmi.RemoteException {
		
		String path = "."; 
		String respuesta = "";
		
		String files;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles(); 
 
		for (int i = 0; i < listOfFiles.length; i++)
		{
			if (listOfFiles[i].isFile()){
				files = listOfFiles[i].getName();
				/*
				 * Se ignoran los archivos relacionados con el proyecto. 
				 */
				if	( !(files.equals("Servicios.java")
						|| files.equals("Servicios.class")
						|| files.equals("Servidor.java")
						|| files.equals("Servidor.class")
						|| files.equals("ServiciosImpl.java")
						|| files.equals("ServiciosImpl.class")
						|| files.equals("ServiciosImpl$1.class")
						|| files.equals("ServiciosImpl$Archivo.class")
						)
					){
					if (nombreArchivo == null) {
						respuesta += "\t" + files + "\n";
					} else {
						if (files.equals(nombreArchivo)) {
							return "true";
						}
					}
				}
			}
		}
		
		return respuesta;
	}
	
	/** 
	 * subirArchivo:
	 * 	Funcion encargada de subir un archivo 
	 * 	al servidor.
	 * 
	 * @param 	Nombre del usuario a autenticar.
	 * @param	Clave del usuario a autenticar.
	 * @param	Nombre del archivo a ser agregado.
	 * @param	Arreglo de bytes que contienen la informacion del archivo.
	 * @return	Mensaje de exito en caso de exito y otro mensaje en 
	 * 			caso de error.
	 */
	public String subirArchivo(String nombreArchivo, byte[] datosArchivo)
	throws java.rmi.RemoteException {

		String res = null;

		try{
			System.out.println("Trying the commit");
			int replicated = trab.commit(nombreArchivo, datosArchivo,k);
			if(replicated > 0){
				res = "Se han hecho " + replicated + " replicas con exito";
			}else{
				res = "Error haciendo commit del archivo";
			}
		}catch(IOException e){
			e.printStackTrace();
		}

		return res;
	}
	
	/** 
	 * updateArchivos:
	 * 	Funcion encargada de bajar un archivo 
	 * 	del servidor.
	 * 
	 * @param 	Nombre del usuario a autenticar.
	 * @param	Clave del usuario a autenticar.
	 * @param	Nombre del archivo a ser bajado.
	 * @return	Devuelve null en caso de error y un arreglo de bytes con
	 * 			el contenido del archivo.
	 */
	public byte[] updateArchivos(String nombreArchivo)
	throws java.rmi.RemoteException {

		byte [] res = null;

		try{
			res = trab.update(nombreArchivo);
		}catch(IOException e){
			e.printStackTrace();
		}

		return res;

	}
	
}
