package com.qinfengwu.tinnews.ui.save;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qinfengwu.tinnews.databinding.FragmentSaveBinding;
import com.qinfengwu.tinnews.model.Article;
import com.qinfengwu.tinnews.repository.NewsRepository;
import com.qinfengwu.tinnews.repository.NewsViewModelFactory;


public class SaveFragment extends Fragment {
    private SaveViewModel viewModel;
    private FragmentSaveBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // repo.getAllArticles()
        // adapter.notifyDataSetChange()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentSaveBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SavedNewsAdapter savedNewsAdapter = new SavedNewsAdapter();
        savedNewsAdapter.setItemCallback(new SavedNewsAdapter.ItemCallback() {
            @Override
            public void onOpenDetails(Article article) {
                Log.d("onOpenDetails", article.toString());
                SaveFragmentDirections.ActionNavigationSaveToNavigationDetails direction =
                        SaveFragmentDirections.actionNavigationSaveToNavigationDetails(article);

                NavHostFragment.findNavController(SaveFragment.this).navigate(direction);
            }

            @Override
            public void onRemoveFavorite(Article article) {
                viewModel.deleteSavedArticles(article);
            }
        });
        binding.newsSavedRecyclerView.setAdapter(savedNewsAdapter);
        binding.newsSavedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        NewsRepository newsRepository = new NewsRepository();
        viewModel = new ViewModelProvider(
                this,
                new NewsViewModelFactory(newsRepository)).get(SaveViewModel.class);
        viewModel.getAllSavedArticles().observe(getViewLifecycleOwner(), articles -> {
            // update UI
            if (articles != null) {
                Log.d("SaveFragment", articles.toString());
                savedNewsAdapter.setArticles(articles);
            }
        });

    }
}