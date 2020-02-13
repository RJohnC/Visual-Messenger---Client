/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;

/**
 *
 * @author MadProgrammer (Stack Overflow) and johnr
 */
public class PrivMessList extends JFrame{
    
    public static int count = 0;
        
    public static void main(String[] args) {
            count++;
            new PrivMessList();
    }
    

    public PrivMessList() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                }

                
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                WindowListener exitListener = new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    MainWindow.enableBut();
                    }
                };  
                addWindowListener(exitListener);
                add(new TestPane());
                pack();
                setVisible(true);
            }
        });
    }

    public class TestPane extends JPanel {

        private JPanel mainList;
        private LinkedList<JPanel> users;
        private ServerCommunicator toServer = new ServerCommunicator();

        public TestPane() {
            users = getUserPanels();
            setLayout(new BorderLayout());

            mainList = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1;
            gbc.weighty = 1;
            mainList.add(new JPanel(), gbc);

            add(new JScrollPane(mainList));

            for(JPanel user : users){
                    user.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    gbc.weightx = 1;
                    gbc.fill = GridBagConstraints.HORIZONTAL;
                    mainList.add(user, gbc, 0);
                    
                    validate();
            }
        }
        
        //sets a user panel for every user in mutual
        private LinkedList<JPanel> getUserPanels(){
            LinkedList<JPanel> userPanels = new LinkedList<>();
            for(String name : getMutual()){
                javax.swing.JButton jButton = new javax.swing.JButton();
                javax.swing.JLabel jLabel = new javax.swing.JLabel();
                javax.swing.JPanel jPanel = new javax.swing.JPanel();
                jButton.setText(name);
                jButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        DMessage dMess 
                                = new DMessage(PrivMessList.this,true, jButton.getText());
                        dMess.setDefaultCloseOperation(DMessage.DISPOSE_ON_CLOSE);
                        dMess.setVisible(true);
                    
                    }
                });

                jLabel.setText("Unread Message Notifications");

                javax.swing.GroupLayout jPanelLayout 
                        = new javax.swing.GroupLayout(jPanel);
                jPanel.setLayout(jPanelLayout);
                jPanelLayout.setHorizontalGroup(
                    jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addComponent(jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanelLayout.setVerticalGroup(
                    jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                        .addContainerGap())
                );

                userPanels.add(jPanel);
            }
            return userPanels;
        }
        
        //communicates with serverCommunicator
        private LinkedList<String> getMutual(){
            LinkedList<String> mutual = new LinkedList<>();
            LinkedList<String> following 
                    = toServer.retrieveFollowing(MainWindow.loggedIn);
            LinkedList<String> followers 
                    = toServer.retrieveFollowers(MainWindow.loggedIn);
            
            for(String name : following){
                if(followers.contains(name))
                    mutual.add(name);
            }
           
            return mutual;
        }
        

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1000, 600);
        }
    }
}
