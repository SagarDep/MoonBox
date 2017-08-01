package net.qiujuer.jumper.sample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import net.qiujuer.genius.ui.widget.Loading;
import net.qiujuer.jumper.sample.factory.HashContract;


public class HashFragment extends Fragment implements HashContract.View {
    private HashContract.Presenter mPresenter;

    private Loading mLoading;
    private Button mSubmit;

    public HashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_hash, container, false);

        mLoading = (Loading) root.findViewById(R.id.loading);
        mSubmit = (Button) root.findViewById(R.id.btn_submit);


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.startCalculate();
            }
        });

        return root;
    }

    @Override
    public void setPresenter(HashContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public void onStartCalculate() {
        mLoading.setProgress(0);
        mSubmit.setEnabled(false);
    }

    @Override
    public void onProgress(float progress) {
        mLoading.setProgress(progress);
    }

    @Override
    public void onSucceed(String str) {
        mLoading.setProgress(1);
        mSubmit.setEnabled(true);
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(String str) {
        mLoading.setProgress(0);
        mSubmit.setEnabled(true);
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }
}
