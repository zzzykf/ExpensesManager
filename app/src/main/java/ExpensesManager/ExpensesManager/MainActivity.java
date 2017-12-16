package ExpensesManager.ExpensesManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnFragmentInteractionListener {
    private CoordinatorLayout coordinatorLayout;
    private ActionBarDrawerToggle toggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    public FloatingActionButton fab;
    DatabaseHelper databaseHelper;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 123){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        else if (resultCode == 99){
            Shared.FROM_INTENT = true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        SharedPreferences settings = getSharedPreferences("username",0);
        if(settings.getString("username","").equals("") ) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Shared.USER = settings.getString("username","");
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
            //TransactionsModel transactionsModel = new TransactionsModel("user", 12.2, "Category", "Note", "SubCategory");
            //TransactionsModel transactionsModel1 = new TransactionsModel("user1", 13.3, "Category1", "Note1", "SubCategory1");
            //databaseHelper.addTransaction(transactionsModel);
            //databaseHelper.addTransaction(transactionsModel1);
            fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            SnackBarMessage("Happy coding.");
            }
        });*/

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setItemIconTintList(null);
            View headerView = navigationView.getHeaderView(0);
            ((TextView)headerView.findViewById(R.id.tvUsername)).setText(Shared.USER);
            navigationView.setNavigationItemSelectedListener(this);
            databaseHelper.close();
            homeFragment();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(getFragmentManager().getBackStackEntryCount()>0){
            getFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }
    }


    public void SnackBarMessage(String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        Bundle args = new Bundle();

        switch (id){
            case R.id.nav_Transaction:fragmentClass =FragmentA.class;args.putString(Constants.FRAG_A,"Transactions");break;
            case R.id.nav_Category:fragmentClass =FragmentA.class;args.putString(Constants.FRAG_A,"Categories");break;
            case R.id.nav_Logout:
                SharedPreferences settings = getSharedPreferences("username",0);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.commit();
                Shared.USER = "";
                Shared.RATED = false;
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_Contact:
                Intent intent2 = new Intent(Intent.ACTION_DIAL);
                intent2.setData(Uri.parse("tel:01111916091")); // This ensures only SMS apps respond
                startActivity(intent2);
                break;
            case R.id.nav_Location:
                Intent intentLocation = new Intent(this, MapsActivity.class);
                startActivity(intentLocation);
                break;
            case R.id.nav_RateUs:
                if(Shared.RATED == true) Toast.makeText(getApplicationContext(),"You've rated our application. Thank You", Toast.LENGTH_SHORT).show();
                else {
                    Intent intentRate = new Intent (this, Rate.class);
                    startActivity(intentRate);
                }
                break;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(args);
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_layout_for_activity_navigation, fragment).addToBackStack(null).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentMessage(int TAG, String data) {

    }
    public void changeAccount (MenuItem menuItem){
        Intent intent = new Intent(this, SelectCategoryActivity.class);
        intent.putExtra("Action","Change Display Account");
        startActivityForResult(intent,123);

    }
    public void homeFragment(){

        try {
            Bundle args = new Bundle();
            Class fragmentClass = FragmentA.class;
            Fragment fragment;
            args.putString(Constants.FRAG_A,"Transactions");
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(args);
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_layout_for_activity_navigation, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
