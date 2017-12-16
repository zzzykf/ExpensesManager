package ExpensesManager.ExpensesManager;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by zzzyk on 11/17/2017.
 */

public class ChangeAccountAdapter extends ArrayAdapter<String> {
    ViewHolder viewHolder = null;
    DatabaseHelper databaseHelper;
    private static class ViewHolder{
        private TextView tvCategory, tvAmount;
    }
    public ChangeAccountAdapter(Context context, int textViewResourceId, List<String> accountName){
        super(context, textViewResourceId,accountName);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        databaseHelper = new DatabaseHelper(getContext());
        if(convertView == null){
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.summary_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvCategory = (TextView)convertView.findViewById(R.id.tvCategory);
            viewHolder.tvAmount = (TextView)convertView.findViewById(R.id.tvAmount);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        String name = getItem(position);
        if(name!=null){
            viewHolder.tvCategory.setText(name);
            DecimalFormat df = new DecimalFormat("0.00");
            Double amount = databaseHelper.getBalance(name);
            if(amount <0) {
                viewHolder.tvAmount.setText("RM " + df.format(amount*-1));
                viewHolder.tvAmount.setTextColor(Color.RED);
            }
            else{
                viewHolder.tvAmount.setText("RM " + df.format(amount));
                viewHolder.tvAmount.setTextColor(Color.BLACK);
            }
        }
        return convertView;
    }
}
