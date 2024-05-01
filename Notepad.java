//
// Name: Fok, Johnny
// Final project
// Due: 05/10/2024
// Course: cs-2450-01-sp24
//
// Description:
//              notepad
//

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Notepad 
{
    public Notepad()
    {
        JFrame frame = new JFrame("Notepad");
        frame.setLayout(new BorderLayout());
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("Notepad.png").getImage());
        frame.setJMenuBar(createMenu(frame));    

        JTextArea textArea = new JTextArea();

        JScrollPane scrollPane = new JScrollPane(textArea);





        frame.add(scrollPane);


        frame.setVisible(true);
    }

    public JMenuBar createMenu(JFrame frame)
    {

//============================= File Menu ===================================
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        
        JMenuItem New = new JMenuItem("New");
        New.setAccelerator(KeyStroke.getKeyStroke('N', ActionEvent.CTRL_MASK));

        JMenuItem Open = new JMenuItem("Open...");
        Open.setAccelerator(KeyStroke.getKeyStroke('O', ActionEvent.CTRL_MASK));

        JMenuItem Save = new JMenuItem("Save", 'S');
        Save.setAccelerator(KeyStroke.getKeyStroke('S', ActionEvent.CTRL_MASK));

        JMenuItem SaveAs = new JMenuItem("Save As...", 'A');

        JMenuItem PageSetUp = new JMenuItem("Page Setup", 'u');
        PageSetUp.setEnabled(false);

        JMenuItem Print = new JMenuItem("Print", 'P');
        Print.setAccelerator(KeyStroke.getKeyStroke('P', ActionEvent.CTRL_MASK));
        Print.setEnabled(false);

        JMenuItem Exit = new JMenuItem("Exit", 'x');

        fileMenu.add(New);
        fileMenu.add(Open);
        fileMenu.add(Save);
        fileMenu.add(SaveAs);
        fileMenu.addSeparator();
        fileMenu.add(PageSetUp);
        fileMenu.add(Print);
        fileMenu.addSeparator();
        fileMenu.add(Exit);

//============================= Edit Menu ===================================
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');

        JMenuItem Undo = new JMenuItem("Undo", 'U');
        Undo.setEnabled(false);

        JMenuItem Cut = new JMenuItem("Cut", 't');
        Cut.setAccelerator(KeyStroke.getKeyStroke('X', ActionEvent.CTRL_MASK));

        JMenuItem Copy = new JMenuItem("Copy", 'C');
        Copy.setAccelerator(KeyStroke.getKeyStroke('C', ActionEvent.CTRL_MASK));

        JMenuItem Paste = new JMenuItem("Paste", 'P');
        Paste.setAccelerator(KeyStroke.getKeyStroke('V', ActionEvent.CTRL_MASK));

        JMenuItem Delete = new JMenuItem("Delete");
        Delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));

        JMenuItem Find = new JMenuItem("Find...", 'F');
        Find.setAccelerator(KeyStroke.getKeyStroke('F', ActionEvent.CTRL_MASK));
 
        JMenuItem FindNext = new JMenuItem("Find Next", 'N');
        FindNext.setEnabled(false);

        JMenuItem Replace = new JMenuItem("Replace", 'R');
        Replace.setAccelerator(KeyStroke.getKeyStroke('H', ActionEvent.CTRL_MASK));
        Replace.setEnabled(false);

        JMenuItem GoTo = new JMenuItem("Go To...", 'G');
        GoTo.setAccelerator(KeyStroke.getKeyStroke('G', ActionEvent.CTRL_MASK));

        JMenuItem SelectAll = new JMenuItem("Select All", 'A');
        SelectAll.setAccelerator(KeyStroke.getKeyStroke('A', ActionEvent.CTRL_MASK));

        JMenuItem TimeDate = new JMenuItem("Time/Date", 'D');
        TimeDate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));

        editMenu.add(Undo);
        editMenu.addSeparator();
        editMenu.add(Cut);
        editMenu.add(Copy);
        editMenu.add(Paste);
        editMenu.add(Delete);
        editMenu.addSeparator();
        editMenu.add(Find);
        editMenu.add(FindNext);
        editMenu.add(Replace);
        editMenu.add(GoTo);
        editMenu.addSeparator();
        editMenu.add(SelectAll);
        editMenu.add(TimeDate);

//============================= Format Menu ===================================
        JMenu formatMenu = new JMenu("Format");
        formatMenu.setMnemonic('o');

        JMenuItem WordWrap = new JMenuItem("Word Wrap", 'W');

        JMenuItem Font = new JMenuItem("Font...", 'F');

        JMenu Color = new JMenu("Color");
        Color.setMnemonic('C');
        JMenuItem Background = new JMenuItem("Background...", 'B');
        JMenuItem Foreground = new JMenuItem("Foreground...", 'F');
        Color.add(Background);
        Color.add(Foreground);


        formatMenu.add(WordWrap);
        formatMenu.add(Font);
        formatMenu.add(Color);
//============================= View Menu ===================================
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V');

        JMenuItem StatusBar = new JMenuItem("Status Bar", 'S');
        StatusBar.setEnabled(false);

        viewMenu.add(StatusBar);
//============================= Help Menu ===================================
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        JMenuItem ViewHelp = new JMenuItem("View Help", 'H');
        ViewHelp.setEnabled(false);

        JMenuItem ExtraCredits = new JMenuItem("Extra Credits...", 'x');
        ExtraCredits.setEnabled(false);

        JMenuItem AboutNotePad = new JMenuItem("About Notepad");
        AboutNotePad.addActionListener(event->Dialogs.showAboutDialog(frame, AboutNotePad));


        helpMenu.add(ViewHelp);
        helpMenu.add(ExtraCredits);
        helpMenu.addSeparator();
        helpMenu.add(AboutNotePad);
//============================= Menu Bar ===================================
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new Notepad());
    }
}
