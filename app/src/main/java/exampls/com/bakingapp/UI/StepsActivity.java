package exampls.com.bakingapp.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import exampls.com.bakingapp.Controller.StepsRecyclerView;
import exampls.com.bakingapp.R;
import exampls.com.bakingapp.data.Recipe;
import io.realm.Realm;

public class StepsActivity extends AppCompatActivity implements StepsRecyclerView.OnStepClicked {
    String TAG = "stepsActivity";
    private static final String POSITION_KEY = "position";
    boolean twoPane = false;
    public static final String TWO_PANE = "twopane";
    DescriptionFragment descriptionFragment = null;
    @Override public boolean onNavigateUpFromChild(Activity child) {
        return super.onNavigateUpFromChild(child);
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        Log.e(TAG, "oncreate");
        Bundle bundle = getIntent().getExtras();
        int recipeId = bundle.getInt(RecipesActivity.RECIPE_KEY);

        Log.e(TAG, "recipe id : " + recipeId);
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        Recipe recipe = realm.where(Recipe.class).equalTo("id", recipeId).findFirst();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        if(toolbar != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        if (recipe != null) {
            getSupportActionBar().setTitle(recipe.getName());
            Log.e(TAG, "RECIPE IS N'T NULL");
            FragmentManager fragmentManager = getSupportFragmentManager();

            if (null != findViewById(R.id.fragment_description)) {
                Log.e("StepsActivity", "yes two pane");
                twoPane = true;
//                DescriptionFragment descriptionFragment = new DescriptionFragment();
//                descriptionFragment.setArguments(null);
//                fragmentManager.beginTransaction()
//                        .add(R.id.fragment_description, descriptionFragment)
//                        .commit();


            }
            bundle.putBoolean(TWO_PANE, twoPane );
            StepsRecyclerView stepsRecyclerView = new StepsRecyclerView(recipe, this);
            stepsRecyclerView.setOnStepClicked(this);
            StepsFragment stepsFragment = new StepsFragment();
            bundle.putParcelable("stepAdapter", stepsRecyclerView);

            stepsFragment.setArguments(bundle);
            if (savedInstanceState == null)
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_steps, stepsFragment, "").commit();
        } else {

            Log.e(TAG, "recipe is null");

        }
    }



    @Override public void onStepClick(int id, int position) {
        Log.e(TAG, "onStepClicked");

        Bundle bundle = new Bundle();
        bundle.putInt(RecipesActivity.RECIPE_KEY, id);
        bundle.putInt(POSITION_KEY, position);
        if (twoPane) {

                /*FragmentManager fragmentManager = getSupportFragmentManager();
                DescriptionFragment descriptionFragment = new DescriptionFragment();
                descriptionFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_description, descriptionFragment)
                        .commit();*/
            descriptionFragment = (DescriptionFragment)getSupportFragmentManager().findFragmentByTag("descriptionFragment");
            descriptionFragment.getArguments().putInt(RecipesActivity.RECIPE_KEY, id);
            descriptionFragment.getArguments().putInt(POSITION_KEY, position);
            Log.e(TAG, "fragment tag "+descriptionFragment.getTag());
            Log.e(TAG, "before setup layout");
            descriptionFragment.setUpLayout();

        } else {

            Intent intent = new Intent(this, DescriptionActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }


    }

    @Override  public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.TAG);
    }

    public StepsActivity() {
    }

    protected StepsActivity(Parcel in) {
        this.TAG = in.readString();
    }

    public static final Creator<StepsActivity> CREATOR = new Creator<StepsActivity>() {
        @Override public StepsActivity createFromParcel(Parcel source) {
            return new StepsActivity(source);
        }

        @Override public StepsActivity[] newArray(int size) {
            return new StepsActivity[size];
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(TAG, "onRestoreInstanceState");
        if (twoPane) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            descriptionFragment = new DescriptionFragment();
            descriptionFragment.setArguments(savedInstanceState);
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_description, descriptionFragment, "descriptionFragment")
                    .commit();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG ,"onResume");
        if (twoPane) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("descriptionFragment");

            if(!(fragment instanceof DescriptionFragment ) && (fragment == null)) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                descriptionFragment = new DescriptionFragment();
                descriptionFragment.setArguments(new Bundle());
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_description, descriptionFragment, "descriptionFragment")
                        .commit();

                Log.e(TAG,"commited");

            }
        }

    }

    public void initializeFragment(){

    }

    public void initializeFragment(Bundle bundle){



    }
}
