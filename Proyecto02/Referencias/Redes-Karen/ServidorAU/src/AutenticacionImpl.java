/**
* @author Karen Troiano		09-10855
* @author Yeiker Vazquez	09-10882
* @grupo  15
*
* Archivo: AutenticacionImpl.java
*
* Descripcion: Implementacion de la interfaz para el programa 
* principal del servidor de autenticacion (Autenticacion.java).
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AutenticacionImpl 
        extends        java.rmi.server.UnicastRemoteObject 
        implements Autenticacion {
        
        /*
         * Version de la implementacion de la interfaz Autenticacion
         */
        private static final long serialVersionUID = 1L;
        
        /*
         * Variable que contiene el nombre del archivo que posee
         * 	los nombres y claves de los clientes permitidos en el
         * 	servidor de archivos. 
         */
        public static String archivoUsu = "";
        
        /*
         * AutenticacionImpl:
         * 	Constructor de la clase.
         * 	Se encarga de asignar a la variable el nombre del 
         * 	archivo.
         * 
         * @param 	Es el nombre del archivo que contiene los nombres
         * 			y claves de los usuarios con acceso al servidor.
         * @throws 	java.rmi.RemoteException.
         */
        public AutenticacionImpl(String archivo)
        throws java.rmi.RemoteException {
                super();
                archivoUsu = archivo;
        }

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
        throws java.rmi.RemoteException {
        	
        		/*
        		 * Variables necesarias para hacer la lectura del
        		 * 	archivo.
        		 */
                BufferedReader usuarios = null;
                String[] datosUsuario = { "" , ""};
                String linea = "";
                String nombreLinea = "";
                String claveLinea = "";
                
                try {
                	
                		/*
                		 * Se abre el archivo y se recorre linea por
                		 * 	linea buscando los nombres y las claves.
                		 */
                		
                        usuarios = new BufferedReader(new FileReader(new File(archivoUsu)));
                        linea = usuarios.readLine();
                        while ( linea != null) {
                                datosUsuario = linea.split(":");
                                nombreLinea = datosUsuario[0];
                                claveLinea = datosUsuario[1];
                                
                                /*
                                 * Caso en el que coincide el nombre
                                 * 	y la clave.
                                 */
                                if (nombre.equals(nombreLinea) && clave.equals(claveLinea)) {
                                		usuarios.close();
                                        return true;
                                }
                                linea = usuarios.readLine();
                        }
                        usuarios.close();
                      
                        
                        
                }
                
                /*
                 * Excepcion que protege al programa de que no
                 * 	encuentre el archivo solicitado.
                 */
                catch (FileNotFoundException e) {
                        System.out.println("Archivo no encontrado.");
                        System.exit(0);
                } 
                
                /*
                 * Excepcion que protege al programa de que haya
                 * 	algun problema leyendo el archivo.
                 */
                catch (IOException e) {
                        e.printStackTrace();
                }
                
                /*
                 * Caso en el que el nombre y clave del usuario
                 * no coincide con ninguno de los registrados
                 * en el archivo.
                 */
                return false;
        }

}