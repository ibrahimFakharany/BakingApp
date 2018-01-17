package exampls.com.bakingapp.Controller;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import exampls.com.bakingapp.data.Ingredient;
import exampls.com.bakingapp.data.NetworkCalls;
import exampls.com.bakingapp.data.Recipe;
import exampls.com.bakingapp.data.Step;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by 450 G1 on 28/09/2017.
 */

public class RecipesFragmentController {

    private Context context;
    private final String TAG = "RecipesController";
    Recipe recipe = null;
    OnAdapterFinishIntializing onAdapterFinishIntializing;
    //OnStepAdapterFinishIntializing onStepAdapterFinishIntializing;
    OnRecieveRecipeFromCardView onRecieveRecipeFromCardView;

    public void setOnRecieveStepListener(OnRecieveStepListener onRecieveStepListener) {
        this.onRecieveStepListener = onRecieveStepListener;
    }

    OnRecieveStepListener onRecieveStepListener;

    private int id;


    public interface OnRecieveStepListener {
        void onRecieveStep(Step step);
    }

    public void setOnRecieveRecipeFromCardView(OnRecieveRecipeFromCardView onRecieveRecipeFromCardView) {
        this.onRecieveRecipeFromCardView = onRecieveRecipeFromCardView;
    }

    public void setOnAdapterFinishIntializing(OnAdapterFinishIntializing onAdapterFinishIntializing) {
        this.onAdapterFinishIntializing = onAdapterFinishIntializing;
    }

    /*public void setOnStepAdapterFinishIntializing(OnStepAdapterFinishIntializing onStepAdapterFinishIntializing) {
        this.onStepAdapterFinishIntializing = onStepAdapterFinishIntializing;
    }*/

    public interface OnRecieveRecipeFromCardView {
        void onRecieve(Recipe recipe);
    }

    public RecipesFragmentController(Context context, int id) {
        this.id = id;
        this.context = context;
    }

    public interface OnAdapterFinishIntializing {
        void passAdapter(RecipesRecyclerView recipesRecyclerView);
    }

    /*public interface OnStepAdapterFinishIntializing {
        void passAdapter(StepsRecyclerView stepsRecyclerView);
    }*/

    public void getOnlineResponse() {

        NetworkCalls networkCalls = new NetworkCalls(context);

        OnCallFinishCallback onCallFinishCallback = new OnCallFinishCallback() {
            @Override
            public void onSuccess(String response) {
                initializeAdapter(response);
            }
        };
        networkCalls.getFromUrl(onCallFinishCallback);

    }

    private List<Recipe> cuttingJSON(String response) {

        final ArrayList<Recipe> list = new ArrayList<>();
        try {

            JSONArray mainArray = new JSONArray(response);
            JSONObject object = null;
            for (int i = 0; i < mainArray.length(); ++i) {
                object = mainArray.getJSONObject(i);
                String recipeName = object.getString("name");
                int id = object.getInt("id");
                String image = object.getString("image");
                JSONArray ingrendientsArray = object.getJSONArray("ingredients");
                JSONArray stepsArray = object.getJSONArray("steps");

                RealmList<Ingredient> ingredients = new RealmList<>();
                RealmList<Step> steps = new RealmList<>();

                JSONObject ingredientsObj = null;
                for (int j = 0; j < ingrendientsArray.length(); j++) {
                    ingredientsObj = ingrendientsArray.getJSONObject(j);

                    ingredients.add(new Ingredient(
                            ingredientsObj.getInt("quantity")
                            , ingredientsObj.getString("measure")
                            , ingredientsObj.getString("ingredient")));

                }

                JSONObject stepObj = null;
                for (int k = 0; k < stepsArray.length(); k++) {
                    stepObj = stepsArray.getJSONObject(k);

                    steps.add(new Step(
                            stepObj.getInt("id")
                            , stepObj.getString("shortDescription")
                            , stepObj.getString("description")
                            , stepObj.getString("videoURL")
                            , stepObj.getString("thumbnailURL")));

                }

                list.add(new Recipe(id, recipeName, ingredients, steps, image));
            }


        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return list;

    }

    private void initializeAdapter(String response) {

        List<Recipe> list = cuttingJSON(response);

        Realm.init(context.getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        for (int i = 0; i < list.size(); i++) {

            realm.beginTransaction();
            Recipe recipeModel = new Recipe();
            recipeModel.setName(list.get(i).getName());
            recipeModel.setId(list.get(i).getId());
            recipeModel.setIngredients(list.get(i).getIngredients());
            recipeModel.setSteps(list.get(i).getSteps());
            recipeModel.setImage(list.get(i).getImage());
            realm.copyToRealmOrUpdate(recipeModel);
            realm.commitTransaction();
        }
        realm.close();

        RecipesRecyclerView adapter = new RecipesRecyclerView(context, list);

        adapter.setOnCardViewClicked(new RecipesRecyclerView.OnCardViewClicked() {
            @Override
            public void onCardClicked(Recipe recipe) {
                onRecieveRecipeFromCardView.onRecieve(recipe);
            }
        });

        onAdapterFinishIntializing.passAdapter(adapter);


    }
}
