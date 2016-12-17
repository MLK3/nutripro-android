package com.oddsix.nutripro.rest;

import android.app.Activity;
import android.content.Intent;

import com.oddsix.nutripro.activities.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by filippecl on 03/12/16.
 */

public class ResponseHandler<T> {

    public static final int CODE_RESPONSE_SUCCESS = 200;
    public static final int CODE_RESPONSE_UNAUTHORIZED = 401;
    public static final int CODE_BAD_REQUEST = 400;
    public static final int CODE_NOT_FOUND = 340;
    public static final int CODE_FORBIDDEN = 403;
    public static final int CODE_UNKNOWN = 500;

    public final String UNKNOWN_ERROR = "Erro desconhecido, não foi possível completar sua requisição.";

    private Activity mActivity;

    public ResponseHandler(Activity activity) {
        mActivity = activity;
    }

    public interface OnResponse<T> {
        void onResponseSuccess(T response);

        void onFailure(String error, int code);
    }

    private void handleResponse(Response<T> response, ResponseHandler.OnResponse<T> handler) {
        if (response.isSuccess() && response.code() == CODE_RESPONSE_SUCCESS) {
            handler.onResponseSuccess(response.body());
        } else if (response.code() == CODE_UNKNOWN) {
            handler.onFailure(UNKNOWN_ERROR, response.code());
//        } else if (response.code() == CODE_RESPONSE_UNAUTHORIZED && !(mActivity instanceof LoginActivity)) {
//            Intent loginIntent = new Intent(mActivity, LoginActivity.class);
//            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            mActivity.startActivity(loginIntent);
        } else {
            handler.onFailure(response.message(), response.code());
        }
    }

    public Callback<T> retrofitCallback(final ResponseHandler.OnResponse<T> handler) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (mActivity != null && !mActivity.isFinishing()) {
                    handleResponse(response, handler);
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (mActivity != null && !mActivity.isFinishing()) {
                    handler.onFailure(t.getMessage(), 0);
                }
            }
        };
    }
}
