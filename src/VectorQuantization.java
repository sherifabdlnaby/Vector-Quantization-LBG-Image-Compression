import java.util.*;

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

    public static int EuclidDistance(Vector<Integer> x, Vector<Integer> y)
    {
        int distance = 0;
        for (int i = 0; i < x.size(); i++)
            distance += Math.pow(x.get(i) - y.get(i), 2);
        return (int) Math.sqrt(distance);
    }
    public static void Quantize(int Level, Vector<Vector<Integer>> Vectors, Vector<Vector<Integer>> Quantized)
    {
        if(Level == 1)
        {
            Quantized.add(vectorAverage(Vectors));
            return;
        }
        //Split
        Vector<Vector<Integer>> leftVectors = new Vector<>();
        Vector<Vector<Integer>> rightVectors =  new Vector<>();
        Vector<Integer> mean = vectorAverage(Vectors);

        //Calculate Euclidean Distance
        for (Vector<Integer> vec : Vectors ) {
            int eDistance1 = 0;
            int eDistance2 = 0;
            for (int i = 0; i < vec.size(); i++) {
                eDistance1 += Math.pow(vec.get(i)-mean.get(i), 2);
                eDistance2 += Math.pow(vec.get(i)-mean.get(i)+1, 2);
                eDistance1 = (int) Math.sqrt(eDistance1);
                eDistance2 = (int) Math.sqrt(eDistance2);
            }
            //Add To Right OR Left Vector
            if(eDistance1 >= eDistance2)
                leftVectors.add(vec);
            else
                rightVectors.add(vec);
        }
        //Recurse
        Quantize(Level / 2, leftVectors, Quantized);
        Quantize(Level / 2, rightVectors, Quantized);
    }
    public static Vector<Integer> Optimize(Vector<Vector<Integer>> Vectors, Vector<Vector<Integer>> Quantized)
    {
        Vector<Integer> VectorsToOptimizeIndices = new Vector<>();
        for (Vector<Integer> vector : Vectors ) {
            int smallestDistance = EuclidDistance(vector, Quantized.get(0));
            int smallestIndex = 0;
            for (int i = 1; i < Quantized.size(); i++) {
                int tempDistance = EuclidDistance(vector, Quantized.get(i));
                if(tempDistance < smallestDistance)
                {
                    smallestDistance = tempDistance;
                    smallestIndex = i;
                }
            }
            VectorsToOptimizeIndices.add(smallestIndex);
        }
        return VectorsToOptimizeIndices;
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

        Vector<Vector<Integer>> Quantized = new Vector<>();

        Quantize(4, Vectors, Quantized);




    }
}
