package com.vladbytsyuk.sebbiatesttask;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements HashTagFinderFragment.OnItemPressed {
    private HashTagFinderFragment hashTagFinder;
    private Boolean exitNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exitNow = false;
        hashTagFinder = new HashTagFinderFragment();
        setCurrentFragment(R.id.main_container, hashTagFinder, false);
    }

    @Override
    protected void onPause() {
        hashTagFinder.removeOnItemClickListener();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hashTagFinder.setOnItemClickListener(this);
    }

    public void itemPressed(String avatarUrl, String name, Integer friendsCount) {
        exitNow = false;
        setCurrentFragment(R.id.main_container, ProfileFragment.getInstance(avatarUrl, name, friendsCount), true);
    }

    private void setCurrentFragment(int container, Fragment fragment, Boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, fragment);
        if (addToBackStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (isBackStackNotEmpty()) {
            getFragmentManager().popBackStack();
        } else if (!exitNow) {
            Toast.makeText(this, "Press \"Back\" again to exit", Toast.LENGTH_SHORT).show();
            exitNow = true;
        } else {
            super.onBackPressed();
        }
    }

    private boolean isBackStackNotEmpty() {
        return getFragmentManager().getBackStackEntryCount() > 0;
    }
}
