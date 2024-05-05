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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Dialogs
{
    final static String DEFAULT_SAMPLE = "a quick brown fox jumps over the lazy dog 0123456789";
    private static JLabel display;

    private static JDialog dialog;
    private static Font newFont;

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


    public static Font fontChooser(JFrame frame, String initialString, Font initialFont)
    {
        JDialog dialog = new JDialog(frame,"Font Viewer", true);
        dialog.setLayout(new BorderLayout());

        JButton displayButton = new JButton("Display the Dialog");
        displayButton.addActionListener(event -> {
            dialog.setVisible(true);
        });


        //========================= NORTH ============================
        JPanel northPanel = new JPanel(new BorderLayout());
        JLabel sizeLabel = new JLabel("Size:");
        sizeLabel.setDisplayedMnemonic('S');

        JSlider sizeSlider = new JSlider(8,20);
        sizeLabel.setLabelFor(sizeSlider);
        sizeSlider.setMajorTickSpacing(2);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setPaintLabels(true);
        sizeSlider.setSnapToTicks(true);

        northPanel.add(sizeLabel, BorderLayout.NORTH);
        northPanel.add(sizeSlider, BorderLayout.SOUTH);

        sizeSlider.addChangeListener(new ChangeListener() 
        {
            public void stateChanged(ChangeEvent ce)
            {
                if(sizeSlider.getValueIsAdjusting())
                {
                    return;
                }
                else
                {
                    Font myFont = new Font(display.getFont().getName(), display.getFont().getStyle(), sizeSlider.getValue());
                    display.setFont(myFont);
                }
            }
        });

        //========================= CENTER ============================
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(3,1));
        JLabel styleLabel = new JLabel("Style:");

        //styles
        JRadioButton regular = new JRadioButton("Regular");
        regular.setMnemonic('R');
        regular.setSelected(true);
        regular.setActionCommand("0");

        JRadioButton italic= new JRadioButton("Italic");
        italic.setMnemonic('I');
        italic.setActionCommand("2");

        JRadioButton bold = new JRadioButton("Bold");
        bold.setMnemonic('B');
        bold.setActionCommand("1");

        ButtonGroup styles = new ButtonGroup();
        styles.add(regular);
        styles.add(italic);
        styles.add(bold);

        centerPanel.add(styleLabel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel);
        buttonPanel.add(regular);
        buttonPanel.add(italic);
        buttonPanel.add(bold);

        ActionListener buttonEvents = new ActionListener() 
        {
            public void actionPerformed(ActionEvent ae)
            {

                int fontNumber = Integer.parseInt(ae.getActionCommand());
                Font myFont = new Font(display.getFont().getName(), fontNumber, display.getFont().getSize());

                switch(ae.getActionCommand())
                {
                    case "0":
                        display.setFont(myFont);
                        break;

                    case "2":
                        display.setFont(myFont);
                        break;

                    case "1":
                        display.setFont(myFont);
                        break;
                }
    
            }
        };

        regular.addActionListener(buttonEvents);
        italic.addActionListener(buttonEvents);
        bold.addActionListener(buttonEvents);
        
        
//========================= WEST ============================
        JPanel westPanel = new JPanel(new BorderLayout());
        JLabel fonts = new JLabel("Fonts:");
        fonts.setDisplayedMnemonic('F');

        JList<String> fontNames = new JList<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());

        //setting the starting font to Dialog
        fonts.setLabelFor(fontNames);
        String[] tempFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (int i = 0; i < tempFonts.length; i++) 
        {
            if (tempFonts[i].equals("Dialog")) 
            {
                fontNames.setSelectedIndex(i);
                break;
            }
        }


        fontNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(fontNames);

        fontNames.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent le)
            {
                String myTextFont = fontNames.getSelectedValue();
                Font textFont = new Font(myTextFont, display.getFont().getStyle(), display.getFont().getSize());
                display.setFont(textFont);
            }

        });

        westPanel.add(fonts, BorderLayout.NORTH);
        westPanel.add(scrollPane, BorderLayout.CENTER);


//========================= SOUTH ============================
        display = new JLabel(initialString == null ? DEFAULT_SAMPLE : initialString, JLabel.CENTER);
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        

        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.add(ok);
        southPanel.add(cancel);



        ok.addActionListener(event -> {

            newFont = new Font(fontNames.getSelectedValue(), display.getFont().getStyle(), display.getFont().getSize());
            // returnFont(newFont);
            dialog.setVisible(false);
        });

        cancel.addActionListener(event -> {
            newFont = initialFont;
            // return(newFont);
            dialog.setVisible(false);
        });


        //Adding Components
        dialog.add(northPanel, BorderLayout.NORTH);

        dialog.add(westPanel, BorderLayout.WEST);
        
        dialog.add(centerPanel, BorderLayout.CENTER);

        dialog.add(southPanel,BorderLayout.SOUTH);

//========================= Final Steps ============================
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);

        fontNames.setSelectedValue("Dialog",true);
        fontNames.ensureIndexIsVisible(fontNames.getSelectedIndex());
        return(newFont);
    }

    private Font returnFont(Font font)
    {
        return font;
    }

    public static void showFindDialog(JFrame frame, String findString)
    {

        //Bug: it looks like ass
        JDialog findDialog = new JDialog(frame, "Find", false);
        findDialog.setResizable(false);
        findDialog.setSize(300, 100);
        findDialog.setLayout(new GridLayout(2,3));

        JLabel findLabel = new JLabel("Find what:  ");
        findLabel.setDisplayedMnemonic('n');

        JTextField textField = new JTextField(100);
        JButton findNext = new JButton("Find Next");
        findNext.setMnemonic('F');

        JButton cancel = new JButton("Cancel");

        JCheckBox matchCase = new JCheckBox("Match case");
        matchCase.setMnemonic('c');

        JCheckBox wrapAround = new JCheckBox("Wrap around");
        wrapAround.setMnemonic('a');



        findDialog.add(findLabel);
        findDialog.add(textField);
        findDialog.add(findNext);
        // findDialog.add(Box.createRigidArea(new Dimension(0, 0)));
        findDialog.add(cancel);
        findDialog.add(matchCase);
        findDialog.add(wrapAround);
        
        findDialog.setLocationRelativeTo(frame);
        findDialog.setVisible(true);
    }


    public static String openFile(JFrame frame, String fileName)
    {
        return "";
    }

}