package com.qinfengwu.tinnews.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qinfengwu.tinnews.R;
import com.qinfengwu.tinnews.repository.NewsRepository;
import com.qinfengwu.tinnews.repository.NewsViewModelFactory;

public class HomeFragment extends Fragment {

    private HomeViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NewsRepository newsRepository = new NewsRepository();
        viewModel = new ViewModelProvider(
                this,
                new NewsViewModelFactory(newsRepository)).get(HomeViewModel.class);

        viewModel.setCountryInput("us");

        // observe(lifecycleOwner, observer)
        viewModel.getTopHeadlines().observe(getViewLifecycleOwner(), newsResponse -> {
            if (newsResponse != null) {
                Log.d("HomeFragment", newsResponse.toString());
            }
        });
    }
}