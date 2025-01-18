package com.example.unigo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

public class thirdActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RecyclerView busListRecyclerView;
    private BusAdapter busAdapter;
    private ImageButton addButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // Initialize views
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        busListRecyclerView = findViewById(R.id.bus_list);
        addButton = findViewById(R.id.addButton);
        backButton = findViewById(R.id.back_button);

        // Set up Toolbar
        setSupportActionBar(toolbar);

        // Set up Drawer Toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation view item click handling
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.END);
            handleMenuSelection(item.getItemId());
            return true;
        });

        // Back button functionality
        backButton.setOnClickListener(v -> onBackPressed());

        // Set up RecyclerView
        busListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        busAdapter = new BusAdapter();
        busListRecyclerView.setAdapter(busAdapter);

        // Floating action button click handler
        addButton.setOnClickListener(v -> showAddBusDialog());

        // Handle bus selection
        busAdapter.setOnItemClickListener(busNumber -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedBusNumber", busNumber);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Drawer menu icon
        toolbar.setNavigationIcon(R.drawable.baseline_menu_24);
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));
    }

    private void showAddBusDialog() {
        // Create an AlertDialog with an EditText
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Bus Number");

        // Set up the input field
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String busNumber = input.getText().toString().trim();
                if (!busNumber.isEmpty()) {
                    busAdapter.addBusNumber(busNumber);
                    Toast.makeText(getApplicationContext(), "Bus Number Added: " + busNumber, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Bus Number cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return handleMenuSelection(item.getItemId());
    }

    private boolean handleMenuSelection(int itemId) {
        if (itemId == R.id.nav_add_new) {
            showAddBusDialog();
        } else if (itemId == R.id.nav_update) {
            Toast.makeText(this, "Update functionality is not yet implemented", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_delete) {
            Toast.makeText(this, "Delete functionality is not yet implemented", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_home) {
            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
        } else if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }
}
