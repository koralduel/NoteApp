package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityRegisterPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends AppCompatActivity {

    FirebaseAuth mAuth;
    private ActivityRegisterPageBinding binding;
    DatabaseReference databaseReference;
    private Boolean gotError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");


        //check if mail is in format of email address->if not -> set an error
        binding.emailValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String email = binding.emailValue.getText().toString();
                 if (!email.matches(emailPattern)){
                    binding.emailValue.setError("Invalid email format");
                    binding.emailValue.requestFocus();
                    gotError = true;
                }else{gotError = false;}
            }
        });

        //check if password is at least 6 chars->if not -> set an error
        binding.passwordValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String password = binding.passwordValue.getText().toString();
                if(password.length()<6){
                    binding.passwordValue.setError("Password must contain at least 6 chars");
                    binding.passwordValue.requestFocus();
                    gotError = true;
                }else{gotError = false;}
            }
        });

        //handling register button click
        binding.BtnRegister.setOnClickListener(view -> {
            String name= binding.nameValue.getText().toString();
            String email= binding.emailValue.getText().toString();
            String password= binding.passwordValue.getText().toString();
            if(TextUtils.isEmpty(name)) {
                binding.nameValue.setError("Full Name cannot be null");
                binding.nameValue.requestFocus();
            }else if(TextUtils.isEmpty(email) ) {
                binding.emailValue.setError("Email cannot be null");
                binding.emailValue.requestFocus();
            } else if(TextUtils.isEmpty(password)) {
                binding.passwordValue.setError("Password cannot be null");
                binding.passwordValue.requestFocus();
            }
            else {
                //try to create user in db
                createUser(name,email,password);
            }
        });

        //handling sign in button click
        binding.btnSignIn.setOnClickListener(view ->{
            Intent intent = new Intent(getApplicationContext(),LoginPage.class);
            startActivity(intent);
        });
    }
    //try to create the new user in firebaseAuth
    private void createUser(String name,String email,String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            //user successfully created in firebaseAuth
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.UserCreatedSuccessfully, Toast.LENGTH_LONG).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = user.getUid();
                    // after created in firebaseAuth -> save user in realTime database under users->userID
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
                    User currentUser = new User(userID,email,name);

                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();
                    user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

                    databaseReference.setValue(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), R.string.registerSuccessfully, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });

                }else{
                    //email or password not valid by the check above
                    if(!gotError)
                    {
                        //a user with the same mail already exist
                        Toast.makeText(getApplicationContext(), R.string.errorRegister,Toast.LENGTH_LONG).show();
                    }
                }

            }

        });

    }
}