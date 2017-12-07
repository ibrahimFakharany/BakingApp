package exampls.com.bakingapp.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by 450 G1 on 21/09/2017.
 */

public class SingleTon {
    private static SingleTon mInstance;
    private Context context;
    private RequestQueue mRequestQueue;

    private SingleTon(Context context) {
        this.context = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized SingleTon getInstance(Context context) {
        if (mInstance == null) {

            mInstance = new SingleTon(context);

        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {

            mRequestQueue = Volley.newRequestQueue(context);

        }
        return mRequestQueue;
    }

    public <T> void addRequest(Request<T> request) {
        mRequestQueue.add(request);
        mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                String nul = (request.getTag() == "string_req")? "":"";
            }
        });
    }
}
