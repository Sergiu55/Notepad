import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;

public class Main extends JFrame implements ActionListener {

    JTextArea t;
    JFrame f;

    Main() {
        f = new JFrame("Notepad");

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        } catch (Exception e) {
            e.printStackTrace();
        }

        t = new JTextArea();

        JMenuBar mb = new JMenuBar();

        // FILE
        JMenu m1 = new JMenu("File");
        JMenuItem mi1 = new JMenuItem("New");
        JMenuItem mi2 = new JMenuItem("Open");
        JMenuItem mi3 = new JMenuItem("Save");
        JMenuItem mi4 = new JMenuItem("Print");

        mi1.addActionListener(this);
        mi2.addActionListener(this);
        mi3.addActionListener(this);
        mi4.addActionListener(this);

        m1.add(mi1);
        m1.add(mi2);
        m1.add(mi3);
        m1.add(mi4);

        // EDIT
        JMenu m2 = new JMenu("Edit");
        JMenuItem mi5 = new JMenuItem("Cut");
        JMenuItem mi6 = new JMenuItem("Copy");
        JMenuItem mi7 = new JMenuItem("Paste");
        JMenuItem mi8 = new JMenuItem("Find & Replace");

        mi5.addActionListener(this);
        mi6.addActionListener(this);
        mi7.addActionListener(this);
        mi8.addActionListener(this);

        m2.add(mi5);
        m2.add(mi6);
        m2.add(mi7);
        m2.addSeparator();
        m2.add(mi8); // aici rămâne Find & Replace în meniul Edit

        JMenuItem mc = new JMenuItem("Close");
        mc.addActionListener(this);

        mb.add(m1);
        mb.add(m2);
        mb.add(mc);

        f.setJMenuBar(mb);
        f.add(new JScrollPane(t));
        f.setSize(600, 500);
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        switch (s) {
            case "Cut":
                t.cut();
                break;
            case "Copy":
                t.copy();
                break;
            case "Paste":
                t.paste();
                break;
            case "Save":
                saveFile();
                break;
            case "Open":
                openFile();
                break;
            case "Print":
                try {
                    t.print();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(f, ex.getMessage());
                }
                break;
            case "New":
                t.setText("");
                break;
            case "Close":
                f.setVisible(false);
                break;
            case "Find & Replace":
                openFindReplaceDialog();
                break;
        }
    }

    // ---------- Find & Replace dialog hrtr ----------
    private void openFindReplaceDialog() {
        JDialog dialog = new JDialog(f, "Find and Replace", false);
        dialog.setSize(420, 220);
        dialog.setLayout(new GridLayout(5, 2, 5, 5));

        JLabel findLabel = new JLabel("Find:");
        JLabel replaceLabel = new JLabel("Replace with:");

        JTextField findField = new JTextField();
        JTextField replaceField = new JTextField();

        JButton findNext = new JButton("Find Next");
        JButton findPrev = new JButton("Find Previous");
        JButton replace = new JButton("Replace");
        JButton replaceAll = new JButton("Replace All");
        JButton close = new JButton("Close");

        dialog.add(findLabel);
        dialog.add(findField);
        dialog.add(replaceLabel);
        dialog.add(replaceField);
        dialog.add(findNext);
        dialog.add(findPrev);
        dialog.add(replace);
        dialog.add(replaceAll);
        dialog.add(close);

        dialog.setLocationRelativeTo(f);

        findNext.addActionListener(ae -> {
            String text = t.getText();
            String find = findField.getText();
            if (find.isEmpty()) return;

            int index = text.indexOf(find, t.getSelectionEnd());
            if (index != -1) {
                t.requestFocus();
                t.select(index, index + find.length());
            } else {
                JOptionPane.showMessageDialog(dialog, "No more matches found.");
            }
        });

        findPrev.addActionListener(ae -> {
            String text = t.getText();
            String find = findField.getText();
            if (find.isEmpty()) return;

            int currentPos = t.getSelectionStart() - 1;
            if (currentPos < 0) currentPos = text.length() - 1;

            int index = text.lastIndexOf(find, currentPos);
            if (index != -1) {
                t.requestFocus();
                t.select(index, index + find.length());
            } else {
                JOptionPane.showMessageDialog(dialog, "No previous matches found.");
            }
        });

        replace.addActionListener(ae -> {
            if (t.getSelectedText() != null && t.getSelectedText().equals(findField.getText())) {
                t.replaceSelection(replaceField.getText());
            }
        });

        replaceAll.addActionListener(ae -> {
            String text = t.getText();
            String find = findField.getText();
            String replaceTxt = replaceField.getText();
            if (find.isEmpty()) return;

            text = text.replace(find, replaceTxt);
            t.setText(text);
            JOptionPane.showMessageDialog(dialog, "All occurrences replaced.");
        });

        close.addActionListener(ae -> dialog.dispose());

        dialog.setVisible(true);
    }

    // ---------- Save ----------
    private void saveFile() {
        JFileChooser j = new JFileChooser("f:");
        int r = j.showSaveDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            File fi = new File(j.getSelectedFile().getAbsolutePath());
            try (BufferedWriter w = new BufferedWriter(new FileWriter(fi))) {
                w.write(t.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        }
    }

    // ---------- Open ----------

    private void openFile() {
        JFileChooser j = new JFileChooser("f:");
        int r = j.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            File fi = new File(j.getSelectedFile().getAbsolutePath());
            try (BufferedReader br = new BufferedReader(new FileReader(fi))) {
                t.read(br, null);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
