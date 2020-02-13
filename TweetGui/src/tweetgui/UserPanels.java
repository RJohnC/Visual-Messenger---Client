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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;

/**
 *
 * @author johnr
 */

/*
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    */
    public class UserPanels extends JFrame{
        
        public UserPanels(LinkedList<String> toCreate, MainWindow parent){
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
                add(new UserPanels.UserPane(toCreate, parent));
                pack();
                setVisible(true);
            }
        });
    }
    
    private class UserPane extends JPanel{
        private JPanel list;
        private LinkedList<JPanel> users;
        
        public UserPane(LinkedList<String> toCreate, MainWindow parent){
            users = getUserPanels(toCreate, parent);
            setLayout(new BorderLayout());
            
            list = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1;
            gbc.weighty = 1;
            list.add(new JPanel(), gbc);
            
            add(new JScrollPane(list));
            
            for(JPanel user : users){
                user.setBorder(new MatteBorder(0,0,1,0, Color.GRAY));
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.weightx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                list.add(user, gbc, 0);
                
                validate();
            } 
            
            
        }
        
        private LinkedList<JPanel> getUserPanels(LinkedList<String> toCreate, MainWindow parent){
            LinkedList<JPanel> namePanels = new LinkedList<>();
            for(String name : toCreate){
                javax.swing.JButton button = new javax.swing.JButton();
                javax.swing.JPanel jPanel = new javax.swing.JPanel();
                javax.swing.JLabel label = new javax.swing.JLabel();
                label.setText(name);
                button.setText("View Page");
                
                button.addActionListener(new java.awt.event.ActionListener(){
                    public void actionPerformed(java.awt.event.ActionEvent evt){
                        UserPage uPage = new UserPage(parent, true, name);
                        uPage.setDefaultCloseOperation(uPage.DISPOSE_ON_CLOSE);
                        uPage.setVisible(true);
                    }
                });
            javax.swing.GroupLayout jPanelLayout 
                        = new javax.swing.GroupLayout(jPanel);
                jPanel.setLayout(jPanelLayout);
                jPanelLayout.setHorizontalGroup(
                    jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addComponent(button, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanelLayout.setVerticalGroup(
                    jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                        .addContainerGap())
                );
                namePanels.add(jPanel);
            }
            return namePanels;
        }
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1000, 600);
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
