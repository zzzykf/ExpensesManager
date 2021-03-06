package ExpensesManager.ExpensesManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by zzzyk on 11/8/2017.
 */

public class CategoryPagerAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;
    ArrayList<String> tabName;
    public CategoryPagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<String> tabName) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.tabName=tabName;
    }

    @Override
    public Fragment getItem(int position) {
        FragmentCategory comn = new FragmentCategory();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        comn.setArguments(bundle);
        return comn;
    }

    @Override
    public int getCount() {
        return this.mNumOfTabs;
    }
}
