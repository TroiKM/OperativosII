/**
* @author Karen Troiano		09-10855
* @author Yeiker Vazquez	09-10882
* @grupo  15
*
* Archivo: a_rmifs.java
*
* Descripcion: Contiene el programa principal del
* servidor de autenticacion.
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class a_rmifs {
        
		/*
		 * Interfaz que permite brindar los servicios de
		 * 	autenticacion.
		 */
        public static Autenticacion a;
        
        /*
         * Puerto mediante el cual se conectaran los clientes
         * del servicio de autenticacion (servidor de archivos).
         */
        public static String puerto = "";
        
        /*
         * Nombre del archivo que contiene los nombres y claves
         * 	de los usuarios permitidos en el sistema de archivos.
         */
        public static String archivoUsu = "";

        /*
         * a_rmifs:
         * 	Constructor de la clase.
         * 	Se encarga de establecer el puerto y nombre del servicio mediante
         * 	el cual los clientes (en este caso el servidor de archivos) se
         * 	conectaran.
         *  
         */
        public a_rmifs() {
                try {
                a = new AutenticacionImpl(archivoUsu);
                        
                        LocateRegistry.createRegistry(Integer.parseInt(puerto));
                        
                        Naming.rebind("rmi://localhost:" + puerto+ "/Autenticacion", a);
                }
                catch (Exception e) {
                        System.out.println ("Trouble: " + e);
                }
        }
        
        
        /*
         * main:
         * 	Funcion principal de a_rmifs
         * @param 	argumentos enviados en la invocacion del 
         * 			programa.
         */
        public static void main(String args[]) {
                
                int argv = args.length;
                // Verificacion de que no se repite algun parametro.
                Boolean[] opciones = { false, false };
                
                /*
                 * Caso en el que la sintaxis de invocacion fue
                 * incorrecta.
                 */
                if (argv != 4){
                        System.out.println ("Sintaxis de invocacion incorrecta.");
                        System.out.println("\nSintaxis de invocacion: ");
                        System.out.println ("java a_rmifs -f usuarios -p puerto");
                        return;
                } else {
                	
                		/*
                		 * Se analiza con un ciclo los parametros
                		 * pasados en la invocacion.
                		 */
                        for(int j = 0; j < argv; j = j + 2){

                                if(args[j].equals("-p") && !opciones[0]){
                                        puerto = args[j+1];
                                        opciones[0] = true;
                                        
                                } else if(args[j].equals("-f") && !opciones[1]) {
                                	try{
                                		BufferedReader prueba = 
                                			new BufferedReader(
                                				new FileReader(
                                					new File(
                                						args[j+1]
                                							)
                                					)
                                				);
                                        archivoUsu = args[j+1];
                                        opciones[1] = true;
                                	} 
                                	
                                	/*
                                     * Excepcion que protege al programa de que no
                                     * 	encuentre el archivo solicitado.
                                     */
                                	catch (FileNotFoundException e){
                                		System.out.println (
                                			"El archivo " + args[j+1]
                                			+ " no se encuentra en el directorio actual.");
            							System.exit(0);
                                	}
                                } 
                                
                                /*
                                 * Caso en el que una misma opcion se haya repetido
                                 * 	en la invocacion.
                                 */
                                else {
                                        System.out.println ("Sintaxis de invocacion incorrecta.");
                                        System.out.println("\nSintaxis de invocacion: ");
                                        System.out.println ("java a_rmifs -f usuarios -p puerto");
                                        System.exit(0);
                                }
                        }
                }
                
                /*
                 * Se llama al constructor de la clase que utilizara
                 * 	los parametros de la invocacion si la sintaxis
                 * 	fue correcta.
                 */
                new a_rmifs();
                
        }
        
        
}