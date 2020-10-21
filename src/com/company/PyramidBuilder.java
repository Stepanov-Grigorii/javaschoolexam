package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PyramidBuilder {

    public int[][] buildPyramid(List<Integer> input) {

        int height = getHeight(input.size());
        int base = height * 2 - 1;
        int k = 0;
        int[][] pyramid = new int[height][base];

        if(input.contains(null)) {
            throw new CannotBuildPyramidException();
        }

        List<Integer> inputNumbers = new ArrayList<>(input);

        Collections.sort(inputNumbers);

        for (int i = height - 1; i >= 0; i--) {
            for (int j = base - 1; j >= k; j = j - 2) {
                pyramid[i][j] = inputNumbers.remove(inputNumbers.size() - 1);
            }
            base--;
            k++;
        }
        return pyramid;
    }

    public int getHeight(int size) throws CannotBuildPyramidException {
        if (size > 2) {
            double x = (Math.pow(size * 8 + 1, 0.5) - 1) / 2;
            if (x % 1 == 0.)
                return (int) x;
        }
        throw new CannotBuildPyramidException();
    }
}
