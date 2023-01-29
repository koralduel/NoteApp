package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityLoginPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.view.View;

public class LoginPage extends AppCompatActivity {


     FirebaseAuth firebaseAuth;
     ActivityLoginPageBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        //handling login try->check user detail when click on the login button
        binding.BtnLogin.setOnClickListener(view -> {
            String email = binding.emailValue.getText().toString();
            String password = binding.passwordValue.getText().toString();
            //check email is not empty (if it does set an error)
            if (TextUtils.isEmpty(email)) {
                binding.emailValue.setError("Email cannot be null");
                binding.passwordValue.requestFocus();
                //check password is not empty (if it does set an error)
            } else if (TextUtils.isEmpty(password)) {
                binding.passwordValue.setError("Password cannot be null");
                binding.passwordValue.requestFocus();
            } else {
                //if both password and email are not empty -> try to login
                loginUser(email, password);
            }
        });
        //go to signup page
        binding.BtnSignUp.setOnClickListener(view ->{
            Intent intent = new Intent(getApplicationContext(),RegisterPage.class);
            startActivity(intent);
        });
    }

    //check if a user is already login -> goes to noteList page
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!= null){
            startActivity(new Intent(this,NoteListPage.class));
        }
    }

    //login try -> if user exist in firebase
    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            //if firebaseAuth succeeded (email and password are exist) -> toast + login and go to NoteList page
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), NoteListPage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                //if the user does not exist in firebase
                else {
                    Toast.makeText(getApplicationContext(), "Login Error: " + "Incorrect email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}