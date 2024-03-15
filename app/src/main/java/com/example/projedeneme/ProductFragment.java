package com.example.projedeneme;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProductFragment extends Fragment {

    private static final String ARG_BARCODE_NUMBER = "barcode";
    private static final String urlBarkodoku = "https://www.barkodoku.com/%s";
    private static final String idResultCount = "ContentPlaceHolder1_UcBarkodGetir_lblCount";
    private static final String classElement = "col-xs-24 col-sm-24 col-md-9 excerpet";
    private static final String hrefName = "a[href]";

    TextView tvBarcode;
    RecyclerView rvProducts;

    public ProductFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product, container, false);
        tvBarcode = rootView.findViewById(R.id.tvBarcode);
        Bundle args = getArguments();
        if (args != null) {
            String barcodeNumber = args.getString(ARG_BARCODE_NUMBER, null);
            if (barcodeNumber != null) {
                tvBarcode.setText(String.format(Locale.getDefault(),"Barcode Number: %s", barcodeNumber));

                if (!checkNetworkConnection()) {
                    Toast.makeText(requireActivity(), "INTERNET unavailable!", Toast.LENGTH_SHORT).show();
                }
                else {
                    rvProducts = rootView.findViewById(R.id.rvProducts);
                    Toast.makeText(requireActivity(), String.format(Locale.getDefault(),"Searching Barcode %s", barcodeNumber), Toast.LENGTH_SHORT).show();
                    new FetchProductNameTask().execute(barcodeNumber);
                }
            }
        }

        return rootView;
    }

    public static ProductFragment newInstance(String barcodeNumber) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BARCODE_NUMBER, barcodeNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private class FetchProductNameTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... barcodes) {
            Log.d("okhttp","okhttp girdi");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://go-upc-product-lookup.p.rapidapi.com/code/" + barcodes[0])
                    .addHeader("X-RapidAPI-Key", "69fdf4d0d5msh189df8e586a56b7p11b2b9jsn4e8ddc03af8f")
                    .addHeader("X-RapidAPI-Host", "go-upc-product-lookup.p.rapidapi.com")
                    .build();

            try {

                Response response = client.newCall(request).execute();
                Log.d("request","request atıldı");
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                Log.d("name", jsonObject.getJSONObject("product").getString("name"));
                return jsonObject.getJSONObject("product").getString("name");
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String productName) {
            if (productName != null) {

                new FetchProductInfoTask().execute(productName);
            } else {
                Toast.makeText(getActivity(), "Ürün adı alınamadı.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class FetchProductInfoTask extends AsyncTask<String, Void, ArrayList<Product>> {
        @Override
        protected ArrayList<Product> doInBackground(String... params) {
            String productName = params[0];
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"q\":\"" + productName + "\",\"gl\":\"tr\",\"hl\":\"tr\"}");
            Request request = new Request.Builder()
                    .url("https://google.serper.dev/shopping")
                    .post(body)
                    .addHeader("X-API-KEY", "791f86fc707935a042dce03466bd34cf9709f112")
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray shoppingItems = jsonObject.getJSONArray("shopping");
                ArrayList<Product> products = new ArrayList<>();

                for (int i = 0; i < shoppingItems.length(); i++) {
                    JSONObject item = shoppingItems.getJSONObject(i);
                    String title = item.getString("title");
                    String price = item.getString("price");
                    String source = item.getString("source");
                    String imageUrl = item.getString("imageUrl");
                    String link = item.getString("link");
                    products.add(new Product(title,imageUrl,link, price, source));
                }

                return products;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> products) {
            if (products != null) {
                ProductAdapter adapter = new ProductAdapter(products, getActivity());
                rvProducts.setLayoutManager(new LinearLayoutManager(requireActivity()));
                rvProducts.setAdapter(adapter);
            } else {
                Toast.makeText(getActivity(), "Ürün bilgileri alınamadı.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}