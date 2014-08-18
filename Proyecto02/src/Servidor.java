/**Clase Servidor: Clase que especifica a un servidor de control de versiones
*@param tipo: Tipo del servidor. Puede ser pasivo, activo o principal
*@param info: Informacion del servidor
**/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;

public class Servidor{

	private ServerInfo info;
	private Queue<ServerInfo> servers;

}
