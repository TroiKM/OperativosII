import java.io.*;

public class Mensaje implements Serializable{

	private String comando;
	private int tiempo;
	private Object[] atributos;

	public Mensaje(String c, int t, Object... a){
		comando = c;
		atributos = a;
		tiempo = t;
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

	public int getTime(){
		return tiempo;
	}

}
