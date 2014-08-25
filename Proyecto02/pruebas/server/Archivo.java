/** Clase Archivo: Clase que especifica un archivo en conjunto con su version
 *@param version: Entero que representa la version actual del archivo
 *@param archivo: Apuntador a la version local del archivo
**/

import java.io.File;
import java.io.FileNotFoundException;

public class Archivo{

	private int version;
	private String nombre;

	/**Constructor de Archivo
	*@param f: Apuntador a copia local
	**/
	public Archivo(String f){
		version = 1;
		nombre = f;
	}

	/**getVersion: Retorna la version del archivo
	*@return La version del archivo
	**/
	public int getVersion(){
		return version;
	}

	/**setVersion: Setea la version del archivo
	*@param v: Numero de version a setear
	**/
	public void setVersion(int v){
		version = v;
	}

}
