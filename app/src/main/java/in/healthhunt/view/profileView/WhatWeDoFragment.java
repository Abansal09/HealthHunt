package in.healthhunt.view.profileView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.healthhunt.R;
import in.healthhunt.view.homeScreenView.IHomeView;

/**
 * Created by abhishekkumar on 7/4/18.
 */

public class WhatWeDoFragment extends Fragment {

    private IHomeView IHomeView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IHomeView = (IHomeView) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_what_we_do, null);
        IHomeView.updateTitle(getString(R.string.what_we_de));
        return view;
    }

    /*public boolean onBackPressed(){
        return false;
    }*/
}
