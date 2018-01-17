package exampls.com.bakingapp.UI;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
class StepsRecyclerView extends RecyclerView.Adapter<StepsRecyclerView.MyViewHolder> {
    Recipe recipe;

    public Recipe getRecipe() {
        return recipe;
    }

    Context context;
    LayoutInflater inflater;
    public StepsRecyclerView.OnStepClicked onStepClicked;

    public StepsRecyclerView(Recipe recipe, Context context) {
        this.recipe = recipe;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setOnStepClicked(StepsRecyclerView.OnStepClicked onStepClicked) {
        this.onStepClicked = onStepClicked;
    }

    public interface OnStepClicked {
        void onStepClick(int id, int position);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.receipe_step_description_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        if (position == 0) {

            String ingredients = "";
            for (int i = 0; i < recipe.getIngredients().size(); i++) {

                Ingredient ingredient = recipe.getIngredients().get(i);
                ingredients += ingredient.getQuantity() + " " +
                        ingredient.getMeasure() + " " +
                        ingredient.getIngredient() + "\n";

            }

            holder.receipeStepDescription.setText(ingredients);

        } else {


            String shortDescription = recipe.getSteps().get(position - 1).getShortDescription();
            String thumbnail = recipe.getSteps().get(position - 1).getThumbnailURL();
            if (thumbnail.trim().length() != 0)
                Picasso.with(context).load(thumbnail).into(holder.imageView);

            holder.receipeStepDescription.setText(shortDescription);


        }
        if (position != 0)
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStepClicked.onStepClick(recipe.getId(), position - 1);
                }
            });
    }

    @Override
    public int getItemCount() {

        return recipe.getSteps().size() + 1;

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView receipeStepDescription;
        CardView cardView;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            receipeStepDescription = (TextView) itemView
                    .findViewById(R.id.step_short_description_tv);


            cardView = (CardView) itemView.findViewById(R.id.card_view_steps);

            imageView = (ImageView) itemView.findViewById(R.id.step_image);
        }
    }

}

public class StepsFragment extends Fragment implements StepsRecyclerView.OnStepClicked{
    public static final String COMMITED_FRAGMENT = "commited";
    String ingrendientsStr;
    String TAG = "stepsFragment";
    RecyclerView recyclerViewSteps;
    TextView ingrendientsTV;
    GridLayoutManager mLayoutManager;
    String PREFERENCE_NAME = "passedPreference";
    int position = -1;

    Recipe recipe = null;
    FragmentListener fragmentListener = null;

    public void setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    public interface FragmentListener{


        public void onStepClickFragment(int id, int position);


    }


    public StepsFragment() {
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState");
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
        StepsRecyclerView stepsRecyclerView = new StepsRecyclerView(recipe, getActivity());
        stepsRecyclerView.setOnStepClicked(this);

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

    @Override
    public void onStepClick(int id, int position) {
        fragmentListener.onStepClickFragment(id, position);
    }
}
