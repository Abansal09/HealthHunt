package in.healthhunt.model.database;

import com.activeandroid.Configuration;
import com.activeandroid.content.ContentProvider;

import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.commonResponse.Author;
import in.healthhunt.model.articles.commonResponse.CategoriesItem;
import in.healthhunt.model.articles.commonResponse.Collections;
import in.healthhunt.model.articles.commonResponse.Content;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.commonResponse.Excerpt;
import in.healthhunt.model.articles.commonResponse.Likes;
import in.healthhunt.model.articles.commonResponse.MediaItem;
import in.healthhunt.model.articles.commonResponse.Synopsis;
import in.healthhunt.model.articles.commonResponse.TagsItem;
import in.healthhunt.model.articles.commonResponse.Title;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.login.User;

public class HHContentProvider extends ContentProvider {

	@Override
	protected Configuration getConfiguration() {
		Configuration.Builder dbConfiguration = new Configuration.Builder(getContext())
				.setDatabaseName("MyDatabase.db")
				.addModelClass(User.class)
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
				.addModelClass(Collections.class);
		return dbConfiguration.create();
	}
}