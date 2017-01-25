package com.oddsix.nutripro.rest;

import com.oddsix.nutripro.utils.helpers.SharedPreferencesHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by mobile2you on 11/08/16.
 */
public class ReceivedCookieInterceptor implements Interceptor {

    private static final String RESPONSE_HEADER_COOKIE = "Set-Cookie";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        String cookie = originalResponse.headers().get(RESPONSE_HEADER_COOKIE);
        if (cookie != null) {
            if (!cookie.isEmpty()) {
                SharedPreferencesHelper.getInstance().putSessionCookie(cookie);
            }
        }

        return originalResponse;
    }
}
