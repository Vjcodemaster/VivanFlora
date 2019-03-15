package app_utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static android.app.DownloadManager.COLUMN_ID;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "GOLECHA_DB";

    private static final String TABLE_PRODUCTS = "TABLE_PRODUCTS";

    private static final String TABLE_TEMP_PRODUCTS = "TABLE_TEMP_PRODUCTS";

    private static final String KEY_ID = "_id";

    private static final String KEY_ODOO_ID = "KEY_ODOO_ID";

    private static final String KEY_SALES_ORDER_ID = "KEY_SALES_ORDER_ID";

    private static final String KEY_SALES_ORDER_LINE_ID = "KEY_SALES_ORDER_LINE_ID";

    private static final String KEY_PRODUCT_ID = "KEY_PRODUCT_ID";

    private static final String KEY_PRODUCT_NAME = "KEY_PRODUCT_NAME";

    private static final String KEY_QUANTITY = "KEY_QUANTITY";

    private static final String KEY_UNIT_PRICE = "KEY_UNIT_PRICE";

    private static final String KEY_SUB_TOTAL = "KEY_SUB_TOTAL";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    /*
    leaving gap between "CREATE TABLE" & TABLE_RECENT gives error watch out!
    Follow the below format
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_INDIVIDUAL_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_ODOO_ID + " INTEGER, "
                + KEY_SALES_ORDER_ID + " INTEGER, "
                + KEY_SALES_ORDER_LINE_ID + " INTEGER, "
                + KEY_PRODUCT_ID + " INTEGER, "
                + KEY_PRODUCT_NAME + " TEXT, "
                + KEY_QUANTITY + " INTEGER, "
                + KEY_UNIT_PRICE + " INTEGER, "
                + KEY_SUB_TOTAL + " INTEGER)";

        String CREATE_TEMP_PRODUCTS = "CREATE TABLE " + TABLE_TEMP_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_PRODUCT_ID + " INTEGER, "
                + KEY_PRODUCT_NAME + " TEXT, "
                + KEY_QUANTITY + " INTEGER, "
                + KEY_UNIT_PRICE + " INTEGER, "
                + KEY_SUB_TOTAL + " INTEGER)";
        //+ KEY_INDIVIDUAL_PRODUCT_VARIANT_NAMES + " TEXT, "
        //+ KEY_INDIVIDUAL_PRODUCT_VARIANT_IMAGES + " TEXT)";

        /*String CREATE_TABLE_TEMPORARY = "CREATE TABLE " + TABLE_TEMPORARY + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_STALL_ID + " TEXT, "
                + KEY_EMP_ID + " TEXT, "
                + KEY_SCAN_ID + " TEXT, "
                + KEY_AMOUNT + " TEXT, "
                + KEY_TIME + " TEXT)";*/

        //db.execSQL(CREATE_TABLE_EMPLOYEE);
        //db.execSQL(CREATE_TABLE_TEMPORARY);
        db.execSQL(CREATE_TEMP_PRODUCTS);
        db.execSQL(CREATE_INDIVIDUAL_PRODUCTS);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

        // Create tables again
        onCreate(db);
    }

    // Adding new data
    public void addDataToProductsTable(DataBaseHelper dataBaseHelper) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, dataBaseHelper.get_id());
        values.put(KEY_ODOO_ID, dataBaseHelper.get_odoo_id());
        values.put(KEY_SALES_ORDER_ID, dataBaseHelper.get_sales_order_id());
        values.put(KEY_SALES_ORDER_LINE_ID, dataBaseHelper.get_sales_order_line_id());
        values.put(KEY_PRODUCT_ID, dataBaseHelper.get_product_id());
        values.put(KEY_PRODUCT_NAME, dataBaseHelper.get_product_name());
        values.put(KEY_QUANTITY, dataBaseHelper.get_product_quantity());
        values.put(KEY_UNIT_PRICE, dataBaseHelper.get_unit_price());
        values.put(KEY_SUB_TOTAL, dataBaseHelper.get_sub_total());

        db.insert(TABLE_PRODUCTS, null, values);
        //db.insert(TABLE_PERMANENT, null, values);

        db.close(); // Closing database connection
    }

    public void addDataToTempTable(DataBaseHelper dataBaseHelper) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, dataBaseHelper.get_id());
        values.put(KEY_PRODUCT_ID, dataBaseHelper.get_product_id());
        values.put(KEY_PRODUCT_NAME, dataBaseHelper.get_product_name());
        values.put(KEY_QUANTITY, dataBaseHelper.get_product_quantity());
        values.put(KEY_UNIT_PRICE, dataBaseHelper.get_unit_price());
        values.put(KEY_SUB_TOTAL, dataBaseHelper.get_sub_total());

        // Inserting Row
        //db.insert(TABLE_RECENT, null, values);
        db.insert(TABLE_TEMP_PRODUCTS, null, values);

        db.close(); // Closing database connection
    }

    public int lastID() {
        int res;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, new String[]{COLUMN_ID,
        }, null, null, null, null, null);
        cursor.moveToLast();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            res = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        } else {
            res = -1;
        }
        cursor.close();
        return res;
    }

    public int lastIDOfMainProducts() {
        int res;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, new String[]{COLUMN_ID,
        }, null, null, null, null, null);
        cursor.moveToLast();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            res = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        } else {
            res = -1;
        }
        cursor.close();
        return res;
    }

    /*public List<DataBaseHelper> getAllMainProducts() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                //dataBaseHelper.set_main_product_id(cursor.getInt(1));
                dataBaseHelper.set_main_product_names(cursor.getString(1));
                dataBaseHelper.set_main_product_description(cursor.getString(2));
                dataBaseHelper.set_product_category_names(cursor.getString(3));
                //dataBaseHelper.set_individual_product_description(cursor.getString(5));
                //dataBaseHelper.set_individual_product_images_path(cursor.getString(6));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }*/

    /*public int getIdForStringTablePermanent(String str) {
        int res;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, new String[]{COLUMN_ID,
                }, KEY_MAIN_PRODUCT_NAMES + "=?",
                new String[]{str}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            res = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        } else {
            res = -1;
        }
        if (cursor != null) {
            cursor.close();
        }
        return res;
    }*/

    /*public int getIdForStringTableTemporary(String str) {
        int res;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{COLUMN_ID,
                }, KEY_SUB_PRODUCT_ID + "=?",
                new String[]{str}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            res = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        } else {
            res = -1;
        }
        if (cursor != null) {
            cursor.close();
        }
        return res;
    }*/

    /*public List<DataBaseHelper> getRowData(String sSubName) {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MAIN_PRODUCTS + " WHERE " + " " + KEY_MAIN_PRODUCT_NAMES + " = " + sSubName;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            //do {
            DataBaseHelper dataBaseHelper = new DataBaseHelper();

            dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
            dataBaseHelper.set_product_category_names(cursor.getString(3));
            // Adding data to list
            dataBaseHelperList.add(dataBaseHelper);
            //} while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }*/

    /*public String getDescriptionFromProductName(String sProduct) {
        Cursor cursor;
        String sName;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{KEY_INDIVIDUAL_PRODUCT_DESCRIPTION,
                }, KEY_INDIVIDUAL_PRODUCT_NAMES + "=?",
                new String[]{sProduct}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_INDIVIDUAL_PRODUCT_DESCRIPTION));
        } else {
            sName = "";
        }
        *//*if(sName==null){
            return "";
        }*//*
        cursor.close();
        return sName;
    }*/

    /*public String getProductTechSpecHeading(String sProductName) {
        Cursor cursor;
        String sName;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_HEADER,
                }, KEY_INDIVIDUAL_PRODUCT_NAMES + "=?",
                new String[]{sProductName}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_HEADER));
        } else {
            sName = "";
        }
        *//*if(sName==null){
            return "";
        }*//*
        cursor.close();
        return sName;
    }*/

    /*public String getProductTechSpecValue(String sProductName) {
        Cursor cursor;
        String sName;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_VALUE,
                }, KEY_INDIVIDUAL_PRODUCT_NAMES + "=?",
                new String[]{sProductName}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_VALUE));
        } else {
            sName = "";
        }
        *//*if(sName==null){
            return "";
        }*//*
        cursor.close();
        return sName;
    }*/

    /*public String getImagePathFromProducts(String sProduct) {
        Cursor cursor = null;
        String sName = "";
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{KEY_INDIVIDUAL_PRODUCT_IMAGES_PATH,
                }, KEY_INDIVIDUAL_PRODUCT_NAMES + "=?",
                new String[]{sProduct}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_INDIVIDUAL_PRODUCT_IMAGES_PATH));
        } else {
            sName = "";
        }
        *//*if(sName==null){
            return "";
        }*//*
        cursor.close();
        return sName;
    }*/

    /*public String getImagePathFromProducts(int sID) {
        Cursor cursor = null;
        String sName = "";
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{KEY_INDIVIDUAL_PRODUCT_IMAGES_PATH,
                }, KEY_ID + "=?",
                new String[]{String.valueOf(sID)}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_INDIVIDUAL_PRODUCT_IMAGES_PATH));
        } else {
            sName = "";
        }
        *//*if(sName==null){
            return "";
        }*//*
        cursor.close();
        return sName;
    }*/

    /*public int getRecordsCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);


        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }*/

    /*public List<String> getProductNamesOnly() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        ArrayList<String> alProductNames = new ArrayList<>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        String selectQuery = "SELECT " + KEY_INDIVIDUAL_PRODUCT_NAMES + " FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                //dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                *//*dataBaseHelper.set_main_product_id(cursor.getInt(1));
                dataBaseHelper.set_main_product_names(cursor.getString(2));*//*
                dataBaseHelper.set_individual_product_names(cursor.getString(0));
                *//*dataBaseHelper.set_individual_product_names(cursor.getString(4));
                dataBaseHelper.set_individual_product_description(cursor.getString(5));
                dataBaseHelper.set_individual_product_address(cursor.getString(6));
                dataBaseHelper.set_individual_product_images_path(cursor.getString(7));*//*
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
                String s = String.valueOf(dataBaseHelperList.get(cursor.getPosition()).get_individual_product_names());
                alProductNames.add(s);
            } while (cursor.moveToNext());
        }

        // return recent list
        return alProductNames;
    }*/

    /*public List<Integer> getProductDBIDOnly() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        ArrayList<Integer> alProductDBID = new ArrayList<>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        String selectQuery = "SELECT " + KEY_ID + "," + KEY_INDIVIDUAL_PRODUCT_NAMES + " FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                *//*dataBaseHelper.set_main_product_id(cursor.getInt(1));
                dataBaseHelper.set_main_product_names(cursor.getString(2));*//*
                //dataBaseHelper.set_individual_product_names(cursor.getString(3));
                *//*dataBaseHelper.set_individual_product_names(cursor.getString(4));
                dataBaseHelper.set_individual_product_description(cursor.getString(5));
                dataBaseHelper.set_individual_product_address(cursor.getString(6));
                dataBaseHelper.set_individual_product_images_path(cursor.getString(7));*//*
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
                //String s = String.valueOf(dataBaseHelperList.get(cursor.getPosition()).get_id());
                int id = dataBaseHelperList.get(cursor.getPosition()).get_id();
                alProductDBID.add(id);
            } while (cursor.moveToNext());
        }

        // return recent list
        return alProductDBID;
    }*/

    /*public List<String> getTechSpecsHeading(String sKey) {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        ArrayList<String> alTechSpecs = new ArrayList<>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        String selectQuery = "SELECT " + KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_HEADER + " FROM " + TABLE_INDIVIDUAL_PRODUCTS + " WHERE "
                + KEY_INDIVIDUAL_PRODUCT_NAMES + "=" + sKey;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                //dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                *//*dataBaseHelper.set_main_product_id(cursor.getInt(1));
                dataBaseHelper.set_main_product_names(cursor.getString(2));*//*
                dataBaseHelper.set_individual_product_tech_specs_header(cursor.getString(0));
                *//*dataBaseHelper.set_individual_product_names(cursor.getString(4));
                dataBaseHelper.set_individual_product_description(cursor.getString(5));
                dataBaseHelper.set_individual_product_address(cursor.getString(6));
                dataBaseHelper.set_individual_product_images_path(cursor.getString(7));*//*
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
                String s = String.valueOf(dataBaseHelperList.get(cursor.getPosition()).get_individual_product_names());
                alTechSpecs.add(s);
            } while (cursor.moveToNext());
        }

        // return recent list
        return alTechSpecs;
    }*/

    /*public List<String> getTechSpecsValue() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        ArrayList<String> alTechSpecs = new ArrayList<>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        String selectQuery = "SELECT " + KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_VALUE + " FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                //dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                *//*dataBaseHelper.set_main_product_id(cursor.getInt(1));
                dataBaseHelper.set_main_product_names(cursor.getString(2));*//*
                dataBaseHelper.set_individual_product_tech_specs_value(cursor.getString(0));
                *//*dataBaseHelper.set_individual_product_names(cursor.getString(4));
                dataBaseHelper.set_individual_product_description(cursor.getString(5));
                dataBaseHelper.set_individual_product_address(cursor.getString(6));
                dataBaseHelper.set_individual_product_images_path(cursor.getString(7));*//*
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
                String s = String.valueOf(dataBaseHelperList.get(cursor.getPosition()).get_individual_product_names());
                alTechSpecs.add(s);
            } while (cursor.moveToNext());
        }

        // return recent list
        return alTechSpecs;
    }*/

    /*public List<DataBaseHelper> getAllProductsData1() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                dataBaseHelper.set_main_product_id(cursor.getInt(1));
                dataBaseHelper.set_odoo_product_id(cursor.getInt(2));
                dataBaseHelper.set_main_product_names(cursor.getString(3));
                dataBaseHelper.set_product_category_names(cursor.getString(4));
                dataBaseHelper.set_individual_product_names(cursor.getString(5));
                dataBaseHelper.set_individual_product_description(cursor.getString(6));
                dataBaseHelper.set_individual_product_tech_specs_header(cursor.getString(7));
                dataBaseHelper.set_individual_product_tech_specs_value(cursor.getString(8));
                dataBaseHelper.set_individual_product_address(cursor.getString(9));
                dataBaseHelper.set_individual_product_images_path(cursor.getString(10));
                dataBaseHelper.set_individual_product_tags(cursor.getString(11));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }*/


    /*public List<DataBaseHelper> getDataForSearch() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        String selectQuery = "SELECT " + KEY_ID + "," + KEY_SUB_PRODUCT_CATEGORY_NAMES + "," + KEY_INDIVIDUAL_PRODUCT_NAMES + "," + KEY_INDIVIDUAL_PRODUCT_DESCRIPTION + " FROM " + TABLE_INDIVIDUAL_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                dataBaseHelper.set_product_category_names(cursor.getString(1));
                dataBaseHelper.set_individual_product_names(cursor.getString(2));
                dataBaseHelper.set_individual_product_description(cursor.getString(3));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }*/

    /*if(sName==null){
    return "";
}*//*
            cursor.close();
        return sName;
}


    /*
    this will return only the product data of specific KEY_MAIN_PRODUCT_ID
    so we have to pass the id of main product id to get back only those products under main product
     */
    /*public List<DataBaseHelper> getAllProductsData() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS + " " + "WHERE " + KEY_MAIN_PRODUCT_ID + "=" + 1;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                dataBaseHelper.set_main_product_id(cursor.getInt(1));
                dataBaseHelper.set_main_product_names(cursor.getString(2));
                dataBaseHelper.set_product_category_names(cursor.getString(3));
                dataBaseHelper.set_individual_product_names(cursor.getString(4));
                dataBaseHelper.set_individual_product_description(cursor.getString(5));
                dataBaseHelper.set_individual_product_tech_specs_header(cursor.getString(6));
                dataBaseHelper.set_individual_product_tech_specs_value(cursor.getString(7));
                dataBaseHelper.set_individual_product_address(cursor.getString(8));
                dataBaseHelper.set_individual_product_images_path(cursor.getString(9));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }*/

    /*public String getProductTechSpecHeading(int sID) {
        Cursor cursor;
        String sName;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_HEADER,
                }, KEY_ID + "=?",
                new String[]{String.valueOf(sID)}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_HEADER));
        } else {
            sName = "";
        }
        *//*if(sName==null){
            return "";
        }*//*
        cursor.close();
        return sName;
    }*/

    /*public String getProductTechSpecValue(int sID) {
        Cursor cursor;
        String sName;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_VALUE,
                }, KEY_ID + "=?",
                new String[]{String.valueOf(sID)}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_VALUE));
        } else {
            sName = "";
        }
        *//*if(sName==null){
            return "";
        }*//*
        cursor.close();
        return sName;
    }*/

    /*public int updateImagePathIndividualProducts(DataBaseHelper dataBaseHelper, int KEY_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String column = "last_seen";
        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, dataBaseHelper.getName());
        //values.put(KEY_NUMBER, dataBaseHelper.getPhoneNumber());
        values.put(KEY_INDIVIDUAL_PRODUCT_IMAGES_PATH, dataBaseHelper.get_individual_product_images_path());

        // updating row
        //return db.update(TABLE_RECENT, values, column + "last_seen", new String[] {String.valueOf(KEY_ID)});
        return db.update(TABLE_INDIVIDUAL_PRODUCTS, values, "_id" + " = " + KEY_ID, null);
        //*//**//*ContentValues data=new ContentValues();
        //data.put("Field1","bob");
        //DB.update(Tablename, data, "_id=" + id, null);*//**//*
    }*/

    /*public int update(DataBaseHelper dataBaseHelper, int KEY_ODOO_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String column = "last_seen";
        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, dataBaseHelper.getName());
        //values.put(KEY_NUMBER, dataBaseHelper.getPhoneNumber());
        values.put(KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_HEADER, dataBaseHelper.get_individual_product_tech_specs_header());
        values.put(KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_VALUE, dataBaseHelper.get_individual_product_tech_specs_value());

        // updating row
        //return db.update(TABLE_RECENT, values, column + "last_seen", new String[] {String.valueOf(KEY_ID)});
        return db.update(TABLE_INDIVIDUAL_PRODUCTS, values, "KEY_ODOO_PRODUCT_ID" + " = " + KEY_ODOO_ID, null);
        //*//**//*ContentValues data=new ContentValues();
        //data.put("Field1","bob");
        //DB.update(Tablename, data, "_id=" + id, null);*//**//*
    }*/

    /*public List<Integer> getProductsOdooID() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        ArrayList<Integer> alODooID = new ArrayList<>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        String selectQuery = "SELECT " + KEY_ID + "," + KEY_ODOO_PRODUCT_ID + "," + KEY_INDIVIDUAL_PRODUCT_ADDRESS + " FROM " + TABLE_INDIVIDUAL_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                dataBaseHelper.set_odoo_product_id(cursor.getInt(1));
                dataBaseHelper.set_individual_product_address(cursor.getString(2));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
                //String s = String.valueOf(dataBaseHelperList.get(cursor.getPosition()).get_individual_product_names());
                int id = dataBaseHelperList.get(cursor.getPosition()).get_odoo_product_id();
                alODooID.add(id);
            } while (cursor.moveToNext());
        }
        // return recent list
        return alODooID;
    }*/

}
