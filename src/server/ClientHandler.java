package server;

import java.io.*; 
import java.util.*; 
import java.net.*; 
  

public class ClientHandler implements Runnable  
{ 
    Scanner scn = new Scanner(System.in); 
    private String name; 
    public final DataInputStream dis; 
    public final DataOutputStream dos; 
    Socket s; 
    boolean isloggedin; 
      
    // constructor 
    public ClientHandler(Socket s, String name, 
                            DataInputStream dis, DataOutputStream dos) { 
        this.dis = dis; 
        this.dos = dos; 
        this.name = name; 
        this.s = s; 
        this.isloggedin=true; 
    } 

    public String getName(){
        return this.name;
    }
  
    @Override
    public void run() { 
  
        String received; 
        while (!this.s.isClosed())  
        { 
            try
            { 
                // receive the string 
                received = dis.readUTF(); 
                  
                System.out.println(received); 
                  
                if(received.equals("logout")){ 
                    this.isloggedin=false; 
                    int index = 0;
                    //xoa clienthandler ra khoi cac luong cua server
                    for(ClientHandler x: Server.ar){
                        if(x.name.equals(this.name)){
                            Server.ar.remove(index);
                            break;
                        }
                        index++;
                    }
                    this.s.close(); 
                    break; 
                } 
                  
                // break the string into message and recipient part 
                StringTokenizer st = new StringTokenizer(received, "#"); 
                String recipient = "";
                String MsgToSend = "";
                
                if(st.hasMoreTokens()){
                    MsgToSend = st.nextToken();
                    if(st.hasMoreTokens()){
                        recipient = st.nextToken();
                    }
                }
                
                // search for the recipient in the connected devices list. 
                // ar is the vector storing client of active users 
                for (ClientHandler mc : Server.ar)  
                { 
                    // if the recipient is found, write on its 
                    // output stream 
                    //chi gui cho 1 người nhận duy nhất : mc.name.equals(recipient) && 
                    if (mc.isloggedin==true)  
                    { 
                        mc.dos.writeUTF(this.name+" : "+MsgToSend); 
                        //break; 
                    } 
                }
                 
            } catch (IOException e) { 
                  
                e.printStackTrace(); 
            }
              
        } 
        try
        { 
            // closing resources 
            this.dis.close(); 
            this.dos.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
} 