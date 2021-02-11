package com.traidev.asylum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences sharedPreferences=getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
        final Boolean isloggedin=sharedPreferences.getBoolean("ISLOGGEDIN",false);

        final String name = sharedPreferences.getString("NAME", "Asylum");

        if(!name.equals(null))
        {
            Intent main = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(main);
            finish();
        }
        final String required_email=sharedPreferences.getString("EMAIL","DEFAULT_EMAIL");
        final String required_password=sharedPreferences.getString("PASSWORD","DEFAULT_PASSWORD");
        final EditText email_field=(EditText)findViewById(R.id.login_email);
        final EditText password_field=(EditText)findViewById(R.id.login_password);
        Button login=(Button)findViewById(R.id.login_button);
        TextView register=(TextView) findViewById(R.id.register_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=email_field.getText().toString();
                String password=password_field.getText().toString();
                if(email.equals(required_email)&&password.equals(required_password)) {
                    sharedPreferences.edit().putBoolean("ISLOGGEDIN",false).commit();
                    Intent main = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(main);
                    finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Email address or password is incorrect",Toast.LENGTH_LONG).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register=new Intent(LoginActivity.this,SinginActivity.class);
                startActivity(register);
                finish();
            }
        });



    }



}
