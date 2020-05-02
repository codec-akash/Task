package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ImageFeed extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_feed);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");

        String username = ParseUser.getCurrentUser().getUsername();
        query.whereEqualTo("username",username);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0){
                    for (ParseObject object : objects){
                        ParseFile parseFile = (ParseFile) object.get("image");

                        parseFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null && data != null){
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);

                                    imageView =new ImageView(getApplicationContext());
                                    imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                    ));

                                    imageView.setImageBitmap(bitmap);

                                    linearLayout.addView(imageView);
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
