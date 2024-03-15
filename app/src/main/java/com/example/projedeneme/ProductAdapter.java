package com.example.projedeneme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executor;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private ArrayList<Product> productList;
    private Context mContext;

    private LocationManager locationManager;

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        Button btnViewMore , btnShowOnMap;
        ConstraintLayout mLayout;

        ProductViewHolder(ConstraintLayout layout) {
            super(layout);
            mLayout = layout;

            ivProductImage = layout.findViewById(R.id.ivProductImage);
            btnViewMore = layout.findViewById(R.id.btnViewMore);
            btnShowOnMap = layout.findViewById(R.id.btnShowOnMap);
        }
    }

    ProductAdapter(ArrayList<Product> myProductList, Context context) {
        productList = myProductList;
        mContext = context;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.product_view, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {
        Product product = productList.get(position);


        ((TextView)holder.mLayout.getViewById(R.id.tvName)).setText(String.format(Locale.getDefault(), "Product: %s", product.getName()));
        ((TextView)holder.mLayout.getViewById(R.id.tvPrice)).setText(String.format(Locale.getDefault(), "Price: %s", product.getPrice()));
        ((TextView)holder.mLayout.getViewById(R.id.tvSource)).setText(String.format(Locale.getDefault(), "Source: %s", product.getSource()));

        Glide.with(mContext).load(product.getImageUrl()).into(holder.ivProductImage);

        holder.btnViewMore.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(product.getLink()));
            mContext.startActivity(browserIntent);
        });

        holder.btnShowOnMap.setOnClickListener(v -> {
            String sourceAddress = productList.get(position).getSource();
            handleShowOnMap(mContext, sourceAddress);
        });

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof MainActivity) {
                    ((MainActivity) mContext).searchProduct(productList.get(holder.getAdapterPosition()).getName());
                }
            }
        });
    }

    private void handleShowOnMap(Context context, String sourceAddress) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                showMap(context, sourceAddress, latitude, longitude);
                locationManager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, locationListener);

    }

    private void showMap(Context context, String address, double lat, double lon) {
        String uri = "https://www.google.com/maps/dir/?api=1&origin=" + lat + "," + lon+ "&destination=" + address + "&travelmode=driving";
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}