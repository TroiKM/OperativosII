import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser
{    
    public static void main(String args[])
	{

	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    Document document = null;
	    
	    try{
		  
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    document = db.parse("bash/process_request_file.xml");
	    }catch(ParserConfigurationException pce){
	     	pce.printStackTrace();
	    }catch(SAXException se){
	    	se.printStackTrace();
	    }catch(IOException ioe){
	    	ioe.printStackTrace();
	    }

	    Element docEle = document.getDocumentElement();

	    NodeList nl = docEle.getElementsByTagName("Process");
	    if(nl != null && nl.getLength() > 0 ){
		for(int i = 0 ; i < nl.getLength(); i++){
		    Element el = (Element)nl.item(i);

		    NodeList nl2 = el.getElementsByTagName("Priority");
		    String type;
		    Element el2;
		    int priority=0;
		    int time=0;
		    int cputime=0;
		    		    		    
		    if(nl2 != null && nl.getLength() > 0){
			el2 = (Element)nl2.item(0);
			type = el2.getFirstChild().getNodeValue();
			
			priority = Integer.parseInt(type);
			
		    }

		    nl2 = el.getElementsByTagName("ArrivalTime");
		    if(nl2 != null && nl.getLength() > 0){
			el2 = (Element)nl2.item(0);
			type = el2.getFirstChild().getNodeValue();
			
			time = Integer.parseInt(type);					
		    }

		    Proceso process = new Proceso(priority,time);

		    nl2 = el.getElementsByTagName("UseTime");
		    if(nl2 != null && nl2.getLength() > 0){
			Element el3;
			for(int j = 0; j < nl2.getLength(); j++){
			    el3 = (Element)nl2.item(j);
			    type = el3.getFirstChild().getNodeValue();
			    
			    cputime = Integer.parseInt(type);
			    
			    process.insertUse(cputime);
			    
			}
			
		    }
		    
		    System.out.println(process);		  		    
		}
	    }
	    
	}
}
