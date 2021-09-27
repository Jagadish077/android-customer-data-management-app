package com.example.anmolpipes.datahelperclasses;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class UpdatedHistory {
    private int id;
    private double previousAmount;
    private double updatedAmount;
    private String amountType;
    private double totalAmount;
    private String date;

    public UpdatedHistory(int id, double previousAmount, double updatedAmount, String amountType, double totalAmount) {
        this.id = id;
        this.previousAmount = previousAmount;
        this.updatedAmount = updatedAmount;
        this.amountType = amountType;
        this.totalAmount = totalAmount;
    }

    public UpdatedHistory(int id, double previousAmount, double updatedAmount, String amountType, double totalAmount, String date) {
        this.id = id;
        this.previousAmount = previousAmount;
        this.updatedAmount = updatedAmount;
        this.amountType = amountType;
        this.totalAmount = totalAmount;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPreviousAmount() {
        return previousAmount;
    }

    public void setPreviousAmount(double previousAmount) {
        this.previousAmount = previousAmount;
    }

    public double getUpdatedAmount() {
        return updatedAmount;
    }

    public void setUpdatedAmount(double updatedAmount) {
        this.updatedAmount = updatedAmount;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
