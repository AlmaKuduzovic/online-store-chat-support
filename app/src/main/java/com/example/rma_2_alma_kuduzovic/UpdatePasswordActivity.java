package com.example.rma_2_alma_kuduzovic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class UpdatePasswordActivity extends AppCompatActivity {

    private static final String TAG = "UpdatePasswordActivity";
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        ImageView imageViewCheck = findViewById(R.id.imageViewCheck);
        EditText newPasswordEditText = findViewById(R.id.editTextNewPassword);
        EditText currentPasswordEditText = findViewById(R.id.editTextCurrentPassword);

        imageViewCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPasswordEditText.getText().toString().trim();
                String currentPassword = currentPasswordEditText.getText().toString().trim();

                if (newPassword.isEmpty() || currentPassword.isEmpty()) {
                    Toast.makeText(UpdatePasswordActivity.this, "Please enter new password and current password", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(UpdatePasswordActivity.this, "Password successfully changed.", Toast.LENGTH_SHORT).show();
                                            updateUserPasswordInDatabase(user.getUid(), newPassword);
                                            Intent intent = new Intent(UpdatePasswordActivity.this, EditUserActivity.class);
                                        } else {
                                            Log.e(TAG, "Failed to update password: " + task.getException());
                                            Toast.makeText(UpdatePasswordActivity.this, "Failed to change password: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Log.e(TAG, "Re-authentication failed: " + task.getException());
                                Toast.makeText(UpdatePasswordActivity.this, "Re-authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "User is null");
                    Toast.makeText(UpdatePasswordActivity.this, "User is not logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUserPasswordInDatabase(String userId, String newPassword) {

        databaseReference.child(userId).child("userPassword").setValue(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated in database successfully");
                        } else {
                            Log.e(TAG, "Failed to update user password in database: " + task.getException());
                        }
                    }
                });
    }
}
