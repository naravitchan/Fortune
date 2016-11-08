package com.egco428.a13264;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Random;


public class NewfortuneActivity extends AppCompatActivity implements SensorEventListener {
    private Button shakebtn;
    private TextView resulttxt;
    private TextView datetxt;
    private ImageView showcookies;
    private SensorManager sensorManager;
    private long lastUpdate;
    private CommentDataSource dataSource;
    private String result="result";
    private String image="image";
    private String date="date";
    int check=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.newfortune);
        dataSource = new CommentDataSource(this);
        dataSource.open();
        shakebtn=(Button)findViewById(R.id.button);
        shakebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(check==1){
                    check=2;
                    shakebtn.setText("Shaking");
                }
                else if(check==3){
                    Comment comment = null;
                    comment = dataSource.createComment(result,image,date);
                    finish();
                }
            }
        });
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            getAccelerometer(event);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy) {

    }

    private void getAccelerometer(SensorEvent event){
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();
        if (accelationSquareRoot >= 2) //
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            if (check == 2 && accelationSquareRoot>=2) {
                Toast.makeText(this, "Device was shuffled", Toast.LENGTH_SHORT)
                .show();
                String[] comments = new String[]{"You will get A","You're Lucky","Don't Panic","Something surprise you today","Work Hard"};
                int nextInt = new Random().nextInt(5);
                resulttxt =(TextView)findViewById(R.id.result);
                resulttxt.setText("Result : "+comments[nextInt]);
                result=comments[nextInt];
                shakebtn.setText("SAVE");
                showcookies=(ImageView)findViewById(R.id.newfortuneimage);
                int res = getResources().getIdentifier("image"+nextInt,"drawable",getPackageName());
                image="image"+nextInt;
                showcookies.setImageResource(res);
                SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                Date now = new Date();
                String strDate = sdfDate.format(now);
                datetxt=(TextView)findViewById(R.id.date);
                datetxt.setText(strDate);
                date=strDate;
                check=3;


            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }

}
