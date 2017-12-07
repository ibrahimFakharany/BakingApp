package exampls.com.bakingapp.UI;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import exampls.com.bakingapp.R;

public class DescriptionActivity extends AppCompatActivity {

    private static final String TAG = "descriptionactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Log.e(TAG, "oncreate description activity");

        DescriptionFragment descriptionFragment = new DescriptionFragment();
        descriptionFragment.setArguments(getIntent().getExtras());
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(savedInstanceState == null){
            fragmentManager.beginTransaction().add(R.id.fragment_description, descriptionFragment , "")
                    .commit();
        }

    }
}
