package com.example.projedeneme;



import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();

    ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

   void init() {
        fragmentList.add(new BarcodeFragment());
        fragmentList.add(ProductFragment.newInstance(null));
        fragmentList.add(new AboutFragment());
    }

    void newProduct(String barcodeNumber) {
        fragmentList.set(1, ProductFragment.newInstance(barcodeNumber));
    }

}