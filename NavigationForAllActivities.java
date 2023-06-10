package com.example.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class NavigationForAllActivities {
    public static void SetNavigationForActivities(DrawerLayout drawerLayout,
                                                  NavigationView navigationView,
                                                  Activity activity, Customer currentCustomer) {


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Intent intent;
                if (itemId == R.id.home_page) {
                    intent = new Intent(activity, MainActivity.class);
                    intent.putExtra("customer",currentCustomer);
                    activity.startActivity(intent);
                } else if (itemId == R.id.create_sport_activity) {
                    intent = new Intent(activity, CreateSportActivity.class);
                    intent.putExtra("customer",currentCustomer);
                    activity.startActivity(intent);
                } else if (itemId == R.id.view_sport_activity) {
                    intent = new Intent(activity, MySportingActivitiesActivity.class);
                    intent.putExtra("customer",currentCustomer);
                    activity.startActivity(intent);
                } else if (itemId == R.id.exit_app) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("אשרו יציאה");
                    builder.setPositiveButton("מאשר", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            System.exit(0);
                        }
                    });
                    builder.setNegativeButton("ביטול", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if (itemId == R.id.logout) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("אשרו התנקות");
                    builder.setPositiveButton("מאשר", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).setNegativeButton("ביטול", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }}