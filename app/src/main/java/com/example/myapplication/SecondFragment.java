package com.example.myapplication;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int position;
    ListView listView;
    Button buttonBruteforce;
    MediaPlayer mMediaPlayer = null;
    String mSound = "alert_air_horn";

    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Second.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        listView = view.findViewById(R.id.listAudio);

        buttonBruteforce = view.findViewById(R.id.playAll);
        setupSoundSelectionView();
        return view;
    }


    /**
     * Play sound file stored in res/raw/ directory
     */
    private void playRawSound(String rawName) {
        // Defined Array values to show in ListView

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
            path = RESOURCE_PATH + getActivity().getPackageName() + "/raw/" + rawName;

            Uri soundUri = Uri.parse(path);
            Log.d("mytag", path);

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
                    Toast.makeText(getActivity().getApplicationContext(),
                            "start playing sound", Toast.LENGTH_SHORT).show();
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(getActivity().getApplicationContext(), String.format(Locale.US,
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
                mMediaPlayer.setDataSource(getActivity().getApplicationContext(), soundUri);
                mMediaPlayer.prepare();
            }   else {
                // 2. Load using content provider, passing file descriptor.
                ContentResolver resolver = getActivity().getContentResolver();
                AssetFileDescriptor afd = resolver.openAssetFileDescriptor(soundUri, "r");
                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                mMediaPlayer.prepareAsync();
            }


            // See setOnPreparedListener above
            //  mMediaPlayer.start();

        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void playAllSound(String rawName[], int position) {
        // Defined Array values to show in ListView

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
            path = RESOURCE_PATH + getActivity().getPackageName() + "/raw/" + rawName[position];

            Uri soundUri = Uri.parse(path);
            Log.d("mytag", path);

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
                    Toast.makeText(getActivity().getApplicationContext(),
                            "start playing sound", Toast.LENGTH_SHORT).show();
                    mMediaPlayer.start();
                }
            });


            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    int currentPosition = position;
                    if (currentPosition <= rawName.length) {
                        currentPosition++;
                    } else {
                        currentPosition = 0;
                    }
                    playAllSound(rawName, currentPosition);
                }
            });

            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(getActivity().getApplicationContext(), String.format(Locale.US,
                            "Media error what=%d extra=%d", what, extra), Toast.LENGTH_LONG).show();
                    return false;
                }
            });
            //  Different ways to load audio into player:
            //   1. Using path to resource by name or by id
            //   2. Using content provider to load audio and passing a file descriptor.
            if (true) {
                // 1. open audio using path to data inside package
                mMediaPlayer.setDataSource(getActivity().getApplicationContext(), soundUri);
                mMediaPlayer.prepare();
            }   else {
                // 2. Load using content provider, passing file descriptor.
                ContentResolver resolver = getActivity().getContentResolver();
                AssetFileDescriptor afd = resolver.openAssetFileDescriptor(soundUri, "r");
                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                mMediaPlayer.prepareAsync();
            }

        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
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

        buttonBruteforce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAllSound(values, 0);
            }
        });
    }

    public void Release() {
// Cleanup
        this.mMediaPlayer.release();
        this.mMediaPlayer = null;
    }

    public void stopPlaying(MediaPlayer mp) {
        mp.stop();
        mp.release();
    }
}