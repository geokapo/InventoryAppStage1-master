package com.example.georgia.inventoryapp_stage_1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.georgia.inventoryapp_stage_1.data.InventorContract.InventorEntry;
import com.example.georgia.inventoryapp_stage_1.data.InventorDbHelper;

import static com.example.georgia.inventoryapp_stage_1.data.InventorContract.InventorEntry.COLUMN_PRODUCT_SUPPLIER_PHONE;


/**
 * Displays list of books that were entered and stored in the app.
 */
public class InventoryActivity extends AppCompatActivity {

    /** Database helper that will provide us access to the database */
    private InventorDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new InventorDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = new String[]{
                InventorEntry._ID,
                InventorEntry.COLUMN_PRODUCT_NAME,
                InventorEntry.COLUMN_PRODUCT_PRICE,
                InventorEntry.COLUMN_PRODUCT_QUANTITY,
                InventorEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                COLUMN_PRODUCT_SUPPLIER_PHONE};

        // Perform a query on the pets table
        Cursor cursor = db.query(
                InventorEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        TextView displayView = (TextView) findViewById(R.id.text_view_inventory);

        try {
            displayView.setText("The products tables contains " + cursor.getCount() + " products.\n\n");
            displayView.append(InventorEntry._ID + " - " +
                    InventorEntry.COLUMN_PRODUCT_NAME + " - " +
                    InventorEntry.COLUMN_PRODUCT_PRICE + " - " +
                    InventorEntry.COLUMN_PRODUCT_QUANTITY + " - " +
                    InventorEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " - " +
                    COLUMN_PRODUCT_SUPPLIER_PHONE + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex( InventorEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex( InventorEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex( InventorEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex ( InventorEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex( InventorEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex( COLUMN_PRODUCT_SUPPLIER_PHONE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                int currentSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);

                /* Display the values from each column of the current row in the cursor in the TextView */
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhone));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    /**
     * Helper method to insert hardcoded book data into the database. For debugging purposes only.
     */
    private void insertBook() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and attributes are the values.
        ContentValues values = new ContentValues();
        values.put( InventorEntry.COLUMN_PRODUCT_NAME, "Steven King");
        values.put( InventorEntry.COLUMN_PRODUCT_PRICE, "17");
        values.put( InventorEntry.COLUMN_PRODUCT_QUANTITY,"1");
        values.put( InventorEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "Amazon.com");
         values.put( COLUMN_PRODUCT_SUPPLIER_PHONE, "001-03-00000003");

        // Insert a new row for Toto in the database, returning the ID of that new row.
        // The first argument for db.insert() is the pets table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Toto.
        long newRowId = db.insert(InventorEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


}
