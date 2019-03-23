package com.magad.testmyrestapi;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private android.support.v7.widget.SearchView searchView;
    private  String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rView = findViewById(R.id.rv_test);
        rView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rView.setItemAnimator(new DefaultItemAnimator());
        rView.setNestedScrollingEnabled(false);
        LoadJson("");
    }

    public void LoadJson(final String keyword ) {
        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        String country = Utils.getCountry();
        String language = Utils.getLanguange();

        Call<ResponseItem> call;

        if(keyword.length() > 0){
            call = apiService.getNewsSearch(keyword,country , "publishedAt",API_KEY );
        }else{
            call = apiService.getItems(country, API_KEY);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Latest News...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>2){
                    LoadJson(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LoadJson(newText);
                return false;
            }
        });
        searchMenuItem.getIcon().setVisible(false, false);
    return true;
    }
}
