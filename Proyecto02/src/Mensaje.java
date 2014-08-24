import java.io.*;

public class Mensaje implements Serializable{

	private String comando;
	private Object[] atributos;

	public Mensaje(String c, Object... a){
		comando = c;
		atributos = a;
	}

	public Object getAttribute(int i){
		if(i < atributos.length){
			return atributos[i];
		}else{
			return null;
		}
	}

	public Object[] getAttributes(){
		return atributos;
	}

	public String getCommand(){
		return comando;
	}

}
