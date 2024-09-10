package com.example.rma_2_alma_kuduzovic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MyCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyCartAdapter adapter;
    private TextView totalTextView;
    private Button sendOrderButton;
    private Button resetCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart2);



        recyclerView = findViewById(R.id.cartRecyclerView);
        totalTextView = findViewById(R.id.totalTextView);
        sendOrderButton = findViewById(R.id.sendOrderButton);
        resetCartButton = findViewById(R.id.resetCartButton);


        List<UserCartModel> cartList = DatabaseHelper.getDB(this)
                .userCartDao()
                .getUserCart(FirebaseAuth.getInstance().getCurrentUser().getUid());


        sendOrderButton.setEnabled(false);
        resetCartButton.setEnabled(false);

        sendOrderButton.setEnabled(!cartList.isEmpty());
        resetCartButton.setEnabled(!cartList.isEmpty());

        adapter = new MyCartAdapter(this, cartList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        double total = calculateTotal(cartList);
        totalTextView.setText("Total: $" + total);


        sendOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteAllItemsForCurrentUser();
                Intent intent = new Intent(MyCartActivity.this, HomeActivity.class);
                startActivity(intent);
                Toast.makeText(MyCartActivity.this, "Cart Cleared", Toast.LENGTH_SHORT).show();
            }
        });

        resetCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllItemsForCurrentUser();
                Toast.makeText(MyCartActivity.this, "Order Sent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAllItemsForCurrentUser() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseHelper.getDB(MyCartActivity.this).userCartDao().deleteUserCart(userId);
        adapter.clear();
        totalTextView.setText("Total: $0.00");
    }


    private double calculateTotal(List<UserCartModel> cartList) {
        double total = 0;
        for (UserCartModel item : cartList) {
            total += item.getPrice();
        }
        total = Math.round(total * 100.0) / 100.0;

        return total;
    }
}
