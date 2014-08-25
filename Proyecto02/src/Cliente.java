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

public class Cliente {
	
	/**
	 * Interfaz que permite la invocacion remota
	 *   los metodos del servidor.
	 **/
	private Servicios s;
	
	/** 
	 * Variables que almacenan informacion sobre
	 * 	el cliente. 
	 */
	private int puertoDNS;
	private InetAddress dirDNS;
	private DatagramSocket socket;

	private InetAddress dirServer;
	private int puertoServer;
	
	public Cliente(String n, int p, int dPort, InetAddress d){
		puertoDNS = dPort;
		dirDNS = d;
		
		try{
			socket = new DatagramSocket(p);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		getMainServer();
	}

	//Helper para obtener un servidor principal nuevo
	private void getMainServer(){

		String respuesta = "NOTHING";
	
		try{
			while(respuesta.equals("NOTHING")){
				Mensajeria.sendMessage(socket,dirDNS,puertoDNS,"WHO",0);
				DatagramPacket rec = Mensajeria.receivePacket(socket);
				Mensaje men = Mensajeria.decodePacket(rec);
				if(men.getCommand().equals("OK")){
					dirServer = (InetAddress) men.getAttribute(0);
					puertoServer = (int) men.getAttribute(1);
					respuesta = "OK";
				}
			}

         Thread.sleep(1000);
			s = (Servicios) Naming.lookup("rmi://" + dirServer.getHostAddress() + 
			":" + puertoServer + "/Servicios");

		}catch(IOException e){
			e.printStackTrace();
		}catch(NotBoundException e){
			e.printStackTrace();
		}catch(InterruptedException e){
			e.printStackTrace();
		}

	}


	
	
	/**
	 * Funcion principal del cliente.
	 * 
	 * @param Argumentos enviados.
	 * 
	 */
	public static void main(String[] args) {
	
		Cliente c = null;

		try{
			c = new
			Cliente("Name",3333,1111,InetAddress.getByName("localhost"));
		}catch(UnknownHostException e){
			e.printStackTrace();
		}

		String comando = " ";
		boolean recovering = false;
		while(true){
		 	if(!recovering) comando = System.console().readLine();
			recovering = false;
			try{
				c.ejecutarComando(comando);
			}catch(RemoteException e){
				c.sendFailed();
				c.getMainServer();
				recovering = true;
				continue;
			}
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
	public void mostrarArchivosLocales(){
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
	public void ejecutarComando(String comando) throws RemoteException{

		if(comando == null){
			System.out.println("No se introdujo ningún comando");
			return;
		}
		String[] full = comando.split(" ");
		
			if (full[0].equals("rls")) {
				
				String listaArchivos = s.listarArchivosEnServidor(null);
				
				System.out.println ( "\nArchivos en servidor remoto:\n" );
				System.out.println (listaArchivos);
								
			} else if (full[0].equals("lls")) {
				
				System.out.println ( "\nArchivos locales:\n" );
				mostrarArchivosLocales();

			} else if  (full[0].equals("info")){
				imprimirInfo();
				
			}  else if  (full[0].equals("salir")) {
				System.out.println ( "Hasta luego.\n" );
				System.exit(0);

			}else if(full.length != 2){
				System.out.println("Sintaxis incorrecta");
				return;
				
			} else if (full[0].equals("update")) {
						
					
							String nombreArchivo = full[1];
							byte[] datosArchivo = s.updateArchivos(nombreArchivo);
							
							if (datosArchivo != null) {
								if(Archivador.escribirArchivo(nombreArchivo,datosArchivo)){
									System.out.println("Archivo " + nombreArchivo + " bajado con exito.\n");
								}else{
									System.out.println("Error guardando el archivo localmente");
								}
							} else {
								System.out.println("El archivo "+ nombreArchivo + " puede que no exista en el servidor," +
										"\no hubo problemas en el servidor procesando el archivo.\n");
							}
							
					
					return;		
					
			}else if (full[0].equals("commit")) { 

				byte buffer[] = Archivador.devolverArchivo(full[1]);
				if(buffer == null){
					System.out.println("Error leyendo el archivo");
					return;
				}

				System.out.println(s.subirArchivo(full[1],buffer));	
						
			}else{			
				/* 
			 	* Mensaje que se imprime si se elige una opcion
			 	*	invalida
			 	*/
				System.out.println ( "Opcion invalida. Intente de nuevo.\n" );
				imprimirInfo();
			
			}
	} 		 

	private void imprimirInfo(){
		System.out.println (
			"Comandos disponibles:\n" +
			"rls\t\tMuestra la lista de archivos en el repositorio. \n" +
			"lls\t\tMuestra la lista de archivos locales.\n" +
			"commit archivo\tSube un archivo al servidor remoto " +
			"(Ej: commit clase.pdf).  \n"+
			"update archivo\tActualiza el repositorio local con la version del" +
			"archivo del cliente\n" +
			"info\t\tMuestra la lista de comandos que el cliente puede " +
			"usar.\n" +
			"salir\t\tTermina la ejecución del programa cliente.\n" +
			"\n" 
		);
	}

	private void sendFailed(){
		try{
			Mensajeria.sendMessage(socket,dirDNS,puertoDNS,"FAILED",0);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
