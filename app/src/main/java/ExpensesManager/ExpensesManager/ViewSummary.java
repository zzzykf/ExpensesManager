package ExpensesManager.ExpensesManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewSummary extends AppCompatActivity {
    ListView listView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_summary);
        int month = getIntent().getIntExtra("Month",0);
        int year = getIntent().getIntExtra("Year",0);
        listView = (ListView)findViewById(R.id.listView);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<TransactionsModel> transactionsModelList = databaseHelper.getSummary(year, month);
        SummaryAdapter summaryAdapter = new SummaryAdapter(this,R.layout.summary_item, transactionsModelList);
        listView.setAdapter(summaryAdapter);
        getSupportActionBar().setTitle(Shared.TAB_NAME);

    }
}
