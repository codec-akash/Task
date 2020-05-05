package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class Profile extends AppCompatActivity {

    TextView profile_name;
    TextView profile_email;
    TextView profile_age;
    TextView profile_country;

    public void getPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getPhoto();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.image){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            } else {
                getPhoto();
            }
        } else if (item.getItemId() == R.id.viewImage){
            Intent intent = new Intent(Profile.this,ImageFeed.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImage = data.getData();

        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);

                byte[] bytes = byteArrayOutputStream.toByteArray();

                ParseFile file = new ParseFile("images.png",bytes);
                ParseObject object = new ParseObject("Image");

                object.put("image",file);
                object.put("username",ParseUser.getCurrentUser().getUsername());

                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            Toast.makeText(Profile.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Profile.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_age = (TextView) findViewById(R.id.profile_age);
        profile_name = (TextView) findViewById(R.id.name);
        profile_country = (TextView) findViewById(R.id.profile_country);
        profile_email = (TextView) findViewById(R.id.profile_email);

        //Toast.makeText(this, ParseUser.getCurrentUser().get("username").toString(), Toast.LENGTH_SHORT).show();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();

            profile_name.setText(personName);
            profile_email.setText(personEmail);

        }

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null){
            profile_name.setText(currentUser.getUsername());
            profile_email.setText(currentUser.getEmail());
            profile_country.setText(currentUser.get("country").toString());
            profile_age.setText(currentUser.get("age").toString());
        }
    }
}
