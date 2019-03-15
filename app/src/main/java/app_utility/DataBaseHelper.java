package app_utility;


public class DataBaseHelper {

    //private variables
    private int _id;
    //private String _phone_number;
    private int _odoo_id;
    private int _sales_order_id;
    private int _sales_order_line_id;
    private int _product_id;
    private String _product_name;
    private int _product_quantity;
    private int _unit_price;
    private int _sub_total;


    // Empty constructor
    public DataBaseHelper() {

    }


    public DataBaseHelper(int _product_id, String _product_name, int _product_quantity, int _unit_price, int _sub_total){
        this._product_id = _product_id;
        this._product_name = _product_name;
        this._product_quantity = _product_quantity;
        this._unit_price = _unit_price;
        this._sub_total = _sub_total;
    }

    public DataBaseHelper(int _odoo_id, int _sales_order_id, int _sales_order_line_id, int _product_id,
                          String _product_name, int _product_quantity, int _unit_price, int _sub_total){
        this._odoo_id = _odoo_id;
        this._sales_order_id = _sales_order_id;
        this._sales_order_line_id = _sales_order_line_id;
        this._product_id = _product_id;
        this._product_name = _product_name;
        this._product_quantity = _product_quantity;
        this._unit_price = _unit_price;
        this._sub_total = _sub_total;
    }

    // getting ID
    public int get_id() {
        return this._id;
    }

    // setting id
    public void set_id(int id) {
        this._id = id;
    }

    public int get_odoo_id() {
        return this._odoo_id;
    }

    public void set_odoo_id(int odoo_id) {
        this._odoo_id = odoo_id;
    }

    public int get_sales_order_id() {
        return this._sales_order_id;
    }

    public void set_sales_order_id(int sales_order_id) {
        this._sales_order_id = sales_order_id;
    }

    public int get_sales_order_line_id() {
        return this._sales_order_line_id;
    }

    public void set_sales_order_line_id(int sales_order_line_id) {
        this._sales_order_line_id = sales_order_line_id;
    }

    public int get_product_id() {
        return this._product_id;
    }

    public void set_product_id(int product_id) {
        this._product_id = product_id;
    }

    public String get_product_name() {
        return this._product_name;
    }

    public void set_product_name(String product_name) {
        this._product_name = product_name;
    }

    public int get_product_quantity() {
        return this._product_quantity;
    }

    public void set_product_quantity(int product_quantity) {
        this._product_quantity = product_quantity;
    }

    public int get_unit_price() {
        return this._unit_price;
    }

    public void set_unit_price(int unit_price) {
        this._unit_price = unit_price;
    }

    public int get_sub_total() {
        return this._sub_total;
    }

    public void set_sub_total(int sub_total) {
        this._sub_total = sub_total;
    }
}
