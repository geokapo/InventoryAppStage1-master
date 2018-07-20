package com.example.georgia.inventoryapp_stage_1;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.georgia.inventoryapp_stage_1.data.InventorContract.InventorEntry;
import com.example.georgia.inventoryapp_stage_1.data.InventorDbHelper;

public class EditorActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSupplierNameEditText;
    private EditText mNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.inventory_editor );

        mNameEditText = (EditText) findViewById ( R.id.product_name_edit_text );
        mPriceEditText = (EditText) findViewById ( R.id.product_price_edit_text );
        mQuantityEditText = (EditText) findViewById ( R.id.product_quantity_edit_text );
        mSupplierNameEditText = (EditText) findViewById ( R.id.product_supplier_name_edit_text );
        mNumberEditText = (EditText) findViewById ( R.id.product_supplier_phone_number_edit_text );
    }

    /**
     * Get user input from editor and save new to database.
     */
    private void insertBook() {
        // Read from input fields
        // Use trim to loading or trailing white space
        String nameString = mNameEditText.getText ().toString ().trim ();
        String priceString = mPriceEditText.getText ().toString ().trim ();
        int price = Integer.parseInt ( priceString );
        String quantityString = mQuantityEditText.getText ().toString ().trim ();
        int quantity = Integer.parseInt ( quantityString );
        String supplierNameString = mSupplierNameEditText.getText ().toString ().trim ();
        String supplierNumberString = mNumberEditText.getText ().toString ().trim ();
        int supplierNumber = Integer.parseInt ( supplierNumberString );


        InventorDbHelper mDbHelper = new InventorDbHelper ( this );

        SQLiteDatabase db = mDbHelper.getWritableDatabase ();

        ContentValues values = new ContentValues ();
        values.put ( InventorEntry.COLUMN_PRODUCT_NAME, nameString );
        values.put ( InventorEntry.COLUMN_PRODUCT_PRICE, price );
        values.put ( InventorEntry.COLUMN_PRODUCT_QUANTITY, quantity );
        values.put ( InventorEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierNameString );
        values.put ( InventorEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, supplierNumber );
        long newRowId = db.insert ( InventorEntry.TABLE_NAME, null, values );

        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText ( this, getString( R.string.error_saving), Toast.LENGTH_SHORT ).show ();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the new ID.
            Toast.makeText ( this, getString( R.string.item_saved) + newRowId, Toast.LENGTH_SHORT ).show ();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu.xml file.
        // This adds menu items to the app bar.
        getMenuInflater ().inflate ( R.menu.menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId ()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save item to database
                insertBook ();
                // Exit activity
                finish ();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (InventoryActivity)
                NavUtils.navigateUpFromSameTask ( this );
                return true;
        }
        return super.onOptionsItemSelected ( item );
    }
}
