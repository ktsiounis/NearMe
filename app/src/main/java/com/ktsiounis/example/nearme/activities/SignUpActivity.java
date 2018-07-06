package com.ktsiounis.example.nearme.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.email)
    public EditText emailEditText;
    @BindView(R.id.password)
    public EditText passwordEditText;
    @BindView(R.id.username)
    public EditText usernameEditText;
    @BindView(R.id.firstName)
    public EditText firstnameEditText;
    @BindView(R.id.lastName)
    public EditText lastnameEditText;
    @BindView(R.id.signUpButton)
    public Button signUpBtn;

    public FirebaseAuth mAuth;
    public User aUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String firstname = firstnameEditText.getText().toString();
                String lastname = lastnameEditText.getText().toString();

                aUser = new User(firstname, username, lastname);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(!task.isSuccessful()){
                                    Toast.makeText(SignUpActivity.this,"Could not Register",Toast.LENGTH_LONG).show();
                                }
                                else{
                                    String userID = mAuth.getCurrentUser().getUid();

                                    FirebaseDatabase.getInstance().getReference("users").child(userID).child("profile").setValue(aUser);
                                }
                            }
                        });

                setResult(1);
                finish();
            }
        });

    }
}
