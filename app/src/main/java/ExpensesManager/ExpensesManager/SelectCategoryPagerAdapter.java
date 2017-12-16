package ExpensesManager.ExpensesManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by zzzyk on 11/10/2017.
 */

public class SelectCategoryPagerAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;
    ArrayList<String> tabName;
    public SelectCategoryPagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<String> tabName) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.tabName=tabName;
    }

    @Override
    public Fragment getItem(int position) {
        FragmentSelectCategory comn = new FragmentSelectCategory();
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
