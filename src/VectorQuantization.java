import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

public class VectorQuantization {

    public static Vector<Integer> vectorAverage (Vector<Vector<Integer>> Vectors){
        int[] summation = new int[Vectors.get(0).size()];
        
        for (Vector<Integer> vector : Vectors )
            for (int i = 0; i < vector.size(); i++)
                summation[i] += vector.get(i);


        Vector<Integer> returnVector = new Vector<>();
        for (int i = 0; i < summation.length; i++)
            returnVector.add(summation[i] / Vectors.size());
        
        return returnVector;
    }

    public static void Compress(){

        int vectorHeight = 2;
        int vectorWidth = 2;

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

        //Get Array Of Vectors
        int numberOfVectors = (matrixHeight*matrixWidth)/(vectorHeight*vectorWidth);
        Vector<Vector<Integer>> Vectors = new Vector<>();

        //Fill Array Of Vectors
        for (int i = 0; i < matrixHeight; i+= vectorHeight) {
            for (int j = 0; j < matrixWidth; j+= vectorWidth) {
                Vectors.add(new Vector<>());
                for (int x = i; x < i + vectorHeight; x++) {
                    for (int y = j; y < j + vectorWidth; y++) {
                        Vectors.lastElement().add(scaledImage[x][y]);
                    }
                }
            }
        }


        Vector<Integer> mean = vectorAverage(Vectors);

        for (Vector<Integer> vec : Vectors ) {
            int distance1 = 0;
            int distance2 = 0;
            for (int i = 0; i < vec.size(); i++) {
                distance1 += Math.pow(vec.get(i)-mean.get(i), 2);
                distance2 += Math.pow(vec.get(i)-mean.get(i)+1, 2);
            }
        }

    }
}
