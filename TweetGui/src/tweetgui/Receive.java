package tweetgui;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author johnr
 */
public class Receive extends Thread {
    private BufferedReader in;
    private PrintWriter out;
    private ServerSocket receive;
    private ServerCommunicator toServer;
    
            
    
    public Receive(ServerCommunicator toServer){
        this.toServer = toServer;
    }
    
    public void run(){
        while(true){
            try{
                receive = new ServerSocket(2006);
                Socket client = receive.accept();

                in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);
                if(in.readLine().equals("TOCL")){
                    String name = in.readLine();
                    String message = in.readLine();
                    if(toServer.privMess.containsKey(name))
                        toServer.privMess.get(name).add(message);
                    else{
                        LinkedList<String> temp = new LinkedList<>();
                        temp.add(message);
                        toServer.privMess.put(name, temp);
                    }
                    DMessage.addMessToDisplay(message);
                    out.println("SUCCESS");
                }

            }
            catch(IOException e){
                System.out.println(e.toString());
            }
        }
    }
}
