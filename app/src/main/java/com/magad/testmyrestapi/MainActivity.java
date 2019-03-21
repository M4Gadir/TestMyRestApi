package com.magad.testmyrestapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.magad.testmyrestapi.api.ApiClient;
import com.magad.testmyrestapi.api.ApiService;
import com.magad.testmyrestapi.models.ArticlesItem;
import com.magad.testmyrestapi.models.ResponseItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public  static  final String API_KEY = "b962bf8dadda4339992f2229d4ce0276";
    private RecyclerView rView;
    private List<ArticlesItem> articles;
    private AdapterApi adapter;
    private  String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rView = findViewById(R.id.rv_test);
        rView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rView.setItemAnimator(new DefaultItemAnimator());
        rView.setNestedScrollingEnabled(false);
        LoadJson();
    }

    public void LoadJson() {
        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        String country = Utils.getCountry();

        Call<ResponseItem> call;
        call = apiService.getItems(country, API_KEY);
        call.enqueue(new Callback<ResponseItem>() {
            @Override
            public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {
                if (response.isSuccessful()) {
//                    if (!articles.isEmpty()) {
//                        articles.clear();
//                    }
                    articles = response.body().getArticles();
                    adapter = new AdapterApi(MainActivity.this, articles);
                    rView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(MainActivity.this, "no result", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseItem> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
