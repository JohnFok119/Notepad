//
// Name: Fok, Johnny
// Final project
// Due: 05/10/2024
// Course: cs-2450-01-sp24
//
// Description:
//              notepad
//
import java.io.File;
import java.io.FileReader;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit;

import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;




public class Notepad 
{
    private JFrame frame;
    private JTextArea textArea;
    private int findIndex;
    private String fileName = "";
    private String filePath = "";
    private Boolean edited = false;
    String originalText = "";
    String finalText = "";
    // File Menu
    private JMenuItem New;
    private JMenuItem Open;
    private JMenuItem Save;
    private JMenuItem SaveAs;
    private JMenuItem PageSetUp;
    // Edit Menu
    private JMenuItem Print;
    private JMenuItem Exit;
    private JMenuItem Undo;
    private JMenuItem Cut;
    private JMenuItem Copy;
    private JMenuItem Paste;
    private JMenuItem Delete;
    private JMenuItem Find;
    private JMenuItem FindNext;
    private JMenuItem Replace;
    private JMenuItem GoTo;
    private JMenuItem SelectAll;
    private JMenuItem TimeDate;
    // Format Menu
    private JMenuItem WordWrap;
    private JMenuItem Font;
    private JMenuItem Color;
    private JMenuItem Background;
    private JMenuItem Foreground;
    // View Menu
    private JMenuItem StatusBar;
    // Help Menu
    private JMenuItem ViewHelp;
    private JMenuItem ExtraCredits;
    private JMenuItem AboutNotePad;

    public Notepad() 
    {
        frame = new JFrame("Untitled - Notepad");
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("Notepad.png").getImage());
        frame.setJMenuBar(createMenu(frame));

        Font font = new Font("Courier", 0, 12);

        textArea = new JTextArea();
        textArea.setFont(font);

        // ============================= Right-Click ===================================
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem popupCut = new JMenuItem("Cut", 't');
        popupCut.addActionListener(event -> CutMethod());
        JMenuItem popupCopy = new JMenuItem("Copy", 'C');
        popupCopy.addActionListener(event -> CopyMethod());
        JMenuItem popupPaste = new JMenuItem("Paste", 'P');
        popupPaste.addActionListener(event -> PasteMethod());

        popupMenu.add(popupCut);
        popupMenu.add(popupCopy);
        popupMenu.add(popupPaste);

        
        textArea.addMouseListener(new MouseAdapter() 
        {
            public void mouseClicked(MouseEvent me)
            {
                if(SwingUtilities.isRightMouseButton(me))
                {
                    popupMenu.show(textArea, me.getX(), me.getY());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(textArea);


//============================= File =============================

        // ============================= New MenuItem ===================================
        New.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aEvent)
            {
                if(edited == true)
                {

                }
            }
        });

        // ============================= Open... MenuItem ===================================
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter javaFilter = new FileNameExtensionFilter(".java files","java");
        FileNameExtensionFilter textFilter = new FileNameExtensionFilter(".txt files","txt");
        fileChooser.addChoosableFileFilter(javaFilter);
        fileChooser.addChoosableFileFilter(textFilter);
        Open.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent ae) 
            {
                int result = 0;
                result = fileChooser.showOpenDialog(frame);

                if( result == JFileChooser.APPROVE_OPTION)
                {
                    File file = fileChooser.getSelectedFile();
                    fileName = file.getName();
                    filePath = file.getAbsolutePath();
                    System.out.println(fileName);
                    try {
                        FileReader fileReader = new FileReader(file);
                        textArea.read(fileReader, null);
                        fileReader.close();
                    } catch(IOException exception)
                    {
                        Dialogs.showFailedDialog(frame);
                    }

                }
            }
        });

        // ============================= Save MenuItem ===================================
        Save.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent ae)
            {
                System.out.println("We are using SAVE");
                checkForChanges();   
                if(edited == true)
                {
                    save();
                }

            }
        });

        // ============================= Save As MenuItem ===================================
        //Bug: If we make changes and hit save but don't actually save anything
        //      then try to save it, we SHOULD get the save menu again but we don't.
        //Solution: We have to check IF the file exists or not using the fileName
        //          OR
        //         ** Have a check to see if the try catch in void save() was actually used **
        SaveAs.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent ae)
            {
                System.out.println("We are using SAVE AS");
                int result = fileChooser.showSaveDialog(frame);
                File file = fileChooser.getSelectedFile();
                fileName = file.getName();
                filePath = file.getAbsolutePath();
                System.out.println(fileName);
                try {
                    FileReader fileReader = new FileReader(file);
                    textArea.read(fileReader, null);
                    fileReader.close();
                } catch(IOException exception)
                {
                    Dialogs.showFailedDialog(frame);
                }
            }
        });

        // ============================= Exit MenuItem ===================================
        Exit.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent ae)
            {
                checkForChanges(); 
                if(edited == true)
                {
                    showSaveDialog(frame, edited, fileName);
                } else
                {
                    frame.dispose();
                }

            }
            
        }); 


//============================= Edit =============================

        //============================= Cut/Copy/Paste =============================
        Cut.addActionListener(event -> CutMethod());
        Copy.addActionListener(event -> CopyMethod());
        Paste.addActionListener(event -> PasteMethod());

        //============================= Delete =============================
        
        //============================= Find =============================
        Find.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)
            {
                Dialogs.showFindDialog(frame, "");
            }
        });

        //============================= Go To... =============================

        // ============================= Font Chooser MenuItem ===================================
        Font.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent ae)
            {
                Font newFont = Dialogs.fontChooser(frame, textArea.getText(), font);
                // System.out.println("\nFont: " + newFont.getFontName() +
                //                    "\nStyle: " + newFont.getStyle() + 
                //                    "\nSize: " + newFont.getSize());
                textArea.setFont(newFont);
            }
        });



        // ============================= Other Methods ===================================
        textArea.addCaretListener(new CaretListener() 
        {
            public void caretUpdate(CaretEvent ce) 
            {
                String str = textArea.getText();
                // jlabMsg.setText("Current size: " + str.length());
                findIndex = textArea.getCaretPosition();
                // System.out.println(findIndex);
            }
        });


        //Window Listener
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent wEvent)
            {
                checkForChanges(); 
                if(edited == true)
                {
                    showSaveDialog(frame, edited, fileName);
                } else
                {
                    frame.dispose();
                }
            }
        });

        frame.add(scrollPane);

        frame.setVisible(true);
    }

    public void checkForChanges()
    {
        finalText = textArea.getText();
        if(originalText.equals(finalText))
        {
            edited = false;
            System.out.println("No Changes");
        } 
        else
        {
            System.out.println("Changes were made");
            edited = true;
            originalText = finalText;
        }
    }

	// Save the file.
	public void save() 
    {
		FileWriter fileWriter;
		// Save the file.
		try { //DOES THE FILE EXIST? YES, IT DOES
            System.out.println("We are saving onto " + fileName);
			fileWriter = new FileWriter(filePath, false);
			textArea.write(fileWriter);
			fileWriter.close();
            // JOptionPane.showConfirmDialog(frame, "file exists, overwrite?");
		} 
        catch (IOException exc) {   //DOES THE FILE EXIST? IT SHOULD NOT
            JFileChooser fc = new JFileChooser();
            int result = fc.showSaveDialog(frame);
            if(result == JFileChooser.APPROVE_OPTION)
            {
                File newFile = fc.getSelectedFile();
                try 
                {
                    FileWriter fw = new FileWriter(newFile);
                    textArea.write(fw);
                    fw.close();
			        System.out.println("File saved to: " + newFile.getAbsolutePath());
                } 
                catch(IOException exception) 
                {
                    Dialogs.showFailedDialog(frame);
                }
            }
            else if(result == JFileChooser.CANCEL_OPTION)
            {
                edited = false;
            }
		}
	}



    public void CutMethod()
    {
        String selectedText = textArea.getSelectedText();
        if(selectedText != null)
        {
            DefaultEditorKit.CutAction cutAction = new DefaultEditorKit.CutAction();
            cutAction.actionPerformed(new ActionEvent(textArea, ActionEvent.ACTION_PERFORMED, null));
        }
    }


    public void CopyMethod() 
    {
        String selectedText = textArea.getSelectedText();
        if (selectedText != null) 
        {
            DefaultEditorKit.CopyAction copyAction = new DefaultEditorKit.CopyAction();
            copyAction.actionPerformed(new ActionEvent(textArea, ActionEvent.ACTION_PERFORMED, null));
        }
    }

    public void PasteMethod() 
    {
        DefaultEditorKit.PasteAction pasteAction = new DefaultEditorKit.PasteAction();
        pasteAction.actionPerformed(new ActionEvent(textArea, ActionEvent.ACTION_PERFORMED, null));
    }

    public void find(int start)
    {
        // String findString = textArea.getText()
    }

    public void showSaveDialog(JFrame frame, Boolean edited, String fileName)
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
                save();
                saveDialog.dispose();
                frame.dispose();
            }
        });

        JButton dontSaveButton = new JButton("Don't Save");
        dontSaveButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent ae)
            {
                saveDialog.dispose();
                frame.dispose();
            }
        });
        JButton CancelButton = new JButton("Cancel");
        CancelButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent ae)
            {
                saveDialog.dispose();
            }
        });

        saveDialog.add(saveText, BorderLayout.NORTH);
        saveDialog.add(SaveButton, BorderLayout.WEST);
        saveDialog.add(dontSaveButton, BorderLayout.CENTER);
        saveDialog.add(CancelButton, BorderLayout.EAST);

        saveDialog.setLocationRelativeTo(frame);
        saveDialog.setVisible(true);
    }



    public JMenuBar createMenu(JFrame frame) 
    {
        // ============================= File Menu ===================================
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        New = new JMenuItem("New");
        New.setAccelerator(KeyStroke.getKeyStroke('N', ActionEvent.CTRL_MASK));

        Open = new JMenuItem("Open...");
        Open.setAccelerator(KeyStroke.getKeyStroke('O', ActionEvent.CTRL_MASK));

        Save = new JMenuItem("Save", 'S');
        Save.setAccelerator(KeyStroke.getKeyStroke('S', ActionEvent.CTRL_MASK));

        SaveAs = new JMenuItem("Save As...", 'A');

        PageSetUp = new JMenuItem("Page Setup", 'u');
        PageSetUp.setEnabled(false);

        Print = new JMenuItem("Print", 'P');
        Print.setAccelerator(KeyStroke.getKeyStroke('P', ActionEvent.CTRL_MASK));
        Print.setEnabled(false);

        Exit = new JMenuItem("Exit", 'x');

        fileMenu.add(New);
        fileMenu.add(Open);
        fileMenu.add(Save);
        fileMenu.add(SaveAs);
        fileMenu.addSeparator();
        fileMenu.add(PageSetUp);
        fileMenu.add(Print);
        fileMenu.addSeparator();
        fileMenu.add(Exit);

        // ============================= Edit Menu ===================================
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');

        Undo = new JMenuItem("Undo", 'U');
        Undo.setEnabled(false);

        Cut = new JMenuItem("Cut", 't');
        Cut.setAccelerator(KeyStroke.getKeyStroke('X', ActionEvent.CTRL_MASK));

        Copy = new JMenuItem("Copy", 'C');
        Copy.setAccelerator(KeyStroke.getKeyStroke('C', ActionEvent.CTRL_MASK));

        Paste = new JMenuItem("Paste", 'P');
        Paste.setAccelerator(KeyStroke.getKeyStroke('V', ActionEvent.CTRL_MASK));

        Delete = new JMenuItem("Delete");
        Delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));

        Find = new JMenuItem("Find...", 'F');
        Find.setAccelerator(KeyStroke.getKeyStroke('F', ActionEvent.CTRL_MASK));

        FindNext = new JMenuItem("Find Next", 'N');
        FindNext.setEnabled(false);

        Replace = new JMenuItem("Replace", 'R');
        Replace.setAccelerator(KeyStroke.getKeyStroke('H', ActionEvent.CTRL_MASK));
        Replace.setEnabled(false);

        GoTo = new JMenuItem("Go To...", 'G');
        GoTo.setAccelerator(KeyStroke.getKeyStroke('G', ActionEvent.CTRL_MASK));

        SelectAll = new JMenuItem("Select All", 'A');
        SelectAll.setAccelerator(KeyStroke.getKeyStroke('A', ActionEvent.CTRL_MASK));

        TimeDate = new JMenuItem("Time/Date", 'D');
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

        // ============================= Format Menu ===================================
        JMenu formatMenu = new JMenu("Format");
        formatMenu.setMnemonic('o');

        WordWrap = new JMenuItem("Word Wrap", 'W');

        Font = new JMenuItem("Font...", 'F');

        Color = new JMenu("Color");
        Color.setMnemonic('C');
        Background = new JMenuItem("Background...", 'B');
        Foreground = new JMenuItem("Foreground...", 'F');
        Color.add(Background);
        Color.add(Foreground);

        formatMenu.add(WordWrap);
        formatMenu.add(Font);
        formatMenu.add(Color);
        // ============================= View Menu ===================================
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V');

        StatusBar = new JMenuItem("Status Bar", 'S');
        StatusBar.setEnabled(false);

        viewMenu.add(StatusBar);
        // ============================= Help Menu ===================================
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        ViewHelp = new JMenuItem("View Help", 'H');
        ViewHelp.setEnabled(false);

        ExtraCredits = new JMenuItem("Extra Credits...", 'x');
        ExtraCredits.setEnabled(false);

        AboutNotePad = new JMenuItem("About Notepad");
        AboutNotePad.addActionListener(event -> Dialogs.showAboutDialog(frame, AboutNotePad));

        helpMenu.add(ViewHelp);
        helpMenu.add(ExtraCredits);
        helpMenu.addSeparator();
        helpMenu.add(AboutNotePad);
        // ============================= Menu Bar ===================================
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
