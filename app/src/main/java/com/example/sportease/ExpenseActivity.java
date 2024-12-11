package com.example.sportease;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ExpenseActivity extends AppCompatActivity {

    private TextView tvArtificialGrass, tvSideNet, tvSalary, tvBookingRevenue, tvTotalExpense, tvProfit;

    // Static prices
    private static final int ARTIFICIAL_GRASS_COST = 200;  // per sq ft
    private static final int SIDE_NET_COST = 5000;
    private static final int SALARY_COST = 10000;
    private static final int BOOKING_REVENUE = 20000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        // Initialize views
        tvArtificialGrass = findViewById(R.id.tvArtificialGrassCost);
        tvSideNet = findViewById(R.id.tvSideNetCost);
        tvSalary = findViewById(R.id.tvSalaryCost);
        tvBookingRevenue = findViewById(R.id.tvBookingRevenue);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        tvProfit = findViewById(R.id.tvProfit);

        // Calculate and set expense details
        calculateAndDisplayExpenses();
    }

    private void calculateAndDisplayExpenses() {
        // Set static costs
        tvArtificialGrass.setText("₹" + ARTIFICIAL_GRASS_COST + " per sq ft");
        tvSideNet.setText("₹" + SIDE_NET_COST);
        tvSalary.setText("₹" + SALARY_COST);
        tvBookingRevenue.setText("₹" + BOOKING_REVENUE);

        // Calculate total expense and profit
        int totalExpense = ARTIFICIAL_GRASS_COST + SIDE_NET_COST + SALARY_COST;
        int profit = BOOKING_REVENUE - totalExpense;

        // Display calculated values
        tvTotalExpense.setText("₹" + totalExpense);
        tvProfit.setText("₹" + profit);
    }
}
