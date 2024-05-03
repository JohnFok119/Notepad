//
// Name: Fok, Johnny
// Final project
// Due: 05/10/2024
// Course: cs-2450-01-sp24
//
// Description:
//              notepad Dialogs
//

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Dialogs
{

    public Dialogs(JFrame frame)
    {   

    }   

    public static void showAboutDialog(JFrame frame, JMenuItem aboutMenuItem)
    {
        JOptionPane.showMessageDialog(frame,"<html>Notepad v0.1<br>(c) 2024 Johnny Fok", "About", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Notepad.png") );
    }

    public static void showSaveDialog(JFrame frame, Boolean edited, String fileName)
    { //UNUSED
        if(edited == true)
        {
            JDialog saveDialog = new JDialog(frame, "Notepad", true);
            saveDialog.setResizable(false);
            saveDialog.setSize(300, 75);
            saveDialog.setLayout(new BorderLayout());
    
            JLabel saveText = new JLabel("Do you want to save your changes?", SwingConstants.CENTER);
            JButton SaveButton = new JButton("Save");
            SaveButton.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent ae)
                {
                }
            });



            JButton DontSaveButton = new JButton("Don't Save");
            JButton CancelButton = new JButton("Cancel");
    
            saveDialog.add(saveText, BorderLayout.NORTH);
            saveDialog.add(SaveButton, BorderLayout.WEST);
            saveDialog.add(DontSaveButton, BorderLayout.CENTER);
            saveDialog.add(CancelButton, BorderLayout.EAST);
    
            saveDialog.setLocationRelativeTo(frame);
            saveDialog.setVisible(true);
        }

    }


    public static void showFailedDialog(JFrame frame)
    {
        JDialog failed = new JDialog(frame, "Notepad", true);
        failed.setResizable(false);
        failed.setSize(50, 75);
        failed.setLayout(new BorderLayout());

        JLabel failedLabel = new JLabel("File Not Found");
        JButton ok = new JButton("OK");
        ok.addActionListener(event -> failed.dispose());
        failed.getRootPane().setDefaultButton(ok);

        failed.add(failedLabel, BorderLayout.NORTH);
        failed.add(ok);

        failed.setLocationRelativeTo(frame);
        failed.setVisible(true);

    }

    public static String openFile(JFrame frame, String fileName)
    {
        return "";
    }

}