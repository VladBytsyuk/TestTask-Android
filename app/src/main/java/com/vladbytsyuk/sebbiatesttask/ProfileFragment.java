package com.vladbytsyuk.sebbiatesttask;

import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;


/**
 * Created by VladBytsyuk on 30.10.2015.
 */
public class ProfileFragment extends Fragment {
    private String avatarUrl;
    private String name;
    private Integer friendsCount;


    private ImageView avatarImageView;
    private TextView nameTextView;
    private TextView friendsTextView;


    public static ProfileFragment getInstance(Tweet tweet) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("avatarUrl", tweet.getAvatarUrl());
        bundle.putString("name", tweet.getName());
        bundle.putInt("friendsCount", tweet.getFriendsCount());
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        avatarUrl = null;
        name = null;
        friendsCount = 0;

        avatarImageView = (ImageView) rootView.findViewById(R.id.avatarImageView);
        nameTextView = (TextView) rootView.findViewById(R.id.nameTextView);
        friendsTextView = (TextView) rootView.findViewById(R.id.friendsTextView);


        Bundle bundle = getArguments();
        if (bundle != null) {
            avatarUrl = bundle.getString("avatarUrl");
            Glide.with(this).load(avatarUrl).into(avatarImageView);

            name = bundle.getString("name");
            nameTextView.setText(name);

            friendsCount = bundle.getInt("friendsCount");
            friendsTextView.setText(friendsCount + " friends");
        } else {
            // No data
        }

        return rootView;

    }


}
