import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;
import javax.swing.text.*;

public class Main extends JFrame implements ActionListener {

    JTextArea t;
    JFrame f;

    Main() {
        f = new JFrame("Notepad");

        try {
            // set metal look and feel
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

            // set theme to ocean
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());

        } catch (Exception e) {
            e.printStackTrace();
        }

        t = new JTextArea();

        JMenuBar mb = new JMenuBar();

        // FILE menu
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

        // EDIT menu
        JMenu m2 = new JMenu("Edit");

        JMenuItem mi5 = new JMenuItem("Cut");
        JMenuItem mi6 = new JMenuItem("Copy");
        JMenuItem mi7 = new JMenuItem("Paste");

        mi5.addActionListener(this);
        mi6.addActionListener(this);
        mi7.addActionListener(this);

        m2.add(mi5);
        m2.add(mi6);
        m2.add(mi7);

        JMenuItem mc = new JMenuItem("Close");
        mc.addActionListener(this);

        mb.add(m1);
        mb.add(m2);
        mb.add(mc);

        f.setJMenuBar(mb);
        f.add(t);
        f.setSize(500, 500);
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        if (s.equals("Cut")) {
            t.cut();
        } else if (s.equals("Copy")) {
            t.copy();
        } else if (s.equals("Paste")) {
            t.paste();
        } else if (s.equals("Save")) {
            JFileChooser j = new JFileChooser("f:");
            int r = j.showSaveDialog(null);

            if (r == JFileChooser.APPROVE_OPTION) {
                File fi = new File(j.getSelectedFile().getAbsolutePath());

                try {
                    FileWriter wr = new FileWriter(fi, false);
                    BufferedWriter w = new BufferedWriter(wr);
                    w.write(t.getText());
                    w.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(f, ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(f, "User cancelled the operation");
            }
        } else if (s.equals("Print")) {
            try {
                t.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        } else if (s.equals("Open")) {
            JFileChooser j = new JFileChooser("f:");
            int r = j.showOpenDialog(null);

            if (r == JFileChooser.APPROVE_OPTION) {
                File fi = new File(j.getSelectedFile().getAbsolutePath());

                try {
                    FileReader fr = new FileReader(fi);
                    BufferedReader br = new BufferedReader(fr);
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    t.setText(sb.toString());
                    br.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(f, ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(f, "User cancelled the operation");
            }
        } else if (s.equals("New")) {
            t.setText("");
        } else if (s.equals("Close")) {
            f.setVisible(false);
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
