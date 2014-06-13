package de.recuti;

import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import de.recuti.util.SystemUiHider;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity extends Activity {

	private EditText mainChrono;
	private int mainMin = 0, mainSec = 0;
	
	private EditText subChrono1;
	private int sub1Min, sub1Sec;
	
	private EditText subChrono2;
	private int sub2Min, sub2Sec;
	
	private Button button;
	
	private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainChrono = (EditText) findViewById(R.id.editText1);
        subChrono1 = (EditText) findViewById(R.id.editText2);
        subChrono2 = (EditText) findViewById(R.id.editText3);
        button = (Button) findViewById(R.id.button1);

        
        mainChrono.addTextChangedListener(new TextWatcher () {

        	private CharSequence old = "00:00";
        	private int colons = 1;
        	
			@Override
			public void afterTextChanged(Editable s) {
				if(!old.toString().equals(s.toString())) {
					s.replace(0, s.length(), old);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				colons = 0;
				for(int i = 0; i < s.length(); i++) {
					if(s.charAt(i) == ':') {
						if(++colons >= 2) {
							colons = 1;
							return;
						}
					}
				}
				String[] tok = s.toString().split(":");
				if(s.length() == 0) {
					
				}
				else if(tok.length == 1) {
					try {
						mainMin = Integer.parseInt(tok[0]);
					} catch (NumberFormatException e) {
						return;
					}
					mainSec = 0;
				}
				else if(tok.length == 2) {
					try {
						mainMin = Integer.parseInt(tok[0]);
						mainSec = Integer.parseInt(tok[1]);
					} catch (NumberFormatException e) {
						return;
					}
					if(mainSec >= 60) {
						return;
					}
				}
				else {
					return;
				}
				old = s.toString();
			}
        	
        });
        
        
        button.setOnClickListener(new OnClickListener() {
			private boolean done = false;
			@Override
			public void onClick(View v) {
				if(done)
					return;
				timer = new Timer(true);
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if(mainSec == 0) {
									if(mainMin == 0) {
										timer.cancel();
										done = false;
									}
									else {
										mainMin--;
										mainSec = 59;
									}
								}
								else {
									mainSec--;
								}
								
								mainChrono.setText(String.format("%02d:%02d", mainMin, mainSec));
							}
						});
					}
				}, 1000, 1000);
				done = true;
			}
		});
        
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

}
