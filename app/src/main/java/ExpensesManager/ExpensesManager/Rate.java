package ExpensesManager.ExpensesManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


public class Rate extends AppCompatActivity implements View.OnClickListener {
    SeekBar sbRate;
    TextView tvRate;
    Button btnRate, btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        tvRate = (TextView)findViewById(R.id.tvRate);
        sbRate = (SeekBar)findViewById(R.id.sbRate);
        btnRate = (Button)findViewById(R.id.btnRate);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        sbRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvRate.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnRate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel:
                finish();
                break;
            case R.id.btnRate:
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try  {
                            HttpClient client = new DefaultHttpClient();
                            HttpPost httpPost = new HttpPost("http://mobileapp-rate.herokuapp.com/index.php?rate=" + tvRate.getText().toString());
                            HttpResponse httpResponse = client.execute(httpPost);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                Shared.RATED = true;
                Toast.makeText(getApplicationContext(),"You rated our app " + tvRate.getText().toString() + " score successfully!",Toast.LENGTH_SHORT).show();
                finish();
        }
    }
}
