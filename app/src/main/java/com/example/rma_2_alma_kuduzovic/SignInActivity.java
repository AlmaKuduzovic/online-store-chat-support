package com.example.rma_2_alma_kuduzovic;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    EditText userEmail, userPassword;
    TextView siginBtn, signUpBtn;
    String email, password;
    private DatabaseHelper databaseHelper;
    private CardDao cardDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        userEmail = findViewById(R.id.emailText);
        userPassword = findViewById(R.id.passwordText);
        siginBtn = findViewById(R.id.login);
        signUpBtn = findViewById(R.id.signup);

        databaseHelper = DatabaseHelper.getDB(this);
        cardDao = databaseHelper.cardDao();

     /*  deleteAllCards();*/
        /*cardDao.insert(new CardModel(R.drawable.mrkva, "Carrot", 3.45));
        cardDao.insert(new CardModel(R.drawable.avocado, "Avokado", 6.35));
        cardDao.insert(new CardModel(R.drawable.apple, "Apple", 1.50));
        cardDao.insert(new CardModel(R.drawable.ananas, "Ananas", 7.80));
        cardDao.insert(new CardModel(R.drawable.pear, "Pear", 9.20));
        cardDao.insert(new CardModel(R.drawable.mango, "Mango", 10.00));
        cardDao.insert(new CardModel(R.drawable.lime, "Lime", 12.20));
        cardDao.insert(new CardModel(R.drawable.papaya, "Papaya", 15.60));*/

        siginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = userEmail.getText().toString().trim();
                password = userPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    userEmail.setError("Please enter your email");
                    userEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    userPassword.setError("Please enter your password");
                    userPassword.requestFocus();
                    return;
                }
                signIn();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
            finish();
        }
    }

    private void signIn() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            String username = user.getDisplayName();
                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                            intent.putExtra("name", username);
                            intent.putExtra("password", password);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignInActivity.this, "Please verify your email to sign in", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignInActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
  /*private void deleteAllCards() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                cardDao.deleteAll();
            }
        }).start();
    }*/}