package com.levirs.example.simpletts;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextToSpeech mTextToSpeech;
    private Button mButton;
    private EditText mEditText;
    static  String voice1;

    private void showChangeLanguageDialog() {
        final String[] listitems = {"English-INDIA", "English-US", "English-UK", "हिन्दी", "മലയാളം", "தமிழ்", "తెలుగు",
                "ಕನ್ನಡ", "español", "French", "मराठी"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Choose Language..");
        mBuilder.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    voice1 = "en-IN";
                    setlocale ("en-IN");
                    recreate();
                }
                else if (i == 1) {
                    voice1 = "en-US";
                    setlocale( "en-US");
                    recreate();
                }else if (i == 2) {
                    voice1 = "en-GB";
                    setlocale( "en-GB");
                    recreate();
                }else if (i == 3) {
                    voice1 = "hi-IN";
                    setlocale( "hi-IN");
                    recreate();
                }else if (i == 4) {
                    voice1 = "ml-IN";
                    setlocale( "ml-IN");
                    recreate();
                }else if (i == 5) {
                    voice1 = "ta-IN";
                    setlocale( "ta-IN");
                    recreate();
                }else if (i == 6) {
                    voice1 = "te-IN";
                    setlocale( "te-IN");
                    recreate();
                }
                else if (i == 7) {
                    voice1 = "kn-IN";
                    setlocale( "kn-IN");
                    recreate();
                }
                else if (i == 8) {
                    voice1 = "es-ES";
                    setlocale( "es-ES");
                    recreate();
                }else if (i == 9) {
                    voice1 = "fr";
                    setlocale( "fr");
                    recreate();
                }else if (i == 10) {
                    voice1 = "mr-IN";
                    setlocale( "mr-IN");
                    recreate();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setlocale(String lang) {
        Locale locale =new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration =new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
    }
    public void loadLocale(){
        SharedPreferences prefs = (SharedPreferences) getSharedPreferences("Settings",Activity.MODE_PRIVATE).edit();
        String language =prefs.getString("My_Lang","");
        setlocale(language);
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        loadLocale();
        setContentView(R.layout.activity_main);
        checkPermission();
        mButton = findViewById(R.id.speak);
        mEditText = findViewById(R.id.textToSpeak);
        Button language = findViewById(R.id.language);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  showChangeLanguageDialog();
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakNow();
            }
        });

        mTextToSpeech = new TextToSpeech(this, this);
        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);


        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH, Uri.parse(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS));
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                voice1);

//        Intent msp =  new Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
//        sendOrderedBroadcast(
//                mSpeechRecognizerIntent, null, new LanguageDetailsChecker(), null, Activity.RESULT_OK, null, null);
//        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
//                "ta-IN");
//        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
//                "en-IN");
//        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
//                "hi-IN");


        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null)
                    mEditText.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        findViewById(R.id.mic).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        mEditText.setHint("You will see input here");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        mEditText.setText("");
                        mEditText.setHint("Listening...");
                        break;
                }
                return false;
            }
        });
    }





    @Override
    protected void onDestroy() {
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }

        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
//            mTextToSpeech.setSpeechRate(0.5F);
//            mTextToSpeech.setPitch(2F);
            //setting language to Indonesia
            int result = mTextToSpeech.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_NOT_SUPPORTED ||
                    result == TextToSpeech.LANG_MISSING_DATA) {
                Log.e("SimpleTTS", "Language not supported!");
                mButton.setEnabled(false);
            } else {
                mButton.setEnabled(true);
                speakNow();
            }
        } else {
            Log.e("SimpleTTS", "Initializing failed");
        }
    }

    private void speakNow() {
        String str = mEditText.getText().toString();
        if (!str.isEmpty()) {
            mTextToSpeech.speak(str, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


}
