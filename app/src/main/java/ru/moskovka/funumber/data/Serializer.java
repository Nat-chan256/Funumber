package ru.moskovka.funumber.data;

import android.content.Context;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class Serializer {
    private static final String FILE_NAME = "facts.json";

    public static boolean serialize(Context context, Map<String, String> facts) throws IOException {
        Gson gson = new Gson();
        DataItems dataItems = new DataItems();
        dataItems.setFacts(facts);
        String jsonString = gson.toJson(dataItems);

        try(FileOutputStream fileOutputStream
                    = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)){
            fileOutputStream.write(jsonString.getBytes(StandardCharsets.UTF_8));
            return true;
        }
    }

    public static Map<String, String> deserialize(Context context) throws IOException {
        try(FileInputStream fileInputStream = context.openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream)){
            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(inputStreamReader, DataItems.class);
            return dataItems.getFacts();
        }
    }

    private static class DataItems{
        private Map<String, String> facts;

        public Map<String, String> getFacts() {
            return facts;
        }

        public void setFacts(Map<String, String> songs) {
            this.facts = songs;
        }
    }
}
