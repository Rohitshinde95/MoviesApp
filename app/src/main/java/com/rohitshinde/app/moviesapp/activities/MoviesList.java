package com.rohitshinde.app.moviesapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitshinde.app.moviesapp.R;
import com.rohitshinde.app.moviesapp.adapters.MovieGridAdapter;
import com.rohitshinde.app.moviesapp.bean.MovieBean;
import com.rohitshinde.app.moviesapp.utils.FileUtils;
import com.rohitshinde.app.moviesapp.web.WebHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoviesList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private RelativeLayout relativeLayoutNoInternet;
    private TextView textViewErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        relativeLayoutNoInternet = findViewById(R.id.layout_no_internet);
        textViewErrorMessage = findViewById(R.id.text_view_error);
        recyclerView = findViewById(R.id.recyclerView);

        loadContent(getResources().getString(R.string.popular_movies_url));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_popular) {
            loadContent(getResources().getString(R.string.popular_movies_url));
        } else if (id == R.id.sort_top_rated) {
            loadContent(getResources().getString(R.string.top_rated_movies_url));
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadContent(String url) {
        showProgressDialog();
        LoadMoviesAsyncTask loadMoviesAsyncTask = new LoadMoviesAsyncTask();
        loadMoviesAsyncTask.execute(url);
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.load_movies_message));
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void initUi(List<MovieBean> movieBeanList) {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        MovieGridAdapter mAdapter = new MovieGridAdapter(movieBeanList);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new MovieGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieBean movieBean) {
                Intent intent = new Intent(MoviesList.this, MoviesDetails.class);
                intent.putExtra(getResources().getString(R.string.intent_movie), movieBean);
                startActivity(intent);
            }
        });

    }

    private void showNoInternetMessage(String message) {
        recyclerView.setVisibility(View.GONE);
        relativeLayoutNoInternet.setVisibility(View.VISIBLE);
        textViewErrorMessage.setText(message);
    }

    private void showMovieList() {
        relativeLayoutNoInternet.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private class LoadMoviesAsyncTask extends AsyncTask<String, String, JSONObject> {
        final WebHelper webHelper = new WebHelper();

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject;
            String url = params[0] + getResources().getString(R.string.API_KEY);
            try {
                jsonObject = webHelper.fetchDataFromServer(MoviesList.this, url);
            } catch (JSONException e) {
                return null;
            }
            return jsonObject;
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (jsonObject.has(getResources().getString(R.string.key_error))) {
                    showNoInternetMessage(jsonObject.getString(getResources().getString(R.string.key_error)));
                } else {
                    JSONArray results = jsonObject.getJSONArray(getResources().getString(R.string.key_results));
                    ArrayList<MovieBean> movieBeanArrayList = new ArrayList<>();
                    DisplayMetrics metrics = getResources().getDisplayMetrics();
                    int densityDpi = (int) (metrics.density * 160f);
                    String imageType = FileUtils.selectImageSize(densityDpi) + "/";
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movieJSONObject = results.getJSONObject(i);
                        String title = movieJSONObject.getString(getResources().getString(R.string.key_movie_original_title));
                        String releaseDate = movieJSONObject.getString(getResources().getString(R.string.key_movie_release_date));
                        String moviePoster = movieJSONObject.getString(getResources().getString(R.string.key_movie_poster_path));
                        moviePoster = getResources().getString(R.string.image_base_url) + imageType + moviePoster;
                        String voteAverage = movieJSONObject.getString(getResources().getString(R.string.key_movie_vote_count));
                        String plotSynopsis = movieJSONObject.getString(getResources().getString(R.string.key_movie_overview));
                        MovieBean movieBean = new MovieBean(title, releaseDate, moviePoster, voteAverage, plotSynopsis);
                        movieBeanArrayList.add(movieBean);
                    }
                    initUi(movieBeanArrayList);
                    showMovieList();
                }
            } catch (Exception e) {
                showNoInternetMessage(getResources().getString(R.string.no_internet_message));
            }
            dismissProgressDialog();
        }
    }
}
