package pl.wojciechpitek.shoplist.helpers;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.*;
import pl.wojciechpitek.shoplist.R;

public class InputDialogHelper {

    private Context context;
    private Dialog dialog;
    private DBHelper dbHelper;

    public interface OnButtonClickListener {
        void onButtonClickListener(String itemContent, Boolean isChecked);
    }

    public InputDialogHelper(Context context, DBHelper dbHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
    }

    public Dialog showItemDialog(final OnButtonClickListener onButtonClickListener) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.item_input);
        Button confirmButton = (Button) dialog.findViewById(R.id.confirmButton);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, dbHelper.getAutocompletePhrases());
        final AutoCompleteTextView itemName = (AutoCompleteTextView) dialog.findViewById(R.id.item_input);
        final CheckBox markAsImportant = (CheckBox) dialog.findViewById(R.id.markAsImportant);
        itemName.setAdapter(adapter);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClickListener.onButtonClickListener(itemName.getText().toString(), markAsImportant.isChecked());
            }
        });
        dialog.show();
        return dialog;
    }

    public void hideDialog() {
        if(dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
