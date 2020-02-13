package tweetgui;

/**
 * Client side - communicates with the server
 * @author johnr
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerCommunicator {
        private Socket sock;
        private BufferedReader in;
        private PrintWriter out;
        private String host;
        public static HashMap<String, LinkedList<String>> privMess;
        private static Receive incoming;
        private Thread background;
        
    public ServerCommunicator() {
            host = "216.159.69.103";
            
    }
    
    private void close() {
        try{
            in.close();
            out.close();
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
    }

    public String register(String name, String pass) {  
        if(name.equals("") || pass.equals(""));
        try{
            sock = new Socket( host , 2005);
            //System.out.println(sock.getLocalAddress());
            
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);
            
            out.println("REG");
            System.out.println("REG");
            out.println(name);
            System.out.println(name);
            out.println(pass);      
            System.out.println(pass);

            if(in.readLine().equals("RESULT")){
                String result = in.readLine();
                System.out.println(result);
                close();

                if(result.equals("Used")){
                    return "Username in use.";
                }
                else{
                    incoming = new Receive(this);
                    incoming.start();
                    return result;
                }
            }
            else{
                close();
                return "Error in protocol" ;
            }
        }
        catch(IOException e){
                return e.toString();
        }    
        catch(NullPointerException e){
            return "Make sure fields are filled.";
        }
    }
    
    public String loggingIn(String name, String pass) {
            
        try{
            sock = new Socket( host , 2005);
            //System.out.println(sock.getLocalAddress());
            
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);

            System.out.println("LOGI");
            out.println("LOGI");
            System.out.println(name);
            out.println(name);
            System.out.println(pass);
            out.println(pass);
            
            if(in.readLine().equals("RESULT")){
                String result = in.readLine();
                System.out.println(result);
                close();
                
                if(result.equals("Success")){
                    incoming = new Receive(this);
                    incoming.start();;
                    return "Success";
                }
                if(result.equals("Incorrect")){
                    return "Incorrect Username or Password";
                }
                if(result.equals("LoggedIn")){
                    return "User logged in elsewhere";
                }
                if(result.equals("Failed")){
                    return "Login Failed";
                }
                return "Error in protocol 1";
            }
            else{
                close();
                return "Error in protocol 2" ;
            }
        }
        catch(IOException e){
                return e.toString();
        }
        catch(NullPointerException e){
            return "Make sure fields are filled.";
        }
    }
    
    public String loggingOff(String name){
        if(name.equals(""))
            return "Noone is logged in.";
        
        try{    
            sock = new Socket( host , 2005);
            //System.out.println(sock.getLocalAddress());
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            out = new PrintWriter(sock.getOutputStream(), true);

            System.out.println("LOGO");
            out.println("LOGO");
            System.out.println(name);
            out.println(name);

            if(in.readLine().equals("RESULT")){
                String result = in.readLine();
                close();
                if(result.equals("Success")){
                    incoming.stop();
                    return "Logged Out " + name;
                }
                   

                if(result.equals("Failed"))
                    return "Failed to Log Out " + name; 

                return "Error in Protocol 1";
            }
            else{
                close();
                return "Error in protocol 2" ;
            }
        }
        catch(IOException e){
                return e.toString();
        }   
        catch(NullPointerException e){
            return "No one is logged in.";
        }
    }
    
    public String postTweet(String name, String tweetText) {
        if(name.equals(""))
            return "Noone is logged In.";
        
        if(tweetText.equals("") || tweetText == null)
            return "Can't post empty tweet";

        //sets array with the topics in tweetText that start with #
        int length = tweetText.length();
        char[] text = tweetText.toCharArray();
        boolean onTopic = false;

        int count = 0;
        for(char c : text){
            if(c == '#'){
                count++;
            }
        }

        int[] tagIndex = new int[count];
        int[] endIndex = new int[count];
        String[] topics = new String[count];

        int tag = 0;
        for(int i = 0; i < length-1; i++){
            if(text[i] == '#')
                onTopic = true;
            if(onTopic == true){
                if(text[i] == '#'){
                    tagIndex[tag] = i;
                }
                else if(text[i] == '#' || text[i] == ' '){
                    endIndex[tag] = i;
                    onTopic = false;
                    tag++;
                } 
                else if(i+2 == length){
                    endIndex[tag] = length;
                }
            }   
        }

        tag = 0;
        while(tag != count){
            if(endIndex[tag] == length)
                topics[tag] = tweetText.substring(tagIndex[tag]+1);
            else
                topics[tag] = tweetText.substring(tagIndex[tag]+1, endIndex[tag]);
            tag++;

        } 

        //sets timestamp for the tweet
        Date date = new Date();
        long time = date.getTime();
        Timestamp timeStamp = new Timestamp(time);
        
        try{
            sock = new Socket( host , 2005);
            //System.out.println(sock.getLocalAddress());
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);
         
            //starts post protocol
            System.out.println("POST");
            out.println("POST");

            //sends username
            System.out.println("NAME");
            System.out.println(name);
            out.println("NAME");
            out.println(name);
            
            //sends tweet text
            System.out.println("TWEET");
            System.out.println(tweetText);
            out.println("TWEET");
            out.println(tweetText);
            System.out.println("Sent Tweet Text");
            
            
            //sends topics
            System.out.println("TOPICS");
            System.out.println(count);
            out.println("TOPICS");
            out.println(count);
            for(String t : topics){
                System.out.println(t);
                out.println(t);
            }
            
            
            //sends timestamp
            System.out.println("TIME");
            System.out.println(timeStamp.toString());
            out.println("TIME");
            out.println(timeStamp.toString());
            
            System.out.println("DONE");
            out.println("DONE");
            
            if(in.readLine().equals("RESULT")){
                String result = in.readLine();
                System.out.println(result);
                close();
                if(result.equals("Successful"))
                    return "Posted Tweet";
                else if(result.equals("Failed"))
                   return "Post Failed";
                else 
                    return "Error in protocol 1";
                
            }
            else
                return "Error in protocol 2";
        }
        catch(IOException e){
                return e.toString();
        }   
        catch(NullPointerException e){
            return "Error sending tweet. Make sure field is filled.";
        }
    }

    public String followUser(String name, String toFollow){
        if(name.equals(""))
            return "Noone is logged in.";
        if(toFollow.equals(""))
            return "No user given to follow";
        if(toFollow.equals(name))
            return "Cannot follow yourself.";
        
        try{
            sock = new Socket( host , 2005);
            //System.out.println(sock.getLocalAddress());
            String result;
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);

            System.out.println("FOLL");
            System.out.println(name);
            System.out.println(toFollow);
            out.println("FOLL");
            out.println(name);
            out.println(toFollow);

            if(in.readLine().equals("RESULT")){
                result = in.readLine();
                System.out.println(result);
                close();
            }
            else{
                close();
                return "Error in protocol 1";
            }
                
            if(result.equals("Success"))
                return "Successfully followed " + toFollow;
            
            if(result.equals("Failed"))
                return "Failed to follow " + toFollow;
            
            if(result.equals("Already"))
                return "Already following " + toFollow;
        }
        catch(IOException e){
                return e.toString();
        }  
        catch(NullPointerException np){
            System.out.println(np.toString());
        }
        return "Error in protocol 2";
    }

    public String unfollowUser(String name, String toUnfollow) {
        if(name.equals(""))
            return "Noone is logged in.";
        if(toUnfollow.equals(""))
            return "No user given to Unfollow";
        if(toUnfollow.equals(name))
            return "Cannot unfollow yourself.";
        
        try{
            sock = new Socket( host , 2005);
            //System.out.println(sock.getLocalAddress());
            String result;
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);

            System.out.println("UNFO");
            System.out.println(name);
            System.out.println(toUnfollow);
            out.println("UNFO");
            out.println(name);
            out.println(toUnfollow);

            if(in.readLine().equals("RESULT")){
                result = in.readLine();
                System.out.println(result);
                close();
            }
            else{
                close();
                return "Error in protocol 1";
            }
                
            if(result.equals("Success"))
                return "Successfully unfollowed " + toUnfollow;
            if(result.equals("Failed"))
                return "Failed to unfollow " + toUnfollow;
            if(result.equals("Already"))
                return "Not following " + toUnfollow + " already.";
        }
        catch(IOException e){
                return e.toString();
        }   
        catch(NullPointerException np){
            System.out.println(np.toString());
        }
        return "Error in protocol 2";
    }
    
    //private static String retrieveFollowers(Socket sock, String name) throws IOException{
    public LinkedList<String> retrieveFollowing(String username) {
        if(username.equals("") || username == null)
            return null;

        try{
            sock = new Socket( host , 2005);

            LinkedList<String> followers = new LinkedList<>();
            in =  new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);
            
            System.out.println("GETF");
            System.out.println(username);
            out.println("GETF");
            out.println(username);
            
            int size = Integer.parseInt(in.readLine());
            System.out.println("" + size);
            
            String name = in.readLine();
            while(!name.equals("DONE")){
                followers.add(name);
                System.out.println(name);
                name = in.readLine();
            }
            close();
            return followers;
        }
        catch(IOException e){
                System.out.print(e.toString());
        }  
        catch(NullPointerException np){
            System.out.println(np.toString());
        }
        
        return null;
    }
    
    public LinkedList<String> retrieveFollowers(String username) {
        if(username.equals(""))
            return null;

        try{
            sock = new Socket( host , 2005);

            LinkedList<String> followers = new LinkedList<>();
            in =  new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);
            
            System.out.println("GTFG");
            System.out.println(username);
            out.println("GTFG");
            out.println(username);
            
            int size = Integer.parseInt(in.readLine());
            System.out.println("" + size);
            
            String name = in.readLine();
            while(!name.equals("DONE")){
                followers.add(name);
                System.out.println(name);
                name = in.readLine();
            }
          
            close();
            return followers;
        }
        catch(IOException e){
                System.out.print(e.toString());
        }  
        catch(NullPointerException np){
            System.out.println(np.toString());
        }
        
        return null;
    }
    
    public LinkedList<String> getMutual(String username){
            if(username == null){
                return null;
            }
            LinkedList<String> followers = retrieveFollowers(username);
            LinkedList<String> following = retrieveFollowing(username);
            LinkedList<String> mutual = new LinkedList<>();
            
            for(String fName : followers){
                if(following.contains(fName)){
                    mutual.add(fName);
                }
            }
            
            return mutual;
    }
    
    public ArrayList<Tweet> retrieveTweets(String username){
        ArrayList<Tweet> toRead = new ArrayList<>();
        try{
            if(username != null){
                sock = new Socket( host , 2005);
                
                in =  new BufferedReader(new InputStreamReader(sock.getInputStream()));
                out = new PrintWriter(sock.getOutputStream(), true);

                out.println("TWTS");
                out.println(MainWindow.loggedIn);
                
                int size = Integer.parseInt(in.readLine());
                
                for(int i = 0; i < size; i++){
                    String text = in.readLine();
                    String name = in.readLine();
                    System.out.println(text);
                    toRead.add(new Tweet(name, text));
                }
                System.out.println("done");
                close();
            }
     
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
        catch(NullPointerException np){
            System.out.println(np.toString());
        }
        
        return toRead;
    }
    
    public String privateMessageToServer(String username){
        try{
            in =  new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);
            
            out.println("MESS");
            
            if(in.readLine().equals("ADDR")){
                String address = in.readLine();
                close();
                return address;
            }
            else
                return "User Offline";
            
        }
        catch(IOException e){
            System.out.println(e.toString());
            return "Error";
        }
    }
    
    public String sendPrivateMessage(String address, String message, String username){
        try{
            sock = new Socket(address, 2006);
            in =  new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);
            
            out.println("TOCL");
            out.println(username);
            out.println(message);
            if(in.readLine().equals("SUCCESS")){
                if(privMess.keySet().contains(username))
                    privMess.get(username).add(message);
                else {
                    setHashmap(username);
                    privMess.get(username).add(message);
                }
                return message;
            }
            
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
        
        return "Could not Send.";
    }
    
    public void setHashmap(String username){
        if(!privMess.containsKey(username))
            privMess.put(username, new LinkedList<String>());
    }
    
    public LinkedList<String> getFromHashmap(String username){
        return privMess.get(username);
    }
    
    public ArrayList<Tweet> search(String topic){
        if(topic.equals(""))
            return null;
        
        try{
            sock = new Socket(host, 2005);

            ArrayList<Tweet> searchedFor = new ArrayList<>();
            in =  new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);

            out.println("SRCH");
            System.out.println("SRCH");

            out.println(topic);
            System.out.println(topic);

            int size = Integer.parseInt(in.readLine());
            if(size == 0){
                close();
                return null;
            }
            System.out.println(size);
            for(int i = 0; i < size; i++){
                String name = in.readLine();
                String text = in.readLine();
                String time = in.readLine();

                searchedFor.add(new Tweet(name, text, Timestamp.valueOf(time)));
                System.out.println("Added: " + name + text + time);
            }
            
            String fin = in.readLine();
            if(fin.equals("DONE")){
                System.out.println("sent sf");
                return searchedFor;
            }
         }
        catch(IOException e){
            System.out.println(e.toString());
        }
        catch(NullPointerException np){
            System.out.println(np.toString());
        }
        
        return null;
    }

}