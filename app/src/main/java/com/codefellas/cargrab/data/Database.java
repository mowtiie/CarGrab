package com.codefellas.cargrab.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.number.UnlocalizedNumberFormatter;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    public Database(@Nullable Context context) {
        super(context, "cargrab.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createPassengerTable = "CREATE TABLE IF NOT EXISTS passengers (" +
                "passengerID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "firstName TEXT NOT NULL," +
                "middleName TEXT," +
                "lastName TEXT NOT NULL," +
                "email TEXT," +
                "phone TEXT," +
                "password TEXT NOT NULL);";

        String createDriverTable = "CREATE TABLE IF NOT EXISTS drivers (" +
                "driverID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "firstName TEXT NOT NULL," +
                "middleName TEXT ," +
                "lastName TEXT NOT NULL," +
                "phone TEXT," +
                "licenseID TEXT NOT NULL," +
                "status TEXT NOT NULL);";

        String createVehicleTable = "CREATE TABLE IF NOT EXISTS vehicles (" +
                "vehicleID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "vehicleModel TEXT NOT NULL, " +
                "vehiclePlateNumber TEXT NOT NULL);";

        String createAdminTable = "CREATE TABLE IF NOT EXISTS admins (" +
                "adminID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL);";

        String createPassengerLocationsTable = "CREATE TABLE IF NOT EXISTS passengerLocations (" +
                "passengerLocationID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "passengerID INTEGER NOT NULL," +
                "locationTitle TEXT NOT NULL," +
                "location TEXT NOT NULL);";

        sqLiteDatabase.execSQL(createPassengerTable);
        sqLiteDatabase.execSQL(createDriverTable);
        sqLiteDatabase.execSQL(createAdminTable);
        sqLiteDatabase.execSQL(createPassengerLocationsTable);
        sqLiteDatabase.execSQL(createVehicleTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS passengers");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS drivers");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS passengerLocations");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS admins");
        onCreate(sqLiteDatabase);
    }

    public void registerPassenger(Passenger passenger) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("firstName", passenger.getFirstName());
        values.put("middleName", passenger.getMiddleName());
        values.put("lastName", passenger.getLastName());
        values.put("email", passenger.getEmail());
        values.put("password", passenger.getPassword());
        values.put("phone", passenger.getPhone());
        database.insert("passengers", null, values);
        database.close();
    }

    public void registerDriver(Driver driver) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("firstName", driver.getFirstName());
        values.put("middleName", driver.getMiddleName());
        values.put("lastName", driver.getLastName());
        values.put("email", driver.getEmail());
        values.put("password", driver.getPassword());
        values.put("licenseID", driver.getLicenseID());
        values.put("phone", driver.getPhone());
        values.put("status", driver.getStatus());
        database.insert("drivers", null, values);
        database.close();
    }

    public Passenger loginPassenger(String email, String password) {
        Passenger passenger = new Passenger();
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM passengers WHERE email = ? AND password = ?";
        Cursor cursor = database.rawQuery(query, new String[]{email, password});

        if (cursor.moveToFirst()) {
            passenger.setPassengerID(cursor.getInt(cursor.getColumnIndexOrThrow("passengerID")));
            passenger.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("firstName")));
            passenger.setMiddleName(cursor.getString(cursor.getColumnIndexOrThrow("middleName")));
            passenger.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("lastName")));
            passenger.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            passenger.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            passenger.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));

            cursor.close();
            database.close();
            return passenger;
        }
        cursor.close();
        database.close();
        return null;
    }

    public Passenger getPassengerByID(int id) {
        Passenger passenger = new Passenger();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM passengers WHERE passengerID = ?", new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            passenger.setPassengerID(cursor.getInt(cursor.getColumnIndexOrThrow("passengerID")));
            passenger.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("firstName")));
            passenger.setMiddleName(cursor.getString(cursor.getColumnIndexOrThrow("middleName")));
            passenger.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("lastName")));
            passenger.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            passenger.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            passenger.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
        }
        cursor.close();
        db.close();
        return passenger;
    }

    public void editPassenger(int passengerID, String fName, String mName, String lName, String email, String password, String phone) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("firstName", fName);
        values.put("middleName", mName);
        values.put("lastName", lName);
        values.put("email", email);
        values.put("password", password);
        values.put("phone", phone);

        db.update("passengers", values, "passengerID = ?", new String[]{String.valueOf(passengerID)});
        db.close();
    }

    public Driver loginDriver(String email, String password) {
        Driver driver = new Driver();
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM drivers WHERE email = ? AND password = ?";
        Cursor cursor = database.rawQuery(query, new String[]{email, password});

        if (cursor.moveToFirst()) {
            driver.setDriverID(cursor.getInt(cursor.getColumnIndexOrThrow("driverID")));
            driver.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("firstName")));
            driver.setMiddleName(cursor.getString(cursor.getColumnIndexOrThrow("middleName")));
            driver.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("lastName")));
            driver.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            driver.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            driver.setLicenseID(cursor.getString(cursor.getColumnIndexOrThrow("licenseID")));
            driver.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            driver.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));

            cursor.close();
            database.close();
            return driver;
        }
        cursor.close();
        database.close();
        return null;
    }

    public ArrayList<Passenger> getAllPassengers() {
        ArrayList<Passenger> list = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM passengers", null);

        if (cursor.moveToFirst()) {
            do {
                Passenger passenger = new Passenger();
                passenger.setPassengerID(cursor.getInt(cursor.getColumnIndexOrThrow("passengerID")));
                passenger.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("firstName")));
                passenger.setMiddleName(cursor.getString(cursor.getColumnIndexOrThrow("middleName")));
                passenger.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("lastName")));
                passenger.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                passenger.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
                passenger.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
                list.add(passenger);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
    }

    public ArrayList<Driver> getAllDrivers() {
        ArrayList<Driver> list = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM drivers", null);

        if (cursor.moveToFirst()) {
            do {
                Driver driver = new Driver();
                driver.setDriverID(cursor.getInt(cursor.getColumnIndexOrThrow("driverID")));
                driver.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("firstName")));
                driver.setMiddleName(cursor.getString(cursor.getColumnIndexOrThrow("middleName")));
                driver.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("lastName")));
                driver.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                driver.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
                driver.setLicenseID(cursor.getString(cursor.getColumnIndexOrThrow("licenseID")));
                driver.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
                driver.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                list.add(driver);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
    }

    public void approveDriver(int driverID) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", "Approved");
        database.update("drivers", values, "driverID = ?", new String[]{String.valueOf(driverID)});
        database.close();
    }

    public void rejectDriver(int driverID) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", "Rejected");
        database.update("drivers", values, "driverID = ?", new String[]{String.valueOf(driverID)});
        database.close();
    }
}