package ExpensesManager.ExpensesManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


/**
 * Created by zzzyk on 10/18/2017.
 */

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsViewHolder> {
    private LayoutInflater inflater = null;
    private List<TransactionsModel> transactionsModelList;
    private Context adapterContext;
    public boolean isLast = false;

    public TransactionsAdapter (Context context, List<TransactionsModel> transactionsModelList) {
        this.transactionsModelList = transactionsModelList;
        inflater = LayoutInflater.from(context);
        adapterContext = context;
    }
    @Override
    public TransactionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == 0) view = inflater.inflate(R.layout.transaction_date, parent, false);
        else if (viewType == 1) view = inflater.inflate(R.layout.transaction_item, parent, false);
        else if (viewType == 2) view = inflater.inflate(R.layout.summary, parent, false);
        return new TransactionsViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TransactionsViewHolder holder, int position) {
        Double amount = 0.00;
        if(holder.rowTransaction!= null){
            holder.rowTransaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int transactionId = Integer.parseInt(((TextView)v.findViewById(R.id.row_transaction_id)).getText().toString());
                    if(transactionId%2==0) transactionId = transactionId-1;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(transactionsModelList.get(0).getTransactionDatetime());
                    int month = calendar.get(Calendar.MONTH)+1;
                    Intent intent = new Intent(v.getContext(),AddTransaction.class);
                    intent.putExtra("TransactionMonth",String.valueOf(month));
                    intent.putExtra("TransactionID", String.valueOf(transactionId));
                    ((Activity)v.getContext()).startActivityForResult(intent, 472);
                }
            });

        }
        else if(holder.rowSummary != null){
            holder.rowSummary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(transactionsModelList.get(0).getTransactionDatetime());
                    int month = calendar.get(Calendar.MONTH)+1;
                    int year = calendar.get(Calendar.YEAR);
                    Intent intent = new Intent (v.getContext(), ViewSummary.class);
                    intent.putExtra("Month",month);
                    intent.putExtra("Year",year);
                    v.getContext().startActivity(intent);
                }
            });
        }
        if(holder.tvDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String date = format.format(transactionsModelList.get(position).getTransactionDatetime());
            holder.tvDate.setText(date);
        }
        else if (holder.tvTransactions != null){
            holder.tvTransactions.setText(transactionsModelList.get(position).getTransactionCategory());
            if(transactionsModelList.get(position).getTransactionAmount()<0){
                holder.tvAmount.setTextColor(Color.BLACK);
                amount = transactionsModelList.get(position).getTransactionAmount()*-1;
            }else {
                amount = transactionsModelList.get(position).getTransactionAmount();
                holder.tvAmount.setTextColor(Color.RED);
            }
            int id = transactionsModelList.get(position).getTransactionId();
            holder.tvTransactionId.setText(String.valueOf(id));
            holder.tvAmount.setText("RM " + amount.toString());
        }
        else if (holder.tvInflow != null){
            DatabaseHelper databaseHelper = new DatabaseHelper(adapterContext);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(transactionsModelList.get(0).getTransactionDatetime());
            DecimalFormat df = new DecimalFormat("0.00");
            Double inflow = databaseHelper.getInflow(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1);
            Double outflow = databaseHelper.getOutflow(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1);
            holder.tvInflow.setText("RM " + df.format(inflow));
            if(outflow ==0) holder.tvOutflow.setText("RM 0.00");
            else holder.tvOutflow.setText("RM " + df.format(outflow*-1));
            holder.tvOutflow.setTextColor(Color.RED);
            Double total = inflow + outflow;
            if(total == 0){
                holder.tvTotal.setText("RM 0.00");
            }
            else if(total <0) {
                holder.tvTotal.setTextColor(Color.RED);
                holder.tvTotal.setText("RM " + df.format(total*-1));
            }
            else holder.tvTotal.setText("RM " + df.format(total));
        }
    }

    @Override
    public int getItemCount() {
        return (transactionsModelList != null && transactionsModelList.size() >0) ? transactionsModelList.size() : 0;
    }

    @Override
    public int getItemViewType (int position) {
        TransactionsModel previousModel = new TransactionsModel();
        TransactionsModel transactionModel = transactionsModelList.get(position);
        String previousDate = "";
        if(position == 0) return 2;
        if(position != 1)
            previousModel = transactionsModelList.get(position-1);

        if(transactionModel != null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String date = format.format(transactionModel.getTransactionDatetime());
            if(position != 1){
                previousDate = format.format(previousModel.getTransactionDatetime());
            }
            int max = transactionsModelList.size();
            if(!date.equals(previousDate)) return 0;
            else return 1;
        }
        return 0;

    }
    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder viewHolder;
        TransactionsModel transaction = getItem(position);
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listitems, null);
            viewHolder = new ViewHolder();
            viewHolder.tvAmount = (TextView)v.findViewById(R.id.amount_entry);
            viewHolder.tvCategory = (TextView)v.findViewById(R.id.category_entry);
            viewHolder.tvDatetime = (TextView)v.findViewById(R.id.datetime_entry);
            v.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)v.getTag();
        }
        viewHolder.tvDatetime.setText(transaction.getTransactionDatetime());
        viewHolder.tvAmount.setText(Double.toString(transaction.getTransactionAmount()));
        viewHolder.tvCategory.setText(transaction.getTransactionCategory());

        return v;

    }*/

}
