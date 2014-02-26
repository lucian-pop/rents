package com.personal.rents.activity;

import java.util.ArrayList;

import com.personal.rents.R;
import com.personal.rents.fragment.ImageGridFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

public class ImageGridActivity extends ActionBarActivity {
    private static final String TAG = "ImageGridActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_grid_activity_layout);

        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ImageGridFragment imagesFragment = new ImageGridFragment();
            imagesFragment.setImageURIs(new ArrayList<String>(6));
            ft.add(R.id.fragment_placeholder, new ImageGridFragment(), TAG);
            ft.commit();
        }
    }
}
