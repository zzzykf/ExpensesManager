package ExpensesManager.ExpensesManager;

import java.util.Date;

/**
 * Created by zzzyk on 10/18/2017.
 */

public class TransactionsModel {
    private int _id;
    private String _user, _category, _note, _subCategory;
    private Double _amount;
    private Date _datetime;

    public TransactionsModel() {}
    public TransactionsModel(String user, Double amount, String category, String note, String subCategory, Date datetime){
        this._user = user;
        this._amount = amount;
        this._category = category;
        this._note = note;
        this._subCategory = subCategory;
        this._datetime = datetime;
    }

    public int getTransactionId() {
        return this._id;
    }
    public String getTransactionUser() {
        return this._user;
    }
    public Double getTransactionAmount() {
        return this._amount;
    }
    public String getTransactionCategory() {
        return this._category;
    }

    public String getTransactionNote() {
        return this._note;
    }
    public Date getTransactionDatetime() {
        return this._datetime;
    }
    public String getTransactionSubCategory() {
        return this._subCategory;
    }

    public void setTransactionId (int id) {
        this._id = id;
    }
    public void setTransactionUser(String user) {
        this._user = user;
    }
    public void setTransactionAmount(Double amount) {
        this._amount = amount;
    }
    public void setTransactionCategory(String category) {
        this._category = category;
        this._category = category;
    }
    public void setTransactionDatetime(Date datetime) {
        this._datetime = datetime;
    }
    public void setTransactionNote(String note) {
        this._note= note;
    }
    public void setTransactionSubCategory(String subCategory) {
        this._subCategory = subCategory;
    }

}
