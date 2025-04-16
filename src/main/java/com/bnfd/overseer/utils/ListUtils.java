package com.bnfd.overseer.utils;

import java.util.List;

public class ListUtils {
    public static <T> List<T> shrinkTo(List<T> list, int newSize) {
        return list.subList(0, newSize - 1);
    }
}
