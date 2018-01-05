package exampls.com.bakingapp.UI;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import exampls.com.bakingapp.Controller.StepsRecyclerView;
import exampls.com.bakingapp.MyWidgeProvider;
import exampls.com.bakingapp.R;
import exampls.com.bakingapp.data.Ingredient;
import exampls.com.bakingapp.data.Recipe;
import exampls.com.bakingapp.data.WidgetTable;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by 450 G1 on 03/10/2017.
 */

public class StepsFragment extends Fragment {
    String ingrendientsStr;
    String TAG = "stepsFragment";
    RecyclerView recyclerViewSteps;
    TextView ingrendientsTV;
    GridLayoutManager mLayoutManager;

    int position = -1;

    Recipe recipe = null;

    public StepsFragment() {
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int recyclerStateBeforeRotate = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        outState.putInt("test", recyclerStateBeforeRotate);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("test");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_steps, container, false);

        Bundle bundle = getArguments();


        if (bundle != null) {
            Realm.init(getActivity());
            Realm realm = Realm.getDefaultInstance();
            recipe = realm.where(Recipe.class).equalTo("id", bundle.getInt(RecipesActivity.RECIPE_KEY)).findFirst();

        } else Log.e(TAG, "bundle is null");
        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerViewSteps = (RecyclerView) v.findViewById(R.id.rv_steps);
        recyclerViewSteps.setLayoutManager(mLayoutManager);
        StepsRecyclerView stepsRecyclerView = bundle.getParcelable("stepAdapter");


        recyclerViewSteps.setAdapter(stepsRecyclerView);
        if (position != -1) {
            recyclerViewSteps.scrollToPosition(position);

        }

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");

    }

    @Override
    public void onResume() {
        super.onResume();

        Realm.init(getActivity().getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        int id = recipe.getId();
        RealmResults<WidgetTable> results = realm.where(WidgetTable.class).findAll();

        results.deleteAllFromRealm();

        String name = recipe.getName();
        String ingredients = getIngredient();

        WidgetTable widgetTable = new WidgetTable();
        widgetTable.setId(id);
        widgetTable.setName(name);
        widgetTable.setIngredients(ingredients);

        realm.copyToRealmOrUpdate(widgetTable);
        realm.commitTransaction();
        realm.close();

        Realm.init(getActivity().getApplicationContext());


        Intent intent = new Intent(getActivity(), MyWidgeProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = AppWidgetManager.getInstance(getActivity()).getAppWidgetIds(new ComponentName(getActivity(), MyWidgeProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getActivity().sendBroadcast(intent);
    }

    private String getIngredient() {
        String ingredients = "";
        for (int i = 0; i < recipe.getIngredients().size(); i++) {

            Ingredient ingredient = recipe.getIngredients().get(i);
            ingredients += ingredient.getQuantity() + " " +
                    ingredient.getMeasure() + " " +
                    ingredient.getIngredient() + "\n";

        }
        return ingredients;
    }
}
