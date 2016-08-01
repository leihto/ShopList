package pl.wojciechpitek.shoplist.holders;

import android.view.View;
import android.widget.ProgressBar;
import pl.wojciechpitek.shoplist.R;
import pl.wojciechpitek.shoplist.helpers.DBHelper;

public class LoaderHolder extends BaseHolder {

    public ProgressBar progressBar;

    public LoaderHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
    }

    @Override
    public void setItem(Object item, DBHelper dbHelper) {}
}
