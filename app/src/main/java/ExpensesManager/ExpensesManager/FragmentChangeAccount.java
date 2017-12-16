package ExpensesManager.ExpensesManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzzyk on 11/17/2017.
 */

public class FragmentChangeAccount extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private List<String> accountName = new ArrayList<>();
    private ChangeAccountAdapter changeAccountAdapter;

    public FragmentChangeAccount(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_account, container, false);
        listView = (ListView) view.findViewById(R.id.changeAccountListView);
        int position = getArguments().getInt("position");
        String type = "";
        if(position == 0 )type = "Account";
        else if (position == 1) type = "Friend";
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        accountName = databaseHelper.getAccount(type);
        changeAccountAdapter = new ChangeAccountAdapter(getContext(), R.layout.summary_item, accountName);
        listView.setAdapter(changeAccountAdapter);

        databaseHelper.close();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Shared.ACCOUNT = ((TextView)view.findViewById(R.id.tvCategory)).getText().toString();
                getActivity().setResult(123);
                getActivity().finish();
            }
        });
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
        SelectCategoryActivity selectCategoryActivity = (SelectCategoryActivity)getActivity();
        selectCategoryActivity.fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCategory.class);
                intent.putExtra("Action","Add");
                startActivityForResult(intent,23);
            }
        });
        if(Shared.FROM_INTENT == true){
            if(Shared.TIME_FROM_INTENT >= 1) {
                Shared.FROM_INTENT = false;
                Shared.TIME_FROM_INTENT = 0;
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Shared.TIME_FROM_INTENT =+ 1;
            if(Shared.FROM_INTENT == false) Shared.TIME_FROM_INTENT = 0;
            ft.detach(this).attach(this).commit();
        }
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
