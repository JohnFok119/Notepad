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


public class Dialogs
{
    public Dialogs(JFrame frame)
    {

    }

    public static void showAboutDialog(JFrame frame, JMenuItem aboutMenuItem)
    {
        JOptionPane.showMessageDialog(frame,"<html>Notepad v0.1<br>(c) 2024 Johnny Fok", "About", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Notepad.png") );
    }
}