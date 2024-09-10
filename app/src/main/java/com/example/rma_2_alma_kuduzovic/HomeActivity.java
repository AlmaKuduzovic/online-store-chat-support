package com.example.rma_2_alma_kuduzovic;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private DatabaseHelper databaseHelper;
    private CardDao cardDao;
    private UserCartDao userCartDao;
    private ExecutorService executorService;
    private List<CardModel> cardModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        databaseHelper = DatabaseHelper.getDB(this);
        cardDao = databaseHelper.cardDao();
        userCartDao = databaseHelper.userCartDao();
        executorService = Executors.newSingleThreadExecutor();//executorService = Executors.newSingleThreadExecutor();


        loadCards();

        ImageView firstIcon = findViewById(R.id.firstIcon);
        firstIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MyCartActivity.class);
                startActivity(intent);
            }
        });
        ImageView thirdIcon = findViewById(R.id.thirdIcon);
        thirdIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        ImageView userIcon = findViewById(R.id.userIcon);
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        SearchView searchView = findViewById(R.id.searchView);
        setupSearchView(searchView);
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.user_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_logout) {
                        FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(HomeActivity.this, SignInActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.action_my_profile) {
                    Intent intent = new Intent(HomeActivity.this, EditUserActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void loadCards() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                cardModels = cardDao.getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cardAdapter = new CardAdapter(HomeActivity.this, cardModels, userCartDao);
                        recyclerView.setAdapter(cardAdapter);
                    }
                });
            }
        });
    }

    private void setupSearchView(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cardAdapter.filter(newText);
                return true;
            }
        });
    }


}
