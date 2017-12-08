package exampls.com.bakingapp.Controller;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import exampls.com.bakingapp.R;
import exampls.com.bakingapp.data.Ingredient;
import exampls.com.bakingapp.data.Recipe;

/**
 * Created by 450 G1 on 03/10/2017.
 */

public class StepsRecyclerView extends RecyclerView.Adapter<StepsRecyclerView.MyViewHolder> implements Parcelable {
    Recipe recipe;

    public Recipe getRecipe() {
        return recipe;
    }

    Context context;
    LayoutInflater inflater;
    public OnStepClicked onStepClicked;

    public StepsRecyclerView(Recipe recipe, Context context) {
        this.recipe = recipe;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setOnStepClicked(OnStepClicked onStepClicked) {
        this.onStepClicked = onStepClicked;
    }

    public interface OnStepClicked extends Parcelable {
        void onStepClick(int id, int position);

        @Override
        int describeContents();

        @Override
        void writeToParcel(Parcel dest, int flags);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.receipe_step_description_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if(position == 0 ){

            String ingredients = "";
            for (int i = 0; i < recipe.getIngredients().size(); i++) {

                Ingredient ingredient = recipe.getIngredients().get(i);
                ingredients += ingredient.getQuantity() + " " +
                        ingredient.getMeasure() + " " +
                        ingredient.getIngredient() + "\n";

            }

            holder.receipeStepDescription.setText(ingredients);

        }else {


            String shortDescription = recipe.getSteps().get(position-1).getShortDescription();
            String thumbnail = recipe.getSteps().get(position-1).getThumbnailURL();
            if (thumbnail.trim().length() != 0)
                Picasso.with(context).load(thumbnail).into(holder.imageView);

            holder.receipeStepDescription.setText(shortDescription);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStepClicked.onStepClick(recipe.getId(), position-1);
                }
            });


        }
    }

    @Override
    public int getItemCount() {

        return recipe.getSteps().size() + 1 ;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.recipe, flags);
        dest.writeParcelable(this.onStepClicked, flags);
    }

    protected StepsRecyclerView(Parcel in) {
        this.recipe = in.readParcelable(Recipe.class.getClassLoader());
        this.context = in.readParcelable(Context.class.getClassLoader());
        this.inflater = in.readParcelable(LayoutInflater.class.getClassLoader());
        this.onStepClicked = in.readParcelable(OnStepClicked.class.getClassLoader());
    }

    public static final Parcelable.Creator<StepsRecyclerView> CREATOR = new Parcelable.Creator<StepsRecyclerView>() {
        @Override
        public StepsRecyclerView createFromParcel(Parcel source) {
            return new StepsRecyclerView(source);
        }

        @Override
        public StepsRecyclerView[] newArray(int size) {
            return new StepsRecyclerView[size];
        }
    };
}
