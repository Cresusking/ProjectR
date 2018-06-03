package com.example.cresu.projectr.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import com.example.cresu.projectr.activities.MainActivity;
import com.example.cresu.projectr.constantes.AppConstantes;
import com.example.cresu.projectr.constantes.HttpRequestConstantes;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by cresu on 06/05/2018.
 */

public class MyMqtt {

    private MqttAndroidClient client;

    private Context context;
    private String topic;

    public MyMqtt(Context context, String topic){
        this.context = context;
        this.topic = topic;
    }

    public Context getMyContext() {
        return context;
    }

    public void setMyContext(Context context) {
        this.context = context;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void connect(final TextView soldeTextView){
        String clientId = MqttClient.generateClientId();

        client =
                new MqttAndroidClient(context, "tcp://"+ HttpRequestConstantes.BROKER+":1883",
                        clientId);

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            //options.setUserName("USERNAME");
            //options.setPassword("PASSWORD".toCharArray());

            IMqttToken token = client.connect(options);

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //Toast.makeText(context, "Connected !!", Toast.LENGTH_LONG).show();
                    Log.d("MainActivity", "onSuccess");
                    subcribe();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    //Toast.makeText(context, "Connection Failed", Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        receive(soldeTextView);
    }

    public void receive(final TextView soldeTextView){

        if(client != null){
            client.setCallback(new MqttCallback() {
                // Crée pour éviter la répétition de la requête de récupération des données
                int compteur = 0;
                @Override
                public void connectionLost(Throwable cause) {
                    //Toast.makeText(context, "Connection Lost : "+cause.getMessage(), Toast.LENGTH_LONG).show();

                    // On se reconnecte automatiquement dès qu'on perd la connexion au broker
                    connect(soldeTextView);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if(compteur == 0) {
                        VariablesGlobales.getInstance().updateSolde(soldeTextView);

                        Intent resultIntent = new Intent(context, MainActivity.class);
                        NotificationUtils notificationUtils = new NotificationUtils(context);
                        notificationUtils.showNotificationMessage("Transfert Reçu","Vous avez reçu un transfert controlez votre solde","", resultIntent);
                    }
                    compteur++;
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        }
    }

    public void send(String id_destinataire, String messageToSend){


        byte[] encodedPayload = new byte[0];
        try {
            //JSONObject obj = new JSONObject();
            //obj.put("id_destinataire", id_destinataire);
            //obj.put("montant", montant);

            encodedPayload = messageToSend.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(false);
            message.setQos(AppConstantes.QoS_2);

            /*
                Faire un json avec id recpteur et montant
                C'est seulement le récepteur qui reçoit le montant dans son solde
             */

            // Chaque client suit son propre topic
            client.publish(id_destinataire, message);

        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public void subcribe(){
        try {
            IMqttToken subToken = client.subscribe(HttpRequestConstantes.TOPIC, AppConstantes.QoS_2);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void unsubscribe(){
        final String topic = HttpRequestConstantes.TOPIC;
        try {
            IMqttToken unsubToken = client.unsubscribe(topic);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The subscription could successfully be removed from the client
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // some error occurred, this is very unlikely as even if the client
                    // did not had a subscription to the topic the unsubscribe action
                    // will be successfully
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            IMqttToken disconToken = client.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // we are now successfully disconnected
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // something went wrong, but probably we are disconnected anyway
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void ssl_tsl_connexion(){
        String clientId = MqttClient.generateClientId();
        final MqttAndroidClient client =
                new MqttAndroidClient(context, "ssl://iot.eclipse.org:8883",
                        clientId);
        try {
            MqttConnectOptions options = new MqttConnectOptions();

            InputStream input =
                    context.getAssets().open("iot.eclipse.org.bks");

            options.setSocketFactory(client.getSSLSocketFactory(input, "eclipse-password"));


            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {


                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {


                }
            });


        } catch (MqttException | IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
