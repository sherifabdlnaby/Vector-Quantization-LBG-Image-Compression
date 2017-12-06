import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import javax.imageio.ImageIO;


public class ImageRW {
    public static int height;
    public static int width;
    public static int[][] readImage(String path) {
        BufferedImage img;
        try {
            img = ImageIO.read(new File(path));
            height = img.getHeight();
            width = img.getWidth();
            int[][] imagePixels= new int[height][width];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixel = img.getRGB(x, y);
                    int red = (pixel & 0x00ff0000) >> 16;
                    int grean = (pixel & 0x0000ff00) >> 8;
                    int blue = pixel & 0x000000ff;
                    int alpha = (pixel & 0xff000000) >> 24;
                    imagePixels[y][x] = red;
                }
            }
            return imagePixels;
        } catch (IOException e) {
            return null;
        }
    }

    public static BufferedImage getBufferedImage(int[][] imagePixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            int s;
            if(y == 199)
                s = 13;
            for (int x = 0; x < width; x++) {
                int value = -1 << 24;
                value = 0xff000000 | (imagePixels[y][x] << 16) | (imagePixels[y][x] << 8) | (imagePixels[y][x]);
                image.setRGB(x, y, value);
            }
        }
        return image;
    }

    public static void writeImage(int[][] imagePixels, int width, int height, String outPath) {
        BufferedImage image = getBufferedImage(imagePixels, width, height);
        File ImageFile = new File(outPath);
        try {
            ImageIO.write(image, "jpg", ImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
