package com.example.englishvocabprep1v3;
import android.app.*;import android.os.*;import android.Manifest;import android.content.*;import android.content.pm.PackageManager;import android.speech.*;import android.speech.tts.TextToSpeech;import android.webkit.*;import java.util.*;
public class MainActivity extends Activity{
 WebView w; TextToSpeech t; SpeechRecognizer r;
 public void onCreate(Bundle b){super.onCreate(b);w=new WebView(this);w.getSettings().setJavaScriptEnabled(true);w.getSettings().setDomStorageEnabled(true);w.setWebViewClient(new WebViewClient());
 w.addJavascriptInterface(new Object(){@JavascriptInterface public void speak(String x){if(t!=null)t.speak(x,TextToSpeech.QUEUE_FLUSH,null,"v3");}},"AndroidTTS");
 w.addJavascriptInterface(new Object(){@JavascriptInterface public void start(){runOnUiThread(()->recognize());}},"AndroidSpeech");
 t=new TextToSpeech(this,s->{if(s==TextToSpeech.SUCCESS)t.setLanguage(Locale.US);});w.loadUrl("file:///android_asset/index.html");setContentView(w);}
 void recognize(){if(Build.VERSION.SDK_INT>=23&&checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED){requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},7);return;}
 if(!SpeechRecognizer.isRecognitionAvailable(this)){w.evaluateJavascript("androidSpeechResult('')",null);return;} if(r!=null)r.destroy();r=SpeechRecognizer.createSpeechRecognizer(this);
 r.setRecognitionListener(new RecognitionListener(){public void onReadyForSpeech(Bundle b){}public void onBeginningOfSpeech(){}public void onRmsChanged(float v){}public void onBufferReceived(byte[] b){}public void onEndOfSpeech(){}public void onError(int e){w.evaluateJavascript("androidSpeechResult('')",null);}public void onResults(Bundle b){ArrayList<String>a=b.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);String x=a==null||a.isEmpty()?"":a.get(0);x=x.replace("\\","\\\\").replace("'","\\'");w.evaluateJavascript("androidSpeechResult('"+x+"')",null);}public void onPartialResults(Bundle b){}public void onEvent(int a,Bundle b){}});
 Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.US.toLanguageTag());i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);r.startListening(i);}
 public void onRequestPermissionsResult(int a,String[]p,int[]g){super.onRequestPermissionsResult(a,p,g);if(a==7&&g.length>0&&g[0]==PackageManager.PERMISSION_GRANTED)recognize();}
 protected void onDestroy(){if(r!=null)r.destroy();if(t!=null)t.shutdown();super.onDestroy();}
}