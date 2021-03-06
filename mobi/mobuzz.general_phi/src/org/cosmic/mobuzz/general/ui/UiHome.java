package org.cosmic.mobuzz.general.ui;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.LogAction;
import org.cosmic.mobuzz.general.util.PopupDialogWidget;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class UiHome extends Fragment {

	ImageButton bsite, imap, adengue;
	Activity activity;
	PopupDialogWidget pdw;

	private final String TAG = UiHome.class.getName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View fv = inflater.inflate(R.layout.fragment_ui_home, container, false);

		bsite = (ImageButton) fv.findViewById(R.id.ibut_bsite);
		imap = (ImageButton) fv.findViewById(R.id.ibut_imap);
		adengue = (ImageButton) fv.findViewById(R.id.ibut_adengue);

		activity = this.getActivity();

		breedingsite();
		informationmap();
		aboutdengue();

		GlobalIO.writeLog(getActivity(), TAG, LogAction.SHOW, true);
		
		return fv;
		
	}

	@Override
	public void onResume() {
		super.onResume();
		GlobalIO.writeLog(getActivity(), TAG, LogAction.RESUME, true);
	}

	@Override
	public void onPause() {
		super.onPause();
		
		if (pdw!=null && pdw.isShowing()) {
			pdw.dismiss();
		}
		GlobalIO.writeLog(getActivity(), TAG, LogAction.PAUSE, true);
	}

	private void breedingsite() {

		bsite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), UiBreedingsiteMainpage.class);
				intent.putExtra("startPage", 0);
				startActivity(intent);
			}
		});

	}

	private void informationmap() {

		imap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isNetworkAvailable()) {
					Intent intent = new Intent(getActivity(), UiMap.class);
					startActivity(intent);
				} else {
					popdNoInternet();
				}
			}
		});

	}

	private void aboutdengue() {

		adengue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), UiEducation.class);
				startActivity(intent);
			}
		});

	}

	//-------------------- Supporting functions -----------------------//

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	private void popdNoInternet() {

		pdw = new PopupDialogWidget(getActivity(), R.string.msg_internet_no_title1, R.string.msg_internet_no_para1, R.string.msg_common_ok, false);
		pdw.show();

	}

}
