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

import android.content.Context;
import android.content.SharedPreferences;

import org.projectmaxs.shared.global.util.Log;
import org.projectmaxs.shared.transport.MAXSTransportReceiver;

import java.util.Set;

public class TransportReceiver extends MAXSTransportReceiver {

	private static final Log LOG = Log.getLog();

	public TransportReceiver() {
		super(LOG, TransportService.sTransportInformation);
	}

	@Override
	public void initLog(Context context) {
		LOG.initialize(Settings.getInstance(context));
	}

	@Override
	public SharedPreferences getSharedPreferences(Context context) {
		return Settings.getInstance(context).getSharedPreferences();
	}

	@Override
	public Set<String> doNotExport() {
		return Settings.DO_NOT_EXPORT;
	}
}
