package ExpensesManager.ExpensesManager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentC extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView text_view_for_normal_selection;

    public FragmentC() {
        // Required empty public constructor
    }

    public static FragmentC newInstance(String normalText) {
        FragmentC fragment = new FragmentC();
        Bundle args = new Bundle();
        args.putString(Constants.FRAG_C, normalText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_c, container, false);
        text_view_for_normal_selection=(TextView)view.findViewById(R.id.text_view_for_normal_selection);
        text_view_for_normal_selection.setText(getArguments().getString(Constants.FRAG_C));
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
    }
}
