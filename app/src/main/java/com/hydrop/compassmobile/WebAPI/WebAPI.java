package com.hydrop.compassmobile.WebAPI;

import com.hydrop.compassmobile.WebAPI.Responses.AssetWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.LocationGroupMembershipWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.LocationGroupWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.LocationWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.SendTaskWrapper.NewTask;
import com.hydrop.compassmobile.WebAPI.Responses.SendTaskWrapper.NewTaskResponseWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.OperativeWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.OrganisationWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.PropertyWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.ReferenceDataWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.SiteWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.TaskParameterWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.TaskTemplateParameterWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.TaskTemplateWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.TaskWrapper;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Panos on 06/05/16.
 */
public class WebAPI {

    private static WebAPIInterface webAPIInterface;
//    private static final String endPoint = "http://compass2dev.hecs.local/services/servicepdautility2json.asmx/";
    public static  String endPoint = null;
    public static String halfEndPoint = "/services/servicepdautility2json.asmx/";

    public static void resetWebAPI() {
        webAPIInterface = null;
    }

    public static WebAPIInterface getWebAPIInterface() {
        if (webAPIInterface == null) {


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(endPoint)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();



            webAPIInterface = retrofit.create(WebAPIInterface.class);


        }
        return webAPIInterface;
    }

    public interface WebAPIInterface {

        @POST("ValidateOperative")
        @Headers({ "WWW-Authenticate: None"})
        Call<LoginResponse> login(@Body HashMap body);

        @POST("GetSynchronisationPackage")
        Call<ReferenceDataWrapper> getReferenceData(@Body HashMap body);

        @POST("GetSynchronisationPackage")
        Call<OrganisationWrapper> getOrganisations(@Body HashMap body);

        @POST("GetSynchronisationPackage")
        Call<SiteWrapper> getSites(@Body HashMap body);

        @POST("GetSynchronisationPackage")
        Call<PropertyWrapper> getProperties(@Body HashMap body);

        @POST("GetSynchronisationPackage")
        Call<LocationWrapper> getLocations(@Body HashMap body);

        @POST("GetSynchronisationPackage")
        Call<LocationGroupWrapper> getLocationGroups(@Body HashMap body);

        @POST("GetSynchronisationPackage")
        Call<LocationGroupMembershipWrapper> getLocationGroupsMembership(@Body HashMap body);

        @POST("GetSynchronisationPackage")
        Call<AssetWrapper> getAssets(@Body HashMap body);

        @POST("GetSynchronisationPackage")
        Call<OperativeWrapper> getOperatives(@Body HashMap body);

        @POST("GetSynchronisationPackage")
        Call<TaskTemplateWrapper> getTaskTemplates(@Body HashMap body);

        @POST("GetSynchronisationPackage")
        Call<TaskTemplateParameterWrapper> getTaskTemplateParameters(@Body HashMap body);

        @POST("GetSynchronisationPackage")
        Call<TaskWrapper> getTasks(@Body HashMap body);

        @POST("GetSynchronisationPackage")
        Call<TaskParameterWrapper> getTaskParameter(@Body HashMap body);

        @POST("SetSynchronisationPackage")
        Call<NewTaskResponseWrapper> sendTask(@Body NewTask body);













    }
}

