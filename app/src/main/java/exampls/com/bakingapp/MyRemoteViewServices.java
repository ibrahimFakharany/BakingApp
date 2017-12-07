package exampls.com.bakingapp;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by 450 G1 on 14/10/2017.
 */

public class  MyRemoteViewServices extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewServices(this.getApplicationContext());
    }
}
