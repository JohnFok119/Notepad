//
// Name: Fok, Johnny
// Final project
// Due: 05/10/2024
// Course: cs-2450-01-sp24
//
// Description:
//              notepad
//
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;

import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.time.*;
import java.time.format.DateTimeFormatter;



public class Notepad 
{
    private JFrame frame;
    private JTextArea textArea;
    private int findIndex;
    private String fileName = "";
    private String filePath = "";
    private Boolean edited = false;
    private Boolean fileInputted = false;
    private String originalText = "";
    private String finalText = "";
    private JFileChooser fileChooser = new JFileChooser();
    private Font font = new Font("Courier", 0, 12);
    private Boolean windowReturn = false;

    //status Bar
    private JLabel lineColumnLabel = new JLabel("Ln " + 1 + ", Col " + 1);
    private JLabel numOfChars = new JLabel("0 characters");
    private JLabel lineFeedLabel = new JLabel("Line Feed (\\n)");
    private JLabel utfLabel = new JLabel("UTF-8");
    private JLabel zoomLabel = new JLabel("100%");
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

    public Notepad(File fileInput) 
    {
        if(fileInput.getName().length() > 0)
        {
            fileInputted = true;
            frame = new JFrame(fileInput.getName());
        }
        else
        {
            frame = new JFrame("Untitled - Notepad");
        }
        
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("Notepad.png").getImage());
        frame.setJMenuBar(createMenu(frame));


        textArea = new JTextArea();
        textArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke)
            {
                if(ke.getKeyCode() == KeyEvent.VK_DELETE)
                {
                    delete();
                }
            }
        });

        if(fileInputted == true)
        {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileInput))) 
            {
                StringBuilder content = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                textArea.setText(content.toString());
            } catch (IOException e) 
            {

                e.printStackTrace();
            }
        }

        textArea.setFont(font);

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de)
            {
                edited = true;
            }

            @Override
            public void removeUpdate(DocumentEvent de)
            {
                edited = true;
            }

            @Override
            public void changedUpdate(DocumentEvent de)
            {
                edited = true;
            }
        });




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
        // Make scroll bars always visible
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


//============================= File =============================
        JPanel statusBar = new JPanel(new GridLayout());
        Border border = BorderFactory.createEtchedBorder();
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        statusBar.setBorder(border);
        statusBar.add(lineColumnLabel);
        // statusBar.add(separator);
        statusBar.add(numOfChars);
        // statusBar.add(separator);
        statusBar.add(zoomLabel);
        // statusBar.add(separator);
        statusBar.add(lineFeedLabel);
        // statusBar.add(separator);
        statusBar.add(utfLabel);

        

        // ============================= Other Methods ===================================

        textArea.addCaretListener(new CaretListener() 
        {
            public void caretUpdate(CaretEvent ce) 
            {
                String str = textArea.getText();
                findIndex = textArea.getCaretPosition();
                updateStatusBar(lineColumnLabel, numOfChars);
                // System.out.println(findIndex);
            }
        });


        //Window Listener
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent wEvent)
            {
                // checkForChanges(); 
                // System.out.println("We have selected X");
                if(edited == true)
                {
                    showSaveDialog(frame, edited, fileName);
                } 
                else
                {
                    frame.dispose();
                }
            }
        });

        frame.add(scrollPane);
        frame.add(statusBar, BorderLayout.SOUTH);

        frame.setVisible(true);
    }


    public void updateStatusBar(JLabel lineColumnLabel, JLabel numOfChars)
    {
        int lineNumber = 1;
        int columnNumber = 1;

        try {
            lineNumber = textArea.getLineOfOffset(findIndex) + 1;
            columnNumber = findIndex - textArea.getLineStartOffset(lineNumber - 1) + 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        lineColumnLabel.setText("Ln " + lineNumber + ", Col " + columnNumber);
        numOfChars.setText(findIndex + " characters");

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
                edited = true;
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

    public void delete()
    {
        int startIndex = findIndex - 2;
        int endIndex = findIndex - 1;
        if(startIndex >= 0 && endIndex >= 0)
        {
            try {
                textArea.getDocument().remove(startIndex, endIndex - startIndex);
                textArea.setCaretPosition(startIndex);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
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

    public Boolean showSaveDialog(JFrame frame, Boolean edited, String fileName)
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
                // System.out.println("Cancel was selected");
                windowReturn = true;
                saveDialog.dispose();
            }
        });

        saveDialog.add(saveText, BorderLayout.NORTH);
        saveDialog.add(SaveButton, BorderLayout.WEST);
        saveDialog.add(dontSaveButton, BorderLayout.CENTER);
        saveDialog.add(CancelButton, BorderLayout.EAST);

        saveDialog.setLocationRelativeTo(frame);
        saveDialog.setVisible(true);
        return windowReturn;
    }



    public JMenuBar createMenu(JFrame frame) 
    {
        // ============================= File Menu ===================================
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        New = new JMenuItem("New");
        New.setAccelerator(KeyStroke.getKeyStroke('N', ActionEvent.CTRL_MASK));
        New.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent aEvent)
            {
                // checkForChanges();
                if(edited == true)
                {
                    save();
                    textArea.setText("");
                }
            }
        });

        Open = new JMenuItem("Open...");
        Open.setAccelerator(KeyStroke.getKeyStroke('O', ActionEvent.CTRL_MASK));

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
                        frame.setTitle(fileName);
                        fileReader.close();
                    } catch(IOException exception)
                    {
                        Dialogs.showFailedDialog(frame);
                    }

                }
            }
        });

        Save = new JMenuItem("Save", 'S');
        Save.setAccelerator(KeyStroke.getKeyStroke('S', ActionEvent.CTRL_MASK));
        Save.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent ae)
            {
                System.out.println("We are using SAVE");
                // checkForChanges();   
                if(edited == true)
                {
                    save();
                }

            }
        });

        SaveAs = new JMenuItem("Save As...", 'A');
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

        PageSetUp = new JMenuItem("Page Setup", 'u');
        PageSetUp.addActionListener(event -> {
            Dialogs.showPageSetup(frame);
        });
        // PageSetUp.setEnabled(false);

        Print = new JMenuItem("Print", 'P');
        Print.setAccelerator(KeyStroke.getKeyStroke('P', ActionEvent.CTRL_MASK));
        Print.addActionListener(event -> {
            try {
                PrinterJob printerJob = PrinterJob.getPrinterJob();
                if(printerJob.printDialog())
                {
                    printerJob.print();
                }
            } catch(PrinterException pe)
            {
                pe.printStackTrace();
            }
           
        });

        Exit = new JMenuItem("Exit", 'x');
        Exit.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent ae)
            {
                // checkForChanges(); 
                if(edited == true)
                {
                    showSaveDialog(frame, edited, fileName);
                } else
                {
                    frame.dispose();
                }

            }
            
        }); 

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
        Cut.addActionListener(event -> CutMethod());

        Copy = new JMenuItem("Copy", 'C');
        Copy.setAccelerator(KeyStroke.getKeyStroke('C', ActionEvent.CTRL_MASK));
        Copy.addActionListener(event -> CopyMethod());

        Paste = new JMenuItem("Paste", 'P');
        Paste.setAccelerator(KeyStroke.getKeyStroke('V', ActionEvent.CTRL_MASK));
        Paste.addActionListener(event -> PasteMethod());

        Delete = new JMenuItem("Delete");
        Delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        Delete.addActionListener(event -> { 
            delete();
        });



        Find = new JMenuItem("Find...", 'F');
        Find.setAccelerator(KeyStroke.getKeyStroke('F', ActionEvent.CTRL_MASK));
        Find.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)
            {
                int foundIndex = Dialogs.showFindDialog(frame, textArea, findIndex + 1, false);
                // System.out.println(foundIndex);
                textArea.setCaretPosition(foundIndex);
                findIndex = foundIndex;
                textArea.requestFocusInWindow();
            }
        });

        FindNext = new JMenuItem("Find Next", 'N');
        FindNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)
            {
                int foundIndex = Dialogs.showFindDialog(frame, textArea, findIndex + 1, true);
                // System.out.println(foundIndex);
                textArea.setCaretPosition(foundIndex);
                findIndex = foundIndex;
                textArea.requestFocusInWindow();
            }
        });

        Replace = new JMenuItem("Replace", 'R');
        Replace.setAccelerator(KeyStroke.getKeyStroke('H', ActionEvent.CTRL_MASK));
        Replace.setEnabled(false);

        GoTo = new JMenuItem("Go To...", 'G');
        GoTo.setAccelerator(KeyStroke.getKeyStroke('G', ActionEvent.CTRL_MASK));
        GoTo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aEvent)
            {
                Dialogs.showGoToDialog(frame, textArea, findIndex);
            }
        });

        SelectAll = new JMenuItem("Select All", 'A');
        SelectAll.setAccelerator(KeyStroke.getKeyStroke('A', ActionEvent.CTRL_MASK));
        SelectAll.addActionListener(event -> {
            textArea.selectAll();
        });

        TimeDate = new JMenuItem("Time/Date", 'D');
        TimeDate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        TimeDate.addActionListener(event -> {
            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:m a MM/d/yyyy");
            String date = currentDate.format(formatter);
            textArea.append(date);
        
        });

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

        Color = new JMenu("Color");
        Color.setMnemonic('C');
        Background = new JMenuItem("Background...", 'B');
        Background.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)
            {
                JColorChooser cc = new JColorChooser();
                Color selectedColor = cc.showDialog(frame, "Choose a Color for the Background", Color.getBackground());
                textArea.setBackground(selectedColor);
            }
        });
        Foreground = new JMenuItem("Foreground...", 'F');
        Foreground.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)
            {
                JColorChooser cc = new JColorChooser();
                Color selectedColor = cc.showDialog(frame, "Choose a Color for the Background", Color.getBackground());
                textArea.setForeground(selectedColor);
            }
        });
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
        ExtraCredits.addActionListener(event ->
        {
            Dialogs.showExtraCredits(frame);
        });

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
        if(args.length > 0)
        {
            String fileName = args[0];
            try (FileReader fr = new FileReader(fileName))
            {
                File file = new File(fileName);
                System.out.println(fileName);
                SwingUtilities.invokeLater(() -> new Notepad(file));
            } catch (Exception e) 
            {
                System.out.println("Invalid File Name\n");
                e.printStackTrace();
            }
        }
        else
        {
            File file = new File("");
            SwingUtilities.invokeLater(() -> new Notepad(file));
        }
        
    }
}
