package net.matkam.projectmaxs.transport.matrix.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.matkam.projectmaxs.transport.matrix.R;
import net.matkam.projectmaxs.transport.matrix.Settings;

import org.projectmaxs.shared.global.jul.JULHandler;
import org.projectmaxs.shared.global.util.Log;
import org.projectmaxs.shared.global.util.SpannedUtil;

public class InfoAndSettings extends Activity {
	static {
		JULHandler.setAsDefaultUncaughtExceptionHandler();
	}

	private static final Log LOG = Log.getLog();

	private Settings mSettings;
//	private PingServerButtonHandler mPingServerButtonHandler;

	private LinearLayout mMasterAddresses;
	private EditText mFirstMasterAddress;
	private EditText mJID;
	private EditTextWatcher mJidEditTextWatcher;
	private String mLastJidText;
	private EditText mPassword;
	private EditTextWatcher mPasswordEditTextWachter;
	private Button mAdvancedSettings;

	public void openAdvancedSettings(View view) {
		startActivity(new Intent(this, AdvancedSettings.class));
	}

	public void showAbout(View view) {
		final SpannableStringBuilder sb = new SpannableStringBuilder();
		final String appName = getResources().getString(R.string.app_name);
		sb.append(Html.fromHtml("<h1>" + appName + "</h1>"));
		sb.append(getResources().getString(R.string.version)).append('\n');
		sb.append(getResources().getString(R.string.copyright)).append(" (").append(SpannedUtil
				.createAuthorsLink("transport-xmpp", getResources().getString(R.string.authors)))
				.append(")\n");
		sb.append('\n');
		sb.append(appName).append(' ').append(getResources().getText(R.string.gplv3)).append('\n');
		sb.append('\n');
		sb.append(Html.fromHtml(
// @formatter:off
"<h1>Open Source</h1>" +
"&#8226; <a href=\"http://www.igniterealtime.org/projects/smack\">Smack</a><br>" +
"&#8226; <a	href=\"https://github.com/ge0rg/MemorizingTrustManager\">MemorizingTrustManager</a><br>" +
"&#8226; <a	href=\"https://github.com/rtreffer/minidns\">MiniDNS</a><br>" +
"Copyright © 2011-2016 Florian Schmaus<br>" +
"Copyright © 2013-2014 Georg Lukas<br>" +
"Copyright © 2014 Lars Noschinski<br>" +
"Copyright © 2014 Vyacheslav Blinov<br>" +
"Copyright © 2014 Andriy Tsykholyas<br>" +
"Copyright © 2009-2013 Robin Collier<br>" +
"Copyright © 2009 Jonas Ådahl<br>" +
"Copyright © 2003-2010 Jive Software<br>" +
"Copyright © 2001-2004 Apache Software Foundation<br>" +
"Apache License, Version 2.0<br>" +
"<h2>MemorizingTrustManager</h2>" +
"<a href=\"https://github.com/ge0rg/MemorizingTrustManager\">https://github.com/ge0rg/MemorizingTrustManager</a><br>" +
"<br>" +
"Copyright © 2010-2104 Georg Lukas<br>" +
"MIT License<br>" +
"<h2>MiniDNS</h2>" +
"<a href=\"https://github.com/rtreffer/minidns\">https://github.com/rtreffer/minidns<a/><br>" +
"<br>" +
"Apache License, Version 2.0<br>" +
"<h1>License Links</h1>" +
"&#8226; <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">Apache License, Version 2.0</a><br>" +
"&#8226; <a href=\"http://opensource.org/licenses/MIT\">MIT License</a>"
// @formatter:on
		));
		final TextView textView = new TextView(this);
		textView.setText(sb);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		// Sadly we can't make this text view also selectable
		// http://stackoverflow.com/questions/14862750
		// @formatter:off
		final AlertDialog alertDialog = new AlertDialog.Builder(this)
			.setPositiveButton(getResources().getString(R.string.close), null)
			.setView(textView)
			.create();
		// @formatter:on
		alertDialog.show();
	}

	public void registerAccount(View view) {
//		final EntityBareJid jid = mSettings.getJid();
//		final String password = mSettings.getPassword();
//		if (jid == null) {
//			Toast.makeText(this, "Please enter a valid bare JID", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (password.isEmpty()) {
//			Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		(new Thread() {
//
//			@Override
//			public void run() {
//				if (!ConnectivityManagerUtil.hasDataConnection(InfoAndSettings.this)) {
//					showToast("Data connection not available", Toast.LENGTH_SHORT);
//					return;
//				}
//
//				try {
//					final Localpart username = jid.getLocalpart();
//					final AbstractXMPPConnection connection = new XMPPTCPConnection(
//							mSettings.getConnectionConfiguration(InfoAndSettings.this));
//					showToast("Connecting to server", Toast.LENGTH_SHORT);
//					connection.connect();
//					AccountManager accountManager = AccountManager.getInstance(connection);
//					showToast("Connected, trying to create account", Toast.LENGTH_SHORT);
//					accountManager.createAccount(username, password);
//					connection.disconnect();
//				} catch (Exception e) {
//					LOG.i("registerAccount", e);
//					showToast("Error creating account: " + e, Toast.LENGTH_LONG);
//					return;
//				}
//				showToast("Account created", Toast.LENGTH_SHORT);
//			}
//
//			private final void showToast(final String text, final int duration) {
//				InfoAndSettings.this.runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						Toast.makeText(InfoAndSettings.this, text, duration).show();
//					}
//				});
//			}
//
//		}).start();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infoandsettings);

		mSettings = Settings.getInstance(this);

		// Views
		mMasterAddresses = (LinearLayout) findViewById(R.id.masterAddresses);
		mFirstMasterAddress = (EditText) findViewById(R.id.firstMasterAddress);
		mJID = (EditText) findViewById(R.id.jid);
		mPassword = (EditText) findViewById(R.id.password);
		mAdvancedSettings = (Button) findViewById(R.id.advancedSettings);

		// Avoid the virtual keyboard by focusing a button
		mAdvancedSettings.requestFocus();

		new MasterAddressCallbacks(mFirstMasterAddress);
//		mJidEditTextWatcher = new EditTextWatcher(mJID) {
//			@Override
//			public void lostFocusOrDone(View v) {
//				String text = mJID.getText().toString();
//				EntityBareJid jid;
//				try {
//					jid = JidUtil.validateEntityBareJid(text);
//				} catch (NotAEntityBareJidStringException | XmppStringprepException e) {
//					Toast.makeText(InfoAndSettings.this,
//							"'" + text + "' is not a valid bare JID: " + e.getLocalizedMessage(),
//							Toast.LENGTH_LONG).show();
//					mJID.setText(mLastJidText);
//					return;
//				}
//				mSettings.setJid(jid);
//			}
//		};
		mPasswordEditTextWachter = new EditTextWatcher(mPassword) {
			@Override
			public void lostFocusOrDone(View v) {
				mSettings.setPassword(mPassword.getText().toString());
			}
		};

		// initialize the master jid linear layout if there are already some
		// configured
//		Set<EntityBareJid> masterJids = mSettings.getMasterJids();
//		if (!masterJids.isEmpty()) {
//			Iterator<EntityBareJid> it = masterJids.iterator();
//			mFirstMasterAddress.setText(it.next());
//			while (it.hasNext()) {
//				EditText et = addEmptyMasterJidEditText();
//				et.setText(it.next());
//			}
//			addEmptyMasterJidEditText();
//		}
//		if (mSettings.getJid() != null) mJID.setText(mSettings.getJid());
//		if (!mSettings.getPassword().equals("")) mPassword.setText(mSettings.getPassword());
//
//		mPingServerButtonHandler = new PingServerButtonHandler(this);
//
//		AndroidDozeUtil.requestWhitelistIfNecessary(this, mSettings.getSharedPreferences(),
//				R.string.DozeAskForWhitelist, R.string.DozeDoNotWhitelist, R.string.AskAgain,
//				R.string.DozeWhitelist);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mJidEditTextWatcher.onPause();
		mPasswordEditTextWachter.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Removing this handler yields actually a problem: If onDestroy() is called shortly after
		// onCreate() and the MatrixService was not yet initialized/constructed, then this call may
		// lead to network IO and an NetworkOnMainThreadException. But a fix wouldn't be trivial
		// and this is a corner case.
//		MatrixService.getInstance(this).removeListener(mPingServerButtonHandler);
	}

	private final EditText addEmptyMasterJidEditText() {
		EditText newEditText = new EditText(this);
//		newEditText.setHint(getString(R.string.hint_jid));
		newEditText.setInputType(
				InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		new MasterAddressCallbacks(newEditText);
		mMasterAddresses.addView(newEditText);
		return newEditText;
	}

	private final class MasterAddressCallbacks extends EditTextWatcher {

		MasterAddressCallbacks(EditText editText) {
			super(editText);
		}

		public void lostFocusOrDone(View v) {
//			String text = mEditText.getText().toString();
//			EntityBareJid beforeJid = null;
//			if (!mBeforeText.equals("")) {
//				try {
//					beforeJid = JidCreate.entityBareFrom(mBeforeText);
//				} catch (Exception e) {
//					LOG.d("Could not transform '" + mBeforeText + "' to bare JID", e);
//				}
//			}
//			if (text.equals("") && beforeJid != null) {
//				int childCount = mMasterAddresses.getChildCount();
//				mSettings.removeMasterJid(beforeJid);
//				mMasterAddresses.removeView(mEditText);
//				if (childCount <= 2) {
//					mMasterAddresses.addView(mEditText, 2);
//					mEditText.setHint(InfoAndSettings.this.getString(R.string.hint_jid));
//				}
//				return;
//			}
//
//			if (text.equals("")) return;
//
//			// an attempt to change an empty master jid to an invalid jid. abort
//			// here and leave the original value untouched
//			EntityBareJid newJid;
//			try {
//				newJid = JidUtil.validateEntityBareJid(text);
//			} catch (NotAEntityBareJidStringException | XmppStringprepException e) {
//				Toast.makeText(InfoAndSettings.this,
//						"This is not a valid bare JID: " + e.getLocalizedMessage(),
//						Toast.LENGTH_LONG).show();
//				mEditText.setText(mBeforeText);
//				return;
//			}
//
//			// an empty master jid was change to a valid jid
//			if (beforeJid == null) {
//				mSettings.addMasterJid(newJid);
//				addEmptyMasterJidEditText();
//			}
//			// a valid master jid was changed with another valid value
//			else if (!mBeforeText.equals(text)) {
//				mSettings.removeMasterJid(beforeJid);
//				mSettings.addMasterJid(newJid);
//			}
			return;
		}
	}

//	class PingServerButtonHandler extends StateChangeListener implements OnClickListener {
//
//		private final Button mPingServerButton;
//
//		private volatile PingManager mPingManager;
//
//		public PingServerButtonHandler(Activity activity) {
//			mPingServerButton = (Button) activity.findViewById(R.id.pingServer);
//			mPingServerButton.setOnClickListener(this);
//
//			// Ugly workaround for NetworkOnMainThreadException, because MatrixService's constructor
//			// call leads to a call to Socks5Proxy.getSocks5Proxy, which does
//			// InetAddress.getLocalHost().getHostAddress() which finally leads to some network IO.
//			new AsyncTask<Activity, Void, MatrixService>() {
//				@Override
//				protected MatrixService doInBackground(Activity... activities) {
//					return MatrixService.getInstance(activities[0]);
//				}
//
//				@Override
//				protected void onPostExecute(MatrixService xmppService) {
//					if (xmppService.isConnected()) {
//						PingServerButtonHandler.this.mPingManager = PingManager
//								.getInstanceFor(xmppService.getConnection());
//						mPingServerButton.setEnabled(true);
//					}
//					xmppService.addListener(PingServerButtonHandler.this);
//				}
//			}.execute(activity);
//		}
//
//		/**
//		 * This onClick() method can only be called when we are connected, because otherwise the
//		 * button will be disabled. Therefore there is no need to check mPingManager for null.
//		 */
//		@Override
//		public synchronized void onClick(View v) {
//			Toast.makeText(InfoAndSettings.this, "Sending ping to server", Toast.LENGTH_SHORT)
//					.show();
//			new AsyncTask<PingManager, Void, Long>() {
//				@Override
//				protected Long doInBackground(PingManager... pingManagers) {
//					XMPPBundleAndDefer.disableBundleAndDefer();
//					try {
//						long start = System.currentTimeMillis();
//						boolean res = pingManagers[0].pingMyServer();
//						if (res) {
//							long stop = System.currentTimeMillis();
//							return stop - start;
//						}
//					} catch (InterruptedException | SmackException e) {
//						LOG.w("pingMyServer", e);
//					} catch (RuntimeException e) {
//						LOG.e("pingMyServer: RuntimeException", e);
//					} finally {
//						XMPPBundleAndDefer.enableBundleAndDefer();
//					}
//					return (long) -1;
//				}
//
//				@Override
//				protected void onPostExecute(Long result) {
//					String text;
//					if (result > 0) {
//						text = "Pong received within "
//								+ SharedStringUtil.humanReadableMilliseconds(result)
//								+ ". Ping successful. ☺";
//					} else {
//						text = "Pong timeout. Ping failed!";
//					}
//					Toast.makeText(InfoAndSettings.this, text, Toast.LENGTH_LONG).show();
//				}
//			}.execute(mPingManager);
//		}
//
//		@Override
//		public synchronized void connected(XMPPConnection connection) {
//			mPingManager = PingManager.getInstanceFor(connection);
//			setPingButtonEnabled(true);
//		}
//
//		@Override
//		public synchronized void disconnected(XMPPConnection connection) {
//			mPingManager = null;
//			setPingButtonEnabled(false);
//		}
//
//		@Override
//		public synchronized void disconnected(String reason) {
//			mPingManager = null;
//			setPingButtonEnabled(false);
//		}
//
//		private final void setPingButtonEnabled(final boolean enabled) {
//			InfoAndSettings.this.runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					mPingServerButton.setEnabled(enabled);
//				}
//			});
//		}
//	}
}
