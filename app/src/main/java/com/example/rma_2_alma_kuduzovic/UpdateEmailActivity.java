package com.example.rma_2_alma_kuduzovic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateEmailActivity extends AppCompatActivity {

    private static final String TAG = "UpdateEmailActivity";
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_email);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        ImageView imageViewCheck = findViewById(R.id.imageViewCheck);
        EditText newEmailEditText = findViewById(R.id.editTextEmail);
        EditText passwordEditText = findViewById(R.id.editTextPassword);

        imageViewCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = newEmailEditText.getText().toString().trim();
                String userPassword = passwordEditText.getText().toString().trim();

                if (newEmail.isEmpty() || userPassword.isEmpty()) {
                    Toast.makeText(UpdateEmailActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), userPassword);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            updateUserEmailInDatabase(user.getUid(), newEmail);
                                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(UpdateEmailActivity.this, "Email successfully changed. Verification email sent.", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(UpdateEmailActivity.this, EditUserActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        Log.e(TAG, "Failed to send verification email: " + task.getException());
                                                        Toast.makeText(UpdateEmailActivity.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Log.e(TAG, "Failed to update email: " + task.getException());
                                            Toast.makeText(UpdateEmailActivity.this, "Failed to change email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Log.e(TAG, "Re-authentication failed: " + task.getException());
                                Toast.makeText(UpdateEmailActivity.this, "Re-authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "User is null");
                    Toast.makeText(UpdateEmailActivity.this, "User is not logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUserEmailInDatabase(String userId, String newEmail) {
        databaseReference.child(userId).child("userEmail").setValue(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email updated in database successfully");
                        } else {
                            Log.e(TAG, "Failed to update user email in database: " + task.getException());
                        }
                    }
                });
    }
}
