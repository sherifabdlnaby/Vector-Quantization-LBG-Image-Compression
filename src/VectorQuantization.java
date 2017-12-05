import java.util.List;

public class VectorQuantization {
    public static void Compress(){

        int vectorHeight = 5;
        int vectorWidth = 5;

        //Read Image
        int[][] image = ImageRW.readImage("C:\\Users\\Sherif\\Desktop\\img.jpg");

        //Calculate Dimensions == to vectorSizes ratio.
        int imageHeight = ImageRW.height;
        int imageWidth = ImageRW.width;
        int matrixHeight = imageHeight % vectorHeight == 0 ? imageHeight : ((imageHeight / vectorHeight) + 1) * vectorHeight;
        int matrixWidth  = imageWidth  % vectorWidth  == 0 ? imageWidth  : ((imageWidth  /  vectorWidth) + 1) * vectorWidth;

        //Scale Image
        int[][] scaledImage = new int[matrixHeight][matrixWidth];
        for (int i = 0; i < matrixHeight; i++) {
            int x = i >= imageHeight ? imageHeight - 1 : i;
            for (int j = 0; j < matrixWidth; j++) {
                int y = j >= imageWidth ? imageWidth - 1 : j;
                scaledImage[i][j] = image[x][y];
            }
        }

    }
}
