package exampls.com.bakingapp.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
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
    boolean enteredOnRestoreInstanceState = false;
    DescriptionFragment descriptionFragment = null;

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
            FragmentManager fragmentManager = getSupportFragmentManager();

            if (null != findViewById(R.id.fragment_description)) {
                twoPane = true;


                FragmentManager fragmentManager1 = getSupportFragmentManager();
                descriptionFragment = new DescriptionFragment();
                if (savedInstanceState == null)

                    descriptionFragment.setArguments(new Bundle());
                else
                    descriptionFragment.setArguments(savedInstanceState);

                fragmentManager1.beginTransaction()
                        .add(R.id.fragment_description, descriptionFragment, "descriptionFragment")
                        .commitNow();

                Log.e(TAG, "descriptionfragment commited");

                if(getSupportFragmentManager().findFragmentByTag("descriptionFragment") == null){

                    Log.e(TAG, "try find descriptionfragment: not commited");
                }
                else{
                    Log.e(TAG, "try find descriptionfragment: sure commited");


                }



            }

            bundle.putBoolean(TWO_PANE, twoPane);
            StepsRecyclerView stepsRecyclerView = new StepsRecyclerView(recipe, this);
            stepsRecyclerView.setOnStepClicked(this);
            StepsFragment stepsFragment = new StepsFragment();
            bundle.putParcelable("stepAdapter", stepsRecyclerView);

            stepsFragment.setArguments(bundle);
            if (savedInstanceState == null)
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_steps, stepsFragment, "").commit();
        } else {

        }
    }


    @Override
    public void onStepClick(int id, int position) {

        Bundle bundle = new Bundle();
        bundle.putInt(RecipesActivity.RECIPE_KEY, id);
        bundle.putInt(POSITION_KEY, position);
        if (twoPane) {
                descriptionFragment.getArguments().putInt(RecipesActivity.RECIPE_KEY, id);
                descriptionFragment.getArguments().putInt(POSITION_KEY, position);
                Log.e(TAG, "before setup layout");
                descriptionFragment.setUpLayout();
        } else {
            Intent intent = new Intent(this, DescriptionActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.TAG);
    }

    public StepsActivity() {
    }

    protected StepsActivity(Parcel in) {
        this.TAG = in.readString();
    }

    public static final Creator<StepsActivity> CREATOR = new Creator<StepsActivity>() {
        @Override
        public StepsActivity createFromParcel(Parcel source) {
            return new StepsActivity(source);
        }

        @Override
        public StepsActivity[] newArray(int size) {
            return new StepsActivity[size];
        }
    };

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
}
