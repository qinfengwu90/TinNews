package com.qinfengwu.tinnews.ui.search;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.qinfengwu.tinnews.R;
import com.qinfengwu.tinnews.databinding.FragmentSearchBinding;
import com.qinfengwu.tinnews.model.Article;
import com.qinfengwu.tinnews.repository.NewsRepository;
import com.qinfengwu.tinnews.repository.NewsViewModelFactory;

public class SearchFragment extends Fragment {

    private SearchViewModel viewModel;
    private FragmentSearchBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_search, container, false);
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchNewsAdapter newsAdapter = new SearchNewsAdapter();
        newsAdapter.setItemCallback(article -> {
            SearchFragmentDirections.ActionNavigationSearchToNavigationDetails
                    direction = SearchFragmentDirections.actionNavigationSearchToNavigationDetails(article);
            NavHostFragment.findNavController(SearchFragment.this).navigate(direction);
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        binding.newsResultsRecyclerView.setLayoutManager(gridLayoutManager);
        binding.newsResultsRecyclerView.setAdapter(newsAdapter);

        binding.newsResultsRecyclerView.setPadding(10, 0, 10, 0);
        binding.newsResultsRecyclerView.setClipToPadding(false);
        binding.newsResultsRecyclerView.setClipChildren(false);
        binding.newsResultsRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.set(10,0,10,0);
            }
        });

        binding.newsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    viewModel.setSearchInput(query);
                }
                binding.newsSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        NewsRepository repository = new NewsRepository();
        viewModel = new ViewModelProvider(this,
                new NewsViewModelFactory(repository)).get(SearchViewModel.class);
        viewModel.searchNews().observe(getViewLifecycleOwner(), newsResponse -> {
            if (newsResponse != null) {
                Log.d("SearchFragment", newsResponse.toString());
                newsAdapter.setArticles(newsResponse.articles);
            }
        });
    }
}