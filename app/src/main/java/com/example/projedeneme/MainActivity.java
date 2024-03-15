package com.example.projedeneme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private MenuItem prevMenuItem;
    private ViewPager viewPager;
    private static final String urlSearch = "https://www.akakce.com/arama/?q=%s";
    public ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.fragmentContainer);
        navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(
                item -> {
                    if(item.getItemId() == R.id.nav_barcode)
                    {
                        viewPager.setCurrentItem(0);
                    }
                    else if(item.getItemId() == R.id.nav_product)
                    {
                        viewPager.setCurrentItem(1);
                    }
                    else if(item.getItemId() == R.id.nav_about) {
                        viewPager.setCurrentItem(2);
                    }
                    return false;
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) prevMenuItem.setChecked(false);
                else navigation.getMenu().getItem(0).setChecked(false);
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {  }
        });

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.init();
        viewPager.setAdapter(adapter);

    }

    void newProduct(String barcodeNumber) {
        adapter.newProduct(barcodeNumber);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    void searchProduct(String productName) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(Locale.getDefault(), urlSearch, productName))));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String barcode = result.getContents();
                Toast.makeText(this, "Scan Complete: " + barcode + "\nSearching...", Toast.LENGTH_LONG).show();
                newProduct(barcode);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}