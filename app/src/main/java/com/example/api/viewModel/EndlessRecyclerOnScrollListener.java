package com.example.api.viewModel;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    int pastVisibleItems, visibleItemCount, totalItemCount;
    private int previousTotal = 0;
    private boolean isLoading = true;
    private int page = 1;
    private GridLayoutManager layoutManager;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public EndlessRecyclerOnScrollListener(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        this.visibleItemCount = recyclerView.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

        if (dy > 0) {
            if (isLoading) {
                if (totalItemCount > previousTotal) {
                    isLoading = false;
                    previousTotal = totalItemCount;
                }
            }
        }
        if (!isLoading && (totalItemCount - visibleItemCount) <= (pastVisibleItems + 20)) {
            page++;
            onLoadMore(page);
            isLoading = true;
        }
    }

    public abstract void onLoadMore(int nextPage);
}
