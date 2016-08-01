package pl.wojciechpitek.shoplist.holders;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import pl.wojciechpitek.shoplist.R;
import pl.wojciechpitek.shoplist.dtos.ListElementDto;
import pl.wojciechpitek.shoplist.helpers.DBHelper;

public class RowHolder extends BaseHolder<ListElementDto> {

    private TextView content;
    private ImageView stateIcon;
    private ImageView menuToggle;
    private View itemView;
    private Context context;

    public RowHolder(Context context, View itemView) {
        super(itemView);
        content = (TextView) itemView.findViewById(R.id.name);
        stateIcon = (ImageView) itemView.findViewById(R.id.state_icon);
        menuToggle = (ImageView) itemView.findViewById(R.id.menu_toggle);
        this.itemView = itemView;
        this.context = context;
    }

    @Override
    public void setItem(final ListElementDto item, DBHelper dbHelper) {
        content.setText(item.getName());
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnItemClickListener().onItemClickListener(view, getLayoutPosition());
            }
        });

        menuToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return getOnMenuClickListener() != null && getOnMenuClickListener().onMenuClickListener(item, getLayoutPosition());
                    }
                });
                popupMenu.inflate(item.getState().equals(ListElementDto.StateTypes.DELETED) ? R.menu.item_actions_deleted : R.menu.item_actions);
                popupMenu.show();
            }
        });

        switch(item.getState()){
            case CHECKED:
                setRowStyle(R.drawable.tick_green, R.color.rowTickBg);
                break;
            case DELETED:
                setRowStyle(R.drawable.deleted_red, R.color.rowDeletedBg);
                break;
            default:
                if(item.getImportant()) {
                    setRowStyle(R.drawable.fav_blue, 0);
                } else {
                    setRowStyle(0, 0); // Default. No image & color
                }
                break;
        }
    }

    private void setRowStyle(int imageResource, int color) {
        stateIcon.setImageResource(imageResource);
        itemView.setBackgroundColor(color > 0 ? context.getResources().getColor(color) : color);
    }
}