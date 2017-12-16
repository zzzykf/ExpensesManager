package ExpensesManager.ExpensesManager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zzzyk on 11/4/2017.
 */

public class TransactionsViewHolder extends RecyclerView.ViewHolder{

    public TextView tvTransactions, tvTransactionId;
    public TextView tvDate;
    public TextView tvAmount;
    public TextView tvRM;
    public LinearLayout rowTransaction;
    public TextView tvInflow, tvOutflow, tvTotal;
    public LinearLayout rowSummary;
    private int type;

    public TransactionsViewHolder(View itemView, int type) {
        super(itemView);
        if(type == 0) {
            tvDate = (TextView)itemView.findViewById(R.id.row_transaction_date);
        }else if (type == 1){
            rowTransaction = (LinearLayout)itemView.findViewById(R.id.row_transaction);
            tvTransactions = (TextView)itemView.findViewById(R.id.row_transaction_items);
            tvAmount = (TextView)itemView.findViewById(R.id.row_transaction_amount);
            tvTransactionId = (TextView)itemView.findViewById(R.id.row_transaction_id);

        }else if (type == 2){
            tvInflow = (TextView)itemView.findViewById(R.id.tvInflow);
            tvOutflow = (TextView)itemView.findViewById(R.id.tvOutflow);
            tvTotal = (TextView)itemView.findViewById(R.id.tvTotal);
            rowSummary = (LinearLayout)itemView.findViewById(R.id.summary);

        }
    }
}
