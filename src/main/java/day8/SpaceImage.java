package day8;

import java.util.ArrayList;
import java.util.List;

class SpaceImage {

    static List<int[][]> splitIntoLayers(int[] encodedImage, int width, int height) {
        List<int[][]> layers = new ArrayList<>();
        int imageSize = encodedImage.length;
        int numberOfLayers = imageSize / (width * height);
        for (int i = 0; i < numberOfLayers; i++) {
            int layerStartPosition = i * width * height;
            layers.add(createLayerStartingFrom(encodedImage, layerStartPosition, width, height));
        }
        return layers;
    }

    private static int[][] createLayerStartingFrom(int[] encodedImage, int layerStartPosition, int width, int height) {
        int[][] layer = new int[height][width];
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int sourcePosition = layerStartPosition + (h * width + w);
                layer[h][w] = encodedImage[sourcePosition];
            }
        }
        return layer;
    }

    static int countDigits(int[][] layer, int digit) {
        int count = 0;
        for (int[] row : layer) {
            for (int i : row) {
                if (i == digit) {
                    count++;
                }
            }
        }
        return count;
    }

    private static final int BLACK = 0;
    private static final int WHITE = 1;
    private static final int TRANSPARENT = 2;

    static int[][] decodeImage(List<int[][]> layers) {
        if (layers.isEmpty()) throw new IllegalArgumentException("Layers are empty");
        int[][] first = layers.get(0);
        if (layers.size() == 1) {
            return first;
        }
        int height = first.length;
        int width = first[0].length;
        int[][] result = first.clone();
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                for (int i = 1; i < layers.size(); i++) {
                    int[][] layer = layers.get(i);
                    if (result[h][w] == TRANSPARENT && layer[h][w] != TRANSPARENT) {
                        result[h][w] = layer[h][w];
                    }
                }
            }
        }
        return result;
    }

    static String printImage(int[][] image) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] row : image) {
            for (int pixel : row) {
                switch (pixel) {
                    case WHITE:
                        stringBuilder.append("X ");
                        break;
                    case BLACK:
                    case TRANSPARENT:
                        stringBuilder.append("  ");
                        break;
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
