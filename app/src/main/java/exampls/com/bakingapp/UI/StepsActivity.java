package exampls.com.bakingapp.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import exampls.com.bakingapp.R;
import exampls.com.bakingapp.data.Recipe;
import io.realm.Realm;

public class StepsActivity extends AppCompatActivity implements StepsFragment.FragmentListener {
    String TAG = "stepsActivity";
    private static final String POSITION_KEY = "position";
    boolean twoPane = false;
    public static final String TWO_PANE = "twopane";
    DescriptionFragment descriptionFragment = null;
    FragmentManager fragmentManager = null;

    @Override
    public boolean onNavigateUpFromChild(Activity child) {
        return super.onNavigateUpFromChild(child);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        Log.e(TAG, "oncreate");
        Bundle bundle = getIntent().getExtras();
        int recipeId = bundle.getInt(RecipesActivity.RECIPE_KEY);

        // get all of the recipe from DB by its ID
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        Recipe recipe = realm.where(Recipe.class).equalTo("id", recipeId).findFirst();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        if (recipe != null) {
            getSupportActionBar().setTitle(recipe.getName());
            fragmentManager = getSupportFragmentManager();
            if (null != findViewById(R.id.fragment_description)) {
                twoPane = true;



                descriptionFragment = new DescriptionFragment();
                if (savedInstanceState == null) {
                    Log.e(TAG, "savedInstanceState not null");
                    descriptionFragment.setArguments(new Bundle());
                    fragmentManager.beginTransaction()
                            .add(R.id.fragment_description, descriptionFragment, "descriptionFragment")
                            .commitNow();
                } else {
                    Log.e(TAG, "savedInstanceState not null");
                }
            }


            if (savedInstanceState == null) {
                bundle.putBoolean(TWO_PANE, twoPane);
                StepsFragment stepsFragment = new StepsFragment();
                stepsFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_steps, stepsFragment, "").commit();
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onStepClickFragment(int id, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(RecipesActivity.RECIPE_KEY, id);
        bundle.putInt(POSITION_KEY, position);
        if (twoPane) {
            DescriptionFragment myDescriptionFragment = (DescriptionFragment) fragmentManager.findFragmentByTag("descriptionFragment");
            /*i deleted all the check if the savedinstance not null, but how to get the arguments from the description fragment after it restores, i want to get it to put  */
            myDescriptionFragment.getArguments().putInt(RecipesActivity.RECIPE_KEY, id);
            myDescriptionFragment.getArguments().putInt(POSITION_KEY, position);
            Log.e(TAG, "before setup layout");
            myDescriptionFragment.setUpLayout(-1, -1);


        } else {
            Intent intent = new Intent(this, DescriptionActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }
}
