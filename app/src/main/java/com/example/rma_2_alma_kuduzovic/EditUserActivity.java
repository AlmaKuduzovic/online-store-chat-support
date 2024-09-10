package com.example.rma_2_alma_kuduzovic;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class EditUserActivity extends AppCompatActivity {

    String email, password;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String username = user.getDisplayName();
    String userEmail = user.getEmail();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        ImageView thirdIcon = findViewById(R.id.firstIcon);
        thirdIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditUserActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewEmail = findViewById(R.id.textViewEmail);
        TextView textViewPassword = findViewById(R.id.textViewPassword);

        textViewName.setText(username);
        textViewEmail.setText(userEmail);
        /*textViewPassword.setText(userPassword);*/
        textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditUserActivity.this, UpdateNameActivity.class);
                startActivity(intent);
            }
        });

        textViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditUserActivity.this, UpdateEmailActivity.class);
                /*intent.putExtra("password", userPassword); */
                startActivity(intent);
            }
        });

        textViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditUserActivity.this, UpdatePasswordActivity.class);
                startActivity(intent);
            }
        });

    }
}
