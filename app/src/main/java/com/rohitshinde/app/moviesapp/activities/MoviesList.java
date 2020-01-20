package com.rohitshinde.app.moviesapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitshinde.app.moviesapp.R;
import com.rohitshinde.app.moviesapp.adapters.MovieGridAdapter;
import com.rohitshinde.app.moviesapp.bean.MovieBean;
import com.rohitshinde.app.moviesapp.utils.FileUtils;
import com.rohitshinde.app.moviesapp.web.WebHelper;
import com.rohitshinde.app.moviesapp.web.WebLinks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoviesList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieGridAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        loadContent(WebLinks.POPULAR_MOVIES_URL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_popular) {
            loadContent(WebLinks.POPULAR_MOVIES_URL);
        } else if (id == R.id.sort_top_rated) {
            loadContent(WebLinks.TOP_RATED_MOVIES_URL);
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadContent(String url) {
        LoadMoviesAsyncTask loadMoviesAsyncTask = new LoadMoviesAsyncTask();
        loadMoviesAsyncTask.execute(url);
    }

    private void initUi(List<String> moviePosters) {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        mAdapter = new MovieGridAdapter(this, moviePosters);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new MovieGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String obj, int position) {
                Log.e("Main", "Item " + position + " clicked");
            }
        });

    }

    private class LoadMoviesAsyncTask extends AsyncTask<String, String, JSONObject> {
        WebHelper webHelper = new WebHelper();

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            String url = params[0] + getResources().getString(R.string.API_KEY);
            Log.e("TAG", "Loaded Url=" + url);
            try {
                jsonObject = webHelper.fetchDataFromServer(url);
            } catch (JSONException e) {
            }
            return jsonObject;
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (jsonObject != null) {
                    if (jsonObject.has("error")) {
                        Log.e("TAG", "Poor internet connection");
                    } else {
                        JSONArray results = jsonObject.getJSONArray("results");
                        ArrayList<MovieBean> movieBeanArrayList = new ArrayList<>();
                        ArrayList<String> moviePosters = new ArrayList<>();
                        DisplayMetrics metrics = getResources().getDisplayMetrics();
                        int densityDpi = (int) (metrics.density * 160f);
                        String imageType = FileUtils.selectImageSize(densityDpi) + "/";
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject movieJSONObject = results.getJSONObject(i);
                            String title = movieJSONObject.getString("original_title");
                            String releaseDate = movieJSONObject.getString("release_date");
                            String moviePoster = movieJSONObject.getString("poster_path");
                            moviePoster = WebLinks.IMAGE_BASE_URL + imageType + moviePoster;
                            String voteAverage = movieJSONObject.getString("vote_count");
                            String plotSynopsis = movieJSONObject.getString("overview");
                            MovieBean movieBean = new MovieBean(title, releaseDate, moviePoster, voteAverage, plotSynopsis);
                            movieBeanArrayList.add(movieBean);
                            moviePosters.add(moviePoster);
                        }
                        initUi(moviePosters);
                    }
                } else {
                    Log.e("TAG", "Null");
                }
            } catch (Exception e) {
                Log.e("TAG", "Data parsing error=" + Log.getStackTraceString(e));
            }
        }


    }
}
