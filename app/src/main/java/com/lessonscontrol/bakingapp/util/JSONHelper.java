package com.lessonscontrol.bakingapp.util;

import android.content.res.AssetManager;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class JSONHelper {

    private JSONHelper() {
        //Empty
    }

    public static <T> List<T> loadObjectListFromJSONAsset(AssetManager assetManager, String assetName, Class<T> objectsClass) {
        try {
            InputStream inputStream = assetManager.open(assetName);
            ObjectMapper objectMapper = new ObjectMapper();
            CollectionType type = objectMapper
                    .getTypeFactory()
                    .constructCollectionType(List.class, objectsClass);
            return objectMapper.readValue(inputStream, type);
        } catch (IOException e) {
            Log.e(JSONHelper.class.getName(), "Error loading asset.");
            e.printStackTrace();
            return null;
        }
    }

}
