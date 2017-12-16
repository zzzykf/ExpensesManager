package ExpensesManager.ExpensesManager;

/**
 * Created by zzzyk on 11/8/2017.
 */

public class CategoryModel {
    private int _id;
    private String _category, _type, _user;
    public CategoryModel(){}
    public CategoryModel(String _category, String _type, String _user) {
        this._category = _category;
        this._type = _type;
        this._user = _user;
    }

    public CategoryModel(int _id, String _category, String _type, String _user) {
        this._id = _id;
        this._category = _category;
        this._type = _type;
        this._user = _user;
    }
    public int getCategoryId () {
        return this._id;
    }
    public String getCategoryName () {
        return this._category;
    }
    public String getCategoryType () {
        return this._type;
    }
    public String getCategoryUser() {
        return this._user;
    }

    public void setCategoryId (int id) {
        this._id = id;
    }
    public void setCategoryName (String name){
        this._category = name;
    }
    public void setCategoryType (String type){
        this._type = type;
    }
    public void setCategoryUser (String user){
        this._user = user;
    }
}
