package com.hydrop.compassmobile.WebAPI.Requests;

import android.content.Context;
import android.util.Log;

import com.hydrop.compassmobile.R;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.LoginResponse;
import com.hydrop.compassmobile.WebAPI.WebAPI;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Panos on 11/05/16.
 */
public class LoginRequest extends BaseWebRequests {

    public void login(final Context context, String username, String password){
        HashMap map = new HashMap();
        map.put("Username",username);
        map.put("Password",password);
        Call<LoginResponse> call = WebAPI.getWebAPIInterface().login(map);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (Utils.checkNotNull(response.body())){
                    if (Utils.checkNotNull(response.body().getD())){
                        String operativeId = response.body().getD();
                        if (operativeId.isEmpty()){
                            failed(context.getString(R.string.incorrect_credentials));
                        }else{
                            BaseWebRequests.operativeId = operativeId;
                            success();

                        }
                    }else{
                        failed(MALFORMED_RESPONSE);
                    }
                }else{
                    failed(SERVER_ERROR_RESPONSE);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                failed(t.getMessage());
            }
        });

    }
}
