package exampls.com.bakingapp.UI;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import exampls.com.bakingapp.Controller.RecipesFragmentController;
import exampls.com.bakingapp.Controller.RecipesRecyclerView;
import exampls.com.bakingapp.R;
import exampls.com.bakingapp.SimpleIdlingResource;
import exampls.com.bakingapp.data.Recipe;

/**
 * Created by 450 G1 on 28/09/2017.
 */

public class RecipesFragment extends Fragment implements RecipesFragmentController.OnAdapterFinishIntializing, RecipesFragmentController.OnRecieveRecipeFromCardView {
    private static final String TAG = "RecipesFragment";
    RecyclerView recipesRv;
    private OnRecieveListener onRecieveListener;
    String LIST_STATE_KEY = "list_state";
    Parcelable mListState = null;
    LinearLayoutManager mLinearLayoutManager;
    String RECYCLERVIEW_POSITION = "rvpostion";

    int position = -1;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECYCLERVIEW_POSITION, position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onRecieveListener = (OnRecieveListener) context;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public RecipesFragment() {
    }

    public interface OnRecieveListener {
        void onClick(Recipe recipe);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        recipesRv = (RecyclerView) view.findViewById(R.id.recipes_rv);
        Bundle bundle = getArguments();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        recipesRv.setLayoutManager(mLinearLayoutManager);

        RecipesFragmentController recipesFragmentController = new RecipesFragmentController(getActivity(), -1);
        recipesFragmentController.setOnAdapterFinishIntializing(this);
        recipesFragmentController.setOnRecieveRecipeFromCardView(this);
        recipesFragmentController.getOnlineResponse();
        return view;
    }

    @Override
    public void passAdapter(RecipesRecyclerView recipesRecyclerView) {
        if (recipesRecyclerView != null) {
            Log.e(TAG, "adapter is here don't worry");
            recipesRv.setAdapter(recipesRecyclerView);
            if (position != -1)
                recipesRv.scrollToPosition(position);

        }
        SimpleIdlingResource simpleIdlingResource = ((RecipesActivity) getActivity()).getmSimpleIdlingResource();

        if (simpleIdlingResource != null)
            simpleIdlingResource.setIdleState(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(RECYCLERVIEW_POSITION);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // deleting the sharedpreference that store the position of the recyclerview


    }

    @Override
    public void onRecieve(Recipe recipe) {
        onRecieveListener.onClick(recipe);
    }
}
