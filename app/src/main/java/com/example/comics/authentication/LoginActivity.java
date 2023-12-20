package com.example.comics.authentication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comics.MainActivity;
import com.example.comics.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private TextInputEditText emailInput, passwordInput;
    private TextView tvForgotPassword, tvRegister;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgot();
            }
        });
    }
    private void customToast(String content, String color, int imageResource) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast, this.findViewById(R.id.custom_toast));

        TextView textView = view.findViewById(R.id.tvToast);
        textView.setText(content);

        ImageView imageView = view.findViewById(R.id.icToast);
        imageView.setImageResource(imageResource);

        view.setBackgroundColor(Color.parseColor(color));

        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.TOP | Gravity.END, 16, 100);
        toast.show();
    }



    private void forgot() {
        Intent intent = new Intent(LoginActivity.this, ResetActivity.class);
        startActivity(intent);
    }


    private void register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void login() {
        String email, password;
        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();
        if(TextUtils.isEmpty(email)) {
            customToast("Email is required", "#FF0000", R.drawable.ic_error);
            return;
        }
        if (!isValidEmail(email)) {
            customToast("Invalid email address", "#FF0000", R.drawable.ic_error);
            return;
        }
        if(TextUtils.isEmpty(password)) {
            customToast("Password is required", "#FF0000", R.drawable.ic_error);
            return;
        }
        if (password.length() < 8) {
            customToast("Password must be at least 8 characters", "#FF0000", R.drawable.ic_error);
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    customToast("Login successfully", "#FF299032", R.drawable.ic_check);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    customToast("Login failed, please check your email or password again", "#FF0000", R.drawable.ic_error);
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
