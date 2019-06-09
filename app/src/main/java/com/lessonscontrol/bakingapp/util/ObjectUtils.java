package com.lessonscontrol.bakingapp.util;

public final class ObjectUtils {

    private ObjectUtils() {
        //Empty
    }

    public static <T> T nvl(T object, T defaultValue) {
        return object != null ? object : defaultValue;
    }
}
