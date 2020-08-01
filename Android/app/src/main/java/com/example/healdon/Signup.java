package com.example.healdon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    EditText name,username,email,password;
    Button signup;
    private FirebaseAuth mAuth;


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!= null)
        {
             //handel already login user
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

       name = findViewById(R.id.name);
       username = findViewById(R.id.username);
       email = findViewById(R.id.email);
       password = findViewById(R.id.password);
       signup = findViewById(R.id.signup);
       mAuth = FirebaseAuth.getInstance();





            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registeruser();
                }
            });











    }




    private void registeruser() {

        final String names = name.getText().toString();
        final String usernames = username.getText().toString();
        final String emails = email.getText().toString();
        String passwords = password.getText().toString();

        if(names.isEmpty())
        {
            name.setError("Enter valid name ");
        }


        if(usernames.isEmpty())
        {
            name.setError("Enter valid username ");
        }


        if(emails.isEmpty())
        {
            name.setError("Enter valid email ");
        }


        if(passwords.isEmpty())
        {
            name.setError("Enter valid password ");
        }
  //      @Override
    //    public void onStart() {
      //      super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
        //    FirebaseUser currentUser = mAuth.getCurrentUser();
            //updateUI(currentUser);
      //  }

        mAuth.createUserWithEmailAndPassword(emails, passwords)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user u = new user(
                               names,emails,usernames
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {

                                        Toast.makeText(Signup.this, "Successful",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Signup.this,Homepage.class);
                                        startActivity(intent);



                                    }

                                    else
                                    {
                                        Toast.makeText(Signup.this, "Failed",Toast.LENGTH_LONG).show();
                                    }

                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });





    }



}


