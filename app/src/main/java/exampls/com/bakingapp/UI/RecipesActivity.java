package exampls.com.bakingapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import exampls.com.bakingapp.R;
import exampls.com.bakingapp.SimpleIdlingResource;
import exampls.com.bakingapp.data.Recipe;

public class RecipesActivity extends AppCompatActivity implements RecipesFragment.OnRecieveListener {
    private static final String TAG = "RecipesActivity";
    public static final String RECIPE_KEY = "recipe";
    public SimpleIdlingResource mSimpleIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        if(mSimpleIdlingResource == null){
            mSimpleIdlingResource = new SimpleIdlingResource();
        }

        mSimpleIdlingResource.setIdleState(false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipesFragment recipesFragment = new RecipesFragment();

        if (savedInstanceState == null) {

            fragmentManager.beginTransaction().add(R.id.frame_layout, recipesFragment, "").commit();

        }
    }


    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getmSimpleIdlingResource(){

        if(mSimpleIdlingResource == null)
            mSimpleIdlingResource = new SimpleIdlingResource();

        return mSimpleIdlingResource;

    }

    @Override
    public void onClick(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putInt(RECIPE_KEY, recipe.getId());
        Intent intent = new Intent(this, StepsActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
