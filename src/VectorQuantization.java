import java.io.*;
import java.util.*;

public class VectorQuantization {

    public static Vector<Integer> vectorAverage (Vector<Vector<Integer>> Vectors)
    {
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
        if(Level == 1 || Vectors.size() == 0)
        {
            if(Vectors.size() > 0)
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

    public static boolean Compress(int VH, int VW, int lvl, String Path) throws IOException{

        int vectorHeight = VH;
        int vectorWidth = VW;

        //Read Image
        int[][] image = ImageRW.readImage(Path);

        //Calculate new dimensions to vectorSizes ratio.
        int originalHeight = ImageRW.height;
        int originalWidth  = ImageRW.width;
        int scaledHeight = originalHeight % vectorHeight == 0 ? originalHeight : ( (originalHeight / vectorHeight) + 1) * vectorHeight;
        int scaledWidth  = originalWidth  % vectorWidth  == 0 ? originalWidth  : ( (originalWidth  /  vectorWidth) + 1) * vectorWidth;

        //Scale Image (Adding Padding)
        int[][] scaledImage = new int[scaledHeight][scaledWidth];
        for (int i = 0; i < scaledHeight; i++) {
            int x = i >= originalHeight ? originalHeight - 1 : i;
            for (int j = 0; j < scaledWidth; j++) {
                int y = j >= originalWidth ? originalWidth - 1 : j;
                scaledImage[i][j] = image[x][y];
            }
        }

        //Create Array Of Vectors
        Vector<Vector<Integer>> Vectors = new Vector<>();

        //Divide into Vectors and fill The Array Of Vectors
        for (int i = 0; i < scaledHeight; i+= vectorHeight) {
            for (int j = 0; j < scaledWidth; j+= vectorWidth) {
                Vectors.add(new Vector<>());
                for (int x = i; x < i + vectorHeight; x++) {
                    for (int y = j; y < j + vectorWidth; y++) {
                        Vectors.lastElement().add(scaledImage[x][y]);
                    }
                }
            }
        }

        //Create Array to hold Quantized Vectors
        Vector<Vector<Integer>> Quantized = new Vector<>();

        //Fill Quantized Vector (The recursive part)
        Quantize(lvl, Vectors, Quantized);

        //Optimize
        Vector<Integer> VectorsToOptimizeIndices = Optimize(Vectors, Quantized);

        //Write using Java's Object Serialization
        FileOutputStream fileOutputStream = new FileOutputStream(Path+"x");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        //Write
        objectOutputStream.writeObject(ImageRW.width);
        objectOutputStream.writeObject(ImageRW.height);
        objectOutputStream.writeObject(scaledWidth);
        objectOutputStream.writeObject(scaledHeight);
        objectOutputStream.writeObject(vectorWidth);
        objectOutputStream.writeObject(vectorHeight);
        objectOutputStream.writeObject(VectorsToOptimizeIndices);
        objectOutputStream.writeObject(Quantized);
        objectOutputStream.close();
        return true;
    }

    public static boolean Decompress(String Path) throws IOException, ClassNotFoundException {

        InputStream file = new FileInputStream(Path);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);

        //Read Saved Tags
        int width = (int) input.readObject();
        int height = (int) input.readObject();
        int scaledWidth = (int) input.readObject();
        int scaledHeight = (int) input.readObject();
        int vectorWidth = (int) input.readObject();
        int vectorHeight = (int) input.readObject();
        Vector<Integer> VectorsToOptimizeIndices = (Vector<Integer>)input.readObject();
        Vector<Vector<Integer>> Quantized = (Vector<Vector<Integer>>) input.readObject();


        ////////////REWRITE
        int[][] newImg = new int[scaledHeight][scaledWidth];

        for (int i = 0; i < VectorsToOptimizeIndices.size(); i++) {
            int x = i / (scaledWidth / vectorWidth);
            int y = i % (scaledWidth / vectorWidth);
            x *= vectorHeight;
            y *= vectorWidth;
            int v = 0;
            for (int j = x; j < x + vectorHeight; j++) {
                for (int k = y; k < y + vectorWidth; k++) {
                    newImg[j][k] = Quantized.get(VectorsToOptimizeIndices.get(i)).get(v++);
                }
            }
        }
        ImageRW.writeImage(newImg, width, height, Path.substring(0,Path.length()-1));
        return true;
    }
}
