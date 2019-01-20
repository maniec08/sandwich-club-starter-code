package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    private static final String KEY_NAME = "name";
    private static final String KEY_MAIN_NAME = "mainName";
    private static final String KEY_ALSO_KNOWN_AS = "alsoKnownAs";
    private static final String KEY_PLACE_OF_ORIGIN = "placeOfOrigin";
    private static final String KEY_DESCRIBTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_INGREDIENTS = "ingredients";

    /**
     * Parse Json Data and return Sandwich object
     * null on any JSONException, May be
     * Sample Struct  {"name":{"mainName":"Bosna","alsoKnownAs":["Bosner"]},"placeOfOrigin":"Austria",
     * "description":"Bosna is a spicy Austrian fast food dish, said to have originated in either Salzburg or Linz. It is now popular all over western Austria and southern Bavaria.",
     * "image":"https://upload.wikimedia.org/wikipedia/commons/c/ca/Bosna_mit_2_Bratw%C3%BCrsten.jpg",
     * "ingredients":["White bread","Bratwurst","Onions","Tomato ketchup","Mustard","Curry powder"]}
     * @param json json to parse
     * @return Sandwich Struct
     */
    public static Sandwich parseSandwichJson(String json) {
        Sandwich sandwich = new Sandwich();
        try {
            JSONObject sandwichJson = new JSONObject(json);
            JSONObject nameDetailsJson = sandwichJson.getJSONObject(KEY_NAME);
            sandwich.setMainName(nameDetailsJson.optString(KEY_MAIN_NAME, ""));
            sandwich.setAlsoKnownAs(getJsonArrayAsList(nameDetailsJson, KEY_ALSO_KNOWN_AS));
            sandwich.setPlaceOfOrigin(sandwichJson.optString(KEY_PLACE_OF_ORIGIN, ""));
            sandwich.setDescription(sandwichJson.optString(KEY_DESCRIBTION, ""));
            sandwich.setImage(sandwichJson.optString(KEY_IMAGE, ""));
            sandwich.setIngredients(getJsonArrayAsList(sandwichJson, KEY_INGREDIENTS));
            return sandwich;
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * Retrieve a jsonarray for the given key and convert json array to list
     * Empty list is returned on JSON Exception with key retrieval
     * Skips the object, if JSON array produces jsonexception due to data types other than String
     *
     * @param json input json
     * @param key  key to get the Json array
     * @return list of strings/ empty list on JsonException
     */
    public static List<String> getJsonArrayAsList(JSONObject json, String key) {
        JSONArray jsonArray;
        try {
            jsonArray = json.getJSONArray(key);
        } catch (JSONException e) {
            return new ArrayList<>();
        }
        List<String> returnList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {

                returnList.add(jsonArray.getString(i));
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }

        return returnList;
    }
}
