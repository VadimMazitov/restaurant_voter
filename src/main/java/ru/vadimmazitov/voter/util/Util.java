package ru.vadimmazitov.voter.util;

import org.springframework.lang.Nullable;

public class Util {

    private Util() {
    }

    public static <T extends Comparable<? super T>> boolean isBefore(T value, @Nullable T end) {
        return end == null || value.compareTo(end) <= 0;
    }

}
