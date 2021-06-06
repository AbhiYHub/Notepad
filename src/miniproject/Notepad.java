package miniproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.URI;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.*;
import javax.swing.undo.UndoManager;

public class Notepad extends JFrame implements WindowListener {

    static JTextArea area;
    JMenuBar menubar;
    JMenu mFile, mEdit, mFormat, mHelp, smFont;
    JMenuItem iNew, iOpen, iSave, iSaveas, iPrint, iExit,
            iCut, iCopy, iPaste, iFind, iReplace,
            iFontColor, iFont,
            iHelp, iAbout;
    JCheckBoxMenuItem wordWrap;
    JButton bNew, bOpen, bSave, bPrint,
            bCut, bCopy, bPaste, bFind, bReplace,
            bFont, bFontColor;
    JToolBar toolbar;
    String filename;
    JFileChooser fc;
    String fileContent;
    UndoManager undo;
    UndoAction undoAction;
    RedoAction redoAction;
    String findText;
    Desktop d = Desktop.getDesktop();
    int fnext = 1;
    FontHelper font;
    public static Notepad frmMain = new Notepad();

    public Notepad() {
        initComp();
        iNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                open_new();
            }
        });
        bNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                open_new();
            }
        });

        iOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                open();
            }
        });
        bOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                open();
            }
        });

        iSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        bSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });

        iSaveas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        });

        iPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printPage();
            }
        });
        bPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printPage();
            }
        });

        iExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit1();
            }
        });

        iCut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area.cut();
            }
        });
        bCut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area.cut();
            }
        });

        iCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area.copy();
            }
        });
        bCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area.copy();
            }
        });

        iPaste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area.paste();
            }
        });
        bPaste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area.paste();
            }
        });

        area.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undo.addEdit(e.getEdit());
                undoAction.update();
                redoAction.update();
            }
        });

        iFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FindAndReplace(frmMain, false);
            }
        });
        bFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FindAndReplace(frmMain, false);
            }
        });

        iReplace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FindAndReplace(frmMain, true);
            }
        });
        bReplace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FindAndReplace(frmMain, true);
            }
        });

        wordWrap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (wordWrap.isSelected()) {
                    area.setLineWrap(true);
                    area.setWrapStyleWord(true);
                } else {
                    area.setLineWrap(false);
                    area.setWrapStyleWord(false);
                }
            }
        });

        iFont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                font.setVisible(true);
            }
        });
        bFont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                font.setVisible(true);
            }
        });
        font.getOK().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area.setFont(font.font());
                font.setVisible(false);
            }
        });
        font.getCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                font.setVisible(false);
            }
        });

        iFontColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color c = JColorChooser.showDialog(rootPane, "Choose Font Color", Color.blue);
                area.setForeground(c);
            }
        });
        bFontColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color c = JColorChooser.showDialog(rootPane, "Choose Font Color", Color.blue);
                area.setForeground(c);
            }
        });

        iHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                link();
            }
        });

        iAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                about();
            }
        });

    }

    private void initComp() {
        area = new JTextArea();
        undo = new UndoManager();
        toolbar = new JToolBar();
        getContentPane().add(toolbar, BorderLayout.NORTH);
        ImageIcon icUndo = new ImageIcon(getClass().getResource("/Images/undo.png"));
        ImageIcon icRedo = new ImageIcon(getClass().getResource("/Images/redo.png"));
        undoAction = new UndoAction(icUndo);
        redoAction = new RedoAction(icRedo);
        fc = new JFileChooser(".");
        font = new FontHelper();
        getContentPane().add(area);
        getContentPane().add(new JScrollPane(area), BorderLayout.CENTER);
        setTitle("Untitled Notepad");
        setSize(800, 600);
        addWindowListener(this);
        setIconImage(new ImageIcon(getClass().getResource("/Images/notepad.png")).getImage());

        //menubar
        menubar = new JMenuBar();

        //menu
        mFile = new JMenu("File");
        mEdit = new JMenu("Edit");
        mFormat = new JMenu("Format");
        smFont = new JMenu("Font");
        mHelp = new JMenu("Help");

        //imageicon for menuitem
        ImageIcon icNew = new ImageIcon(getClass().getResource("/Images/new3.png"));
        ImageIcon icOpen = new ImageIcon(getClass().getResource("/Images/open1.png"));
        ImageIcon icSave = new ImageIcon(getClass().getResource("/Images/save.png"));
        ImageIcon icSaveas = new ImageIcon(getClass().getResource("/Images/saveas.png"));
        ImageIcon icPrint = new ImageIcon(getClass().getResource("/Images/print.png"));
        ImageIcon icExit = new ImageIcon(getClass().getResource("/Images/exit.png"));

        ImageIcon icCut = new ImageIcon(getClass().getResource("/Images/cut.png"));
        ImageIcon icCopy = new ImageIcon(getClass().getResource("/Images/copy.png"));
        ImageIcon icPaste = new ImageIcon(getClass().getResource("/Images/paste.png"));
        ImageIcon icFind = new ImageIcon(getClass().getResource("/Images/find.png"));
        ImageIcon icReplace = new ImageIcon(getClass().getResource("/Images/replace.png"));

        ImageIcon icFont = new ImageIcon(getClass().getResource("/Images/font.png"));
        ImageIcon icFontColor = new ImageIcon(getClass().getResource("/Images/font_color.png"));

        bNew = new JButton(icNew);
        bOpen = new JButton(icOpen);
        bSave = new JButton(icSave);
        bPrint = new JButton(icPrint);
        bCut = new JButton(icCut);
        bCopy = new JButton(icCopy);
        bPaste = new JButton(icPaste);
        bFind = new JButton(icFind);
        bReplace = new JButton(icReplace);
        bFont = new JButton(icFont);
        bFontColor = new JButton(icFontColor);

        //toolbar
        toolbar.add(bNew);
        toolbar.add(bOpen);
        toolbar.add(bSave);
        toolbar.add(bPrint);
        toolbar.addSeparator();
        toolbar.add(undoAction);
        toolbar.add(redoAction);
        toolbar.addSeparator();
        toolbar.add(bCut);
        toolbar.add(bCopy);
        toolbar.add(bPaste);
        toolbar.addSeparator();
        toolbar.add(bFind);
        toolbar.add(bReplace);
        toolbar.addSeparator();
        toolbar.add(bFont);
        toolbar.add(bFontColor);

        //menuitems
        iNew = new JMenuItem("New", icNew);
        iOpen = new JMenuItem("Open", icOpen);
        iSave = new JMenuItem("Save", icSave);
        iSaveas = new JMenuItem("Save as", icSaveas);
        iPrint = new JMenuItem("Print", icPrint);
        iExit = new JMenuItem("Exit", icExit);

        iCut = new JMenuItem("Cut", icCut);
        iCopy = new JMenuItem("Copy", icCopy);
        iPaste = new JMenuItem("Paste", icPaste);
        iFind = new JMenuItem("Find", icFind);
        iReplace = new JMenuItem("Replace", icReplace);

        wordWrap = new JCheckBoxMenuItem("Word Wrap");
        iFont = new JMenuItem("Font", icFont);
        iFontColor = new JMenuItem("Font Color", icFontColor);

        iHelp = new JMenuItem("View Help");
        iAbout = new JMenuItem("About Notepad");

        //shortcut
        iNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        iOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        iSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        iSaveas.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, java.awt.Event.CTRL_MASK | java.awt.Event.SHIFT_MASK));
        iPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));

        iCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        iCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        iPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));

        //adding menuitem to menu
        mFile.add(iNew);
        mFile.add(iOpen);
        mFile.add(iSave);
        mFile.add(iSaveas);
        mFile.addSeparator();
        mFile.add(iPrint);
        mFile.addSeparator();
        mFile.add(iExit);

        mEdit.add(undoAction);
        mEdit.add(redoAction);
        mEdit.addSeparator();
        mEdit.add(iCut);
        mEdit.add(iCopy);
        mEdit.add(iPaste);
        mEdit.addSeparator();
        mEdit.add(iFind);
        mEdit.add(iReplace);

        smFont.add(iFont);
        smFont.add(iFontColor);

        mFormat.add(wordWrap);
        mFormat.add(smFont);

        mHelp.add(iHelp);
        mHelp.add(iAbout);

        //tooltip
        bNew.setToolTipText("Create a new file");
        bOpen.setToolTipText("Open a file");
        bSave.setToolTipText("Save file");
        bPrint.setToolTipText("Print current file");

        bCut.setToolTipText("Cut selected text");
        bCopy.setToolTipText("Copy selected text");
        bPaste.setToolTipText("Paste cut or copied text");
        bFind.setToolTipText("Find text");
        bReplace.setToolTipText("Replace text");

        wordWrap.setToolTipText("Word Wrap");
        bFont.setToolTipText("Customize your Font");
        bFontColor.setToolTipText("Customize you Font Color");

        //adding menu to menubar
        menubar.add(mFile);
        menubar.add(mEdit);
        menubar.add(mFormat);
        menubar.add(mHelp);

        //adding menubar to frame
        setJMenuBar(menubar);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void clear() {
        area.setText(null);
        setTitle("Untitled Notepad");
        filename = null;
        fileContent = null;
    }

    private void open_new() {
        if (!area.getText().equals("") && !area.getText().equals(fileContent)) {
            if (filename == null) {
                int option = JOptionPane.showConfirmDialog(rootPane, "Do you want to save the changes?");
                if (option == 0) {
                    saveAs();
                    clear();
                } else if (option == 2) {

                } else {
                    clear();
                }
            } else {
                int option = JOptionPane.showConfirmDialog(rootPane, "Do you want to save the changes?");
                if (option == 0) {
                    save();
                    clear();
                } else if (option == 2) {

                } else {
                    clear();
                }
            }
        } else {
            clear();
        }
    }

    private void open() {
        try {
            int retval = fc.showOpenDialog(this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                area.setText(null);
                Reader in = new FileReader(fc.getSelectedFile());
                char[] buff = new char[100000];
                int nch;
                while ((nch = in.read(buff, 0, buff.length)) != -1) {
                    area.append(new String(buff, 0, nch));
                }
                setTitle(filename = fc.getSelectedFile().getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void save() {
        PrintWriter fout = null;
        int retval = -1;
        try {
            if (filename == null) {
                saveAs();
            } else if (retval != JFileChooser.CANCEL_OPTION) {
                fout = new PrintWriter(new FileWriter(filename));
                String s = area.getText();
                StringTokenizer st = new StringTokenizer(s, System.getProperty("line.separator"));
                while (st.hasMoreElements()) {
                    fout.println(st.nextToken());
                }
                JOptionPane.showMessageDialog(rootPane, "File saved");
                fileContent = area.getText();
            } else if (retval == JFileChooser.CANCEL_OPTION) {

            }
        } catch (IOException e) {
        } finally {
            //fout.close();
        }
    }

    private void saveAs() {
        PrintWriter fout = null;
        int retval = -1;
        try {
            retval = fc.showSaveDialog(this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                if (fc.getSelectedFile().exists()) {
                    int option = JOptionPane.showConfirmDialog(rootPane, "Do you want to replace this file?", "Confirmation", JOptionPane.OK_CANCEL_OPTION);
                    if (option == 0) {
                        fout = new PrintWriter(new FileWriter(fc.getSelectedFile()));
                        String s = area.getText();
                        StringTokenizer st = new StringTokenizer(s, System.getProperty("line.separator"));
                        while (st.hasMoreElements()) {
                            fout.println(st.nextToken());
                        }
                        JOptionPane.showMessageDialog(rootPane, "File saved");
                        fileContent = area.getText();
                        filename = fc.getSelectedFile().getName();
                        setTitle(filename = fc.getSelectedFile().getName());
                    } else {
                        saveAs();
                    }
                } else {
                    fout = new PrintWriter(new FileWriter(fc.getSelectedFile()));
                    String s = area.getText();
                    StringTokenizer st = new StringTokenizer(s, System.getProperty("line.separator"));
                    while (st.hasMoreElements()) {
                        fout.println(st.nextToken());
                    }
                    JOptionPane.showMessageDialog(rootPane, "File saved");
                    fileContent = area.getText();
                    filename = fc.getSelectedFile().getName();
                    setTitle(filename = fc.getSelectedFile().getName());
                }
            } else if (retval == JFileChooser.CANCEL_OPTION) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                fout.close();
            }
        }
    }

    private void exit1() {
        if (!area.getText().equals("") && !area.getText().equals(fileContent)) {
            if (filename == null) {
                int option = JOptionPane.showConfirmDialog(rootPane, "Do you want to save the changes?");
                if (option == 0) {
                    saveAs();
                    System.exit(0);
                } else if (option == 2) {

                } else {
                    System.exit(0);
                }
            } else {
                int option = JOptionPane.showConfirmDialog(rootPane, "Do you want to save the changes?");
                if (option == 0) {
                    save();
                    System.exit(0);
                } else if (option == 2) {

                } else {
                    System.exit(0);
                }
            }
        } else {
            System.exit(0);
        }
    }

    private void link() {
        try {
            d.browse(new URI("https://www.google.com/search?q=get+help+with+notepad+in+windows+10&filters=guid:%224466414-en-dia%22%20lang:%22en%22&form=T00032&ocid=HelpPane-BingIA"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void about() {
        JFrame frame = new JFrame("About");
        String str = "<center>This project is created by Abhishek,Siddhant and Tini. We have used Swing for creating this project. We have added Menu,Menuitems and many more things like Toolbar. <br><br>Thank You!</center>";
        JLabel l1 = new JLabel("<html>" + str + "</html>");
        frame.add(l1);

        frame.setSize(385, 300);
        frame.setLocation(300, 200);
        frame.setTitle("About Notepad");

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void printPage() {
        try {
            boolean complete = area.print();
            if (complete) {
                JOptionPane.showMessageDialog(null, "Done Printing", "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Cancelled!", "Printer", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        exit1();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    class UndoAction extends AbstractAction {

        public UndoAction(ImageIcon icUndo) {
            super("Undo", icUndo);
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                ex.printStackTrace();
            }
            update();
            redoAction.update();
        }

        protected void update() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, "Undo");
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }

    }

    class RedoAction extends AbstractAction {

        public RedoAction(ImageIcon icRedo) {
            super("Redo", icRedo);
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException ex) {
                ex.printStackTrace();
            }
            update();
            undoAction.update();
        }

        protected void update() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, "Redo");
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }

    }

    public static void main(String[] args) {
        //Notepad np = new Notepad();
    }

    public static JTextArea getArea() {
        return area;
    }

}
