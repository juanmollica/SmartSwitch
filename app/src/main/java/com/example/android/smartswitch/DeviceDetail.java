package com.example.android.smartswitch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;


public class DeviceDetail extends AppCompatActivity {

    private final String TAG = "DeviceDetail";
    private static int DEVICE_STATE = 0;

    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    String uid;

    MqttAndroidClient mClient;
    final String SERVER_URI = "tcp://m16.cloudmqtt.com:12807";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if(intent.hasExtra(Intent.EXTRA_TITLE)){
            setTitle(intent.getStringExtra(Intent.EXTRA_TITLE));
        }

        if (mUser != null){
            uid = mUser.getUid();
        }

        connect();
     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            disconnect(mClient);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("DEVICE_STATE",DEVICE_STATE);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void lightbulbOn(View view){
        ImageView lightbulbOff = findViewById(R.id.lightbulb_off);
        lightbulbOff.setVisibility(View.INVISIBLE);
        ImageView lightbulbOn = findViewById(R.id.lightbulb_on);
        lightbulbOn.setVisibility(View.VISIBLE);
        publish(mClient,"1",uid + "switch");
        DEVICE_STATE = 1;
    }

    public void lightbulbOff(View view){
        ImageView lightbulbOn = findViewById(R.id.lightbulb_on);
        lightbulbOn.setVisibility(View.INVISIBLE);
        ImageView lightbulbOff = findViewById(R.id.lightbulb_off);
        lightbulbOff.setVisibility(View.VISIBLE);
        publish(mClient,"0", uid + "switch");
        DEVICE_STATE = 0;
    }


    public void connect(){

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName("qnjmmect");
        options.setPassword("skrZqVQ6-DTk".toCharArray());

        String clientId = MqttClient.generateClientId();
        mClient = new MqttAndroidClient(getApplicationContext(),SERVER_URI,clientId);

        try{
            IMqttToken token = mClient.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "onSuccess: ");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "onFailure: " + exception.getMessage());
                }
            });

        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    public void publish(MqttAndroidClient client, String msg, String topic) {

        try {
            byte[] encodedPayload = new byte[0];
            encodedPayload = msg.getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setQos(0);
            message.setRetained(true);
            client.publish(topic, message);
        } catch (MqttException e){
            e.printStackTrace();
            Log.d(TAG, "publish: " + e.getMessage());
        }
    }

    public void subscribe(MqttAndroidClient client, final String topic, int qos) {

        try {
            IMqttToken token = client.subscribe(topic, qos);
            token.setActionCallback(new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Log.d(TAG, "Subscribe Successfully " + topic);
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Log.e(TAG, "Subscribe Failed " + topic);
                }
            });
        } catch (MqttException e){
            e.printStackTrace();
            Log.d(TAG, "subscribe: " + e.getMessage());
        }
    }


    public void unSubscribe(MqttAndroidClient client, final String topic) {

        try {
            IMqttToken token = client.unsubscribe(topic);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Log.d(TAG, "UnSubscribe Successfully " + topic);
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Log.e(TAG, "UnSubscribe Failed " + topic);
                }
            });
        } catch (MqttException e){
            e.printStackTrace();
            Log.d(TAG, "unSubscribe: " + e.getMessage());
        }
    }



    public void disconnect(MqttAndroidClient client) {
        try {
        IMqttToken mqttToken = client.disconnect();
        mqttToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "Successfully disconnected");
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.d(TAG, "Failed to disconnected " + throwable.toString());
            }
          });

        } catch (MqttException e){
            e.printStackTrace();
            Log.d(TAG, "disconnect: " + e.getMessage());
        }
    }

}
