/**
* @author Karen Troiano		09-10855
* @author Luis Miranda		10-10463
* @author Jose Montenegro	10-10469
*
* Clase Cliente: Contiene el programa principal d
* 	el cliente del sistema.
*/

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

public class Cliente
{
	
	/**
	 * Interfaz que permite la invocacion remota
	 *   los metodos del servidor.
	 **/
	public static Servicios s;
	/** 
	 * Variables que almacenan informacion sobre
	 * 	el cliente. 
	 */
	public static Boolean salir;
	public static String nombre;
	public static String clave;

	/**
	 * Funcion principal del cliente.
	 * 
	 * @param Argumentos enviados.
	 * 
	 */
	public static void main(String[] args) {
		try {
			
			/** 
			 * Variables que almacenan informacion sobre
			 * 	el cliente una vez invocado el programa.
			 */
			String servidor = "";
			String puerto = "";
			String [] datosUsuario;
			/** 
			 * Variable que verifica de que no se
			 *  repita algun parametro en la invocacion
			 *  del programa cliente.
			 */
			Boolean[] opciones = { false, false };
			
			int argv = args.length;
			String comando;
			salir = false;
			
			if (argv != 2){
				System.out.println ("java cliente -m servidor -p puerto"); 
				System.exit(0);
			} else {
				for(int j = 0; j < argv; j = j + 2){
					if(args[j].equals("-p") && !opciones[1]){
						puerto = args[j+1];
						opciones[0] = true;
						
					}else if(args[j].equals("-m") && !opciones[2]){
						servidor = args[j+1];
						opciones[1] = true;
						
					} else {
						System.out.println ("java cliente -m servidor -p puerto"); 
						System.exit(0);
					}
				}
			}
			
			if (!(opciones[0] && opciones[1])) {
				System.out.println ("java cliente -m servidor -p puerto"); 
				System.exit(0);
			} 
			
			s = (Servicios)
					Naming.lookup("rmi://" + servidor + ":" + puerto + "/Servicios");
		
			System.out.print ("Introduzca su nombre de usuario: ");
			nombre = System.console().readLine();
			
			clave = new String(System.console().readPassword("\nIntroduzca su clave: "));
			
			while(!salir){
				comando = System.console().readLine();
				ejecutarComando(comando);
			}
			
		} 
		
		catch (MalformedURLException murle) {
			System.out.println ();
			System.out.println (
			"MalformedURLException");
			System.out.println ( murle ); 
		}

		catch (NotBoundException nbe) {
			System.out.println ();
			System.out.println ("NotBoundException");
			System.out.println (nbe);
		}
		
		
		catch (IOException e) {
			System.out.println ("Fallas conectando con el servidor.");
			System.exit(0);
		}
}
	
	/**
	 * mostrarArchivosLocales:
	 * 	Funcion encargada de buscar si un archivo 
	 * 	pertenece a la lista de archivos locales 
	 * 	del cliente.
	 * 
	 * A su vez se encarga de mostrar 
	 * 	la lista de archivos locales al cliente. 
	 * 
	 * @param	Nombre del archivo a ser buscado.
	 * @return	Devuelve true si el nombre del archivo
	 * 			buscado se encuentra en el directorio
	 * 			local, false en caso contrario.
	 * 
	 */		
	public static void mostrarArchivosLocales(){
		String path = "."; 
		
		String files;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles(); 
 
		for (int i = 0; i < listOfFiles.length; i++){
			/*
			 * Se ignoran los archivos relacionados con el proyecto. 
			 */
			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();
				/* 
				 * Si el archivo es nulo entonces se lista al cliente
				 * 	de archivos locales.
				 */
				if	( !(files.equals("Servicios.java")
						|| files.equals("Servicios.class")
						|| files.equals("Cliente.java")
						|| files.equals("Cliente.class")
						)
					)
				{
					System.out.println("\t" + files);	
				}
			}
		}
		
		return;
}
	
	/**
	 * ejecutarComando:
	 * 	Funcion encargada de recibir un comando por archivo o terminal,
	 * 	filtrar el mensaje para verificar su correctitud y envia al
	 * 	servidor dicho mensaje para su debida respuesta. 
	 * 
	 * @param	El comando del cliente.
	 * @trows	RemoteException.
	 * 
	 */
	public static void ejecutarComando(String comando){
		try{
			if (comando.equals("rls")) {
				
				String listaArchivos = s.listarArchivosEnServidor(null);
				
				System.out.println ( "\nArchivos en servidor remoto:\n" );
				System.out.println (listaArchivos);
								
			} else if (comando.equals("lls")) {
				
				System.out.println ( "\nArchivos locales:\n" );
				mostrarArchivosLocales();		
				
			} else if  (comandosCompuestos[0].equals("update")) {
						
					try {
						
							byte[] datosArchivo = s.updateArchivos(nombre, clave);
							
							if (datosArchivo != null) {
								File archivo = new File(nombreArchivo);
								BufferedOutputStream salida = new
								BufferedOutputStream(new FileOutputStream(archivo.getName()));
								
								salida.write(datosArchivo,0,datosArchivo.length);
								salida.flush();
								salida.close();
								System.out.println("Archivo " + nombreArchivo + " bajado con exito.\n");
							} else {
								if (!s.iniciarSesion(nombre, clave, 1)) { 
									System.out.println("Error de autenticacion.");
									System.exit(0);
								} else {
									System.out.println("El archivo "+ nombreArchivo + " puede que no exista en el servidor," +
										"\no hubo problemas en el servidor procesando el archivo.\n");
								}
							}
							
					} catch (Exception e) {
							System.err.println("FileServer exception: "+ e.getMessage());
							e.printStackTrace();
					}
					return;
				
				} else if  (comando.equals("info")){
				
					System.out.println 
					( 	"Comandos disponibles:\n" +
						"rls\t\tMuestra la lista de archivos en el repositorio. \n" +
						"lls\t\tMuestra la lista de archivos locales.\n" +
						"commit archivo\tSube un archivo al servidor remoto " +
							"(Ej: commit clase.pdf).  \n"+
						"commit all\tActualiza el repositorio con los archivos locales. \n" +
						"update \tActualiza el repositorio local\n" +
						"rmv archivo\tBorra el archivo en el repositorio.\n" +
						"info\t\tMuestra la lista de comandos que el cliente puede " +
							"usar.\n" +
						"salir\t\tTermina la ejecución del programa cliente.\n" +
						"\n" );
				
			}  else if  (comando.equals("salir")) {
				System.out.println ( "Hasta luego.\n" );
				System.exit(0);
			} else { 
				
				/*
				 * Else para los comandoscon mas de un 
				 * 	parametro. En caso de no ser ninguna
				 *  guardia imprime un error y la informacion
				 *  con los comandos disponibles.
				 * */
				
				String[] comandosCompuestos = comando.split(" ", 2);
				
				if (comandosCompuestos.length == 2) {
					
					String nombreArchivo = comandosCompuestos[1]; 
					
					if (comandosCompuestos[0].equals("commit")) { 
						
						if (nombreArchivo.equals("all")) {
							
						} else {
							File archivo = new File(nombreArchivo);
							byte buffer[] = new byte[(int)archivo.length()];
							
							try {
								BufferedInputStream entrada = new 
								BufferedInputStream(new FileInputStream(nombreArchivo));	
								
								entrada.read(buffer,0,buffer.length);
								String subidaArchivo = s.subirArchivo(nombre, clave, nombreArchivo, buffer);
								entrada.close();
								
								if ((subidaArchivo.equals("false"))) { 				
									System.out.println("Error de autenticacion.");
									System.exit(0);
								} else {
									System.out.println(subidaArchivo);
								}
							}
							/*
								* Excepcion que controla la existencia del archivo
								* 	buscado.  
								*/
							catch(FileNotFoundException e){
								System.out.println("El archivo " +nombreArchivo + " no se encuentra en el directorio actual.\n");
							} 
							/*
								* Excepcion que controla la lectura del 
								* 	archivo.
								*/
							catch (IOException e) {
								System.out.println("Problemas procesando el archivo " + nombreArchivo + ".\n");
							} 
						}
						
						return;
						
					} else if  (comandosCompuestos[0].equals("rmv")){
						
						String borradoArchivo = s.borrarArchivo(nombre, clave, nombreArchivo);
						System.out.println (borradoArchivo);
						return;
					}
				}
				
				/* 
				 * Mensaje que se imprime si se elige una opcion
				 *	invalida
				 */
				
				System.out.println ( "Opcion invalida. Intente de nuevo.\n" );
				
				System.out.println 
				( 	"Comandos disponibles:\n" +
					"rls\t\tMuestra la lista de archivos en el repositorio. \n" +
					"lls\t\tMuestra la lista de archivos locales.\n" +
					"commit archivo\tSube un archivo al servidor remoto " +
						"(Ej: commit clase.pdf).  \n"+
					"commit all\tActualiza el repositorio con los archivos locales. \n" +
					"update \tActualiza el repositorio" +
						"\n" +
					"rmv archivo\tBorra el archivo en el repositorio.\n" +
					"info\t\tMuestra la lista de comandos que el cliente puede " +
						"usar.\n" +
					"salir\t\tTermina la ejecución del programa cliente.\n" +
					"\n" );
			
			}
			
		} 
		/*
		 * Excepcion que controla la conexion con 
		 * 	el servidor de archivos.  
		 */
		catch (RemoteException re) {
			System.out.println ();
			System.out.println ("Problemas de conexion con el servidor.");
			System.exit(0);
		}
	}
}