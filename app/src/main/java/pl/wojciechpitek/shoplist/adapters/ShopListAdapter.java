package pl.wojciechpitek.shoplist.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import pl.wojciechpitek.shoplist.R;
import pl.wojciechpitek.shoplist.dtos.ListElementDto;
import pl.wojciechpitek.shoplist.helpers.DBHelper;
import pl.wojciechpitek.shoplist.holders.BaseHolder;
import pl.wojciechpitek.shoplist.holders.RowHolder;

public class ShopListAdapter extends AbstractRecyclerViewAdapter<ListElementDto> {

    private Context context;
    private DBHelper dbHelper;

    public ShopListAdapter(RecyclerView recyclerView, View noItemsView, Context context, DBHelper dbHelper) {
        super(recyclerView, noItemsView);
        this.context = context;
        this.dbHelper = dbHelper;
        setItemsComparator(ListElementDto.sortItems);
    }

    @Override
    protected void onBindDataHolder(BaseHolder holder, int position) {
        holder.setItem(getItems().get(position), dbHelper);
    }

    @Override
    protected void loadNextPage(Integer loadedItems) {
        // Nothing ?
    }

    @Override
    protected BaseHolder onCreateBaseViewHolder(ViewGroup viewGroup) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_list_element, viewGroup, false);
        RowHolder rowHolder = new RowHolder(context, v);
        rowHolder.setOnItemClickListener(new BaseHolder.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                ListElementDto item = getItems().get(position);
                if (item.getState().equals(ListElementDto.StateTypes.DEFAULT)) {
                    dbHelper.updateState(item.getId(), ListElementDto.StateTypes.CHECKED);
                    item.setState(ListElementDto.StateTypes.CHECKED);
                } else if(item.getState().equals(ListElementDto.StateTypes.CHECKED)){
                    dbHelper.updateState(item.getId(), ListElementDto.StateTypes.DEFAULT);
                    item.setState(ListElementDto.StateTypes.DEFAULT);
                }
                myNotifyDataSetChanged();
            }
        });

        rowHolder.setOnMenuClickListener(new BaseHolder.OnMenuClickListener() {
            @Override
            public boolean onMenuClickListener(MenuItem menuItem, int position) {
                ListElementDto item = getItems().get(position);
                switch(menuItem.getItemId()) {
                    case R.id.mark_as_deleted:
                        dbHelper.updateState(item.getId(), ListElementDto.StateTypes.DELETED);
                        item.setState(ListElementDto.StateTypes.DELETED);
                        break;
                    case R.id.unmark_as_deleted:
                        dbHelper.updateState(item.getId(), ListElementDto.StateTypes.DEFAULT);
                        item.setState(ListElementDto.StateTypes.DEFAULT);
                        break;
                    case R.id.remove_item:
                        dbHelper.removeItem(item.getId());
                        removeItem(position);
                        break;
                }
                myNotifyDataSetChanged();
                return false;
            }
        });

        return rowHolder;
    }
}
