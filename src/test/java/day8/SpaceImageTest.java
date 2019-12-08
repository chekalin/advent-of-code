package day8;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static util.FileHelper.getScanner;

class SpaceImageTest {

    @Test
    void splitsEncodedImageIntoLayers() {
        int[] image = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2};
        List<int[][]> layers = SpaceImage.splitIntoLayers(image, 3, 2);

        assertThat(layers).hasSize(2);

        assertThat(layers.get(0)[0]).isEqualTo(new int[]{1, 2, 3});
        assertThat(layers.get(0)[1]).isEqualTo(new int[]{4, 5, 6});

        assertThat(layers.get(1)[0]).isEqualTo(new int[]{7, 8, 9});
        assertThat(layers.get(1)[1]).isEqualTo(new int[]{0, 1, 2});
    }

    @Test
    void decodesImageByApplyingLayers() {
        List<int[][]> layers = List.of(
                new int[][]{
                        {0, 2},
                        {2, 2}},
                new int[][]{
                        {1, 1},
                        {2, 2}},
                new int[][]{
                        {2, 2},
                        {1, 2}},
                new int[][]{
                        {0, 0},
                        {0, 0}}
        );

        int[][] result = SpaceImage.decodeImage(layers);

        assertThat(result).isEqualTo(new int[][]{{0, 1}, {1, 0}});
    }

    @Test
    void assignment() {
        int[] image = readImage();
        List<int[][]> layers = SpaceImage.splitIntoLayers(image, 25, 6);
        int[][] maxZeroLayer = layers.stream().min(Comparator.comparingInt(layer -> SpaceImage.countDigits(layer, 0))).orElseThrow();
        int result = SpaceImage.countDigits(maxZeroLayer, 1) * SpaceImage.countDigits(maxZeroLayer, 2);
        System.out.println("result = " + result);

        System.out.println(SpaceImage.printImage(SpaceImage.decodeImage(layers)));
    }

    private int[] readImage() {
        Scanner scanner = getScanner("day8/input.txt");
        char[] chars = scanner.nextLine().toCharArray();
        int[] image = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            image[i] = Character.getNumericValue(chars[i]);
        }
        return image;
    }
}