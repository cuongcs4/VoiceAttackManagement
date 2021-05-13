package com.example.myapplication;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String mSound = "alert_air_horn";
    ListView listView;
    MediaPlayer mMediaPlayer = null;
    private TextToSpeech mTTS;
    private EditText mEditText;
    private SeekBar mSeekBarPitch;
    private SeekBar mSeekBarSpeed;
    //private Button mButtonSpeak;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);



        // Get ListView object from xml
//        listView = findViewById(R.id.listAudio);
//        setupSoundSelectionView();
//
//        mButtonSpeak = findViewById(R.id.button_speak);
//
//        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status == TextToSpeech.SUCCESS) {
//                    Locale loc = new Locale ("en", "US");
//                    int result = mTTS.setLanguage(loc);
//                    if (result == TextToSpeech.LANG_MISSING_DATA
//                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                        Log.e("TTS", "Language not supported");
//                    } else {
//                        mButtonSpeak.setEnabled(true);
//                    }
//                } else {
//                    Log.e("TTS", "Initialization failed");
//                }
//            }
//        }, "com.google.android.tts");
//        mEditText = findViewById(R.id.edit_text);
//        mSeekBarPitch = findViewById(R.id.seek_bar_pitch);
//        mSeekBarSpeed = findViewById(R.id.seek_bar_speed);
//        mButtonSpeak.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    speak();
//            }
//        });

    }
//    private void speak() {
//        String text = mEditText.getText().toString();
//        float pitch = (float) mSeekBarPitch.getProgress() / 50;
//        if (pitch < 0.1) pitch = 0.1f;
//        float speed = (float) mSeekBarSpeed.getProgress() / 50;
//        if (speed < 0.1) speed = 0.1f;
//
//        mTTS.setPitch(pitch);
//        mTTS.setSpeechRate(speed);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
//        } else {
//            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (mTTS != null) {
//            mTTS.stop();
//            mTTS.shutdown();
//        }
//        super.onDestroy();
//    }

    /**
     * Play sound file stored in res/raw/ directory
     */
    private void playRawSound(String rawName) {
        try {
            // Two ways to provide resource, either using its name or resource id.
            //
            // Name
            //    Syntax  :  android.resource://[package]/[res type]/[res name]
            //    Example : Uri.parse("android.resource://com.my.package/raw/sound1");
            //
            // Resource id
            //    Syntax  : android.resource://[package]/[resource_id]
            //    Example : Uri.parse("android.resource://com.my.package/" + R.raw.sound1);

            String RESOURCE_PATH = ContentResolver.SCHEME_ANDROID_RESOURCE + "://";

            String path;
            path = RESOURCE_PATH + getPackageName() + "/raw/" + rawName;

            Uri soundUri = Uri.parse(path);

            if (mMediaPlayer != null) {
                if (mMediaPlayer.isPlaying()) {
                    stopPlaying(mMediaPlayer);
                }
            }
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setVolume(1.0f, 1.0f);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Toast.makeText(getApplicationContext(),
                            "start playing sound", Toast.LENGTH_SHORT).show();
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(getApplicationContext(), String.format(Locale.US,
                            "Media error what=%d extra=%d", what, extra), Toast.LENGTH_LONG).show();
                    return false;
                }
            });

            //
            //  Different ways to load audio into player:
            //   1. Using path to resource by name or by id
            //   2. Using content provider to load audio and passing a file descriptor.
            if (true) {
                // 1. open audio using path to data inside package
                mMediaPlayer.setDataSource(getApplicationContext(), soundUri);
                mMediaPlayer.prepare();
            } else {
                // 2. Load using content provider, passing file descriptor.
                ContentResolver resolver = getContentResolver();
                AssetFileDescriptor afd = resolver.openAssetFileDescriptor(soundUri, "r");
                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                mMediaPlayer.prepareAsync();
            }

            // See setOnPreparedListener above
            //  mMediaPlayer.start();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * setup sound selection list view.
     */
    private void setupSoundSelectionView() {

        // Defined Array values to show in ListView
        String[] values = new String[]{
                "baidu_female_activation",
                "cereproc_adam_activation",
                "cereproc_adam_cerewave_activation",
                "cereproc_amy_activation",
                "cereproc_amy_cerewave_activation",
                "cereproc_andrew_activation",
                "cereproc_andrew_cerewave_activation",
                "cereproc_andy_activation",
                "cereproc_andy_cerewave_activation",
                "cereproc_caitlin_activation",
                "cereproc_caitlin_cerewave_activation",
                "cereproc_carolyn_activation",
                "cereproc_carolyn_cerewave_activation",
                "cereproc_claire_activation",
                "cereproc_demon_activation",
                "cereproc_dodo_activation",
                "cereproc_giles_activation",
                "cereproc_giles_cerewave_activation",
                "cereproc_hannah_activation",
                "cereproc_hannah_cerewave_activation",
                "cereproc_heather_activation",
                "cereproc_heather_cerewave_activation",
                "cereproc_isabella_activation",
                "cereproc_isabella_cerewave_activation",
                "cereproc_jack_activation",
                "cereproc_jack_cerewave_activation",
                "cereproc_jess_activation",
                "cereproc_jess_cerewave_activation",
                "cereproc_jordan_activation",
                "cereproc_jordan_cerewave_activation",
                "cereproc_katherine_activation",
                "cereproc_katherine_cerewave_activation",
                "cereproc_kirsty_activation",
                "cereproc_kirsty_cerewave_activation",
                "cereproc_lauren_activation",
                "cereproc_lauren_cerewave_activation",
                "cereproc_mairi_activation",
                "cereproc_mairi_cerewave_activation",
                "cereproc_megan_activation",
                "cereproc_megan_cerewave_activation",
                "cereproc_nathan_activation",
                "cereproc_nathan_cerewave_activation",
                "cereproc_nicole_activation",
                "cereproc_nicole_cerewave_activation",
                "cereproc_pixie_activation",
                "cereproc_sarah_activation",
                "cereproc_sarah_cerewave_activation",
                "cereproc_stuart_activation",
                "cereproc_stuart_cerewave_activation",
                "cereproc_sue_activation",
                "cereproc_sue_cerewave_activation",
                "cereproc_william_activation",
                "cereproc_william_cerewave_activation",
                "fromtts_alice_activation",
                "fromtts_daisy_activation",
                "fromtts_emma_activation",
                "fromtts_george_activation",
                "fromtts_harry_activation",
                "fromtts_jenna_activation",
                "fromtts_john_activation",
                "readspeaker_alice_activation",
                "readspeaker_ashley_activation",
                "readspeaker_beth_activation",
                "readspeaker_bridget_activation",
                "readspeaker_hugh_activation",
                "readspeaker_jack_activation",
                "readspeaker_james_activation",
                "readspeaker_julie_activation",
                "readspeaker_kate_activation",
                "readspeaker_mark_activation",
                "readspeaker_mason_activation",
                "readspeaker_mia_activation",
                "readspeaker_paul_activation",
                "readspeaker_sophie_activation",
                "selvyspeech_chris_embedded_activation",
                "selvyspeech_chris_mini_activation",
                "selvyspeech_chris_pc_activation",
                "selvyspeech_chris_server_activation",
                "selvyspeech_claire_embedded_activation",
                "selvyspeech_claire_mini_activation",
                "selvyspeech_claire_pc_activation",
                "selvyspeech_claire_server_activation",
                "selvyspeech_estelle_embedded_activation",
                "selvyspeech_estelle_pc_activation",
                "selvyspeech_estelle_server_activation",
                "selvyspeech_judy_embedded_activation",
                "selvyspeech_judy_mini_activation",
                "selvyspeech_judy_pc_activation",
                "selvyspeech_judy_server_activation",
                "selvyspeech_richard_embedded_activation",
                "selvyspeech_richard_mini_activation",
                "selvyspeech_richard_pc_activation",
                "selvyspeech_richard_server_activation",
                "selvyspeech_sarah_embedded_activation",
                "selvyspeech_sarah_mini_activation",
                "selvyspeech_sarah_pc_activation",
                "selvyspeech_sarah_sever_activation",
                "selvyspeech_veronica_pc_activation",
                "selvyspeech_veronica_server_activation",
                "selvyspeech_athena_activation",
                "sestek_craig_activation",
                "sestek_daniel_neural_activation",
                "sestek_delal_activation",
                "sestek_female_activation",
                "sestek_melissa_activation",
                "sestek_melissa_neural_activation",
                "sestek_oliver_activation",
                "sestek_rae_activation",
                "vocalware_alan_activation",
                "vocalware_allison_activation",
                "vocalware_ashley_activation",
                "vocalware_beth_activation",
                "vocalware_bridget_activation",
                "vocalware_catherine_activation",
                "vocalware_dave_activation",
                "vocalware_elizabeth_activation",
                "vocalware_grace_activation",
                "vocalware_hugh_activation",
                "vocalware_james_activation",
                "vocalware_julie_activation",
                "vocalware_kate_activation",
                "vocalware_lakshmi_activation",
                "vocalware_male_activation",
                "vocalware_matilda_activation",
                "vocalware_olivia_activation",
                "vocalware_paul_activation",
                "vocalware_prashant_activation",
                "vocalware_simon_activation",
                "vocalware_steven_activation",
                "vocalware_susan_activation"


        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1,
                // android.R.layout.simple_selectable_list_item,
                values);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSound = (String) listView.getItemAtPosition(position);
                playRawSound(mSound);
            }
        });
    }

    public void stopPlaying(MediaPlayer mp) {
        mp.stop();
        mp.release();
    }

    public void listRaw() {
        Field[] fields = R.raw.class.getFields();
        for (int count = 0; count < fields.length; count++) {
            Log.i("Raw Asset: ", fields[count].getName());
        }
    }

}