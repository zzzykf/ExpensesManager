package ExpensesManager.ExpensesManager;

/**
 * Created by zzzyk on 10/18/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FragmentB extends Fragment{
    private OnFragmentInteractionListener mListener;
    private TextView text_view_for_tab_selection;
    private RecyclerView recyclerView;
    private List<TransactionsModel> transactionsModelList = new ArrayList<>();
    private TransactionsAdapter transactionsAdapter;
    private int year, month;

    public FragmentB() {
        // Required empty public constructor
    }


    public static FragmentB newInstance(String tabSelected) {
        FragmentB fragment = new FragmentB();
        Bundle args = new Bundle();
        args.putString(Constants.FRAG_B, tabSelected);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if(getArguments().getInt("position") == Shared.TOTAL_TABS-1){
                month = Shared.LAST_DATE.get(Calendar.MONTH)+1;
                year = Shared.LAST_DATE.get(Calendar.YEAR);
                String a = "a";
            }
            else {
                int x = getArguments().getInt("position");
                Calendar cal = (Calendar)Shared.LAST_DATE.clone();
                cal.add(Calendar.MONTH, -((Shared.TOTAL_TABS-1) - (getArguments().getInt("position"))));
                month = cal.get(Calendar.MONTH)+1;
                year = cal.get(Calendar.YEAR);
                String a = "a";
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_b, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.transactionRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //fillAdapter();
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        String user = Shared.USER;
        try {
            transactionsModelList =  databaseHelper.getCategoryTransaction(Shared.ACCOUNT, year, month, user);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e){

        }
        int i;
        TransactionsModel Section = new TransactionsModel();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //Add a Section Header for each date.
        for(i=0; i<transactionsModelList.size(); i++){
            if(i == 0){
                Section.setTransactionDatetime(transactionsModelList.get(i).getTransactionDatetime());
                transactionsModelList.add(i, Section);
                transactionsModelList.add(i, Section);
            }
            else if(i == transactionsModelList.size()-1){}
            else {
                String date = format.format(transactionsModelList.get(i).getTransactionDatetime());
                String nextDate = format.format(transactionsModelList.get(i+1).getTransactionDatetime());
                if(!date.equals(nextDate)) {
                    TransactionsModel section = new TransactionsModel();
                    section.setTransactionDatetime(transactionsModelList.get(i+1).getTransactionDatetime());
                    transactionsModelList.add(i+1, section);
                }
            }
        }


        transactionsAdapter = new TransactionsAdapter(getContext(), transactionsModelList);
        recyclerView.setAdapter(transactionsAdapter);
        databaseHelper.close();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTransaction.class);
                startActivityForResult(intent,353);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            onResume();
        }
    }


}
