package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    TextView alsoKnownAs;
    TextView placeOfOrigin;
    TextView describtion;
    TextView ingredients;
    ImageView ingredientsIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ingredientsIv = findViewById(R.id.image_iv);
        alsoKnownAs = findViewById(R.id.also_known_tv);
        placeOfOrigin = findViewById(R.id.place_of_origin_tv);
        describtion = findViewById(R.id.description_tv);
        ingredients = findViewById(R.id.ingredients_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        failSafeLoadImage(ingredientsIv, sandwich.getImage());
        failSafeSetText(placeOfOrigin, sandwich.getPlaceOfOrigin());
        failSafeSetText(alsoKnownAs, sandwich.getAlsoKnownAs());
        failSafeSetText(ingredients, sandwich.getIngredients());
        failSafeSetText(describtion, sandwich.getDescription());
    }

    private void failSafeLoadImage(ImageView imageView, String imagePath) {
        if (imageView == null || imagePath.isEmpty()) {
            return;
        }
        Picasso.with(this)
                .load(imagePath)
                .into(imageView);
    }

    private void failSafeSetText(TextView tv, String value) {
        if (!value.isEmpty()) {
            tv.setText(value);
        } else {
            tv.setText(R.string.not_available_text);
        }

    }

    private void failSafeSetText(TextView tv, List<String> value) {
        if (!value.isEmpty()) {
            tv.setText(TextUtils.join(", ", value));
        } else {
            tv.setText(R.string.not_available_text);
        }
    }
}
