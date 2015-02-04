package com.emilianstankov.drumric;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
	
	private static final int MAX_SAMPLE_DURATION = 5000;
	private MediaRecorder rec;
	private boolean recording;
	private SoundPool sp;
	private AssetManager am;
	private ArrayList<Object> sounds;
	private ArrayList<AssetFileDescriptor> defaultSounds;
	private ArrayList<Button> recButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recording = false;
    	am = this.getAssets();
        sp = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        Button rec1 = (Button)findViewById(R.id.rec1);
        Button rec2 = (Button)findViewById(R.id.rec2);
        Button rec3 = (Button)findViewById(R.id.rec3);
        Button rec4 = (Button)findViewById(R.id.rec4);
        Button rec5 = (Button)findViewById(R.id.rec5);
        Button rec6 = (Button)findViewById(R.id.rec6);
        Button rec7 = (Button)findViewById(R.id.rec7);
        Button rec8 = (Button)findViewById(R.id.rec8);
        Button pad1 = (Button)findViewById(R.id.pad1);
        Button pad2 = (Button)findViewById(R.id.pad2);
        Button pad3 = (Button)findViewById(R.id.pad3);
        Button pad4 = (Button)findViewById(R.id.pad4);
        Button pad5 = (Button)findViewById(R.id.pad5);
        Button pad6 = (Button)findViewById(R.id.pad6);
        Button pad7 = (Button)findViewById(R.id.pad7);
        Button pad8 = (Button)findViewById(R.id.pad8);
        Button reset = (Button)findViewById(R.id.reset);
        AssetFileDescriptor defaultCrash = null;
        AssetFileDescriptor defaultTom = null;
        AssetFileDescriptor defaultRim = null;
        AssetFileDescriptor defaultClap = null;
        AssetFileDescriptor defaultKick = null;
        AssetFileDescriptor defaultSnare = null;
        AssetFileDescriptor defaultOpenHiHat = null;
        AssetFileDescriptor defaultClosedHiHat = null;
        recButtons = new ArrayList<Button>(Arrays.asList(rec1, rec2, rec3, rec4, rec5, rec6, rec7, rec8));
        ArrayList<Button> pads = new ArrayList<Button>(Arrays.asList(pad1, pad2, pad3, pad4, pad5, pad6, pad7, pad8));
		try {
			defaultCrash = am.openFd("sounds/crash.wav");
			defaultTom = am.openFd("sounds/tom.wav");
	        defaultRim = am.openFd("sounds/rim.wav");
	        defaultClap = am.openFd("sounds/clap.wav");
	        defaultKick = am.openFd("sounds/kick.wav");
	        defaultSnare = am.openFd("sounds/snare.wav");
	        defaultOpenHiHat = am.openFd("sounds/hhopen.wav");
	        defaultClosedHiHat = am.openFd("sounds/hhclosed.wav");
		} catch (IOException e) {
			e.printStackTrace();
		}
        defaultSounds = new ArrayList<AssetFileDescriptor>(Arrays.asList(
        		defaultCrash, defaultTom, defaultRim, defaultClap, defaultKick, defaultSnare, defaultOpenHiHat, defaultClosedHiHat));
        sounds = new ArrayList<Object>(defaultSounds);
        fillSoundPool();
        initializeResetButton(reset);
        initializeRecordButtons();
        initializePads(pads);
    }


	private void initializeResetButton(Button reset) {
		reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteSamples();
				sounds = new ArrayList<Object>(defaultSounds);
				sp = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
				for(AssetFileDescriptor sound: defaultSounds) {
		        	sp.load(sound, 1);
				}
			}
		});
	}


	private void initializePads(ArrayList<Button> pads) {
		for(Button btn: pads){
        	final int index = pads.indexOf(btn) + 1;
	        btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					sp.play(index, 1.0f, 1.0f, 0, 0, 1.0f);
				}
			});
        }
	}


	private void initializeRecordButtons() {
		for(final Button btn: recButtons) {
	        btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int normalizedIndex = recButtons.indexOf(btn) + 1;
					if(!recording) {
						startRecording(btn, getSamplePath(normalizedIndex));
					} else {
						stopRecording(btn, getSamplePath(normalizedIndex));
						sounds.set(recButtons.indexOf(btn), getSamplePath(normalizedIndex));
						sp = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
						fillSoundPool();
					}
				}
			});
        }
	}


	private void fillSoundPool() {
		for(Object sound: sounds) {
        	if(defaultSounds.contains(sound))
        		sp.load((AssetFileDescriptor)sound, 1);
        	else
        		sp.load((String)sound, 1);
		}
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void startRecording(final Button btn, final String filename) {
    	btn.setBackgroundResource(R.drawable.recording);
        rec = new MediaRecorder();
        rec.setAudioSource(MediaRecorder.AudioSource.MIC);
        rec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        rec.setMaxDuration(MAX_SAMPLE_DURATION);
        rec.setOutputFile(filename);
        rec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            rec.prepare();
        } catch (IOException e) {
            
        }

        rec.start();
        recording = true;
        rec.setOnInfoListener(new OnInfoListener() { 
        	@Override
        	public void onInfo(MediaRecorder mr, int what, int extra) {
        		if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) { 
        			stopRecording(btn, filename);
        			sp = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        			sounds.set(recButtons.indexOf(btn), getSamplePath(recButtons.indexOf(btn) + 1));
					fillSoundPool();
        		} 
        	} 
        });
    }
    
    private void stopRecording(Button btn, String filename) {
        rec.stop();
        rec.release();
        rec = null;
        sp.load(filename, 1);
        recording = false;
        btn.setBackgroundResource(R.drawable.settings);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private String getSamplePath(int normalizedIndex) {
		return Environment.getExternalStorageDirectory().getAbsolutePath()+"/drumric_record"+normalizedIndex+".mp4";
	}
    
    @Override
    protected void onStop() {
    	super.onStop();
    	deleteSamples();
    }


	private void deleteSamples() {
		for(int i = 1; i <= 8; i++) {
    		File file = new File(getSamplePath(i));
    		if (file.exists()) {
                file.delete();
            }   
    	}
	}
}
