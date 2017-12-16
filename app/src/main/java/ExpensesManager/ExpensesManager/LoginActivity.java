package ExpensesManager.ExpensesManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvRegister;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 330){
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        tvRegister=(TextView)findViewById(R.id.tvRegister);
        tvRegister.setClickable(true);
        tvRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnLogin:
                DatabaseHelper databaseHelper = new DatabaseHelper(this);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if(databaseHelper.validateLogin(username, password)){
                    SharedPreferences settings = getSharedPreferences("username",0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("username", username);
                    editor.commit();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Snackbar snackbar = Snackbar
                            .make(v, "Please enter valid username and password!", Snackbar.LENGTH_SHORT);

                    snackbar.show();
                }
                break;
            case R.id.tvRegister:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, 42);
                break;
        }
    }
}
