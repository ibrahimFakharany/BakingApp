package exampls.com.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 450 G1 on 28/09/2017.
 */

public class Recipe extends RealmObject implements Parcelable {
    public Recipe() {
    }

    @PrimaryKey
    int id;
    String name;
    RealmList<Ingredient> ingredients;
    RealmList<Step> steps;
    String image;

    public Recipe(int id, String name, RealmList<Ingredient> ingredients, RealmList<Step> steps, String image) {
        this.id = id;
        this.steps = steps;
        this.ingredients = ingredients;
        this.name = name;
        this.image = image;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(RealmList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(RealmList<Step> steps) {
        this.steps = steps;
    }

    public int getId() {
        return id;
    }

    public RealmList<Step> getSteps() {
        return steps;
    }

    public RealmList<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getName() {
        return name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.ingredients);
        dest.writeTypedList(this.steps);
        dest.writeString(this.image);
    }

    protected Recipe(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
