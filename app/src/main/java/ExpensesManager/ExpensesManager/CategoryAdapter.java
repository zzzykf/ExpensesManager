package ExpensesManager.ExpensesManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zzzyk on 11/8/2017.
 */

public class CategoryAdapter extends ArrayAdapter<CategoryModel> {
    ViewHolder viewHolder = null;
    private static class ViewHolder{
        private TextView itemView;
    }
    public CategoryAdapter(Context context, int textViewResourceId, List<CategoryModel> categories){
        super(context, textViewResourceId,categories);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.category_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemView = (TextView)convertView.findViewById(R.id.tvCategory);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        CategoryModel categoryModel = getItem(position);
        if(categoryModel!=null){
            viewHolder.itemView.setText(categoryModel.getCategoryName());
        }
        return convertView;
    }
}
