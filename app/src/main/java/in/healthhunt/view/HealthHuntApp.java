package in.healthhunt.view;

import android.app.Application;

import com.activeandroid.ActiveAndroid;


/**
 * Created by abhishekkumar on 5/29/18.
 */

public class HealthHuntApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /*Configuration dbConfiguration = new Configuration.Builder(this)
                .setDatabaseName("MyDatabase.db")*/
                /*.addModelClass(User.class)
                .addModelClass(MediaItem.class)
                .addModelClass(ArticlePostItem.class)
                .addModelClass(ProductPostItem.class)
                .addModelClass(Content.class)
                .addModelClass(Title.class)
                .addModelClass(CategoriesItem.class)
                .addModelClass(Author.class)
                .addModelClass(CurrentUser.class)
                .addModelClass(Excerpt.class)
                .addModelClass(Likes.class)
                .addModelClass(Synopsis.class)
                .addModelClass(TagsItem.class)
                .addModelClass(Collections.class)*/
                //.create();

        //ActiveAndroid.initialize(dbConfiguration);
        ActiveAndroid.initialize(this);
    }
}

