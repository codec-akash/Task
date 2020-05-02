package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import static android.widget.Toast.LENGTH_LONG;

public class SignUp extends AppCompatActivity {

    EditText user_name;
    EditText password;
    EditText email;
    EditText age;
    EditText country;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign_UP");

        user_name = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.pass_word);
        email = (EditText) findViewById(R.id.email);
        age = (EditText) findViewById(R.id.age);
        country = (EditText) findViewById(R.id.country);
        btn = (Button) findViewById(R.id.signUp);

    //   app();

       btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ParseUser user = new ParseUser();
               if (user_name.getText().toString().matches("") || password.getText().toString().matches("")){
                   Toast.makeText(getApplicationContext(), "Username and Password are required", Toast.LENGTH_SHORT).show();
               } else {
                   user.setUsername(user_name.getText().toString().trim());
                   user.setPassword(password.getText().toString().trim());
                   user.setEmail(email.getText().toString());
                   user.put("age",age.getText().toString().trim());
                   user.put("country",country.getText().toString().trim());

                   user.signUpInBackground(new SignUpCallback() {
                       @Override
                       public void done(ParseException e) {
                           if (e == null){
                               Toast.makeText(SignUp.this, "Done", Toast.LENGTH_SHORT).show();
                           } else {
                               Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }
           }
       });
    }

   /* public void app(){
        // password:- CS7GBBDgbP8X

//        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("myappID")
                .clientKey("CS7GBBDgbP8X")
                .server("http://13.126.117.96/parse/")
                .build()
        );

        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL,true);
    }

    */
}
