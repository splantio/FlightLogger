package ca.lakeland.plantsd.flightlogger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class HomeScreen extends AppCompatActivity {

    Gson gson = new Gson();

    private String storageFileName = "storage-json.txt";

    // lazy singleton for getting stored data
    private Storage stor;

    static boolean adminLoggedIn = false;
    Context context = this;

    private static final int MENU_SETTINGS = Menu.FIRST;
    private static final int MENU_ADMIN = MENU_SETTINGS + 1;
    private static final int MENU_ADD_EMAIL = MENU_SETTINGS + 2;
    private static final int MENU_VIEW_EMAILS = MENU_SETTINGS + 3;
    private static final int MENU_LOGIN = MENU_SETTINGS + 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        stor = Storage.getInstance();

        // try to access data about previous flight plans and logs

        try {

            Context appContext = this.getApplicationContext();

            // Import things from the file
            FileInputStream fis = appContext.openFileInput(storageFileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader buffreader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = buffreader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            //System.out.println("Loading: " + json);

            Storage storageTemp = gson.fromJson(json, Storage.class);

            stor.setFlightNum(storageTemp.getFlightNum());
            stor.setPilots(storageTemp.getPilots());
            stor.setSpotters(storageTemp.getSpotters());
            stor.setDoneChecklists(storageTemp.getDoneChecklists());
            stor.setFlightLogs(storageTemp.getFlightLogs());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets called every time the user presses the menu button.
     * Use if your menu is dynamic.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, MENU_SETTINGS, Menu.NONE, "Settings");
        menu.add(0, MENU_ADD_EMAIL, Menu.NONE, "Add an email address");
        menu.add(0, MENU_VIEW_EMAILS, Menu.NONE, "View email addresses");
        if(adminLoggedIn) {
            menu.add(0, MENU_ADMIN, Menu.NONE, "Admin Logged In").setIcon(R.drawable.ic_lock_open_black_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add(0, MENU_LOGIN, Menu.NONE, "Administrator Logout");
        } else {
            menu.add(0, MENU_LOGIN, Menu.NONE, "Login as Administrator");
        }
        supportInvalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SETTINGS:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(this, SettingsMenu.class);
                startActivity(intent);
                return true;
            case MENU_ADMIN:
                Toast.makeText(this, "Logged in as admin", Toast.LENGTH_SHORT).show();
                return true;
            case MENU_ADD_EMAIL:
                SettingsMenu.addEmailAddress(this);
                return true;
            case MENU_VIEW_EMAILS:
                Toast.makeText(this, "view emails", Toast.LENGTH_SHORT).show();
                return true;
            case MENU_LOGIN:
                SettingsMenu.adminLoginLogoutButton(this);

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    public void openFlightLogger(View view) {
        Intent intent = new Intent(this, FlightLogsActivity.class);
        startActivity(intent);
    }
    public void openFlightPlanner(View view) {
        Intent intent = new Intent(this, ChecklistsActivity.class);
        startActivity(intent);
    }

    public void btnPilotsClick(View view) {
        Intent intent = new Intent(this, PilotsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();

        Context appContext = this.getApplicationContext();
        FileOutputStream fos = null;

        try {
            // Saving
            fos = appContext.openFileOutput(storageFileName, Context.MODE_PRIVATE);
            String jsonData = gson.toJson(stor);
            //System.out.println("Saving:  " + jsonData);
            fos.write(jsonData.getBytes());
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean getAdminLoggedIn() {
        return adminLoggedIn;
    }
    public static void setAdminLoggedIn(Boolean bool) {
        adminLoggedIn = bool;
    }

    // To be called on click of FlightLogger text on home screen.
    // Verifies the user wishes to clear all data.
    public void clearStoredData(View view) {
        if (adminLoggedIn) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            // Yes button clicked
                            File dir = getFilesDir();
                            File fileToDelete = new File(dir, storageFileName);

                            Boolean deleted = fileToDelete.delete();

                            if (deleted) {
                                stor.clearAllData();
                                Toast.makeText(context, "All local data deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Local storage could not be deleted", Toast.LENGTH_SHORT).show();
                            }

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            // No button clicked
                            break;
                    }
                }
            };

            Context appContext = HomeScreen.this;
            AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
            builder.setMessage("Do you wish to delete all locally stored data?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }

    public void onLogoClick(View view) {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }
}
