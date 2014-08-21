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
import java.net.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

public class Cliente
{

	private static final int BUFFER_SIZE = 256;
	
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
	public static String respuesta = "NOTHING";
	private int puertoDNS;
	private String dirDNS;
	private DatagramSocket socket;
	
	public Cliente(String n, int gPort, String g, int dPort, String d){
		puertoDNS = dPort;
		dirDNS = d;
		
		try{
			socket = new DatagramSocket(gPort);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		while (respuesta == "NOTHING") {
			byte[] buf = new byte[BUFFER_SIZE];
			String i = "WHO";
			buf = i.getBytes();
			try{
				DatagramPacket init = new
				DatagramPacket(buf,buf.length,InetAddress.getByName(dirDNS),puertoDNS);
				socket.send(init);
				
				buf = new byte[BUFFER_SIZE];
				DatagramPacket rec = new DatagramPacket(buf,buf.length);
				socket.receive(rec);
				i = new String(rec.getData(),0,rec.getLength());
				respuesta = i;
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Funcion principal del cliente.
	 * 
	 * @param Argumentos enviados.
	 * 
	 */
	public static void main(String[] args) {
		
		Cliente c = new Cliente("Name",2222,"224.0.0.1",1111,"localhost");
		System.out.println("puerto: " + respuesta);
		try {
			
			/** 
			 * Variables que almacenan informacion sobre
			 * 	el cliente una vez invocado el programa.
			 */
			String servidor = respuesta;
			String puerto = "5555";
			String comando;
			salir = false;
			
			s = (Servicios)
					Naming.lookup("rmi://" + servidor + ":" + puerto + "/Servicios");
			
			System.out.println("Conect");
			
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
				
			} else if  (comando.equals("update")) {
						
					try {
							String nombreArchivo = "Consejo.pnj-large"; 
							byte[] datosArchivo = s.updateArchivos(nombreArchivo);
							
							if (datosArchivo != null) {
								File archivo = new File(nombreArchivo);
								BufferedOutputStream salida = new
								BufferedOutputStream(new FileOutputStream(archivo.getName()));
								
								salida.write(datosArchivo,0,datosArchivo.length);
								salida.flush();
								salida.close();
								System.out.println("Archivo " + nombreArchivo + " bajado con exito.\n");
							} else {
								System.out.println("El archivo "+ nombreArchivo + " puede que no e	xista en el servidor," +
										"\no hubo problemas en el servidor procesando el archivo.\n");
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
				 * 
				*/
				String[] comandosCompuestos = comando.split(" ", 2);
				
				if (comandosCompuestos.length == 2) {
					
					String nombreArchivo = comandosCompuestos[1]; 
					
					if (comandosCompuestos[0].equals("commit")) { 
						File archivo = new File(nombreArchivo);
						byte buffer[] = new byte[(int)archivo.length()];
						
						try {
							BufferedInputStream entrada = new 
							BufferedInputStream(new FileInputStream(nombreArchivo));	
							
							entrada.read(buffer,0,buffer.length);
							String subidaArchivo = s.subirArchivo(nombreArchivo, buffer);
							entrada.close();
							
							if ((subidaArchivo.equals("false"))) { 				
								System.out.println("Error de autenticacion.");
								System.exit(0);
							} else {
								System.out.println(subidaArchivo);
							}
						/*
							* Excepcion que controla la existencia del archivo
							* 	buscado.  
						*/	
						} catch(FileNotFoundException e){
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
					"update \tActualiza el repositorio local" +
						"\n" +
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