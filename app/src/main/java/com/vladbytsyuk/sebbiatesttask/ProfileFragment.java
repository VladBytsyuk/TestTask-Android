package com.vladbytsyuk.sebbiatesttask;

import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


/**
 * Created by VladBytsyuk on 30.10.2015.
 */
public class ProfileFragment extends Fragment {
    private ProgressBar progressBar;

    public static ProfileFragment getInstance(Tweet tweet) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userName", tweet.getUserName());
        bundle.putString("avatarUrl", tweet.getAvatarUrl());
        bundle.putInt("friendsCount", tweet.getFriendsCount());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        String userName = null;
        String avatarUrl = null;
        Integer friendsCount = 0;

        TextView nameTextView = (TextView) rootView.findViewById(R.id.nameTextView);
        ImageView avatarImageView = (ImageView) rootView.findViewById(R.id.avatarImageView);
        TextView friendsTextView = (TextView) rootView.findViewById(R.id.friendsTextView);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarProfile);
        progressBar.setVisibility(View.VISIBLE);

        Bundle bundle = getArguments();
        if (bundle != null) {
            avatarUrl = bundle.getString("avatarUrl");
            Glide.with(this).load(avatarUrl).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    progressBar.setVisibility(View.INVISIBLE);
                    return false;
                }
            }).into(avatarImageView);

            userName = bundle.getString("userName");
            nameTextView.setText(userName);

            friendsCount = bundle.getInt("friendsCount");
            friendsTextView.setText(friendsCount + " friends");
        }

        return rootView;
    }
}
