package in.healthhunt.presenter.homeScreenPresenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by abhishekkumar on 4/9/18.
 */

public interface IArticlePresenter {
    void loadFragment(String tag, Bundle bundle);
    int getCount();
    Fragment getItem(int position);
}
