package pl.wojciechpitek.shoplist.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pl.wojciechpitek.shoplist.R;
import pl.wojciechpitek.shoplist.holders.BaseHolder;
import pl.wojciechpitek.shoplist.holders.LoaderHolder;

import java.util.*;

/**
 * Abstract class used for creating adapter for recycler view
 *
 * @param <T> Type of items in recycler view
 */
public abstract class AbstractRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseHolder> {

    private static final int ITEM_HOLDER = 1;
    private static final int FOOTER_HOLDER = 0;

    private Boolean isNextPageAvailable = Boolean.FALSE;

    private List<T> items = new ArrayList<>();
    private Comparator<T> itemsComparator = null;

    private RecyclerView recyclerView = null;
    private View noItemsView = null;

    /**
     * Abstract method used to bind data in holder
     *
     * @param holder Superclass {@link BaseHolder} of holder
     * @param position Holder position in items list
     */
    protected abstract void onBindDataHolder(BaseHolder holder, int position);

    /**
     * Abstract method called when last item in list was visible and next items was available
     *
     * @param loadedItems Count of loaded items
     */
    protected abstract void loadNextPage(Integer loadedItems);

    /**
     * Abstract method for create single row holder
     * Method called when holder for new item was created
     *
     * @param viewGroup View group
     * @return Subclass of {@link BaseHolder}
     */
    protected abstract BaseHolder onCreateBaseViewHolder(ViewGroup viewGroup);

    /**
     * Constructor of class
     * This constructor can be called to create a static list without empty list view
     *
     * @param mRecyclerView Link to [@link RecyclerView} in layout
     */
    public AbstractRecyclerViewAdapter(RecyclerView mRecyclerView) {
        this(mRecyclerView, null, Boolean.FALSE);
    }

    /**
     * Constructor of class
     * This constructor can be called to create a static list
     *
     * @param mRecyclerView Link to [@link RecyclerView} in layout
     * @param noItemsView Link to view that shows when there are no items in list
     */
    public AbstractRecyclerViewAdapter(RecyclerView mRecyclerView, View noItemsView) {
        this(mRecyclerView, noItemsView, Boolean.FALSE);
    }

    /**
     * Constructor of class
     * This constructor can be called to create dynamic list
     *
     * @param mRecyclerView Link to [@link RecyclerView} in layout
     * @param noItemsView Link to view that shows when there are no items in list
     * @param startInfiniteLoader Variable used to set that the infinite load should start
     */
    public AbstractRecyclerViewAdapter(RecyclerView mRecyclerView, View noItemsView, Boolean startInfiniteLoader) {
        recyclerView = mRecyclerView;
        this.noItemsView = noItemsView;

        if(startInfiniteLoader) {
            addLoader();
            loadNextPage(0);
        }

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isNextPageAvailable && !isLoaderShown() && linearLayoutManager.findLastVisibleItemPosition() >= items.size() - 1) {
                    loadNextPage(items.size());
                    addLoader();
                }
            }
        });

        setVisibilityListIfItemsExists();
    }

    /**
     * Method used to get list of items
     *
     * @return list of items
     */
    public List<T> getItems() {
        return items;
    }

    /**
     * Method used for add items to static list
     *
     * @param items List of items
     */
    public void addItems(List<T> items) {
        addItems(items, Boolean.FALSE);
    }

    /**
     * Method used for add all items to list
     *
     * @param items List of items
     * @param isNextPageAvailable Variable used to set if next page are available
     */
    public void addItems(List<T> items, Boolean isNextPageAvailable) {
        if(isLoaderShown()) {
            this.items.remove(this.items.size() - 1);
        }
        this.items.addAll(items);
        this.isNextPageAvailable = isNextPageAvailable;
        myNotifyDataSetChanged();
    }

    /**
     * Method used for add item to static list
     *
     * @param item Item to add
     */
    public void addItem(T item) {
        addItem(item, Boolean.FALSE);
    }

    /**
     * Method used for add item to dynamic list
     *
     * @param item Item to add
     * @param isNextPageAvailable Variable used to set if next page are available
     */
    public void addItem(T item, Boolean isNextPageAvailable) {
        List<T> items = new ArrayList<>();
        items.add(item);
        addItems(items, isNextPageAvailable);
    }

    /**
     * Method used for clear items from list
     */
    public void clearList() {
        this.items.clear();
        this.isNextPageAvailable = Boolean.FALSE;
        myNotifyDataSetChanged();
    }

    /**
     * Method used for remove item from list
     *
     * @param position item's position
     */
    public void removeItem(int position) {
        this.items.remove(position);
        myNotifyDataSetChanged();
    }

    /**
     * Method used for set comparator to item's list
     *
     * @param comparator Comparator of list
     */
    public void setItemsComparator(Comparator<T> comparator) {
        this.itemsComparator = comparator;
    }

    /**
     * Method used for notify adapter that data set changed with comparator
     */
    public final void myNotifyDataSetChanged() {
        if(itemsComparator != null) {
            Collections.sort(items, itemsComparator);
        }
        notifyDataSetChanged();
        setVisibilityListIfItemsExists();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) != null ? ITEM_HOLDER : FOOTER_HOLDER;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        switch(i) {
            case ITEM_HOLDER:
                return onCreateBaseViewHolder(viewGroup);

            case FOOTER_HOLDER:
                return onCreateFooterViewHolder(viewGroup);

            default:
                throw new IllegalArgumentException("Bad holder type!");
        }
    }

    @Override
    public void onBindViewHolder(BaseHolder baseHolder, int i) {
        switch(getItemViewType(i)) {
            case ITEM_HOLDER:
                onBindDataHolder(baseHolder, i);
                break;
            case FOOTER_HOLDER:
                onBindFooter(baseHolder);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private LoaderHolder onCreateFooterViewHolder(ViewGroup viewGroup) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_list_loader, viewGroup, false);
        return new LoaderHolder(v);
    }

    private void onBindFooter(BaseHolder holder) {
        ((LoaderHolder) holder).progressBar.setIndeterminate(true);
    }

    private void addLoader() {
        items.add(null);
        myNotifyDataSetChanged();
    }

    private Boolean isLoaderShown() {
        return items.size() > 0 && items.get(items.size() - 1) == null;
    }

    private void setVisibilityListIfItemsExists() {
        if(noItemsView != null) {
            if (items.size() > 0) {
                if(noItemsView.getVisibility() == View.VISIBLE) {
                    noItemsView.setVisibility(View.GONE);
                }
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                noItemsView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }
}
