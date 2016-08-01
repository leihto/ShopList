package pl.wojciechpitek.shoplist.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;
import pl.wojciechpitek.shoplist.R;
import pl.wojciechpitek.shoplist.adapters.AbstractRecyclerViewAdapter;
import pl.wojciechpitek.shoplist.adapters.ShopListAdapter;
import pl.wojciechpitek.shoplist.dtos.ListElementDto;
import pl.wojciechpitek.shoplist.helpers.DBHelper;
import pl.wojciechpitek.shoplist.helpers.InputDialogHelper;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AbstractRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        dbHelper = new DBHelper(ListActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final InputDialogHelper inputDialogHelper = new InputDialogHelper(ListActivity.this, dbHelper);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            inputDialogHelper.showItemDialog(new InputDialogHelper.OnButtonClickListener() {
                @Override
                public void onButtonClickListener(String itemContent, Boolean isChecked) {
                    if(itemContent != null && itemContent.length() > 0) {
                        if (adapter != null) {
                            ListElementDto listElementDto = new ListElementDto(itemContent, ListElementDto.StateTypes.DEFAULT, isChecked);
                            dbHelper.insertRecord(listElementDto);
                            adapter.addItem(listElementDto, Boolean.FALSE);
                        }
                        inputDialogHelper.hideDialog();
                    } else {
                        Toast.makeText(ListActivity.this, R.string.need_set_item_value, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            }
        });

        RelativeLayout noItemsView = (RelativeLayout)findViewById(R.id.noItemsView);
        recyclerView = (RecyclerView) findViewById(R.id.main_list_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ShopListAdapter(recyclerView, noItemsView, ListActivity.this, dbHelper);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear_list) {
            dbHelper.clearDb();
            adapter.clearList();
            adapter.myNotifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.clearList();
        adapter.addItems(dbHelper.fetchAllRecords(), Boolean.FALSE);
    }

}
