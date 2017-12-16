package ExpensesManager.ExpensesManager;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class FragmentA extends Fragment {

    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private OnFragmentInteractionListener mListener;
    private FragmentPagerAdapter fragmentPagerAdapter;
    DatabaseHelper databaseHelper;
    ArrayList<String> tabName;

    public FragmentA() {
        // Required empty public constructor
    }
    public static FragmentA newInstance(String navigation) {
        FragmentA fragment = new FragmentA();
        Bundle args = new Bundle();
        args.putString(Constants.FRAG_A, navigation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext());
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_a, container, false);
        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        mViewPager = (ViewPager)view.findViewById(R.id.container);
        tabName=new ArrayList<String>();
        int i;
        Double amount = databaseHelper.getBalance(Shared.ACCOUNT);
        if (amount<0) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Shared.ACCOUNT + "  -RM " + String.valueOf(amount*-1));
        }else {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Shared.ACCOUNT + " RM " + String.valueOf(amount));
        }
        //Control number of Tabs & Tab name
        switch (getArguments().getString(Constants.FRAG_A)) {
            case "Transactions" :
                Calendar cal;
                SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
                String user = Shared.USER;
                String firstDateString = databaseHelper.getFirstDateString(user);
                String lastDateString = databaseHelper.getLastDateString(user);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Calendar firstDate = Calendar.getInstance();
                Calendar lastDate = Calendar.getInstance();
                Calendar today = Calendar.getInstance();
                try{
                    firstDate.setTime(df.parse(firstDateString));
                    lastDate.setTime(df.parse(lastDateString));

                }catch (Exception e){e.printStackTrace();}
                int year =  (lastDate.get(Calendar.YEAR) - firstDate.get(Calendar.YEAR));
                int month = year * 12 + (lastDate.get(Calendar.MONTH) - firstDate.get(Calendar.MONTH));
                if (month <= 3 ){
                    Shared.TOTAL_TABS = 5;
                }
                else {
                    Shared.TOTAL_TABS = month+1;
                }
                if(today.get(Calendar.MONTH) == lastDate.get(Calendar.MONTH) && today.get(Calendar.YEAR) == lastDate.get(Calendar.YEAR)){
                    cal= Calendar.getInstance();
                }
                else {
                    cal = lastDate;
                }
                Shared.LAST_DATE = (Calendar)lastDate.clone();
                cal.add(Calendar.MONTH,-Shared.TOTAL_TABS+1);
                for(i=0;i<Shared.TOTAL_TABS;i++){
                    Date date = cal.getTime();
                    tabLayout.addTab(tabLayout.newTab().setText(sdf.format(date)));
                    tabName.add(String.valueOf(i));
                    cal.add(Calendar.MONTH,1);
                }
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                fragmentPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
                mViewPager.setAdapter(fragmentPagerAdapter);
                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        mViewPager.setCurrentItem(tab.getPosition());
                        Shared.TAB_NAME = String.valueOf(tab.getText());
                        getChildFragmentManager().beginTransaction().addToBackStack(null).commit();
                    }
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });
                break;
            case "Categories" :
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Category");
                Shared.TOTAL_TABS = 4;
                String[] name = {"Account","Friend","Income", "Expenses"};
                for(i=0;i<Shared.TOTAL_TABS;i++) {
                    tabLayout.addTab(tabLayout.newTab().setText(name[i]));
                    tabName.add(String.valueOf(i));
                }
                tabLayout.setTabMode(TabLayout.MODE_FIXED);
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                fragmentPagerAdapter = new CategoryPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
                mViewPager.setAdapter(fragmentPagerAdapter);
                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        mViewPager.setCurrentItem(tab.getPosition());
                        Shared.TAB_NAME = String.valueOf(tab.getText());
                        getChildFragmentManager().beginTransaction().addToBackStack(null).commit();
                    }
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });

                break;
            case "Select Category":
                Shared.TOTAL_TABS = 4;
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Select Category");
                String[] categoryName = {"Account","Friend","Income", "Expenses"};
                for(i=0;i<Shared.TOTAL_TABS;i++) {
                    tabLayout.addTab(tabLayout.newTab().setText(categoryName[i]));
                    tabName.add(String.valueOf(i));
                }
                tabLayout.setTabMode(TabLayout.MODE_FIXED);
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                fragmentPagerAdapter = new SelectCategoryPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
                mViewPager.setAdapter(fragmentPagerAdapter);
                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        mViewPager.setCurrentItem(tab.getPosition());
                        Shared.TAB_NAME = String.valueOf(tab.getText());
                        getChildFragmentManager().beginTransaction().addToBackStack(null).commit();
                    }
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });

                break;
            case "Select Account":
                Shared.TOTAL_TABS = 2;
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Select Account");
                String[] accountName = {"Account","Friend"};
                for(i=0;i<Shared.TOTAL_TABS;i++) {
                    tabLayout.addTab(tabLayout.newTab().setText(accountName[i]));
                    tabName.add(String.valueOf(i));
                }
                tabLayout.setTabMode(TabLayout.MODE_FIXED);
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                fragmentPagerAdapter = new SelectCategoryPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
                mViewPager.setAdapter(fragmentPagerAdapter);
                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        mViewPager.setCurrentItem(tab.getPosition());
                        Shared.TAB_NAME = String.valueOf(tab.getText());
                        getChildFragmentManager().beginTransaction().addToBackStack(null).commit();
                    }
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });

                break;
            case "Change Display Account":
                Shared.TOTAL_TABS = 2;
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Select Account");
                String[] displayName = {"Account","Friend"};
                for(i=0;i<Shared.TOTAL_TABS;i++) {
                    tabLayout.addTab(tabLayout.newTab().setText(displayName[i]));
                    tabName.add(String.valueOf(i));
                }
                tabLayout.setTabMode(TabLayout.MODE_FIXED);
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                fragmentPagerAdapter = new ChangeAccountPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
                mViewPager.setAdapter(fragmentPagerAdapter);
                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        mViewPager.setCurrentItem(tab.getPosition());
                        Shared.TAB_NAME = String.valueOf(tab.getText());
                        getChildFragmentManager().beginTransaction().addToBackStack(null).commit();
                    }
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });

                break;

        }
        /*for(int i=0;i<totalTabs;i++){
            tabLayout.addTab(tabLayout.newTab().setText(getArguments().getString(Constants.FRAG_A)+" "+String.valueOf(i)));
            tabName.add(String.valueOf(i));
        }*/

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments().getString(Constants.FRAG_A).equals("Categories")){

            // Note that we are passing childFragmentManager, not FragmentManager
            fragmentPagerAdapter = new CategoryPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
            mViewPager.setAdapter(fragmentPagerAdapter);
            mViewPager.setCurrentItem(Shared.TOTAL_TABS-1);
        }
        else if (getArguments().getString(Constants.FRAG_A).equals("Transactions")){
            // Note that we are passing childFragmentManager, not FragmentManager
            fragmentPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
            mViewPager.setAdapter(fragmentPagerAdapter);
            mViewPager.setCurrentItem(Shared.TOTAL_TABS-1);
        }
        else if (getArguments().getString(Constants.FRAG_A).equals("Select Category")){
            // Note that we are passing childFragmentManager, not FragmentManager
            fragmentPagerAdapter = new SelectCategoryPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
            mViewPager.setAdapter(fragmentPagerAdapter);
            mViewPager.setCurrentItem(Shared.TOTAL_TABS-1);
        }
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

}
