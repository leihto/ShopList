package pl.wojciechpitek.shoplist.holders;

import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import pl.wojciechpitek.shoplist.helpers.DBHelper;

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    private OnItemClickListener onItemClickListener;
    private OnMenuClickListener onMenuClickListener;

    /**
     * Method used for set item to holder
     * @param item item
     * @param dbHelper {@link DBHelper} DBHelper - Only for that app
     */
    public abstract void setItem(T item, DBHelper dbHelper);

    /**
     * Interface of OnItemClickListener
     */
    public interface OnItemClickListener {
        void onItemClickListener(View v, int position);
    }

    /**
     * Interface of OnMenuClickListener
     * That interface was only for that app
     */
    public interface OnMenuClickListener {
        boolean onMenuClickListener(MenuItem menuItem, int position);
    }

    /**
     * Constructor of BaseHolder
     *
     * @param itemView item view
     */
    BaseHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    /**
     * Method used to set onItemClickListener
     *
     * @param onItemClickListener {@link OnItemClickListener} implementation
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Method used to set OnMenuClickListener
     * @param onMenuClickListener {@link OnMenuClickListener} implementation
     */
    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    /**
     * Getter for onItemClickListener
     *
     * @return {@link OnItemClickListener} OnItemClickListener
     */
    OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    /**
     * Getter for OnMenuClickListener
     *
     * @return {@link OnMenuClickListener} OnMenuClickListener
     */
    OnMenuClickListener getOnMenuClickListener() {
        return onMenuClickListener;
    }

    @Override
    public void onClick(View v) {
        // Method disabled for that app
        /*if(onItemClickListener != null) {
            onItemClickListener.onItemClickListener(v.getRootView(), getLayoutPosition());
        }*/
    }
}
