package in.healthhunt.view.progressDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import in.healthhunt.R;

/**
 * Created by abhishekkumar on 7/1/18.
 */

public class CustomProgressDialog extends ProgressDialog {
    public CustomProgressDialog(Context context) {
        super(context);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_progress_dialog);
        ImageView dialogIcon = findViewById(R.id.progress_icon);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(dialogIcon);
        Glide.with(getContext()).load(R.mipmap.ic_loader).into(imageViewTarget);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
}
