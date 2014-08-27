import java.net.*;
import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.LinkedList;
import java.util.Queue;

public class Trabajador implements Runnable{

        private Colas<DatagramPacket> mensajes;
        private MulticastSocket socket;
		  private MulticastSocket commandSocket;
    
        private InetAddress group;
        private int puertoDNS;
        private InetAddress dirDNS;
        private int time;

        private Servicios serv;
        private int puerto;
        private ServerInfo info;
    private Colas<ServerInfo> servers;
	     private int k;
    

    public Trabajador(Colas<DatagramPacket> c,MulticastSocket s, int p,
	 ServerInfo i,Colas<ServerInfo> ser, MulticastSocket com,int kes){
                mensajes = c;
                socket = s;
                time = 0;
                puerto = p;
                info = i;
                servers = ser;
					 commandSocket = com;
					 k = kes;
        }

        public void run(){
        
                while(true){
                
                        Mensaje men = null;
                        time++;
                        DatagramPacket wrd = this.mensajes.removeElem();
                        
                        if(wrd!=null){
                
                                try{
                                        men = Mensajeria.decodePacket(wrd);
                                        time = Math.max(men.getTime(),time);
                                        time++;
                                        executeCommand(men,wrd.getAddress(),wrd.getPort());
                                }catch(IOException e){
                                        e.printStackTrace();
                                }
                        }
                }
        }

        private void executeCommand(Mensaje men, InetAddress addr, int port)
		  throws IOException{
                String com = men.getCommand();

                if(com.equals("COORD")){
                        try{
                                serv = new
										  ServiciosImpl("localhost",String.valueOf(puerto),
										  this, k);
                                LocateRegistry.createRegistry(puerto);
                                Naming.rebind("rmi://localhost:" + puerto + "/Servicios",serv);
//                              this.servers.addElem(this.info);

                        }catch(RemoteException e){
                                e.printStackTrace();
                        }catch(MalformedURLException e){
                                e.printStackTrace();
                        }
                        System.out.println("Ahora soy el cordi");
                }else if ( com.equals("OK") ) {
                        try{
                                Mensajeria.sendMessage(this.socket,(InetAddress)men.getAttribute(0),(int)men.getAttribute(1),"SERVER",time, this.info );
                        }catch(IOException e){
                                e.printStackTrace();
                        }
                }else if (com.equals("SERVER")) {
                    ServerInfo i = (ServerInfo) men.getAttribute(0);
                    Queue<ServerInfo> dummy = new LinkedList<ServerInfo>(this.servers.getQueue());
                    dummy.add(this.info);

                    try{
                        Mensajeria.sendMessage(this.socket,i.getIP(),i.getPuerto(),"NEWSERVER",time, dummy);
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    
                    dummy.add(i);
                    Mensajeria.broadcast(this.socket,this.servers.getQueue(),"NEWSERVER",time, dummy);
                    this.servers.addElem(i);

                } else if (com.equals("NEWSERVER")){
                    this.servers.setQueue((Queue<ServerInfo>)men.getAttribute(0));
                    System.out.println(this.servers.getQueue());
                } else if (com.equals("REPLICA")){
					     updateArchivo((String) men.getAttribute(0), (byte[]) men.getAttribute(1));
						  Mensajeria.sendMessage(socket,addr,port,"DONE",time);
					 } else if (com.equals("UPDATE")){
					     byte[] res = Archivador.devolverArchivo((String)
						  men.getAttribute(0));
						  Mensajeria.sendMessage(socket,addr,port,"FILE",time,res);
					 }

        }

		  public int commit(String nombre, byte[] datos, int k) throws IOException{
		          Queue<ServerInfo> servs = servers.getQueue();
					 int res = 0;

					 while(!servs.isEmpty()){
					     Queue<ServerInfo> busy = new LinkedList<ServerInfo>();
					     for(ServerInfo inf : servs){
					         if(res == k+1) return res;
						      if(inf.getTipo().equals("pasivo")){
						          Mensajeria.sendMessage(commandSocket,inf.getIP(),inf.getPuerto(),"REPLICA",time,nombre,datos);
							 	    Mensaje men =
								    Mensajeria.decodePacket(Mensajeria.receivePacket(commandSocket));
								    if(men.getCommand().equals("BUSY")){
								      busy.add(inf);
								    }else if(men.getCommand().equals("DONE")){
								      ++res;
								   }
					         }
					     }
						  servs = busy;
					 }
					 if(res == k+1) return res;
					 updateArchivo(nombre,datos);
					 ++res;
					 return res;
        }

		  private void updateArchivo(String n, byte[] d){
					 Archivo arc = null;

		          for(Archivo a : info.getArchivos()){
					     if(a.getNombre().equals(n)){
						      arc = a;
						  }
					 }

					 if(arc != null){
					     arc.setVersion(arc.getVersion()+1);
					 }else{
					     info.addArchivo(new Archivo(n));
					 }

					 Archivador.escribirArchivo(n,d);
        }

		  public byte[] update(String name) throws IOException{
		          ServerInfo selected = null;
					 int bestVer = 0;
					 Queue<ServerInfo> servs = servers.getQueue();

					 for(ServerInfo inf : servs){
					     int thisVer = findVersionOf(inf,name);
						  if(thisVer > bestVer){
						      bestVer = thisVer;
								selected = inf;
						  }
					 }

					 if(bestVer != 0){
					     Mensajeria.sendMessage(commandSocket, selected.getIP(),
						  selected.getPuerto(),"UPDATE",time,name);
                    Mensaje men =
						  Mensajeria.decodePacket(Mensajeria.receivePacket(commandSocket));
						  return (byte[]) men.getAttribute(0);
					 }else{
					     return Archivador.devolverArchivo(name);
					 }
		  }

		  private int findVersionOf(ServerInfo inf ,String name){
		          Queue<Archivo> arc = inf.getArchivos();
					 for(Archivo a : arc){
					     if(a.getNombre().equals(name)) return a.getVersion();
					 }
					 return 0;
		  }
} 
