package exampls.com.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import exampls.com.bakingapp.UI.RecipesActivity;
import exampls.com.bakingapp.data.WidgetTable;
import io.realm.Realm;
import io.realm.RealmResults;

public class ListViewServices implements RemoteViewsService.RemoteViewsFactory {
    RealmResults<WidgetTable> recipes;
    List<WidgetTable> recipesArrayList;
    Context context;
    Realm realm;

    public ListViewServices(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        realm.init(context);
        realm = Realm.getDefaultInstance();
        recipes = realm.where(WidgetTable.class).findAll();
        recipesArrayList = realm.copyFromRealm(recipes);
        realm.close();
    }

    @Override
    public void onDestroy() {
        if (realm != null)
            realm.close();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view_item);
        if (recipesArrayList != null) {
            WidgetTable recipe = recipesArrayList.get(position);
            String name= recipe.getName();
            String ingredients ="" ;
            String  ingredient =  recipe.getIngredients() ;

       //     String itemContent = name + "\n" + ingredients;

            views.setTextViewText(R.id.widget_list_view_textview, ingredient);
            Bundle bundle= new Bundle();
            bundle.putInt(RecipesActivity.RECIPE_KEY, recipe.getId());
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(bundle);

            views.setOnClickFillInIntent(R.id.widget_list_view_textview, fillInIntent);
        }
        return views;
    }


    @Override
    public int getCount() {
        if (recipesArrayList == null) return 0;
        return recipesArrayList.size();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
