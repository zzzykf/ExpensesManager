package ExpensesManager.ExpensesManager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class SelectCategoryActivity extends AppCompatActivity implements OnFragmentInteractionListener{
    FloatingActionButton fab;@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 99){
            Shared.FROM_INTENT = true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        Bundle args = new Bundle();
        Class fragmentClass = FragmentA.class;
        Fragment fragment = null;
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if(getIntent().getStringExtra("Action").equals("Category")){
            args.putString(Constants.FRAG_A,"Select Category");
        }
        else if (getIntent().getStringExtra("Action").equals("SubCategory")){
            args.putString(Constants.FRAG_A,"Select Account");
        }
        else if (getIntent().getStringExtra("Action").equals("Change Display Account")){
            args.putString(Constants.FRAG_A,"Change Display Account");
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        }catch (Exception e){ e.printStackTrace();}
        fragment.setArguments(args);
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_layout_for_activity_navigation, fragment).commit();
    }

    @Override
    public void onFragmentMessage(int TAG, String data) {

    }
}
