package com.example.chargex;

        import static android.content.ContentValues.TAG;

        import static com.google.android.gms.common.util.CollectionUtils.listOf;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;

        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.TextView;

        import com.fasterxml.jackson.core.JsonProcessingException;
        import com.fasterxml.jackson.databind.ObjectMapper;
        import com.github.kittinunf.fuel.core.Body;
        import com.google.common.net.MediaType;
        import com.stripe.android.PaymentConfiguration;
        import com.stripe.android.paymentsheet.PaymentSheet;
        import com.stripe.android.paymentsheet.PaymentSheetResult;

        import org.json.JSONException;
        import org.json.JSONObject;
        import com.stripe.android.paymentsheet.*;

// Add the following lines to build.gradle to use this example's networking library:
//   implementation 'com.github.kittinunf.fuel:fuel:2.3.1'
        import com.github.kittinunf.fuel.Fuel;
        import com.github.kittinunf.fuel.core.FuelError;
        import com.github.kittinunf.fuel.core.Handler;

        import java.io.IOException;
        import java.nio.charset.StandardCharsets;
        import java.time.Duration;
        import java.time.LocalDate;
        import java.time.LocalTime;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

        import kotlin.Pair;
        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.RequestBody;
        import okhttp3.Response;
        import okhttp3.ResponseBody;
        import okhttp3.Call;
        import okhttp3.Callback;
        import com.fasterxml.jackson.databind.ObjectMapper;

/*public class Checkout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
    }
}*/


public class Checkout extends AppCompatActivity {
    private static final String TAG = "CheckoutActivity";
    PaymentSheet paymentSheet;
    String paymentClientSecret;
    private String startTime;
    private String endTime;
    private String date;
    private String station;
    private Integer machineId;
    private Double Price;

    private Double Amount;
    PaymentSheet.CustomerConfiguration customerConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        startTime=getIntent().getStringExtra("startTime");
        endTime=getIntent().getStringExtra("endTime");
        date=getIntent().getStringExtra("date");
        Price=getIntent().getDoubleExtra("rate",10);
        station=getIntent().getStringExtra("station");
        machineId=getIntent().getIntExtra("machineId",1);

        setContentView(R.layout.activity_checkout);
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        Map<String, Object> requestBody = new HashMap<>();
        Log.d(TAG,"start Time is:"+startTime);
        Log.d(TAG,"end Time is:"+endTime);
        SharedPreferences preferences=getSharedPreferences("user_data", Context.MODE_PRIVATE);

        Duration duration=Duration.between(LocalTime.parse(startTime),LocalTime.parse(endTime));
        Log.d(TAG,"durationn is"+duration);
        Log.d(TAG,"Price is"+Price);
        Amount= duration.toHours()*Price;
        Log.d(TAG,"amount is"+Amount);
        Amount+=(duration.toMinutes()%60)*(Price/60);
        Amount=Amount*100;
        updateUI();
        Log.d(TAG,"amount is"+Amount);
        requestBody.put("amount", Amount);
        requestBody.put("customer",preferences.getString("username","customer"));

        JSONObject json = new JSONObject(requestBody);
        if(Amount>30000) {
            performNetworkRequest(json);
        }
        else{
            //toast here
        }

    }
    private void performNetworkRequest(JSONObject json) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json.toString(),okhttp3.MediaType.parse("application/json"));
        Log.d(TAG,"request body is"+json);
        Request request = new Request.Builder()
                .url("http://192.168.73.176:8080/create-payment-intent")
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Network request failed: " + e.getMessage(), e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (response.isSuccessful() && responseBody != null) {
                        String result = responseBody.string();
                        final JSONObject jsonResult = new JSONObject(result);

                        runOnUiThread(() -> {
                            try {
                                customerConfig = new PaymentSheet.CustomerConfiguration(
                                        jsonResult.getString("customer"),
                                        jsonResult.getString("ephemeralKey")
                                );
                                paymentClientSecret = jsonResult.getString("clientSecret");
                                PaymentConfiguration.init(getApplicationContext(), jsonResult.getString("publishableKey"));
                                Log.d(TAG, "checkout is fine");
                            } catch (JSONException e) {
                                Log.d(TAG, "an error occurred!" + e);
                            }
                        });
                    } else {
                        Log.d(TAG, "Network request failed");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response: " + e.getMessage(), e);
                }
            }
        });
    }

    public void listenr(View a){
        addSlot();
        presentPaymentSheet();
    }
    public void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {

        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d(TAG, "Canceled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e(TAG, "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            //addSlot();
            Log.d(TAG, "Completed");
        }
    }


    private void presentPaymentSheet() {

        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("ChargeX.")
                .customer(customerConfig)
                // Set `allowsDelayedPaymentMethods` to true if your business handles payment methods
                // delayed notification payment methods like US bank accounts.
                .allowsDelayedPaymentMethods(true)
                .build();
        paymentSheet.presentWithPaymentIntent(
                paymentClientSecret,
                configuration
        );
    }
    public void addSlot(){
        SharedPreferences preferences=getSharedPreferences("user_data", Context.MODE_PRIVATE);
        Slot slot=new Slot();
        slot.setUser(preferences.getString("username","customer"));
        slot.setMachine_id(machineId);
        slot.setStation(station);
        slot.setPrice(Price);
        slot.setStatus("Paid");
        slot.setDate(LocalDate.parse(date));
        slot.setStartTime(LocalTime.parse(startTime));
        slot.setEndTime(LocalTime.parse(endTime));
        slot.setData();
        //Intent i=new Intent(getApplicationContext(), BookingIndex.class);
        //startActivity(i);
    }
    public void updateUI(){
        TextView view=findViewById(R.id.slotStartTime);
        view.setText(startTime);
        view=findViewById(R.id.slotEndTime);
        view.setText(endTime);
        view=findViewById(R.id.slotStation);
        view.setText(station);
        view=findViewById(R.id.slotPrice);
        view.setText(Price.toString());
        view=findViewById(R.id.slotAmount);
        view.setText(Amount.toString());


    }

}