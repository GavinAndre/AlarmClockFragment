package com.example.alarmclockfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by GavinAndre on 2016/10/29.
 */
public class CharmBannerFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static CharmBannerFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        CharmBannerFragment fragment = new CharmBannerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charm_banner, container, false);
        switch (mPage) {
            case 1:
                view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));
                break;
            case 2:
                view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.banner_one));
                break;
            case 3:
                view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.banner_three));
                break;
            default:
                break;
        }

        return view;
    }
}
