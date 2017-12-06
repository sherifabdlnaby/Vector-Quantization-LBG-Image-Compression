import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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
    private JScrollPane imagePlaceholder;
    private JButton originalCompressedButton;
    private File CompressFile;
    private File DecompressFile;
    private BufferedImage originalImage;
    private BufferedImage compressedImage;
    private JLabel image = new JLabel();
    private boolean compressedImgActive = false;

    private void switchImage(Boolean True)
    {
        if(True)
        {
            if(compressedImage == null)
                return;
            image.setIcon(new ImageIcon(compressedImage));
            image.setHorizontalAlignment(JLabel.CENTER);
            imagePlaceholder.getViewport().add(image);
            compressedImgActive = true;
        }
        else
        {
            if(originalImage == null)
                return;
            image.setIcon(new ImageIcon(originalImage));
            image.setHorizontalAlignment(JLabel.CENTER);
            imagePlaceholder.getViewport().add(image);
            compressedImgActive = false;
        }
        if(originalImage != null && compressedImage != null)
            originalCompressedButton.setEnabled(true);
    }

    public Appform() {

        Vheight.setValue(2);
        Vwidth.setValue(2);
        Vsize.setValue(64);

        Compress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String path = CompressFile.getAbsolutePath();
                    VectorQuantization.Compress((int)Vheight.getValue(), (int)Vwidth.getValue(), (int)Vsize.getValue(), path);
                    VectorQuantization.Decompress(VectorQuantization.getCompressedPath(path));
                    compressedImage = ImageIO.read(new File(VectorQuantization.getDecompressedPath(path)));
                    switchImage(true);
                    JOptionPane.showMessageDialog(null,"WUBBAA LUBBA DUB DUB IT'S COMPRESSED!!");
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null,"Error Occurred Soz!");
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
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
                try {
                    originalImage = ImageIO.read(new File(CompressFile.getAbsolutePath()));
                    switchImage(false);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null,"Unreadable Image, please select an Image");
                }
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
                    String path = DecompressFile.getAbsolutePath();
                    VectorQuantization.Decompress(path);
                    compressedImage = ImageIO.read(new File(VectorQuantization.getDecompressedPath(path)));
                    switchImage(true);
                    JOptionPane.showMessageDialog(null,"WUBBAA LUBBA DUB DUB IT'S DECOMPRESSED!!");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Error Occurred Soz!");
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        originalCompressedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchImage(!compressedImgActive);
            }
        });
    }


    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Vector Quantization");
        jFrame.setContentPane(new Appform().Panel);
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
