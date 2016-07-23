package www.example.com.nfctest;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import www.example.com.nfctest.NFC.NdefMessageParser;
import www.example.com.nfctest.NFC.ParsedRecord;
import www.example.com.nfctest.NFC.TextRecord;

public class MainActivity extends AppCompatActivity {

	Button r, w;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		r = (Button)findViewById(R.id.readActivityButton);
		w = (Button)findViewById(R.id.writeActivityButton);
		
		r.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getBaseContext(), ReadActivity.class);
				startActivity(intent);
			}
		});
		
		w.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), WriteActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			Log.d("test", "start from nfc");

			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs == null) {
				return;
			}

			// 참고! rawMsgs.length : 스캔한 태그 개수
//			Toast.makeText(getApplicationContext(), "스캔 성공!", Toast.LENGTH_LONG).show();

			NdefMessage[] msgs;
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
					showTag(msgs[i]); // showTag 메소드 호출
//					Toast.makeText(MainActivity.this, "message : " + msgs[i], Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private int showTag(NdefMessage mMessage) {
		List<ParsedRecord> records = NdefMessageParser.parse(mMessage);
		final int size = records.size();
		for (int i = 0; i < size; i++) {
			ParsedRecord record = records.get(i);

			int recordType = record.getType();
//			String recordStr = ""; // NFC 태그로부터 읽어들인 텍스트 값
//			if (recordType == ParsedRecord.TYPE_TEXT) {
//				recordStr = "TEXT : " + ((TextRecord) record).getText();
//			} else if (recordType == ParsedRecord.TYPE_URI) {
//				recordStr = "URI : " + ((UriRecord) record).getUri().toString();
//			}
			String recordStr = ((TextRecord) record).getText();

			if (recordStr.equals("cosmocar")) {
				Toast.makeText(MainActivity.this, recordStr, Toast.LENGTH_SHORT).show();
			} else {
				finish();
			}

//			readResult.append(recordStr + "\n"); // 읽어들인 텍스트 값을 TextView에 덧붙임
			Toast.makeText(MainActivity.this, recordStr, Toast.LENGTH_SHORT).show();
		}

		return size;
	}
}
