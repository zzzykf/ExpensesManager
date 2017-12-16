package ExpensesManager.ExpensesManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddCategory extends AppCompatActivity implements View.OnClickListener {
    EditText etCategoryName;
    Button btnAdd, btnCancel;
    RadioGroup radioGroup;
    RadioButton rbExpenses, rbIncome, rbAccount, rbFriend;
    String action ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        action = getIntent().getStringExtra("Action");

        etCategoryName = (EditText)findViewById(R.id.etCategoryName);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnCancel= (Button)findViewById(R.id.btnCancel);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        rbExpenses = (RadioButton)findViewById(R.id.rbExpenses);
        rbIncome = (RadioButton)findViewById(R.id.rbIncome);
        rbAccount = (RadioButton)findViewById(R.id.rbAccount);
        rbFriend = (RadioButton)findViewById(R.id.rbFriend);
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        radioGroup.check(R.id.rbExpenses);
        if (action.equals("Edit")) {
            getSupportActionBar().setTitle("Edit Category");
            etCategoryName.setText(getIntent().getStringExtra("Name"));
            String type = getIntent().getStringExtra("Type");
            btnAdd.setText("Edit");
            if (type.equals("Expenses")) rbExpenses.setChecked(true);
            else if (type.equals("Income")) rbIncome.setChecked(true);
            else if (type.equals("Account")) rbAccount.setChecked(true);
            else if (type.equals("Friend")) rbFriend.setChecked(true);
            for(int i=0;i<radioGroup.getChildCount(); i++){
                radioGroup.getChildAt(i).setEnabled(false);
            }
        }
        else {
            getSupportActionBar().setTitle("Add New Category");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAdd:
                if(etCategoryName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter the category name",Toast.LENGTH_SHORT).show();
                    break;
                }
                DatabaseHelper databaseHelper = new DatabaseHelper(this);
                String type = "";

                if(action.equals("Edit")){
                    if(getIntent().getStringExtra("Name").equals(etCategoryName.getText().toString())) {
                        Toast.makeText(getApplicationContext(),"No changes are made!",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }
                    if(databaseHelper.editCategoryName(getIntent().getStringExtra("Name"),etCategoryName.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Edited successfully!", Toast.LENGTH_SHORT).show();
                        setResult(99);
                        Shared.ON_RESUME = true;
                        finish();
                    }
                    else Toast.makeText(getApplicationContext(), etCategoryName.getText().toString() + " already exists in database!",Toast.LENGTH_SHORT);
                }
                else{
                    if(rbFriend.isChecked()) type = "Friend";
                    else if (rbAccount.isChecked()) type = "Account";
                    else if (rbIncome.isChecked()) type = "Income";
                    else if (rbExpenses.isChecked()) type ="Expenses";
                    CategoryModel model = new CategoryModel(etCategoryName.getText().toString(),type , Shared.USER);
                    if(databaseHelper.createNewCategory(model)){
                        Toast.makeText(getApplicationContext(),"Added successfully!", Toast.LENGTH_SHORT).show();
                        setResult(99);
                        Shared.ON_RESUME = true;
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), model.getCategoryName() + " already exists in database!",Toast.LENGTH_SHORT);
                    }

                }
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(getIntent().getStringExtra("Action").equals("Edit")) {
            getMenuInflater().inflate(R.menu.menu_add_category, menu);
        }
        return true;
    }
    public void deleteCategory (MenuItem menu){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.deleteCategory(getIntent().getStringExtra("Name"));
        Toast.makeText(getApplicationContext(),"Deleted successfully!", Toast.LENGTH_SHORT).show();
        setResult(99);
        Shared.ON_RESUME = true;
        finish();

    }
}
