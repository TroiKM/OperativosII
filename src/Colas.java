/**
* @author Karen Troiano		09-10855
* @author Luis Miranda		10-10463
* @author Jose Montenegro	10-10469
*
* Colas: Clase que representa al monitor de colas.
*
* @param timer: Tick que representa el timer del CPU
*/

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Colas
{
	
	Queue<Proceso> cola;
	
	/**
	* Constructor de Colas.
	*/
	public Colas() {
		cola = new LinkedList<Proceso>();
	}

    public synchronized Queue<Proceso> getQueue() {
    	return cola;
    }
	
    public synchronized void addElem(Proceso p){
	cola.add(p);
    } 
    
    public synchronized Proceso removeElem() {
		return cola.remove();
    }
    
    public synchronized Proceso peekElem(){
		return cola.peek(); //Nuevo
    }

    public synchronized boolean isEmpty(){
		return cola.isEmpty();
    }

    public synchronized int size(){
    		return cola.size();
    }

    public void parse(String file)
    {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document document = null;
			
		try{  
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.parse(file);
		} catch(ParserConfigurationException pce){
			pce.printStackTrace();
		} catch(SAXException se){
			se.printStackTrace();
		} catch(IOException ioe){
			ioe.printStackTrace();
		}

		Element docEle = document.getDocumentElement();

		NodeList nl = docEle.getElementsByTagName("Process");
		if(nl != null && nl.getLength() > 0 )
		{
			for(int i = 0 ; i < nl.getLength(); i++)
			{
				Element el = (Element)nl.item(i);
				NodeList nl2 = el.getElementsByTagName("Priority");
				String type;
				Element el2;
				int priority=0;
				int time=0;
				int cputime=0;
				
				if(nl2 != null && nl.getLength() > 0)
				{
					el2 = (Element)nl2.item(0);
					type = el2.getFirstChild().getNodeValue();
					priority = Integer.parseInt(type);
				}
				
				nl2 = el.getElementsByTagName("ArrivalTime");
				if(nl2 != null && nl.getLength() > 0)
				{
					el2 = (Element)nl2.item(0);
					type = el2.getFirstChild().getNodeValue();
					time = Integer.parseInt(type);					
				}
				
				Proceso process = new Proceso(priority,time);
				
				nl2 = el.getElementsByTagName("UseTime");
				if(nl2 != null && nl2.getLength() > 0)
				{
					Element el3;
					for(int j = 0; j < nl2.getLength(); j++)
					{
						el3 = (Element)nl2.item(j);
						type = el3.getFirstChild().getNodeValue();
						cputime = Integer.parseInt(type);
						process.insertUse(cputime);
					}
				}
				
				this.addElem(process);
			}
		}
    }
    
}
