package com.vladbytsyuk.sebbiatesttask;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements HashTagFinderFragment.OnItemPressed {
    private HashTagFinderFragment hashTagFinderFragment;
    private Boolean isHashTagFinderFragmentWasCreated;
    private String HASHTAG_FINDER_FRAGMENT_TAG = "hashtag_finder_fragment_tag";
    private String HASHTAG_FINDER_FRAGMENT_WAS_CREATED_TAG = "hashtag_finder_fragment_was_created_tag";

    private ProfileFragment profileFragment;
    private Boolean isProfileFragmentWasCreated;
    private Boolean isProfileFragmentActive;
    private String PROFILE_FRAGMENT_TAG = "profile_fragment_tag";
    private String PROFILE_FRAGMENT_WAS_CREATED_TAG = "profile_fragment_was_created_tag";
    private String PROFILE_FRAGMENT_ACTIVE_TAG = "profile_fragment_active_tag";

    private Boolean exitNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exitNow = false;

        fragmentsInfoInit(savedInstanceState);
        setFirstFragment();
    }

    private void fragmentsInfoInit(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isHashTagFinderFragmentWasCreated = savedInstanceState.getBoolean(HASHTAG_FINDER_FRAGMENT_WAS_CREATED_TAG);
            isProfileFragmentWasCreated = savedInstanceState.getBoolean(PROFILE_FRAGMENT_WAS_CREATED_TAG);
            isProfileFragmentActive = savedInstanceState.getBoolean(PROFILE_FRAGMENT_ACTIVE_TAG);
        } else {
            isHashTagFinderFragmentWasCreated = false;
            isProfileFragmentWasCreated = false;
            isProfileFragmentActive = false;
        }
    }

    private void setFirstFragment() {
        if (isProfileFragmentWasCreated) {
            hashTagFinderFragment = (HashTagFinderFragment) getFragmentManager().findFragmentByTag(HASHTAG_FINDER_FRAGMENT_TAG);
            setCurrentFragment(R.id.main_container, hashTagFinderFragment, HASHTAG_FINDER_FRAGMENT_TAG, false);

            profileFragment = (ProfileFragment) getFragmentManager().findFragmentByTag(PROFILE_FRAGMENT_TAG);
            if (isProfileFragmentActive) {
                setCurrentFragment(R.id.main_container, profileFragment, PROFILE_FRAGMENT_TAG, true);
            }
        } else {
            if (isHashTagFinderFragmentWasCreated) {
                hashTagFinderFragment = (HashTagFinderFragment) getFragmentManager().findFragmentByTag(HASHTAG_FINDER_FRAGMENT_TAG);
            }
            else {
                hashTagFinderFragment = new HashTagFinderFragment();
            }
            setCurrentFragment(R.id.main_container, hashTagFinderFragment, HASHTAG_FINDER_FRAGMENT_TAG, false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        FragmentManager fragmentManager = getFragmentManager();

        isHashTagFinderFragmentWasCreated = fragmentManager.findFragmentByTag(HASHTAG_FINDER_FRAGMENT_TAG) != null;
        outState.putBoolean(HASHTAG_FINDER_FRAGMENT_WAS_CREATED_TAG, isHashTagFinderFragmentWasCreated);

        isProfileFragmentWasCreated = fragmentManager.findFragmentByTag(PROFILE_FRAGMENT_TAG) != null;
        outState.putBoolean(PROFILE_FRAGMENT_WAS_CREATED_TAG, isProfileFragmentWasCreated);

        outState.putBoolean(PROFILE_FRAGMENT_ACTIVE_TAG, isProfileFragmentActive);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        isHashTagFinderFragmentWasCreated = savedInstanceState.getBoolean(HASHTAG_FINDER_FRAGMENT_WAS_CREATED_TAG, false);
        isProfileFragmentWasCreated = savedInstanceState.getBoolean(PROFILE_FRAGMENT_WAS_CREATED_TAG, false);
        isProfileFragmentActive = savedInstanceState.getBoolean(PROFILE_FRAGMENT_ACTIVE_TAG, false);

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        hashTagFinderFragment.removeOnItemClickListener();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hashTagFinderFragment.setOnItemClickListener(this);
    }

    public void itemPressed(Tweet tweet) {
        exitNow = false;
        profileFragment = getProfileFragment(tweet);
        setCurrentFragment(R.id.main_container, profileFragment, PROFILE_FRAGMENT_TAG, true);
    }

    private ProfileFragment getProfileFragment(Tweet tweet) {
        ProfileFragment profileFrag = ProfileFragment.getInstance(tweet);
        isProfileFragmentActive = true;
        return profileFrag;
    }

    private void setCurrentFragment(int container, Fragment fragment, String tag, Boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, fragment, tag);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (isProfileFragmentActive) {
            fragmentManager.popBackStack();
            isProfileFragmentActive = false;
        } else if (!exitNow) {
            Toast.makeText(this, R.string.exitToast, Toast.LENGTH_SHORT).show();
            exitNow = true;
        } else {
            super.onBackPressed();
        }
    }
}
