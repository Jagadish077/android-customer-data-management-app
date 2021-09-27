package com.example.anmolpipes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


import com.example.anmolpipes.datahelperclasses.Customer;
import com.example.anmolpipes.datahelperclasses.DataFields;
import com.example.anmolpipes.datahelperclasses.UpdatedHistory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqlOperation extends SQLiteOpenHelper {

    private static final String CUSTOMERDATA = "CREATE TABLE IF NOT EXISTS CUSTOMER(Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Customer_name TEXT(30) NOT NULL, email TEXT(50), phone TEXT(10), customerStore TEXT(20) NOT NULL, createdAt DATE DEFAULT(datetime('now', 'localtime')) NOT NULL, TotalAmount INTEGER(100) NOT NULL)";
    private static final String PRODUCT = "CREATE TABLE IF NOT EXISTS Product(Id INTEGER NOT NULL, productId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, totalAmount INTEGER(20) NOT NULL, delivered TEXT(10) NOT NULL, productCreatedAt DATE DEFAULT(datetime('now', 'localtime')) NOT NULL)";
    private static final String PRODUCTS = "CREATE TABLE IF NOT EXISTS AllProducts(Id INTEGER NOT NULL, productsId INTEGER NOT NULL, productId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, productName TEXT(20) NOT NULL, size TEXT(20) NOT NULL, quantity INTEGER(10) NOT NULL, rate INTEGER(10) NOT NULL, Total_amount INTEGER(40) NOT NULL, FOREIGN KEY (Id) REFERENCES CUSTOMER(Id), FOREIGN KEY (productsId) REFERENCES PRODUCT(productId))";
    private static final String UPDATE_RECORDS = "CREATE TABLE IF NOT EXISTS UPDATED_RECORDS(updatedId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Id INTEGER NOT NULL, updatedAt DATE DEFAULT(datetime('now', 'localtime')) NOT NULL, previousAmount INTEGER(20) NOT NULL, updatedAmount INTEGER(20) NOT NULL, amountType TEXT(10) NOT NULL, totalAmount INTEGER(100) NOT NULL, FOREIGN KEY (Id) REFERENCES CUSTOMER(Id))";
    private static final String DatabaseName = "CUSTOMER.db";
    private static final String CUSTOMER_DB = "CUSTOMER";
    private static final String tableName = "CUSTOMER";
    private static final String PRODUCT_TABLE = "Product";
    private static final String PRODUCTS_TABLE = "AllProducts";
    private static final String UPDATED_RECORDS = "UPDATED_RECORDS";
    private List<Customer> customers;
    private List<List<DataFields>> allDatas;
    private Context context;
    public SqlOperation(Context context) {
        super(context, DatabaseName, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CUSTOMERDATA);
        db.execSQL(PRODUCT);
        db.execSQL(PRODUCTS);
        db.execSQL(UPDATE_RECORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public List<Customer> selectAllCustomer(){
        customers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM CUSTOMER", null);
        if(c != null && c.moveToFirst()){
            while (!c.isAfterLast()){
                customers.add(new Customer(Integer.parseInt(c.getString(c.getColumnIndex("Id"))), c.getString(c.getColumnIndex("Customer_name")), c.getString(c.getColumnIndex("email")), c.getString(c.getColumnIndex("phone")),c.getString(c.getColumnIndex("customerStore")), c.getString(c.getColumnIndex("createdAt")), c.getDouble(c.getColumnIndex("TotalAmount"))));
                c.moveToNext();
            }
            c.close();
        }

        db.close();
        return customers;
    }
    public Customer selectCustomerById(int id){
        Customer customer = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cust = db.rawQuery("SELECT * FROM CUSTOMER WHERE Id = "+id+"", null);
        try {
            if(cust != null && cust.moveToFirst()){
                Log.d("Iddddddd", ""+id);
                Log.d("customerName", cust.getString(cust.getColumnIndex("Customer_name")));
                return new Customer(cust.getInt(cust.getColumnIndex("Id")), cust.getString(cust.getColumnIndex("Customer_name")),
                        cust.getString(cust.getColumnIndex("email")), cust.getString(cust.getColumnIndex("phone")), cust.getString(cust.getColumnIndex("customerStore"))
                , cust.getString(cust.getColumnIndex("createdAt")), cust.getDouble(cust.getColumnIndex("TotalAmount")));
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            db.close();
            cust.close();
        }
        return customer;
    }

    public boolean insertCustomer(Customer customer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Customer_name", customer.getName());
        cv.put("email", customer.getEmail());
        cv.put("phone", customer.getPhone());
        cv.put("customerStore", customer.getStoreName());
        cv.put("TotalAmount", 0);
        long insert = db.insert(tableName, null, cv);
        if(insert==-1){
            Toast.makeText(context, "Error While inserting", Toast.LENGTH_SHORT).show();
        }else{
            return true;
        }
        return true;
    }

    public boolean updateCustomer(Customer updateValue) {
        try (SQLiteDatabase db = this.getWritableDatabase()){
            ContentValues cv = new ContentValues();
            cv.put("Customer_name", updateValue.getName());
            cv.put("email", updateValue.getEmail());
            cv.put("phone", updateValue.getPhone());
            cv.put("customerStore", updateValue.getStoreName());
            cv.put("createdAt", updateValue.getDate());
            cv.put("TotalAmount", updateValue.getTotalAmount());
            int update = db.update(CUSTOMER_DB, cv, "Id = ?", new String[]{Integer.toString(updateValue.getId())});
            if (update == -1) {
                Toast.makeText(context, "Error While Updating...!", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCustomer(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(tableName, "Id=?", new String[]{Integer.toString(id)});
        if(delete == -1){
            Toast.makeText(context, "Error While deleting customer", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Customer Deleted Successfully", Toast.LENGTH_SHORT).show();
            return true;
        }
        return true;
    }

    public List<List<DataFields>> selectAllProducts(int id){
        Log.d("invoked", "method invoked");
        allDatas = new ArrayList<List<DataFields>>();
        allDatas.clear();
        List<DataFields> singleData;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cProduct = db.rawQuery("SELECT Product.productId, Product.Id, Product.delivered, Product.totalAmount FROM Product INNER JOIN CUSTOMER ON CUSTOMER.Id=Product.Id WHERE CUSTOMER.Id = "+id+"", null);
        if(cProduct != null && cProduct.moveToFirst()){
            while (!cProduct.isAfterLast()){
                singleData = new ArrayList<>();
                singleData.clear();
                int productId =  cProduct.getInt(cProduct.getColumnIndex("productId"));
                Cursor cProducts = db.rawQuery("SELECT AllProducts.Id, AllProducts.productId, AllProducts.productName, AllProducts.size, AllProducts.productsId, AllProducts.quantity, AllProducts.rate, AllProducts.Total_amount from AllProducts INNER JOIN Product ON Product.productId = AllProducts.productsId WHERE AllProducts.productsId="+productId+"", null);
                if(cProducts != null && cProducts.moveToFirst()){
                        while (!cProducts.isAfterLast()){
                            singleData.add(new DataFields(cProducts.getInt(cProducts.getColumnIndex("Id")), cProducts.getInt(cProducts.getColumnIndex("productId")), cProducts.getInt(cProducts.getColumnIndex("productsId")), cProducts.getString(cProducts.getColumnIndex("productName")), cProducts.getString(cProducts.getColumnIndex("size")), cProducts.getDouble(cProducts.getColumnIndex("quantity")), cProducts.getDouble(cProducts.getColumnIndex("rate")), cProducts.getDouble(cProducts.getColumnIndex("Total_amount")), cProduct.getString(cProduct.getColumnIndex("delivered")), cProduct.getDouble(cProduct.getColumnIndex("totalAmount"))));
                            int dbid = cProducts.getInt(cProducts.getColumnIndex("Id"));
                            cProducts.moveToNext();
                        }
                    cProducts.close();
                }
                allDatas.add(singleData);
                cProduct.moveToNext();
            }
            cProduct.close();
        }
        db.close();
        return allDatas;
    }

    public int insertProducts(int customerId, double totalAmount){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put("Id", customerId);
            cv.put("totalAmount", totalAmount);
            cv.put("delivered", "NO");
            long insertProduct = db.insert(PRODUCT_TABLE, null, cv);
            if(insertProduct == -1){
                Toast.makeText(context, "error inserting product..!", Toast.LENGTH_LONG).show();
                return 0;
            }else{
                Cursor curProd = db.rawQuery("SELECT * FROM "+PRODUCT_TABLE+"", null);
                if(curProd != null && curProd.moveToLast()){
                    int prodId = curProd.getInt(curProd.getColumnIndex("productId"));
                    return prodId;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            db.close();
        }
        return 0;
    }

    public double[] insertAllProducts(List<DataFields> data){
        double err = 0.0;
        double total_amount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < data.size(); i++){
                int counter = 0;
                counter++;
                ContentValues cv = new ContentValues();
                cv.put("Id", data.get(i).getCustomerId());
                cv.put("productsId", data.get(i).getProductId());
                cv.put("productName", data.get(i).getProductName());
                cv.put("size", data.get(i).getSize());
                cv.put("quantity", data.get(i).getQuantity());
                cv.put("rate", data.get(i).getRate());
                cv.put("Total_amount", data.get(i).getTotalAmountOfAllProduct());
                total_amount = total_amount+data.get(i).getTotalAmountOfAllProduct();
                long productsInsert = db.insert(PRODUCTS_TABLE, null, cv);
                if (productsInsert == -1){
                    Toast.makeText(context, "Error while inserting Products", Toast.LENGTH_LONG).show();
                    err = 1;
                    break;
                }else{
                    Toast.makeText(context, "Successfully"+counter, Toast.LENGTH_LONG).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return new double[]{err, total_amount};
    }

    public boolean deleteAllProducts(int productId){
        SQLiteDatabase db = this.getWritableDatabase();
            try {
                int deleteProduct = db.delete(PRODUCT_TABLE, "productId=?", new String[]{Integer.toString(productId)});
                Log.d("delete_level1", ""+deleteProduct);
                if (deleteProduct == -1){
                    Toast.makeText(context, "Error While Deleting Product", Toast.LENGTH_SHORT).show();
                }else {
                    int delete = db.delete(PRODUCTS_TABLE, "productsId=?", new String[]{Integer.toString(productId)});
                    Log.d("delete_level2", ""+delete);
                    if (delete == -1){
                        Toast.makeText(context, "Error While Deleting..!", Toast.LENGTH_SHORT).show();
                        return false;
                    }else {
                        Toast.makeText(context, "Deleted All Products Successfully..!", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                db.close();
            }
        return false;
    }

    public boolean updateTotalAmount(int productId, double totalAmount){
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("totalAmount", totalAmount);
            int update = db.update(PRODUCT_TABLE, cv, "productId = ?", new String[]{Integer.toString(productId)});
            if (update == -1) {
                Toast.makeText(context, "Error updating amount", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                    return true;
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getSingleProductAmount(int productId) {
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT totalAmount FROM Product WHERE productId = '" + productId + "'", null);
            if (cursor!=null && cursor.moveToFirst()) {
                double amount = cursor.getDouble(cursor.getColumnIndex("totalAmount"));
                return amount;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return -0.1;
    }

    public void incrementSingleProductTotalAmount(int productId, double totalAmount) {
        double previousAmount = getSingleProductAmount(productId);
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put("totalAmount", totalAmount+previousAmount);
            int update = db.update(PRODUCT_TABLE, cv, "productId = ?", new String[]{Integer.toString(productId)});
            if (update == -1) {
                Toast.makeText(context, "Failed To Update Total Amount", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Updated Total Amount Of Single Product..!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void updateCustomerAmount(int Id, double amount) {
        double previousAmount = getSingleCustomerAmount(Id);
        SQLiteDatabase db2 = this.getWritableDatabase();
        try {
            ContentValues cv2 = new ContentValues();
            cv2.put("TotalAmount", previousAmount+amount);
            int custAmount = db2.update(CUSTOMER_DB, cv2, "Id = ?", new String[] {Integer.toString(Id)});
            if (custAmount == -1) {
                Log.d("UpdateCustomerAmount", ""+custAmount);
                Toast.makeText(context, "error updating customer amount", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Customer Amount Updated", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db2.close();
        }
    }

    private int getCustomerId(int productId) {
        try (SQLiteDatabase db = this.getReadableDatabase()){
            Cursor customer = db.rawQuery("SELECT * FROM "+PRODUCT_TABLE+" WHERE productId = "+productId+"",null);
            if (customer != null && customer.moveToFirst()) {
                return customer.getInt(customer.getColumnIndex("Id"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public double getTotalAmountOfAllProducts(int Id) {
        try (SQLiteDatabase db = this.getWritableDatabase()){
            double total_amount = 0;
            Cursor c = db.rawQuery("SELECT Product.productId, Product.Id, Product.totalAmount FROM Product INNER JOIN CUSTOMER ON CUSTOMER.Id=Product.Id WHERE CUSTOMER.Id = "+Id+"", null);
            if (c!=null && c.moveToFirst()){
                while (!c.isAfterLast()){
                    Log.d("totalAmount", ""+c.getInt(c.getColumnIndex("totalAmount")));
                    total_amount +=c.getInt(c.getColumnIndex("totalAmount"));
                    c.moveToNext();
                }
                c.close();
                return total_amount;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    // recieves List<DataFields> as paramenter and updates all data and returns 1 or -1 based on result
    //TODO: Complete all process
    public boolean updateProductsData(List<DataFields> datas) {
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase dbRead = this.getReadableDatabase();
        boolean err = false;
        double total_amount_of_newlyAddedData = 0.0;
        try {
            for (int i = 0; i < datas.size(); i++) {
                ContentValues cv = new ContentValues();
                cv.put("productName", datas.get(i).getProductName());
                cv.put("size", datas.get(i).getSize());
                cv.put("quantity", datas.get(i).getQuantity());
                cv.put("rate", datas.get(i).getRate());
                cv.put("Total_amount", datas.get(i).getTotalAmountOfAllProduct());
                Cursor cursor = dbRead.rawQuery("SELECT * FROM " + PRODUCTS_TABLE + " WHERE productId = " + datas.get(i).getProductId() + "", null);
                if (cursor != null && cursor.moveToFirst() && datas.get(i).getProductId() != -1) {
                    Log.d("Entered the if condition", ""+datas.get(i).getProductsId());
                    if (cursor.getInt(cursor.getColumnIndex("productId")) == datas.get(i).getProductId()) {
                        int update = db.update(PRODUCTS_TABLE, cv, "productId=?", new String[]{Integer.toString(datas.get(i).getProductId())});
                        if (update == -1) {
                            Toast.makeText(context, "Failed to update..!", Toast.LENGTH_SHORT).show();
                            err = true;
                            break;
                        }
                    }
                    cursor.close();
                } else {
                    Log.d("Enter else part", "Yes");
                    if (datas.get(i).getProductId() == -1) {
                        Log.d("ProductsId", ""+datas.get(i).getProductId());
                        ContentValues cv2 = new ContentValues();
                        cv2.put("Id", datas.get(i).getCustomerId());
                        cv2.put("productsId", datas.get(i).getProductsId());
                        cv2.put("productName", datas.get(i).getProductName());
                        cv2.put("size", datas.get(i).getSize());
                        cv2.put("quantity", datas.get(i).getQuantity());
                        cv2.put("rate", datas.get(i).getRate());
                        cv2.put("Total_amount", datas.get(i).getTotalAmountOfAllProduct());
                        total_amount_of_newlyAddedData+=datas.get(i).getTotalAmountOfAllProduct();
                        long insert = db.insert(PRODUCTS_TABLE, null, cv2);
                        if (insert == -1) {
                            err = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        if (total_amount_of_newlyAddedData!=0.0) {
            updateCustomerAmount(datas.get(0).getCustomerId(), total_amount_of_newlyAddedData);
            incrementSingleProductTotalAmount(datas.get(0).getProductId(), total_amount_of_newlyAddedData);
        }
        return err;
    }
    public boolean enableDelivery(int productId) {
        try (SQLiteDatabase db = this.getWritableDatabase()){
            ContentValues cv = new ContentValues();
            cv.put("delivered", "Yes");
            int update = db.update(PRODUCT_TABLE, cv, "productId = ?", new String[]{Integer.toString(productId)});
            if (update == -1){
                Toast.makeText(context, "Erro Updating Delivery..!", Toast.LENGTH_SHORT).show();
                return false;
            }else {
                Toast.makeText(context, "Updated Delivery..!", Toast.LENGTH_SHORT).show();
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getSingleCustomerAmount(int Id) {
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor c = db.rawQuery("SELECT * FROM "+CUSTOMER_DB+" WHERE Id = "+Id+"", null);
            if (c!=null && c.moveToFirst()) {
                return c.getInt(c.getColumnIndex("TotalAmount"));
            } else {
                return 0;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean addCustomerAmount(int Id, double additionalAmount) {
        double previous_amount = getSingleCustomerAmount(Id);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put("TotalAmount", previous_amount+additionalAmount);
            int update = db.update(CUSTOMER_DB, cv, "Id = ?", new String[]{Integer.toString(Id)});
            if (update == -1) {
                return false;
            } else {
                boolean isHistoryCreated = createHistory(new UpdatedHistory(Id, previous_amount, additionalAmount, "+", getSingleCustomerAmount(Id)));
                if (isHistoryCreated) {
                    return true;
                } else {
                    return false;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return false;
    }

    public boolean reduceSingleCustomertAmount(int id, double additionalAmount) {
        double previous_amount = getSingleCustomerAmount(id);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put("TotalAmount", previous_amount-additionalAmount);
            int update = db.update(CUSTOMER_DB, cv, "Id = ?", new String[]{Integer.toString(id)});
            if (update == -1) {
                return false;
            } else {
                boolean isHistoryCreated = createHistory(new UpdatedHistory(id, previous_amount, additionalAmount, "-", getSingleCustomerAmount(id)));
                if (isHistoryCreated) {
                    return true;
                } else {
                    return false;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return false;
    }

    public boolean createHistory(UpdatedHistory history) {
        try (SQLiteDatabase db = this.getWritableDatabase()){
            ContentValues cv = new ContentValues();
            cv.put("Id", history.getId());
            cv.put("previousAmount", history.getPreviousAmount());
            cv.put("updatedAmount", history.getUpdatedAmount());
            cv.put("amountType", history.getAmountType());
            cv.put("totalAmount", history.getTotalAmount());
            long insert = db.insert(UPDATED_RECORDS, null, cv);
            if (insert == -1) {
                Toast.makeText(context, "Failed To Create History..!", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<UpdatedHistory> getUpdatedHistory(int id) {
        List<UpdatedHistory> history = null;
        try (SQLiteDatabase db = this.getReadableDatabase()){
            Cursor historyCursor = db.rawQuery("SELECT UPDATED_RECORDS.Id, UPDATED_RECORDS.previousAmount, UPDATED_RECORDS.updatedAmount,\n" +
                    "UPDATED_RECORDS.totalAmount, UPDATED_RECORDS.amountType, UPDATED_RECORDS.updatedAt FROM UPDATED_RECORDS\n" +
                    "INNER JOIN CUSTOMER ON UPDATED_RECORDS.Id = CUSTOMER.Id WHERE UPDATED_RECORDS.Id = "+id+"", null);
            if (historyCursor != null && historyCursor.moveToLast()) {
                history = new ArrayList<>();
                while (!historyCursor.isBeforeFirst()) {
                    history.add(new UpdatedHistory(historyCursor.getInt(historyCursor.getColumnIndex("Id")),
                            historyCursor.getDouble(historyCursor.getColumnIndex("previousAmount")), historyCursor.getDouble(historyCursor.getColumnIndex("updatedAmount")),
                            historyCursor.getString(historyCursor.getColumnIndex("amountType")),historyCursor.getDouble(historyCursor.getColumnIndex("totalAmount")), historyCursor.getString(historyCursor.getColumnIndex("updatedAt"))));
                    Log.d("moving to next", ""+history.get(0).getDate());
                    historyCursor.moveToPrevious();
                }
                return history;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }
}
