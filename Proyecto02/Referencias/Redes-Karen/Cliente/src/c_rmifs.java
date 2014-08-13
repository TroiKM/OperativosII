/**
* @author Karen Troiano		09-10855
* @author Yeiker Vazquez	09-10882
* @grupo  15
*
* Archivo: c_rmifs.java
*
* Descripcion: Contiene el programa principal del
* cliente del sistema.
*/

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

public class c_rmifs {
	
	/**
	 * Variables globales.
	 */
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
	 * Variables que contienen informacion sobre
	 *  los archivos del cliente. 
	 */
	public static Boolean hayArchivoCom = false;
	public static Boolean hayArchivoUsu = false;
	public static String archivoCom = "";
	public static String archivoUsu = "";
	/**
	 * Fin de las variables globales.
	 */
	
	/**
	 * Funcion principal del c_rmifs.
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
			Boolean[] opciones = { false, false, false, false };
			
			int argv = args.length;
			String comando;
			salir = false;
			BufferedReader comandos = null;
			BufferedReader usuarios = null;
			
			
			if (argv % 2 != 0){
				System.out.println ("Sintaxis de invocacion incorrecta.");
				System.out.println("\nSintaxis de invocacion: ");
				System.out.println ("java c_rmifs [-f usuarios] -m servidor -p puerto [-c comandos]"); 
				System.exit(0);
			} else {
				for(int j = 0; j < argv; j = j + 2){

					if(args[j].equals("-f") && !opciones[0]){
						hayArchivoUsu = true;
						archivoUsu = args[j+1];
						try {
							usuarios = new BufferedReader(new FileReader(new File(archivoUsu)));
						} catch (FileNotFoundException e) {
							System.out.println ("El archivo " + archivoUsu + " no se encuentra en el directorio actual.");
							System.exit(0);
						} 
						opciones[0] = true;
						
					}else if(args[j].equals("-p") && !opciones[1]){
						puerto = args[j+1];
						opciones[1] = true;
						
					}else if(args[j].equals("-m") && !opciones[2]){
						servidor = args[j+1];
						opciones[2] = true;
						
					}else if(args[j].equals("-c") && !opciones[3]){						
						hayArchivoCom = true;
						archivoCom = args[j+1];
						try {
							comandos = new BufferedReader(new FileReader(new File(archivoCom)));
						}catch (FileNotFoundException e) {
							System.out.println ("El archivo " + archivoCom + " no se encuentra en el directorio actual.");
							System.exit(0);
						} 
						opciones[3] = true;
						
					} else {
						System.out.println ("Sintaxis de invocacion incorrecta.");
						System.out.println("\nSintaxis de invocacion: ");
						System.out.println ("java c_rmifs [-f usuarios] -m servidor -p puerto [-c comandos]");
						System.exit(0);
					}
				}
			}
			
			if (!(opciones[1] && opciones[2])) {
				System.out.println ("Sintaxis de invocacion incorrecta.");
				System.out.println("\nSintaxis de invocacion: ");
				System.out.println ("java c_rmifs [-f usuarios] -m servidor -p puerto [-c comandos]");
				System.exit(0);
			} 
			
			s = (Servicios)
					Naming.lookup("rmi://" + servidor + ":" + puerto + "/Servicios");
			
			if(hayArchivoUsu){
				datosUsuario = (usuarios.readLine()).split(":");
				nombre = datosUsuario[0];
				clave = datosUsuario[1];
				usuarios.close();
			}else{
				System.out.print ("Introduzca su nombre de usuario: ");
				nombre = System.console().readLine();
				
				clave = new String(System.console().readPassword("\nIntroduzca su clave: "));
			}

			if (s.iniciarSesion(nombre,clave, 0)){
				System.out.println("Bienvenido " + nombre + ". Usted esta conectado al servidor.\n");
			} else {
				System.out.println("Error de autenticacion.");
				System.exit(0);
			}
			
			if (hayArchivoCom){
				while( ((comando = comandos.readLine()) != null) && !salir ){
					ejecutarComando(comando);
				}
				comandos.close();
			}
			
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
	public static Boolean mostrarArchivosLocales(String nombreArchivo){
		String path = "."; 
		
		String files;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles(); 
 
		for (int i = 0; i < listOfFiles.length; i++){
 
			/*
			 * Se ignoran los archivos relacionados con el proyecto. 
			 */
			if (	listOfFiles[i].isFile()){
				files = listOfFiles[i].getName();
				/* 
				 * Si el archivo es nulo entonces se lista al cliente
				 * 	de archivos locales.
				 */
				if (nombreArchivo == null){ 
					if	( !(files.equals("Servicios.java")
							|| files.equals("Servicios.class")
							|| files.equals("c_rmifs.java")
							|| files.equals("c_rmifs.class")
							)
						){
						
						
						if(hayArchivoUsu && hayArchivoCom ){
							if(!(files.equals(archivoUsu) || files.equals(archivoCom)) ){
								System.out.println("\t" + files);
							}
							
						} else if(!hayArchivoUsu && hayArchivoCom ){
							if(!files.equals(archivoCom) ){
								System.out.println("\t" + files);
							}
							
						} else if(hayArchivoUsu && !hayArchivoCom ){
							if(!files.equals(archivoUsu)){
								System.out.println("\t" + files);
							}
						} else if(!hayArchivoUsu && !hayArchivoCom ){
							System.out.println("\t" + files);
						}				
						
					}
				} else {
					/* 
					 * Si el archivo no es nulo entonces se 
					 * busca si existe en la lista 	de archivos locales
					 * del cliente.  
					 */
					if (files.equals(nombreArchivo)){
						return true;
					}
				}
			}
		}
		return false;
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
				
				String listaArchivos = s.listarArchivosEnServidor(nombre, clave, null);
				if (!(listaArchivos.equals("false"))) {
					System.out.println ( "\nArchivos en servidor remoto:\n" );
					System.out.println (listaArchivos);
				} else {
					System.out.println("Error de autenticacion.");
					System.exit(0);
				}
				
			} else if (comando.equals("lls")) {
				
				if (s.mostrarArchivosLocales(nombre, clave)){
					System.out.println ( "\nArchivos locales:\n" );
					mostrarArchivosLocales(null);
				} else {
					System.out.println("Error de autenticacion.");
					System.exit(0);
				}			
				
			} else if  (comando.equals("info")){
				
				if (s.mostrarInformacion(nombre, clave)){
					System.out.println 
					( 	"Comandos disponibles:\n" +
						"rls\t\tMuestra la lista de archivos disponibles en servidor " +
							"\n\t\tcentralizado.\n" +
						"lls\t\tMuestra la lista de archivos disponibles localmente.\n" +
						"sub archivo\tSube un archivo al servidor remoto " +
							"(Ej: sub clase.pdf). El\n\t\tarchivo especificado como " +
							"parámetro debe estar en la lista\n\t\tde archivos " +
							"disponibles para el cliente localmente.\n" +
						"baj archivo\tBaja un archivo desde el servidor remoto" +
							"\n\t\t(Ej: baj ejemplo.c). El archivo especificado debe " +
							"estar en\n\t\tla lista de archivos disponibles en el servidor " +
							"\n\t\tcentralizado para que el comando funcione " +
							"adecuadamente.\n" +
						"bor archivo\tBorra el archivo en el servidor remoto.\n" +
						"info\t\tMuestra la lista de comandos que el cliente puede " +
							"usar con\n\t\tuna breve descripción de cada uno de ellos.\n" +
						"sal\t\tTermina la ejecución del programa cliente.\n" +
						"\n" );
					
				} else {
					System.out.println("Error de autenticacion.");
					System.exit(0);
				}	
				
			}  else if  (comando.equals("sal")) {
				if (s.cerrarSesion(nombre,clave)){
					System.out.println("Cerrando sesion.");
				} else {
					System.out.println("Error de autenticacion.");
					System.exit(0);
				}
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
					
					if (comandosCompuestos[0].equals("sub")) { 
						
						if (!s.listarArchivosEnServidor(nombre, clave, nombreArchivo).equals("no")) {
							System.out.println("El archivo " + nombreArchivo + " ya existe en el servidor." +
									"\n¿Desea sobreescribirlo?(si/no)");
							if (!System.console().readLine().equals("si")) {
								System.out.println("No se ha subido el archivo " + nombreArchivo + ".");
								return;
							}
						} 
							
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
							
						return;
						
					} else if  (comandosCompuestos[0].equals("baj")){
						
						try {
							
							if (mostrarArchivosLocales(nombreArchivo)) { 
								/* 
								 * Si el archivo ya existe se pregunta por sobreescribirlo.
								 */
								System.out.println("El archivo " + nombreArchivo + " ya existe en el directorio actual." +
										"\n¿Desea sobreescribirlo?(si/no)");
								if (!System.console().readLine().equals("si")) {
									System.out.println("No se ha bajado el archivo " + nombreArchivo + ".\n	");
									return;
								}	
							}
							
							 byte[] datosArchivo = s.bajarArchivo(nombre, clave, nombreArchivo);
					         
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
					         
					    } catch(Exception e) {
					         System.err.println("FileServer exception: "+ e.getMessage());
					         e.printStackTrace();
					    }
						return;
						
					} else if  (comandosCompuestos[0].equals("bor")){
						
						String borradoArchivo = s.borrarArchivo(nombre, clave, nombreArchivo);
						if (!(borradoArchivo.equals("false"))) {
							System.out.println (borradoArchivo);
						} else {
							System.out.println("Error de autenticacion.");
							System.exit(0);
						}
						
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
					"rls\t\tMuestra la lista de archivos disponibles en servidor " +
						"\n\t\tcentralizado.\n" +
					"lls\t\tMuestra la lista de archivos disponibles localmente.\n" +
					"sub archivo\tSube un archivo al servidor remoto " +
						"(Ej: sub clase.pdf). El\n\t\tarchivo especificado como " +
						"parámetro debe estar en la lista\n\t\tde archivos " +
						"disponibles para el cliente localmente.\n" +
					"baj archivo\tBaja un archivo desde el servidor remoto" +
						"\n\t\t(Ej: baj ejemplo.c). El archivo especificado debe " +
						"estar en\n\t\tla lista de archivos disponibles en el servidor " +
						"\n\t\tcentralizado para que el comando funcione " +
						"adecuadamente.\n" +
					"bor archivo\tBorra el archivo en el servidor remoto.\n" +
					"info\t\tMuestra la lista de comandos que el cliente puede " +
						"usar con\n\t\tuna breve descripción de cada uno de ellos.\n" +
					"sal\t\tTermina la ejecución del programa cliente.\n" +
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