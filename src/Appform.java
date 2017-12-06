import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Appform {
    private JButton Compress;
    private JPanel Panel;
    private JTextField CompressPath;
    private JButton Browse;
    private JButton browseButton;
    private JButton decompressButton;
    private JTextField DecompressPath;
    private JSpinner Vheight;
    private JSpinner Vwidth;
    private JSpinner Vsize;
    private File CompressFile;
    private File DecompressFile;
    public Appform() {
        Compress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(VectorQuantization.Compress((int)Vheight.getValue(), (int)Vwidth.getValue(), (int)Vsize.getValue(), CompressFile.getAbsolutePath()))
                    {
                        JOptionPane.showMessageDialog(null,"WUBBAA LUBBA DUB DUB IT'S COMPRESSED MORTY!!");
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null,"Error Occurred Soz!");
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        Browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser browser = new JFileChooser();
                browser.showOpenDialog(null);
                CompressFile = browser.getSelectedFile();
                CompressPath.setText(CompressFile.getAbsolutePath());
            }
        });
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser browser = new JFileChooser();
                browser.showOpenDialog(null);
                DecompressFile = browser.getSelectedFile();
                DecompressPath.setText(DecompressFile.getAbsolutePath());
            }
        });
        decompressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(VectorQuantization.Decompress(DecompressFile.getAbsolutePath()))
                    {
                        JOptionPane.showMessageDialog(null,"WUBBAA LUBBA DUB DUB IT'S DECOMPRESSED MORTY!!");
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null,"Error Occurred Soz!");
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }


    public static void main(String[] args) {
        File compressFile;
        File decompressFile;
        JFrame jFrame = new JFrame("Vector Quantization");
        jFrame.setContentPane(new Appform().Panel);
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
