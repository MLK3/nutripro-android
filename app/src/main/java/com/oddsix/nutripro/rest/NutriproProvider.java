package com.oddsix.nutripro.rest;

import android.app.Activity;

import com.oddsix.nutripro.BuildConfig;
import com.oddsix.nutripro.rest.models.responses.GeneralResponse;
import com.oddsix.nutripro.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by filippecl on 03/12/16.
 */

public class NutriproProvider {

    private Activity mActivity;
    private ResponseHandler mResponseHandler;
    private NutriproService mNutriproService;

    public NutriproProvider(Activity activity) {
        mActivity = activity;
        mNutriproService = getRetrofit(new ArrayList<Interceptor>()).create(NutriproService.class);
    }

    private void signIn(String mail, String password, final OnResponseListener<GeneralResponse> callback) {
        mNutriproService.postSignin(mail, password).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if(response.isSuccess()) callback.onResponseSuccess(response.body());
                else callback.onResponseFailure("", response.code());
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                callback.onResponseFailure("Não foi possível completar sua requisição, tente novamente", 0);
            }
        });
    }

    public Retrofit getRetrofit(List<Interceptor> interceptors) {
        Retrofit.Builder retroBuilder = new Retrofit.Builder().baseUrl(Constants.MOCK_URL).addConverterFactory(GsonConverterFactory.create());
        OkHttpClient.Builder httpClient = getClient(interceptors);
        retroBuilder.client(httpClient.build());
        return retroBuilder.build();
    }

    private OkHttpClient.Builder getClient(List<Interceptor> interceptors) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        for (Interceptor interceptor : interceptors) {
            httpClientBuilder.addInterceptor(interceptor);
        }
        return httpClientBuilder;
    }

    public interface OnResponseListener <T> {
        void onResponseSuccess(T response);
        void onResponseFailure(String msg, int code);
    }
}
