package com.example.android.smartswitch;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements RenameDialog.RenameDialogListener, DeleteDialog.DeleteDialogListener {

    final static int REQUEST_DEVICE_STATE = 0;

    RelativeLayout mRelativeLayout;
    TextView mDeviceTextView;
    FloatingActionButton mFab;
    ImageView mImageViewOff;
    ImageView mImageViewOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mDeviceTextView = findViewById(R.id.tv_device_data);
        mRelativeLayout = findViewById(R.id.rl_devices_data);
        mImageViewOff = findViewById(R.id.lightbulb_off);
        mImageViewOn = findViewById(R.id.lightbulb_on);
        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String deviceName = mDeviceTextView.getText().toString();

                Context context = MainActivity.this;
                Intent intent = new Intent(context, DeviceDetail.class);
                intent.putExtra(Intent.EXTRA_TITLE, deviceName);
                startActivityForResult(intent, REQUEST_DEVICE_STATE);


            }
        });

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add new device
                Intent intent = new Intent(MainActivity.this, AddDevice.class);
                startActivity(intent);

            }
        });

        registerForContextMenu(mRelativeLayout);

    }

    // Create Context Menu for Renaming and Deleting the device
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_main, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.rename:
                openRenameDialog();
                break;
            case R.id.delete:
                openDeleteDialog();
                break;
        }

        return super.onContextItemSelected(item);
    }

    public void openRenameDialog() {
        RenameDialog renameDialog = new RenameDialog();
        renameDialog.show(getSupportFragmentManager(), "rename");
    }

    @Override
    public void renameDevice(String deviceName) {
        mDeviceTextView.setText(deviceName);
    }

    public void openDeleteDialog() {
        DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.show(getSupportFragmentManager(), "delete");
    }


    //Options menu for accessing Settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
                showSettings();

        }

        return super.onOptionsItemSelected(item);
    }

    private void showSettings() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    // Response from DeviceDetail Activity getting state of device
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_DEVICE_STATE) {
            if (resultCode == RESULT_OK) {
                int deviceState = data.getIntExtra("DEVICE_STATE", 0);

                if (deviceState == 1) {
                    showDeviceOn();
                } else {
                    showDeviceOff();
                }

            }
        }
    }

    private void showDeviceOn() {
        mImageViewOn.setVisibility(View.VISIBLE);
        mImageViewOff.setVisibility(View.INVISIBLE);
    }

    private void showDeviceOff() {
        mImageViewOn.setVisibility(View.INVISIBLE);
        mImageViewOff.setVisibility(View.VISIBLE);
    }


    //Add menu to be able to switch among different houses

}
