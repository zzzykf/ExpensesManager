package ExpensesManager.ExpensesManager;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etUsername, etPassword, etConfirmPassword;
    Button btnRegister;
    TextView tvTick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tvTick = (TextView)findViewById(R.id.tvTick);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etConfirmPassword= (EditText)findViewById(R.id.etConfirmPassword);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etPassword.getText().toString().equals("") || etConfirmPassword.getText().toString().equals("")) tvTick.setText("");
                else if(etPassword.getText().toString().equals(etConfirmPassword.getText().toString()))tvTick.setText("\u2713");
                else tvTick.setText("\u2718");
            }
        });
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etPassword.getText().toString().equals("") || etConfirmPassword.getText().toString().equals("")) tvTick.setText("");
                else if(etPassword.getText().toString().equals(etConfirmPassword.getText().toString()))tvTick.setText("\u2713");
                else tvTick.setText("\u2718");
            }
        });
        getSupportActionBar().setTitle("Register");

    }

    @Override
    public void onClick(View v) {
        if(etUsername.getText().toString().trim().length() ==0){

            Snackbar.make(v, "Please enter username!", Snackbar.LENGTH_SHORT).show();
        }
        else if(etPassword.getText().toString().equals("") || etConfirmPassword.getText().toString().equals("")
                || etUsername.getText().toString().equals("")){
            Snackbar.make(v, "Please fill in all the information!", Snackbar.LENGTH_SHORT).show();

        }
        else if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
            Snackbar snackbar = Snackbar
                    .make(v, "Please enter same password!", Snackbar.LENGTH_SHORT);

            snackbar.show();
        }
        else {
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            if(databaseHelper.createNewUser(etUsername.getText().toString(),etPassword.getText().toString())){
                SharedPreferences settings = getSharedPreferences("username",0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("username", etUsername.getText().toString());
                Shared.USER = etUsername.getText().toString();
                databaseHelper.addDefaultCategories(Shared.USER);
                editor.commit();
                Toast.makeText(this,"Register successfully!",Toast.LENGTH_SHORT).show();
                setResult(330);
                finish();
            }
            else {
                Snackbar snackbar = Snackbar
                        .make(v, "This username already exists!", Snackbar.LENGTH_SHORT);

                snackbar.show();
            }
        }
    }
}
