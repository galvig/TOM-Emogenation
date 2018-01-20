package com.example.a310233826.emogenation;

import java.util.List;
import java.util.Random;

/**
 * Created by 310233826 on 20/12/2017.
 */

public class Utils {
    public static <T> void ShuffleList(List<T> lst) {
        int n = lst.size();
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(lst, i, change);
        }
    }

    private static <T> void swap(List<T> lst, int i, int change) {
        T helper = lst.get(i);
        lst.set(i, lst.get(change));
        lst.set(change, helper);
    }
}
