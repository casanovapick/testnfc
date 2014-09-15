package monstermedia.test.testnfc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	TextView mnfcdataview;
	NFCForegroundUtil nfcForegroundUtil = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mnfcdataview = (TextView) findViewById(R.id.serialtext);
		nfcForegroundUtil = new NFCForegroundUtil(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Tag tag = intent
				.getParcelableExtra(nfcForegroundUtil.getNfc().EXTRA_TAG);
		NfcA mifare = NfcA.get(tag);
		
		try {
			mifare.connect();
			 if(mifare.isConnected()){
				 Short s = mifare.getSak();
		            byte[] a = mifare.getAtqa();
		            String atqa = new String(a, Charset.forName("US-ASCII"));
		            byte[] id =tag.getId();
		          
		            mnfcdataview.setText("ID= "+convertToHex(id) +"\nSAK = "+s+"\nATQA = "+atqa);
		            mifare.close();}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		nfcForegroundUtil.enableForeground();

		if (!nfcForegroundUtil.getNfc().isEnabled()) {
			Toast.makeText(
					getApplicationContext(),
					"Please activate NFC and press Back to return to the application!",
					Toast.LENGTH_LONG).show();
			startActivity(new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		nfcForegroundUtil.disableForeground();
	}

	 private static String convertToHex(byte[] data) {
		    StringBuffer buf = new StringBuffer();
		    for (int i = 0; i < data.length; i++) {
		        int halfbyte = (data[i] >>> 4) & 0x0F;
		        int two_halfs = 0;
		        do {
		            if ((0 <= halfbyte) && (halfbyte <= 9))
		                buf.append((char) ('0' + halfbyte));
		            else
		                buf.append((char) ('a' + (halfbyte - 10)));
		            halfbyte = data[i] & 0x0F;
		        } while(two_halfs++ < 1);
		    }
		    return buf.toString();
		}
}
