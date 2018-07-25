package in.healthhunt.view.profileView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.healthhunt.R;
import in.healthhunt.model.beans.Constants;
import in.healthhunt.view.homeScreenView.IHomeView;

/**
 * Created by abhishekkumar on 7/4/18.
 */

public class AboutUsFragment extends Fragment {


    private final String EVENT = "https://www.healthhunt.in/futureofwellness/";

    @BindView(R.id.contact_us)
    Button mContactUs;

    @BindView(R.id.events_details_view)
    TextView mEventDetails;

    private IHomeView IHomeView;
    private Unbinder mUnbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IHomeView = (IHomeView) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, null);
        mUnbinder = ButterKnife.bind(this, view);
        IHomeView.updateTitle(getString(R.string.about_us));
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mUnbinder != null){
            mUnbinder.unbind();
        }
    }

    @OnClick(R.id.contact_us)
    void onContactUs(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse("mailto:" + Constants.HH_EMAIL));
        startActivity(Intent.createChooser(sendIntent, getString(R.string.share)));
    }

    protected void makeHereClickable()
    {
        String str = getString(R.string.events_details);
        String here = "here";
        int start = str.lastIndexOf(here);
        int end = start + here.length();
        Spannable spannable = new SpannableString(str);

        ClickableSpan termClickable = new ClickableSpan() {
            public void onClick(View view) {

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(getContext(), R.color.hh_text_color));
                //ds.setUnderlineText(false);
            }
        };
        spannable.setSpan(termClickable, start, end, 0);

        mEventDetails.setMovementMethod(LinkMovementMethod.getInstance());
        mEventDetails.setText(spannable);
    }
}
