package com.example.e_commercemobapp.admin;

public class Report {

    private String userEmail;
    private String itemsSummary;
    private double total;
    private String date;

    public Report(String userEmail, String itemsSummary, double total, String date) {
        this.userEmail = userEmail;
        this.itemsSummary = itemsSummary;
        this.total = total;
        this.date = date;
    }

    public String getUserEmail() { return userEmail; }
    public String getItemsSummary() { return itemsSummary; }
    public double getTotal() { return total; }
    public String getDate() { return date; }
}
