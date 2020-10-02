package server;

import java.io.*; 
import java.util.*; 
import java.net.*; 

public class Server{
    // Vector to store active clients 
    static Vector<ClientHandler> ar = new Vector<>(); 
      
    // counter for clients 
    static int i = 0; 
  
    public static void main(String[] args) throws Exception  
    { 
        // server is listening on port 1234 
        ServerSocket ss = new ServerSocket(1234); 
        //DatagramSocket cs = new DatagramSocket(1234);
        //InetAddress ip = InetAddress.getByName("localhost");

        Socket s;
          
        // running infinite loop for getting 
        // client request 
        Thread ssend = new Thread(new Runnable(){
           @Override
           public void run() {
               try{
                    Scanner sc  =new Scanner(System.in);
                    byte[] data = new byte[1024];
                    String dataT = "";
                    String receiver = "";
                    String content = "";
                    while(sc.hasNextLine()){
                        
                        
                        //scan new message to send
                        dataT = sc.nextLine().trim();
                        Boolean flag = false;
                        if(dataT.indexOf("#", 0) != -1){
                            
                            String[] paser = dataT.split("#");
                            receiver = paser[0];
                            content = paser[1];
                            data = content.getBytes();
                        
                            for(ClientHandler client : Server.ar){
                                if(client.getName().equals(receiver) && client.isloggedin == true){
                                    //System.out.println(data);
                                    client.dos.writeUTF("Server: " + content);
                                    //client.dos.write(data, 0, data.length);
                                    flag = true;
                                }
                            }
                        }

                        if(flag == false){
                            for(ClientHandler client : Server.ar){
                                if(client.isloggedin == true){
                                    client.dos.writeUTF("Server: " + dataT);
                                }
                                
                            }
                        }
 
                    }
               } catch (Exception e){
                   e.printStackTrace();
               }
           } 
        });

        ssend.start();
        while (true)  
        { 
            // Accept the incoming request 
            
            s = ss.accept(); 
            
            System.out.println("New client request received : " + s); 
              
            // obtain input and output streams 
            DataInputStream dis = new DataInputStream(s.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
              
            System.out.println("Creating a new handler for this client..."); 
  
            // Create a new handler object for handling this request. 
            ClientHandler mtch = new ClientHandler(s,"client " + i, dis, dos); 
  
            // Create a new Thread with this object. 
            Thread t = new Thread(mtch); 
              
            System.out.println("Adding this client to active client list"); 
  
            // add this client to active clients list 
            ar.add(mtch); 
  
            // start the thread. 
            t.start(); 
  
            // increment i for new client. 
            // i is used for naming only, and can be replaced 
            // by any naming scheme 
            i++; 
  
        } 
    } 
}