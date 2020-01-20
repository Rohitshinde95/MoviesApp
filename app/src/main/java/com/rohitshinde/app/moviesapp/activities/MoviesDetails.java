package com.rohitshinde.app.moviesapp.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rohitshinde.app.moviesapp.R;
import com.rohitshinde.app.moviesapp.bean.MovieBean;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MoviesDetails extends AppCompatActivity {

    private ImageView imageViewMoviePoster;
    private TextView textViewMovieTitle, textViewMovieSynopsis, textViewMovieReleaseDate, textViewMovieVoteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);

        imageViewMoviePoster = findViewById(R.id.movie_poster);
        textViewMovieTitle = findViewById(R.id.movie_title);
        textViewMovieSynopsis = findViewById(R.id.movie_synopsis);
        textViewMovieReleaseDate = findViewById(R.id.movie_release_date);
        textViewMovieVoteAverage = findViewById(R.id.movie_vote_average);

        MovieBean movieBean = (MovieBean) Objects.requireNonNull(getIntent().getExtras()).getSerializable(getResources().getString(R.string.intent_movie));
        initMovieDetails(Objects.requireNonNull(movieBean));
    }

    private void initMovieDetails(MovieBean movieBean) {
        Picasso.get()
                .load(movieBean.getMoviePoster())
                .error(R.drawable.image_not_available)
                .into(imageViewMoviePoster);

        textViewMovieTitle.setText(movieBean.getTitle());
        textViewMovieSynopsis.setText(movieBean.getPlotSynopsis());
        textViewMovieReleaseDate.setText(movieBean.getReleaseDate());
        textViewMovieVoteAverage.setText(movieBean.getVoteAverage());
    }

}
