package com.oddsix.nutripro.rest;

import android.app.Activity;

import com.oddsix.nutripro.BuildConfig;
import com.oddsix.nutripro.rest.models.requests.RegisterRequest;
import com.oddsix.nutripro.rest.models.responses.DayResumeResponse;
import com.oddsix.nutripro.rest.models.responses.FoodFromMealResponse;
import com.oddsix.nutripro.rest.models.responses.GeneralResponse;
import com.oddsix.nutripro.rest.models.responses.MealDetailResponse;
import com.oddsix.nutripro.rest.models.responses.RegisterResponse;
import com.oddsix.nutripro.rest.models.responses.SearchResponse;
import com.oddsix.nutripro.rest.models.responses.SuggestedDietResponse;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.DateHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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

    public void signIn(String mail, String password, final OnResponseListener<GeneralResponse> callback) {
        mNutriproService.postSignin(mail, password)
                .enqueue(new ResponseHandler<GeneralResponse>(mActivity, callback));
    }

    public void createRegister(String name, String gender, int age, String email, String activity, int peso, int altura, OnResponseListener<GeneralResponse> callback) {
        mNutriproService.createRegister(new RegisterRequest(name, gender, age, email, activity, peso, altura))
                .enqueue(new ResponseHandler<GeneralResponse>(mActivity, callback));
    }

    public void getDiet(OnResponseListener<SuggestedDietResponse> callback){
        mNutriproService.getSuggestedDiet()
                .enqueue(new ResponseHandler<SuggestedDietResponse>(mActivity, callback));
    }

    public void getMealsByDay(Date date, OnResponseListener<DayResumeResponse> callback) throws ParseException {
        mNutriproService.getMealsByDay(DateHelper.parseDate(Constants.REQUEST_DATE_FORMAT, date))
                .enqueue(new ResponseHandler<DayResumeResponse>(mActivity, callback));
    }

    public void getRegister(OnResponseListener<RegisterResponse> callback) {
        mNutriproService.getRegister()
                .enqueue(new ResponseHandler<RegisterResponse>(mActivity, callback));
    }

    public void updateRegister(String name, String gender, int age, String email, String activity, int peso, int altura, OnResponseListener<GeneralResponse> callback) {
        mNutriproService.updateRegister(name, gender, age, peso, email, activity, altura)
                .enqueue(new ResponseHandler<GeneralResponse>(mActivity, callback));
    }

    public void getMealDetail(String id, OnResponseListener<MealDetailResponse> callback) {
        mNutriproService.getMealDetail(id)
                .enqueue(new ResponseHandler<MealDetailResponse>(mActivity, callback));
    }

    public void getFoodById(String id, OnResponseListener<FoodFromMealResponse> callback) {
        mNutriproService.getFoodById(id)
                .enqueue(new ResponseHandler<FoodFromMealResponse>(mActivity, callback));
    }

    public void searchFood(String query, OnResponseListener<SearchResponse> callback){
        mNutriproService.searchFoods(query)
                .enqueue(new ResponseHandler<SearchResponse>(mActivity, callback));
    }

    private Retrofit getRetrofit(List<Interceptor> interceptors) {
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
