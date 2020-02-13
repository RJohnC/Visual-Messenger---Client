/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetgui;

import java.sql.Timestamp;

/**
 *
 * @author johnr
 */
public class Tweet {
    private String username;
    private String tweetText;
    private String[] topics;
    private Timestamp timeStamp;
    private int seen;
    
    public Tweet(){
        tweetText = ("");
        topics = null;
        timeStamp = null;
        seen = 0;
    }
    
    public Tweet(String userName, String tweetText){
        this.tweetText = tweetText;
        this.username = userName;
    }
    
     public Tweet(String userName, String tweetText, Timestamp tStamp){
        this.tweetText = tweetText;
        this.username = userName;
        this.timeStamp = tStamp;
    }
    
    public Tweet(String tweetText, String[] topics, Timestamp timeStamp){
       this.tweetText = (tweetText);
       this.topics = topics;
       this.timeStamp = (timeStamp);
       seen = 0;
    }
    
    public Tweet(String username, String tweetText, String[] topics,
            Timestamp timeStamp){
        
       this.username = username;
       this.tweetText = (tweetText);
       this.topics = topics;
       this.timeStamp = (timeStamp);
       seen = 0;
    }
    
    //change and view Text
    public String getText(){
        return tweetText;        
    }
    public void setText(String tweetText){
        this.tweetText = tweetText;
    }
    
    //change and view Topic
    public String[] getTopic(){
        return topics;
    }
    public void setTopics(String[] topics){
        this.topics = topics;
    }
    
    //change and view Time
    public Timestamp getTime(){
        return timeStamp;
    }
    public void setTime(Timestamp timeStamp){
        this.timeStamp = (timeStamp);
    }
    
    //change, add to, and view View Count
    public int getViews(){
        return seen;
    }
    public void setViews(int seen){
        this.seen = seen;
    }
    public void addView(){
        this.seen++;
    }
    
    public String getUsername(){
        return username;
    }
}
