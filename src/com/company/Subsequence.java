package com.company;

import java.util.List;

@SuppressWarnings("rawtypes")
public class Subsequence {

    public boolean find(List x, List y) {
        if (x == null || y == null)
            throw new IllegalArgumentException();

        while (x.size() != 0 && y.size() != 0) {
            if (x.get(0) == y.get(0)) {
                x.remove(0);
            }
            y.remove(0);
        }
        return x.size() == 0;
    }
}
