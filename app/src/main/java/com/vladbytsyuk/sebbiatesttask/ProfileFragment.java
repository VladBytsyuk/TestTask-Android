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

/**
 * Created by VladBytsyuk on 30.10.2015.
 */
public class ProfileFragment extends Fragment {
    private String name;
    private Integer numberOfFriends;

    private ImageView avatarImageView;
    private TextView nameTextView;
    private TextView friendsTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        name = null;
        numberOfFriends = 0;

        avatarImageView = (ImageView) rootView.findViewById(R.id.avatarImageView);
        nameTextView = (TextView) rootView.findViewById(R.id.nameTextView);
        friendsTextView = (TextView) rootView.findViewById(R.id.friendsTextView);

        /* == Fill Views here == */

        return rootView;

    }
}
