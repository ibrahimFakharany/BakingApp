package exampls.com.bakingapp.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import exampls.com.bakingapp.Controller.OnCallFinishCallback;


/**
 * Created by 450 G1 on 21/09/2017.
 */

public class NetworkCalls {
    Context context;
    public final String TAG = "networkcalls";



    public NetworkCalls(Context context) {
        this.context = context;
    }

    public void getFromUrl( final OnCallFinishCallback callback){
        String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
        Response.Listener listener = new Response.Listener<String>() {

            @Override
            public void onResponse(String  response) {
                callback.onSuccess(response);
            }

        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        };

        StringRequest stringRequest  = new StringRequest(Request.Method.GET, URL,  listener, errorListener);

        SingleTon.getInstance(context).addRequest(stringRequest);
    }





}
