package com.oddsix.nutripro.rest;

import com.oddsix.nutripro.rest.models.requests.EditMealRequest;
import com.oddsix.nutripro.rest.models.requests.RegisterFoodRequest;
import com.oddsix.nutripro.rest.models.requests.RegisterRequest;
import com.oddsix.nutripro.rest.models.responses.CreateFoodResponse;
import com.oddsix.nutripro.rest.models.responses.DayResumeResponse;
import com.oddsix.nutripro.rest.models.responses.FoodFromMealResponse;
import com.oddsix.nutripro.rest.models.responses.GeneralResponse;
import com.oddsix.nutripro.rest.models.responses.MealDetailResponse;
import com.oddsix.nutripro.rest.models.responses.RegisterResponse;
import com.oddsix.nutripro.rest.models.responses.SearchResponse;
import com.oddsix.nutripro.rest.models.responses.SuggestedDietResponse;
import com.oddsix.nutripro.rest.models.responses.WeekMealResponse;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by filippecl on 03/12/16.
 */

public interface NutriproService {

    @POST("register")
    Call<GeneralResponse> createRegister(@Body RegisterRequest request);

    @FormUrlEncoded
    @POST("signin")
    Call<GeneralResponse> postSignin(@Field("email") String email,
                                     @Field("password") String password);

    @GET("diet")
    Call<SuggestedDietResponse> getSuggestedDiet();

    @GET("meals")
    Call<DayResumeResponse> getMealsByDay(@Query("date") String date);

    @GET("register")
    Call<RegisterResponse> getRegister();

    @GET("meal-detail")
    Call<MealDetailResponse> getMealDetail(@Query("id") String id);

    @FormUrlEncoded
    @PUT("register")
    Call<GeneralResponse> updateRegister(@Field("name") String name,
                                         @Field("gender") String gender,
                                         @Field("age") int age,
                                         @Field("peso") int weight,
                                         @Field("email") String mail,
                                         @Field("activity") String activity,
                                         @Field("altura") int height);

    @GET("food")
    Call<FoodFromMealResponse> getFoodById(@Query("id") String id);

    @GET("search-food")
    Call<SearchResponse> searchFoods(@Query("name") String name);

    @PUT("meal")
    Call<GeneralResponse> editMeal(@Body EditMealRequest request);

    @POST("food")
    Call<CreateFoodResponse> registerFood(@Body RegisterFoodRequest request);

    @GET("week-meal")
    Call<WeekMealResponse> getWeekMeal(@Query("first_date") String date);

}
