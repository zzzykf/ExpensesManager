package ExpensesManager.ExpensesManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
/**
 * Created by akhil on 12/1/2016.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;
    ArrayList<String> tabName;

    public SectionsPagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<String> tabName) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.tabName=tabName;
    }

    @Override
    public Fragment getItem(int position) {
        FragmentB comn=new FragmentB();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        comn.setArguments(bundle);
        return comn;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
