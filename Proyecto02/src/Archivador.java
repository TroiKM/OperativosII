import java.io.*;

class Archivador{

	public static boolean escribirArchivo(String nombreArchivo, byte[] archivo){

		try{
			File arch = new File(nombreArchivo);
			BufferedOutputStream salida = new BufferedOutputStream(new
			FileOutputStream(arch.getName()));

			salida.write(archivo,0,archivo.length);
			salida.flush();
			salida.close();
		}catch(IOException e){
			return false;
		}

		return true;

	}

	public static byte[] devolverArchivo(String nombreArchivo){
		
		try{
			File archivo = new File(nombreArchivo);
			byte buffer[] = new byte[(int) archivo.length()];

			BufferedInputStream entrada = new BufferedInputStream(new
			FileInputStream(nombreArchivo));

			entrada.read(buffer,0,buffer.length);
			entrada.close();

			return buffer;
		}catch(IOException e){
			return null;
		}
	}

}
