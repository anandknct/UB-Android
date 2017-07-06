package com.unitybound.church.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.unitybound.R;
import butterknife.ButterKnife;

/**
 * Live feed Fragment
 * Created by charchitkasliwal on 10/05/17.
 */

public class ChurchesFragment extends Fragment{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_churches,
                container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }


    /**
     *
     * @param view the view
     */
    private void initViews(View view) {


    }
}
