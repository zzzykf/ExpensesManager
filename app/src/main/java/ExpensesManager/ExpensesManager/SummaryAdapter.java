package ExpensesManager.ExpensesManager;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by zzzyk on 11/17/2017.
 */

public class SummaryAdapter extends ArrayAdapter<TransactionsModel> {
    ViewHolder viewHolder = null;
    private static class ViewHolder{
        private TextView tvAmount;
        private TextView tvCategory;
    }
    public SummaryAdapter(Context context, int textViewResourceId, ArrayList<TransactionsModel> transactionsModelList){
        super(context, textViewResourceId,transactionsModelList);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        DecimalFormat df = new DecimalFormat("0.00");
        if(convertView == null){
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.summary_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvAmount = (TextView)convertView.findViewById(R.id.tvAmount);
            viewHolder.tvCategory = (TextView)convertView.findViewById(R.id.tvCategory);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        TransactionsModel transactionsModel = getItem(position);
        if(transactionsModel!=null){
            Double amount = transactionsModel.getTransactionAmount();
            if (amount > 0) viewHolder.tvAmount.setTextColor(Color.RED);
            else {
                amount = amount *-1;
                viewHolder.tvAmount.setTextColor(Color.BLACK);
            }
            viewHolder.tvCategory.setText(transactionsModel.getTransactionCategory());
            viewHolder.tvAmount.setText("RM " + df.format(amount));
        }
        return convertView;
    }
}