package ExpensesManager.ExpensesManager;

import android.app.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class AddTransaction extends AppCompatActivity implements View.OnClickListener {
    EditText etCategory, etDate, etSubCategory, etAmount, etNote;
    Button btnSave, btnCancel;
    int FirstTransactionId;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        String categoryName;
        if(resultCode == RESULT_OK){
            if(data.hasExtra("Name")){
                categoryName = data.getExtras().getString("Name");
                etCategory.setText(categoryName);
            }
            else if(data.hasExtra("AccountName")){
                categoryName = data.getExtras().getString("AccountName");
                etSubCategory.setText(categoryName);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        setContentView(R.layout.activity_add_transaction);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        etSubCategory = (EditText)findViewById(R.id.etSubCategory);
        etCategory = (EditText)findViewById(R.id.etCategory);
        etAmount = (EditText)findViewById(R.id.etAmount);
        etDate = (EditText)findViewById(R.id.etDate);
        etNote = (EditText)findViewById(R.id.etNote);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        etSubCategory.setOnClickListener(this);
        etCategory.setOnClickListener(this);
        etDate.setOnClickListener(this);
        etSubCategory.setText(Shared.ACCOUNT);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        String date = simpleDateFormat.format(calendar.getTime());
        etDate.setText(date);
        myCalendar = calendar;
        if(getIntent().getStringExtra("TransactionID")!= null){
            FirstTransactionId = Integer.parseInt(getIntent().getStringExtra("TransactionID"));
            if(FirstTransactionId%2 ==0 ) FirstTransactionId =- 1;
            getSupportActionBar().setTitle("Edit Transaction");
            ArrayList<TransactionsModel> transactionsModelList = databaseHelper.getTransactionById(Integer.parseInt(getIntent().getStringExtra("TransactionID")));
            Double amount;
            if(databaseHelper.isIncome(transactionsModelList.get(0).getTransactionCategory())) amount = transactionsModelList.get(0).getTransactionAmount()*-1;
            else amount = transactionsModelList.get(0).getTransactionAmount();
            etAmount.setText(amount.toString());
            etCategory.setText(transactionsModelList.get(0).getTransactionCategory());
            String date2 = simpleDateFormat.format(transactionsModelList.get(0).getTransactionDatetime());
            etDate.setText(date2);
            myCalendar.setTime(transactionsModelList.get(0).getTransactionDatetime());
            etSubCategory.setText(transactionsModelList.get(1).getTransactionCategory());
            etNote.setText(transactionsModelList.get(0).getTransactionNote());
            btnSave.setText("Edit");

        } else {
            getSupportActionBar().setTitle("Add Transaction");
            btnSave.setText("Add");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch(v.getId()){
            case R.id.etCategory:
                intent = new Intent(this, SelectCategoryActivity.class);
                intent.putExtra("Action","Category");
                startActivityForResult(intent,13);
                break;
            case R.id.etSubCategory:
                intent = new Intent(this, SelectCategoryActivity.class);
                intent.putExtra("Action","SubCategory");
                startActivityForResult(intent,13);
                break;
            case R.id.etDate:
                new DatePickerDialog(this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btnSave:
                if(etAmount.getText().toString().equals("") || etCategory.getText().toString().equals(""))
                {
                    Snackbar.make(v, "Please fill in the required information!", Snackbar.LENGTH_SHORT).show();
                    break;
                }
                TransactionsModel model = new TransactionsModel(Shared.USER,
                        Double.parseDouble(etAmount.getText().toString()),etCategory.getText().toString(),
                        etNote.getText().toString(),etSubCategory.getText().toString(),myCalendar.getTime());
                DatabaseHelper databaseHelper = new DatabaseHelper(this);
                if(getIntent().getStringExtra("TransactionID")!= null){
                    if(databaseHelper.editTransaction(FirstTransactionId, model)){
                        Toast.makeText(getApplicationContext(),"Edited successfully!", Toast.LENGTH_SHORT).show();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(model.getTransactionDatetime());
                        int month = calendar.get(Calendar.MONTH)+1;
                        if(String.valueOf(month).equals(getIntent().getStringExtra("TransactionMonth")))
                            setResult(99);
                        else
                            setResult(123);
                        Shared.ON_RESUME = true;
                        finish();
                    }
                } else{
                    if(databaseHelper.addTransaction(model)){
                        Toast.makeText(getApplicationContext(),"Added successfully!", Toast.LENGTH_SHORT).show();
                        setResult(99);
                        Shared.ON_RESUME = true;
                        finish();
                    }
                }

                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }
    private void updateLabel() {
        String myFormat = "EEE, dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        etDate.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(getIntent().getStringExtra("TransactionID")!= null) {
            getMenuInflater().inflate(R.menu.menu_add_transaction, menu);
        }
        return true;
    }
    public void deleteTransaction (MenuItem menu){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:
                        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                        databaseHelper.deleteTransaction(FirstTransactionId);
                        Toast.makeText(getApplicationContext(),"Deleted successfully!", Toast.LENGTH_SHORT).show();
                        setResult(99);
                        Shared.ON_RESUME = true;
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        try{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete this transaction ?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }catch (Exception e) {e.printStackTrace();}

    }
}
