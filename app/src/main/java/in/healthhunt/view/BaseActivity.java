package in.healthhunt.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;

import framework.permisisons.PermissionActivity;
import in.healthhunt.view.progressDialog.CustomProgressDialog;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class BaseActivity extends PermissionActivity {

    public ProgressDialog mProgress;
    //AlertDialog.Builder builder;
    //public AlertDialog mProgress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View layout = inflater.inflate(R.layout.custom_progress_dialog, null);
        //builder = new AlertDialog.Builder(this);
        //builder.setView(layout);
        //mProgress = builder.create();

        //mProgress = new ProgressDialog(this);
        mProgress = new CustomProgressDialog(this);
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
       // mProgress.setMessage(getResources().getString(R.string.please_wait));

        //ImageView dialogIcon = layout.findViewById(R.id.progress_icon);
        //GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(dialogIcon);
        //Glide.with(this).load(R.mipmap.loader).into(imageViewTarget);
        //mProgress.setView(layout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mProgress != null) {
            mProgress.dismiss();
        }
    }

    @Override
    public void setPermission(int requestCode, boolean isGranted) {
    }
}
