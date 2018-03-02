/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.bosseur.popuplarmoviesapp.utilities;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import br.bosseur.popuplarmoviesapp.BuildConfig;

/**
 * These utilities will be used to communicate with the api.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_API_BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String MOVIE_TRAILER_PATH = "videos";
    private static final String MOVIE_REVIEW_PATH = "reviews";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p";

    /**
     * Builds the URL used to talk to the movie api.
     *
     * @param sortPath The sort order chosen by the user
     * @return The URL to use to query the movie api.
     */
    public static URL buildMovieUrl(String sortPath) {
        Uri builtUri = Uri.parse(MOVIE_API_BASE_URL).buildUpon()
                .appendPath(sortPath)
                .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .build();
        Log.d(TAG, builtUri.toString());
        return buildUrl(builtUri);
    }

    /**
     * Builds the URL used to retrieve the trailers of a specific movie.
     *
     * @param movieId The id of the movie to fetch trailer for.
     * @return The URL to use to query the movie api.
     */
    public static URL buildTrailerUrl(String movieId) {
        Uri builtUri = Uri.parse(MOVIE_API_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(MOVIE_TRAILER_PATH)
                .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .build();
        Log.d(TAG, builtUri.toString());
        return buildUrl(builtUri);
    }


    /**
     * Builds the URL used to retrieve the trailers of a specific movie.
     *
     * @param movieId The id of the movie to fetch trailer for.
     * @return The URL to use to query the movie api.
     */
    public static URL buildReviewUrl(String movieId) {
        Uri builtUri = Uri.parse(MOVIE_API_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(MOVIE_REVIEW_PATH)
                .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .build();
        Log.d(TAG, builtUri.toString());
        return buildUrl(builtUri);
    }

    /**
     * Builds the URL used to talk retrieve the image.
     *
     * @param image      The image from the movie to show
     * @param imageWidth size of the image to be shown
     * @return The URL used to retrieve the image.
     */
    public static URL buildImageUrl(String image, String imageWidth) {
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(imageWidth)
                .appendPath(image.trim())
                .build();

        return buildUrl(builtUri);
    }

    @Nullable
    private static URL buildUrl(Uri builtUri) {
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}