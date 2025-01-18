package com.example.unigo;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.core.view.GravityCompat;
import android.widget.*;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private TextView addBusNumber;
    private EditText addTime;
    private Button btnCreate;
    private RadioGroup radioGroupFor;
    private RadioGroup radioGroupRoute;
    private ImageButton backButton;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private String selectedBusNumber = "None"; // Default bus number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        addBusNumber = findViewById(R.id.AddBusNumber);
        addTime = findViewById(R.id.AddTime);
        btnCreate = findViewById(R.id.btnCreate);
        radioGroupFor = findViewById(R.id.radioGroupFor);
        radioGroupRoute = findViewById(R.id.radioGroupRoute);
        backButton = findViewById(R.id.back_button);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ImageButton menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            // Open the navigation drawer from the right side
            drawerLayout.openDrawer(GravityCompat.END);
        });

        // Handle Navigation Drawer Item Selections
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_add_new) {
                Toast.makeText(this, "Add New selected", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_update) {
                Toast.makeText(this, "Update selected", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_delete) {
                Toast.makeText(this, "Delete selected", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_logout) {
                Toast.makeText(this, "Logout selected", Toast.LENGTH_SHORT).show();
            }
            drawerLayout.closeDrawers(); // Close the drawer after selection
            return true;
        });

        // Get selected bus number from ThirdActivity (if returned)
        Intent intent = getIntent();
        if (intent.hasExtra("selectedBusNumber")) {
            selectedBusNumber = intent.getStringExtra("selectedBusNumber");
            addBusNumber.setText("Bus Number: " + selectedBusNumber);
        }

        // Handle Add Bus Number Intent
        addBusNumber.setOnClickListener(v -> {
            Intent busIntent = new Intent(MainActivity.this, thirdActivity.class);
            startActivityForResult(busIntent, 1);
        });

        // Handle Time Picker Dialog (Spinner Style)
        addTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                    TimePickerDialog.THEME_HOLO_LIGHT,
                    (view, hourOfDay, minuteOfDay) -> addTime.setText(String.format("%02d:%02d", hourOfDay, minuteOfDay)),
                    hour, minute, true);
            timePickerDialog.show();
        });

        // Handle Create Button
        btnCreate.setOnClickListener(v -> {
            String selectedFor = ((RadioButton) findViewById(radioGroupFor.getCheckedRadioButtonId())).getText().toString();
            StringBuilder routesSelected = new StringBuilder();
            int selectedRouteId = radioGroupRoute.getCheckedRadioButtonId();
            if (selectedRouteId != -1) {
                routesSelected.append(((RadioButton) findViewById(selectedRouteId)).getText().toString());
            }

            Toast.makeText(this,
                    "Schedule Created!\nFor: " + selectedFor +
                            "\nBus: " + selectedBusNumber +
                            "\nTime: " + addTime.getText().toString() +
                            "\nRoute: " + routesSelected,
                    Toast.LENGTH_LONG).show();

            // Refresh the activity for a new selection
            Intent refreshIntent = getIntent();
            finish();
            startActivity(refreshIntent);
        });

        // Handle Back Button
        backButton.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Receive the selected bus number
            selectedBusNumber = data.getStringExtra("selectedBusNumber");
            addBusNumber.setText("Bus Number: " + selectedBusNumber);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
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
