package ExpensesManager.ExpensesManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by zzzyk on 10/17/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "ExpensesManager";
    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_TRANSACTIONS = "transactions_table";
    private static final String TABLE_CATEGORY = "category_table";
    private static final String TABLE_USER = "user_table";
    private static final String COL_TRANSACTIONS_ID = "ID";
    private static final String COL_TRANSACTIONS_USER = "USER";
    private static final String COL_TRANSACTIONS_AMOUNT = "AMOUNT";
    private static final String COL_TRANSACTIONS_CATEGORY = "CATEGORY";
    private static final String COL_TRANSACTIONS_NOTE = "NOTE";
    private static final String COL_TRANSACTIONS_DATETIME = "DATETIME";
    private static final String COL_CATEGORY_ID = "ID";
    private static final String COL_CATEGORY_NAME = "CATEGORY";
    private static final String COL_CATEGORY_TYPE = "TYPE";
    private static final String COL_CATEGORY_USER = "USER";
    private static final String COL_USER_ID = "ID";
    private static final String COL_USER_USERNAME = "USERNAME";
    private static final String COL_USER_PASSWORD = "PASSWORD";
    private static Context dbContext = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        dbContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUser = "CREATE TABLE " + TABLE_USER + " (" + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USER_USERNAME + " TEXT UNIQUE, " + COL_USER_PASSWORD + " TEXT)";
        String createTableTransaction = "CREATE TABLE " + TABLE_TRANSACTIONS + " ("+ COL_TRANSACTIONS_ID
                +" INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TRANSACTIONS_USER +" TEXT, " + COL_TRANSACTIONS_AMOUNT
                + " REAL, " + COL_TRANSACTIONS_CATEGORY + " TEXT, " + COL_TRANSACTIONS_DATETIME + " TEXT, "
                + COL_TRANSACTIONS_NOTE + " TEXT)";
        String createTableCategory = "CREATE TABLE " + TABLE_CATEGORY + " (" +COL_TRANSACTIONS_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CATEGORY_NAME + " TEXT, " + COL_CATEGORY_TYPE + " TEXT, " + COL_CATEGORY_USER + " TEXT, UNIQUE( " +
                COL_CATEGORY_NAME + ", " + COL_CATEGORY_USER + ") ON CONFLICT FAIL)";
        db.execSQL(createTableUser);
        db.execSQL(createTableTransaction);
        db.execSQL(createTableCategory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    public boolean createNewCategory (CategoryModel categoryModel){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO " + TABLE_CATEGORY + " (" + COL_CATEGORY_NAME + ", " + COL_CATEGORY_TYPE + ", " + COL_CATEGORY_USER
                + ") VALUES ('" + categoryModel.getCategoryName() + "', '" + categoryModel.getCategoryType() + "', '"
                + categoryModel.getCategoryUser() + "')";
        try {
            db.execSQL(query);
        }catch (SQLiteConstraintException e){
            Toast.makeText(dbContext,categoryModel.getCategoryName() + " already exists!",Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;

    }
    public boolean addDefaultCategories (String user) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "INSERT INTO " + TABLE_CATEGORY + " (" + COL_CATEGORY_NAME + ", " + COL_CATEGORY_TYPE + ", " + COL_CATEGORY_USER
                + ") VALUES('Wallet', 'Account', '" + user + "'), ('Bank', 'Account', '" + user + "'), ('Food & Beverage', 'Expenses', '" + user
                + "'), ('Friends', 'Expenses', '" + user + "'), ('Shopping', 'Expenses', '" + user + "'), ('Transportation', 'Expenses', '" + user
                + "'), ('Utilities', 'Expenses', '" + user + "'), ('Initial', 'Income', '" + user + "'), ('Salary', 'Income', '" + user
                + "'), ('Gifts', 'Income', '" + user +"');";
        try {
            db.execSQL(query);
        }catch (Exception e){

            return false;
        }
        return true;
    }
    public boolean editCategoryName (String oldName, String newName){
        SQLiteDatabase db = this.getWritableDatabase();
        String updateCategory = "UPDATE " + TABLE_CATEGORY + " SET " + COL_CATEGORY_NAME + " = '" + newName + "' WHERE "
                + COL_CATEGORY_NAME  + " = '" + oldName + "'";
        String updateTransaction = "UPDATE " + TABLE_TRANSACTIONS + " SET " + COL_TRANSACTIONS_CATEGORY + " = '"
                + newName + "' WHERE " + COL_TRANSACTIONS_CATEGORY + " = '" + oldName + "'";
        try {
            db.execSQL(updateCategory);
            db.execSQL(updateTransaction);
        }catch (Exception e) {return false;}
        return true;
    }
    public void deleteCategory (String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_CATEGORY + " WHERE " + COL_CATEGORY_NAME + "= '" + name
                + "' ";
        String query2 = "DELETE FROM " + TABLE_TRANSACTIONS + " WHERE " + COL_TRANSACTIONS_ID
                +" IN (SELECT CASE WHEN ID % 2 = 0 THEN ID-1 ELSE ID+1 END COMBINEID FROM "
                + TABLE_TRANSACTIONS + " WHERE " + COL_TRANSACTIONS_CATEGORY  + "= '" + name
                + "') AND " + COL_TRANSACTIONS_USER + " = '" + Shared.USER + "' OR " + COL_TRANSACTIONS_CATEGORY
                + " = '" + name + "' AND " + COL_TRANSACTIONS_USER + " = '" + Shared.USER + "'" ;
        try {
            db.execSQL(query);
            db.execSQL(query2);
        }catch (Exception e ) {e.printStackTrace();}

    }
    public boolean addTransaction(TransactionsModel transactionsModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date date = new Date(System.currentTimeMillis()-120*60*60*1000);
        if(isIncome(transactionsModel.getTransactionCategory())) {
            transactionsModel.setTransactionAmount(transactionsModel.getTransactionAmount()*-1);
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TRANSACTIONS_CATEGORY, transactionsModel.getTransactionCategory());
        contentValues.put(COL_TRANSACTIONS_AMOUNT, transactionsModel.getTransactionAmount());
        contentValues.put(COL_TRANSACTIONS_DATETIME,dateFormat.format(transactionsModel.getTransactionDatetime()));
        //contentValues.put(COL_TRANSACTIONS_DATETIME,dateFormat.format(date).toString());
        contentValues.put(COL_TRANSACTIONS_NOTE, transactionsModel.getTransactionNote());
        contentValues.put(COL_TRANSACTIONS_USER, transactionsModel.getTransactionUser());
        //The opposite side of the transaction (DEBIT / CREDIT)
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(COL_TRANSACTIONS_CATEGORY, transactionsModel.getTransactionSubCategory());
        contentValues1.put(COL_TRANSACTIONS_AMOUNT,-1 * transactionsModel.getTransactionAmount());
        contentValues1.put(COL_TRANSACTIONS_DATETIME, dateFormat.format(transactionsModel.getTransactionDatetime()));
        //contentValues1.put(COL_TRANSACTIONS_DATETIME,dateFormat.format(date).toString());
        contentValues1.put(COL_TRANSACTIONS_NOTE, transactionsModel.getTransactionNote());
        contentValues1.put(COL_TRANSACTIONS_USER, transactionsModel.getTransactionUser());


        db.beginTransaction();
        try {
            db.insert(TABLE_TRANSACTIONS, null, contentValues);
            db.insert(TABLE_TRANSACTIONS, null, contentValues1);
            db.setTransactionSuccessful();
        }catch (Exception e)
        {
            Log.d(TAG,"Error while trying to add transaction to database");
            return false;
        }finally {
            db.endTransaction();
        }
        //if date as inserted incorrectly it will return -1

        return true;
    }
    public boolean isIncome (String category){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+ COL_CATEGORY_TYPE +" FROM " + TABLE_CATEGORY + " WHERE " + COL_CATEGORY_NAME + " = '" + category
                + "' ";
        Cursor c = null;
        c = db.rawQuery(query, null);
        c.moveToFirst();
        if(c.getString(0).equals("Income")) return true;
        else return false;
    }

    /**
     * Returns all the data from database
     * @return
     */
    public ArrayList<CategoryModel> getAllCategory (String user,String type) {
        ArrayList<CategoryModel> categoryModelList= new ArrayList<CategoryModel>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + COL_CATEGORY_USER  + " = '" + user + "' AND "
                + COL_CATEGORY_TYPE + "= '" + type + "'";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                CategoryModel categoryModel1 = new CategoryModel();
                categoryModel1.setCategoryId(Integer.parseInt(cursor.getString(0)));
                categoryModel1.setCategoryName(cursor.getString(1));
                categoryModel1.setCategoryType(cursor.getString(2));
                categoryModel1.setCategoryUser(cursor.getString(3));
                categoryModelList.add(categoryModel1);
            } while (cursor.moveToNext());
        }
        return categoryModelList;
    }
    public ArrayList<TransactionsModel> getAllTransactions() throws ParseException {
        ArrayList<TransactionsModel> transactionsModelList = new ArrayList<TransactionsModel>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " ORDER BY " + COL_TRANSACTIONS_DATETIME + " ASC";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                TransactionsModel transactionsModel = new TransactionsModel();
                transactionsModel.setTransactionId(Integer.parseInt(cursor.getString(0)));
                transactionsModel.setTransactionUser(cursor.getString(1));
                transactionsModel.setTransactionAmount(Double.parseDouble(cursor.getString(2)));
                transactionsModel.setTransactionCategory(cursor.getString(3));
                transactionsModel.setTransactionDatetime(dateFormat.parse(cursor.getString(4)));
                transactionsModel.setTransactionNote(cursor.getString(5));
                transactionsModelList.add(transactionsModel);
            } while (cursor.moveToNext());
        }
        return transactionsModelList;
    }
    public ArrayList<TransactionsModel> getCategoryTransaction (String category,  int year, int monthInt, String user) throws ParseException {
        ArrayList<TransactionsModel> transactionsModelList = new ArrayList<TransactionsModel>();
        String month = String.valueOf(monthInt);
        if(monthInt <10) {
            month = "0" + String.valueOf(monthInt);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COL_TRANSACTIONS_ID
                +" IN (SELECT CASE WHEN ID % 2 = 0 THEN ID-1 ELSE ID+1 END COMBINEID FROM "
                + TABLE_TRANSACTIONS + " WHERE " + COL_TRANSACTIONS_CATEGORY  + "= '" + category
                + "') AND  strftime('%m'," + COL_TRANSACTIONS_DATETIME + ") = '" + month +
                "' AND strftime('%Y',"+ COL_TRANSACTIONS_DATETIME +") = '" +year +"' AND " + COL_TRANSACTIONS_USER
                + " = '" + user + "' ORDER BY " +  COL_TRANSACTIONS_DATETIME + " DESC, "
                + COL_TRANSACTIONS_ID + " ASC";
        Cursor test = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS, null);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                TransactionsModel transactionsModel = new TransactionsModel();
                transactionsModel.setTransactionId(Integer.parseInt(cursor.getString(0)));
                transactionsModel.setTransactionUser(cursor.getString(1));
                transactionsModel.setTransactionAmount(Double.parseDouble(cursor.getString(2)));
                transactionsModel.setTransactionCategory(cursor.getString(3));
                transactionsModel.setTransactionDatetime(dateFormat.parse(cursor.getString(4)));
                transactionsModel.setTransactionNote(cursor.getString(5));
                transactionsModelList.add(transactionsModel);
            } while (cursor.moveToNext());
        }
        return transactionsModelList;
    }

    public boolean validateLogin (String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        int login = 0;
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COL_USER_USERNAME + " = '" + username
               + "' AND " + COL_USER_PASSWORD + " = '" + password + "'";
        Cursor c = null;
        c = db.rawQuery(query, null);
        c.moveToFirst();
        login = c.getCount();
        if (login >= 1 ) {
            return true;
        }
        else return false;
    }
    public boolean createNewUser (String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USER_USERNAME, username);
        contentValues.put(COL_USER_PASSWORD, password);
        db.beginTransaction();
        try {
            db.insertOrThrow(TABLE_USER, null, contentValues);
            db.setTransactionSuccessful();
        }catch (Exception e)

        {
            Log.d(TAG,"Error while trying to add transaction to database");
            return false;
        }finally {
            db.endTransaction();
        }
        return true;
    }
    public String getLastDateString (String user){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT MAX(" + COL_TRANSACTIONS_DATETIME + ") from " + TABLE_TRANSACTIONS + " WHERE " + COL_TRANSACTIONS_USER
                + " = '" + user + "' AND " + COL_TRANSACTIONS_CATEGORY + " = '" + Shared.ACCOUNT + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }
    public String getFirstDateString (String user){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT MIN(" + COL_TRANSACTIONS_DATETIME + ") from " + TABLE_TRANSACTIONS + " WHERE " + COL_TRANSACTIONS_USER
                + " = '" + user + "' AND " + COL_TRANSACTIONS_CATEGORY + " = '" + Shared.ACCOUNT + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public ArrayList<TransactionsModel>  getTransactionById (int id){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int secondId;
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<TransactionsModel> transactionsModelList = new ArrayList<TransactionsModel>();
        if(id%2==0) secondId = id-1;
        else secondId = id+1;
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COL_TRANSACTIONS_ID
                +" = " + id  + " OR " + COL_CATEGORY_ID + " = " + secondId + " ORDER BY " + COL_TRANSACTIONS_ID;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                TransactionsModel transactionsModel = new TransactionsModel();
                transactionsModel.setTransactionId(Integer.parseInt(cursor.getString(0)));
                transactionsModel.setTransactionUser(cursor.getString(1));
                transactionsModel.setTransactionAmount(Double.parseDouble(cursor.getString(2)));
                transactionsModel.setTransactionCategory(cursor.getString(3));
                try {transactionsModel.setTransactionDatetime(dateFormat.parse(cursor.getString(4)));}
                catch (ParseException e) {e.printStackTrace();}
                transactionsModel.setTransactionNote(cursor.getString(5));
                transactionsModelList.add(transactionsModel);
            } while (cursor.moveToNext());
        }
        return transactionsModelList;
    }

    public boolean editTransaction (int id, TransactionsModel transactionsModel){
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(isIncome(transactionsModel.getTransactionCategory())) {
            transactionsModel.setTransactionAmount(transactionsModel.getTransactionAmount()*-1);
        }
        String updateRowOne = "UPDATE " + TABLE_TRANSACTIONS + " SET " + COL_TRANSACTIONS_CATEGORY + " = '" + transactionsModel.getTransactionCategory()
                +"', " + COL_TRANSACTIONS_AMOUNT + " = " + transactionsModel.getTransactionAmount() + ", " + COL_TRANSACTIONS_DATETIME
                + " = '" + sdf.format(transactionsModel.getTransactionDatetime()) + "', " + COL_TRANSACTIONS_NOTE + " = '" + transactionsModel.getTransactionNote()
                + "' WHERE " + COL_TRANSACTIONS_ID  + " = " + id;
        id = id +1;
        String updateRowTwo = "UPDATE " + TABLE_TRANSACTIONS + " SET " + COL_TRANSACTIONS_CATEGORY + " = '" + transactionsModel.getTransactionSubCategory()
                +"', " + COL_TRANSACTIONS_AMOUNT + " = " + transactionsModel.getTransactionAmount()*-1 + ", " + COL_TRANSACTIONS_DATETIME
                + " = '" + sdf.format(transactionsModel.getTransactionDatetime())
                + "', " + COL_TRANSACTIONS_NOTE + " = '" + transactionsModel.getTransactionNote()
                + "' WHERE " + COL_TRANSACTIONS_ID  + " = " + id;
        try {
            db.execSQL(updateRowOne);
            db.execSQL(updateRowTwo);
        }catch (Exception e) {return false;}
        return true;
    }
    public void deleteTransaction (int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_TRANSACTIONS + " WHERE " + COL_TRANSACTIONS_ID + " = " + id;
        id += 1;
        String query2 = "DELETE FROM " + TABLE_TRANSACTIONS + " WHERE " + COL_TRANSACTIONS_ID + " = " + id;
        try {
            db.execSQL(query);
            db.execSQL(query2);
        }catch (Exception e ) {e.printStackTrace();}

    }
    public Double getBalance (String account){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COL_TRANSACTIONS_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE "
                + COL_TRANSACTIONS_CATEGORY + " = '" + account + "' AND " + COL_TRANSACTIONS_USER + " = '"
                + Shared.USER + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        db.close();
        if(cursor.getString(0)== null) return 0.0;
        return Double.parseDouble(cursor.getString(0));
    }
    public Double getInflow (int year, int month){
        SQLiteDatabase db = this.getReadableDatabase();
        String monthString;
        if(month <10) monthString = "0" + month;
        else monthString = String.valueOf(month);
        String query = "SELECT SUM(" + COL_TRANSACTIONS_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE "
                + COL_TRANSACTIONS_USER + " = '" + Shared.USER + "' AND " + COL_TRANSACTIONS_CATEGORY + " = '"
                + Shared.ACCOUNT + "' AND strftime('%m'," + COL_TRANSACTIONS_DATETIME + ") = '" + monthString +
                "' AND strftime('%Y',"+ COL_TRANSACTIONS_DATETIME +") = '" + year + "' AND " + COL_TRANSACTIONS_AMOUNT
                + " >0";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        db.close();
        if(cursor.getString(0)== null) return 0.0;
        return Double.parseDouble(cursor.getString(0));

    }
    public Double getOutflow (int year, int month){
        SQLiteDatabase db = this.getReadableDatabase();
        String monthString;
        if(month <10) monthString = "0" + month;
        else monthString = String.valueOf(month);
        String query = "SELECT SUM(" + COL_TRANSACTIONS_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE "
                + COL_TRANSACTIONS_USER + " = '" + Shared.USER + "' AND " + COL_TRANSACTIONS_CATEGORY + " = '"
                + Shared.ACCOUNT + "' AND strftime('%m'," + COL_TRANSACTIONS_DATETIME + ") = '" + monthString +
                "' AND strftime('%Y',"+ COL_TRANSACTIONS_DATETIME +") = '" + year + "' AND " + COL_TRANSACTIONS_AMOUNT
                + " <0";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        db.close();
        if(cursor.getString(0)== null) return 0.0;
        return Double.parseDouble(cursor.getString(0));

    }
    public ArrayList<TransactionsModel> getSummary (int year, int month){
        SQLiteDatabase db = this.getWritableDatabase();
        String monthString;
        if(month <10) monthString = "0" + month;
        else monthString = String.valueOf(month);
        ArrayList<TransactionsModel> transactionsModelList = new ArrayList<TransactionsModel>();
        String query = "SELECT SUM(" + COL_TRANSACTIONS_AMOUNT + "), " + COL_TRANSACTIONS_CATEGORY +" FROM "
                + TABLE_TRANSACTIONS + " WHERE " + COL_TRANSACTIONS_ID
                +" IN (SELECT CASE WHEN ID % 2 = 0 THEN ID-1 ELSE ID+1 END COMBINEID FROM "
                + TABLE_TRANSACTIONS + " WHERE " + COL_TRANSACTIONS_CATEGORY  + "= '" + Shared.ACCOUNT
                + "') AND  strftime('%m'," + COL_TRANSACTIONS_DATETIME + ") = '" + monthString +
                "' AND strftime('%Y',"+ COL_TRANSACTIONS_DATETIME +") = '" +year +"' AND " + COL_TRANSACTIONS_USER
                + " = '" + Shared.USER + "' GROUP BY " +  COL_CATEGORY_NAME;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                TransactionsModel transactionsModel = new TransactionsModel();
                transactionsModel.setTransactionAmount(Double.parseDouble(cursor.getString(0)));
                transactionsModel.setTransactionCategory(cursor.getString(1));
                transactionsModelList.add(transactionsModel);
            } while (cursor.moveToNext());
        }
        return transactionsModelList;
    }
    public ArrayList<String> getAccount (String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL_CATEGORY_NAME + " FROM " + TABLE_CATEGORY + " WHERE " + COL_CATEGORY_TYPE
                + " = '" + type + "' AND " + COL_CATEGORY_USER + " = '" + Shared.USER + "'";
        ArrayList<String> accountName = new ArrayList<String>();

        Cursor cursor = db.rawQuery(query, null);
        String name;
        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(0);
                accountName.add(name);
            } while (cursor.moveToNext());
        }
        return accountName;
    }
}

