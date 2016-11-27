/*
    This file is part of Project MAXS.

    MAXS and its modules is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    MAXS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with MAXS.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.matkam.projectmaxs.transport.matrix.matrixservice;

import android.content.Context;

import org.projectmaxs.shared.global.util.Log;
import org.projectmaxs.shared.maintransport.CommandOrigin;

import net.matkam.projectmaxs.transport.matrix.Settings;
//import net.matkam.projectmaxs.transport.matrix.database.MessagesTable;
import net.matkam.projectmaxs.transport.matrix.util.Constants;

public class MatrixService {
	private static final Log LOG = Log.getLog();

	private static MatrixService sMatrixService;

	private final Settings mSettings;
	private final Context mContext;

	private State mState = State.Disconnected;

	private final Runnable mReconnectRunnable = new Runnable() {
		@Override
		public void run() {
			LOG.d("scheduleReconnect: calling tryToConnect");
//			tryToConnect();
		}
	};

	/**
	 * Get an MatrixService
	 * 
	 * Note that because of MemorizingTrustManager Context must be an instance of Application,
	 * Service or Activity. Therefore if you have an Context which is not Service or Activity, use
	 * getApplication().
	 * 
	 * @param context
	 *            as an instance of Application, Service or Activity.
	 * @return The MatrixService instance.
	 */
	public static synchronized MatrixService getInstance(Context context) {
		if (sMatrixService == null) sMatrixService = new MatrixService(context);
		return sMatrixService;
	}

	private MatrixService(Context context) {
		mContext = context;
		mSettings = Settings.getInstance(context);
//		mMessagesTable = MessagesTable.getInstance(context);
	}

	public static enum State {
		Connected, Connecting, Disconnecting, Disconnected, InstantDisconnected, WaitingForNetwork, WaitingForRetry;
	}

	public State getCurrentState() {
		return mState;
	}

	public void send(String to, String body) {
		switch (mState) {
		case Disconnected:
		case Disconnecting:
			LOG.w("Transport is disconnected, not going to send message to " + to);
			return;
		default:
			break;
		}
//		if (!shouldUseXmppConnection()) {
//			LOG.w("Connection is not connected and no resumption possible, not going to send message to "
//					+ to);
//			return;
//		}
//		Message message = new Message();
//		message.setTo(to);
//		message.setBody(body);
//		try {
//			mConnection.sendStanza(message);
//		} catch (InterruptedException | NotConnectedException e) {
//			LOG.w("send", e);
//		}
	}

	public void send(org.projectmaxs.shared.global.Message message, CommandOrigin origin) {
		// If the origin is null, then we are receiving a broadcast message from
		// main. TODO document that origin can be null
		if (origin == null) {
			sendAsMessage(message, null, null);
			return;
		}

		String action = origin.getIntentAction();
		String originId = origin.getOriginId();
		String originIssuerInfo = origin.getOriginIssuerInfo();

		if (Constants.ACTION_SEND_AS_MESSAGE.equals(action)) {
			sendAsMessage(message, originIssuerInfo, originId);
		} else if (Constants.ACTION_SEND_AS_IQ.equals(action)) {
//			sendAsIQ(message, originIssuerInfo, originId);
		} else {
			throw new IllegalStateException("MatrixService send: unknown action=" + action);
		}
	}

	Context getContext() {
		return mContext;
	}

	private void sendAsMessage(org.projectmaxs.shared.global.Message message,
			String originIssuerInfo, String originId) {
//		if (!shouldUseXmppConnection()) {
//			// TODO I think that this could for example happen when the service
//			// is not started but e.g. the SMS receiver get's a new message.
//			LOG.i("sendAsMessage: Not connected, adding message to DB. mConnection=" + mConnection);
//			mMessagesTable.addMessage(message, Constants.ACTION_SEND_AS_MESSAGE, originIssuerInfo,
//					originId);
//			return;
//		}
//
//		Message packet = new Message();
//		packet.setType(Message.Type.chat);
//		packet.setBody(TransformMessageContent.toString(message));
//		packet.setThread(originId);
//
//		// Add a private carbon extension so that this message wont get carbon copied. MAXS does
//		// already send the message to all resources. If a recipient has carbons enabled and we
//		// wouldn't add the private element, then he would receive the message multiple times.
//		CarbonExtension.Private.addTo(packet);
//
//		// Add a MAXS element. MAXS itself will ignore messages with a MAXS element in order to
//		// prevent endless loops of message sending between one or multiple MAXS instances.
//		MAXSElement.addTo(packet);
//
//		List<EntityJid> toList = new LinkedList<>();
//
//		// No 'originIssueInfo (which is the to JID in this case) specified. The message is typical
//		// a notification, so we are going to broadcast it to all master JIDs.
//		if (originIssuerInfo == null) {
//			Set<BareJid> jidsWithExcludedResources = new HashSet<BareJid>();
//			Roster roster = Roster.getInstanceFor(mConnection);
//			// Broadcast to all masterJID resources
//			for (BareJid masterJid : mSettings.getMasterJids()) {
//				Collection<Presence> presences = roster.getAvailablePresences(masterJid);
//				for (Presence p : presences) {
//					Jid jid = p.getFrom();
//					EntityFullJid fullJID = jid.asEntityFullJidIfPossible();
//					if (fullJID == null) {
//						LOG.e("Could not convert '" + jid + "' to full JID");
//						continue;
//					}
//					if (!mSettings.isExcludedResource(fullJID.getResourcepart())) {
//						toList.add(fullJID);
//					} else {
//						jidsWithExcludedResources.add(fullJID.asBareJid());
//					}
//				}
//			}
//
//			// Broadcast to all offline masterJIDs
//			for (EntityBareJid masterJid : mSettings.getMasterJids()) {
//				boolean found = false;
//				for (EntityJid toJid : toList) {
//					if (toJid.asBareJid().equals(masterJid)) {
//						found = true;
//						break;
//					}
//				}
//				// Maybe add this master JID, if it isn't already contained in toList
//				if (!found) {
//					if (jidsWithExcludedResources.contains(masterJid)
//							&& roster.getPresences(masterJid).size() == 1) {
//						// Do not send a message to this JID if it would get received by an excluded
//						// resource, ie. when the excluded resource is the only online presence.
//						continue;
//					}
//					toList.add(masterJid);
//				}
//			}
//		}
//		// A JID was specified as receiver. This are typical replies to a command send by the
//		// receiver. This is not a notification, do not broadcast.
//		else {
//			EntityFullJid to;
//			try {
//				to = JidCreate.entityFullFrom(originIssuerInfo);
//			} catch (XmppStringprepException e) {
//				LOG.e("Could not convert originIssueInfo to full JID", e);
//				return;
//			}
//			toList.add(to);
//		}
//
//		boolean atLeastOneSupportsXHTMLIM = false;
//		for (EntityJid jid : toList) {
//			if (!jid.hasResource()) {
//				continue;
//			}
//
//			try {
//				atLeastOneSupportsXHTMLIM = XHTMLManager.isServiceEnabled(mConnection, jid);
//			} catch (Exception e) {
//				atLeastOneSupportsXHTMLIM = false;
//			}
//			if (atLeastOneSupportsXHTMLIM) break;
//		}
//		if (atLeastOneSupportsXHTMLIM)
//			XHTMLIMUtil.addXHTMLIM(packet, TransformMessageContent.toFormatedText(message));
//
//		try {
//			MultipleRecipientManager.send(mConnection, packet, toList, null, null);
//		} catch (Exception e) {
//			LOG.e("sendAsMessage: Got Exception, adding message to DB", e);
//			mMessagesTable.addMessage(message, Constants.ACTION_SEND_AS_MESSAGE, originIssuerInfo,
//					originId);
//		}
//
//		// Stop the current bundleAndDefer *after* the message has been sent.
//		XMPPBundleAndDefer.stopCurrentBundleAndDefer();
	}

//	protected void newMessageFromMasterJID(Message message) {
//		String command = message.getBody();
//		if (command == null) {
//			LOG.e("newMessageFromMasterJID: empty body");
//			return;
//		}
//
//		// Trim the command to remove extra whitespace, which e.g. could be send by clients trying
//		// to negotiate OTR. References:
//		// - https://github.com/python-otr/gajim-otr/issues/9
//		// - https://trac-plugins.gajim.org/ticket/97
//		command = command.trim();
//
//		String issuerInfo = message.getFrom().toString();
//		LOG.d("newMessageFromMasterJID: command=" + command + " from=" + issuerInfo);
//
//		Intent intent = new Intent(GlobalConstants.ACTION_PERFORM_COMMAND);
//		CommandOrigin origin = new CommandOrigin(Constants.PACKAGE,
//				Constants.ACTION_SEND_AS_MESSAGE, issuerInfo, null);
//		intent.putExtra(TransportConstants.EXTRA_COMMAND, command);
//		intent.putExtra(TransportConstants.EXTRA_COMMAND_ORIGIN, origin);
//		intent.setClassName(GlobalConstants.MAIN_PACKAGE,
//				TransportConstants.MAIN_TRANSPORT_SERVICE);
//		ComponentName cn = mContext.startService(intent);
//		if (cn == null) {
//			LOG.e("newMessageFromMasterJID: could not start main transport service");
//		}
//	}

}
