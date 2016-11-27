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

package net.matkam.projectmaxs.transport.matrix;

import android.app.IntentService;
import android.content.Intent;

import org.projectmaxs.shared.global.util.Log;
import net.matkam.projectmaxs.transport.matrix.util.Constants;
import net.matkam.projectmaxs.transport.matrix.matrixservice.MatrixService;

public class MatrixIntentService extends IntentService {

	private static Log LOG = Log.getLog();

	private MatrixService mMatrixService;
	private Settings mSettings;

	public MatrixIntentService() {
		super("Matrix Intent Service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (mSettings == null) mSettings = Settings.getInstance(this);
		if (!mSettings.isMatrixIntentEnabled()) {
			LOG.d("Matrix intent not enabled");
			return;
		}
		final String sharedToken = mSettings.getMatrixIntentSharedToken();
		if (sharedToken == null) {
			LOG.i("Matrix intent shared token not set (or Matrix intent disabled)");
			return;
		}
		final String givenSharedToken = intent.getStringExtra(Constants.PACKAGE + ".TOKEN");
		if (!sharedToken.equals(givenSharedToken)) {
			LOG.w("Given shared token '" + givenSharedToken + "' does not match shared token '"
					+ sharedToken + "'");
			return;
		}

		if (mMatrixService == null) mMatrixService = MatrixService.getInstance(this);
		final String action = intent.getAction();
		switch (action) {
		case Constants.PACKAGE + ".SEND_MATRIX_MESSAGE":
            intent.getStringExtra(Constants.PACKAGE + ".TO");

			String body = intent.getStringExtra(Constants.PACKAGE + ".BODY");
			if (body == null || body.isEmpty()) {
				LOG.w("BODY extra not set or empty");
				return;
			}
//			mMatrixService.send(to, body);
			break;
		default:
			LOG.w("Unkown action: " + action);
		}
	}
}
