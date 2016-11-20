package com.landtanin.hitchhacker.HitchHiker;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.landtanin.hitchhacker.R;

import java.io.File;

public class RecordVoice2 extends Activity implements View.OnClickListener, OnCompleteListener{

    TextView statusTextView, amplitudeTextView;
    Button startRecording, stopRecording, playRecording, finishButton;
    MediaRecorder recorder;
    MediaPlayer player;
    File audioFile;

    RecordAmplitude recordAmplitude;
    boolean isRecording = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_voice2);

        statusTextView = (TextView) this.findViewById(R.id.StatusTextView);
        statusTextView.setText("Ready");

        amplitudeTextView = (TextView) this.findViewById(R.id.AmplitudeTextView);
        amplitudeTextView.setText("0");

        stopRecording = (Button) this.findViewById(R.id.StopRecording);
        startRecording = (Button) this.findViewById(R.id.StartRecording);
        playRecording = (Button) this.findViewById(R.id.PlayRecording);
        finishButton = (Button) this.findViewById(R.id.FinishButton);

        startRecording.setOnClickListener(this);
        stopRecording.setOnClickListener(this);
        playRecording.setOnClickListener(this);
        finishButton.setOnClickListener(this);

        recorder = new MediaRecorder();
        recorder.reset();

        stopRecording.setEnabled(false);
        playRecording.setEnabled(false);
    }

    public void onClick(View v) {
        recorder.reset();
        if (v == finishButton) {
            finish();
        } else if (v == stopRecording) {
            isRecording = false;
            recordAmplitude.cancel(true);

            recorder.stop();
            recorder.release();

            player = new MediaPlayer();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Log.d("RecordVoice2", "ItWORKSS");
                }
            });
            try {
                player.setDataSource(audioFile.getAbsolutePath());
                player.prepare();
            } catch (Exception e) {
                throw new RuntimeException("IOException in MediaPlayer.prepare", e);
            }
            statusTextView.setText("Ready to Play");
            playRecording.setEnabled(true);
            stopRecording.setEnabled(false);
            startRecording.setEnabled(true);

        } else if (v == startRecording) {
            recorder = new MediaRecorder(); // add

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/dsf.3gp");
            Log.d(Environment.getExternalStorageDirectory().getAbsolutePath(), "Yea!");
            try {
//                audioFile = File.createTempFile("recordings123", ".3gp", path);
                Log.d(path.getAbsolutePath(), "Works!");
                recorder.setOutputFile(path.getAbsolutePath());
                recorder.prepare();
            } catch (Exception e) {
                throw new RuntimeException(
                        "IOException on MediaRecorder.prepare", e);
            }
            recorder.start();
            isRecording = true;
            recordAmplitude = new RecordAmplitude();
            recordAmplitude.execute();
            statusTextView.setText("Recording");
            playRecording.setEnabled(false);
            stopRecording.setEnabled(true);
            startRecording.setEnabled(false);
        } else if (v == playRecording) {
            player.start();
            statusTextView.setText("Playing");
            playRecording.setEnabled(false);
            stopRecording.setEnabled(false);
            startRecording.setEnabled(false);
        }
    }

    public void onCompletion(MediaPlayer mp) {
        playRecording.setEnabled(true);
        stopRecording.setEnabled(false);
        startRecording.setEnabled(true);
        statusTextView.setText("Ready");
    }

    @Override
    public void onComplete(@NonNull Task task) {

    }

    private class RecordAmplitude extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            while (isRecording) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(recorder.getMaxAmplitude());
            }
            return null;
        }
        protected void onProgressUpdate(Integer... progress) {
            amplitudeTextView.setText(progress[0].toString());
        }
    }
}
