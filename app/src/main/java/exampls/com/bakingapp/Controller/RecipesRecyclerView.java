package exampls.com.bakingapp.Controller;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import exampls.com.bakingapp.R;
import exampls.com.bakingapp.data.Recipe;

/**
 * Created by 450 G1 on 28/09/2017.
 */

public class RecipesRecyclerView extends RecyclerView.Adapter<RecipesRecyclerView.MyViewHolder> {
    List<Recipe> recipesList;
    Context context;
    LayoutInflater inflater;
    OnCardViewClicked onCardViewClicked;

    public void setOnCardViewClicked(OnCardViewClicked onCardViewClicked) {
        this.onCardViewClicked = onCardViewClicked;
    }

    public interface OnCardViewClicked {
        void onCardClicked(Recipe recipe);
    }

    public RecipesRecyclerView(Context context, List<Recipe> recipesList) {
        this.recipesList = recipesList;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_item_recipes, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String recipeName = recipesList.get(position).getName();
        String image = recipesList.get(position).getImage();
        holder.recipeNameTV.setText(recipeName);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onCardViewClicked.onCardClicked(recipesList.get(position));
            }
        });

        if (image.trim().length()!=0){
            Picasso.with(context).load(recipesList.get(position).getImage()).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView recipeNameTV;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            recipeNameTV = (TextView) itemView.findViewById(R.id.recipe_name);
            cardView = (CardView) itemView.findViewById(R.id.card_view_recipe);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }
}
