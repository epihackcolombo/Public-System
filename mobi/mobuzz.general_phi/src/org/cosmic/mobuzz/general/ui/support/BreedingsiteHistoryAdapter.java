package org.cosmic.mobuzz.general.ui.support;

import java.util.ArrayList;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.pojo.HistoryPojo;
import org.cosmic.mobuzz.general.ui.UiBreedingsiteHistoryDetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//Adapter for reported breeding site summary items
public class BreedingsiteHistoryAdapter extends BaseAdapter implements OnClickListener {

	private final Activity activity;
	private static LayoutInflater inflater = null;
	public Resources res;
	HistoryPojo tempValues = null;

	private final ArrayList<HistoryPojo> data = new ArrayList<HistoryPojo>();
	private final String[] wardsArray = new String[] {"Not in CMC jurisdiction", "Mattakkuliya (CMC ward)", "Modera (CMC ward)", "Mahawatte (CMC ward)", "Aluthmawatha (CMC ward)", "Lunupokuna (CMC ward)", "Bloemandhal (CMC ward)", "Kotahena East (CMC ward)", "Kotahena West (CMC ward)", "Kochchikade North (CMC ward)", "Gintupitiya (CMC ward)", "Masangas Weediya (CMC ward)", "New Bazaar (CMC ward)", "Grandpass North (CMC ward)", "Grandpass South (CMC ward)", "Maligawatte West (CMC ward)", "Aluthkade East (CMC ward)", "Aluthkade West (CMC ward)", "Kehelwatte (CMC ward)", "Kochchikade South (CMC ward)", "Fort (CMC ward)", "Kopannaweediya (CMC ward)", "Wekanda (CMC ward)", "Hunupitiya (CMC ward)", "Suduwella (CMC ward)", "Panchikawatte (CMC ward)", "Maradana (CMC ward)", "Maligakanda (CMC ward)", "Maligawatte East (CMC ward)", "Dematagoda (CMC ward)", "Wanathamulla (CMC ward)", "Kuppiyawatte East (CMC ward)", "Kuppiyawatte West (CMC ward)", "Borella North (CMC ward)", "Narahenpita (CMC ward)", "Borella South (CMC ward)", "Cinnamon Gardens (CMC ward)", "Kollupitiya (CMC ward)", "Bambalapitiya (CMC ward)", "Milagiriya (CMC ward)", "Thimbirigasyaya (CMC ward)", "Kirula (CMC ward)", "Havelok Twon (CMC ward)", "Wellewatte North (CMC ward)", "Kirillapone (CMC ward)", "Pamankada East (CMC ward)", "Pamankada West (CMC ward)", "Wellewatte South (CMC ward)"};
	//private final String[] wardsArray = new String[] {"Not in CMC jurisdiction", "Mattakkuliya (CMC ward: 01)", "Modera (CMC ward: 02)", "Mahawatte (CMC ward: 03)", "Aluthmawatha (CMC ward: 04)", "Lunupokuna (CMC ward: 05)", "Bloemandhal (CMC ward: 06)", "Kotahena East (CMC ward: 07)", "Kotahena West (CMC ward: 08)", "Kochchikade North (CMC ward: 09)", "Gintupitiya (CMC ward: 10)", "Masangas Weediya (CMC ward: 11)", "New Bazaar (CMC ward: 12)", "Grandpass North (CMC ward: 13)", "Grandpass South (CMC ward: 14)", "Maligawatte West (CMC ward: 15)", "Aluthkade East (CMC ward: 16)", "Aluthkade West (CMC ward: 17)", "Kehelwatte (CMC ward: 18)", "Kochchikade South (CMC ward: 19)", "Fort (CMC ward: 20)", "Kopannaweediya (CMC ward: 21)", "Wekanda (CMC ward: 22)", "Hunupitiya (CMC ward: 23)", "Suduwella (CMC ward: 24)", "Panchikawatte (CMC ward: 25)", "Maradana (CMC ward: 26)", "Maligakanda (CMC ward: 27)", "Maligawatte East (CMC ward: 28)", "Dematagoda (CMC ward: 29)", "Wanathamulla (CMC ward: 30)", "Kuppiyawatte East (CMC ward: 31)", "Kuppiyawatte West (CMC ward: 32)", "Borella North (CMC ward: 33)", "Narahenpita (CMC ward: 34)", "Borella South (CMC ward: 35)", "Cinnamon Gardens (CMC ward: 36)", "Kollupitiya (CMC ward: 37)", "Bambalapitiya (CMC ward: 38)", "Milagiriya (CMC ward: 39)", "Thimbirigasyaya (CMC ward: 40)", "Kirula (CMC ward: 41)", "Havelok Twon (CMC ward: 42)", "Wellewatte North (CMC ward: 43)", "Kirillapone (CMC ward: 44)", "Pamankada East (CMC ward: 45)", "Pamankada West (CMC ward: 46)", "Wellewatte South (CMC ward: 47)"};
	private final String[] wardsArray_long = new String[] {"Not in CMC jurisdiction", "Mattakkuliya (CMC ward no: 01)", "Modera (CMC ward no: 02)", "Mahawatte (CMC ward no: 03)", "Aluthmawatha (CMC ward no: 04)", "Lunupokuna (CMC ward no: 05)", "Bloemandhal (CMC ward no: 06)", "Kotahena East (CMC ward no: 07)", "Kotahena West (CMC ward no: 08)", "Kochchikade North (CMC ward no: 09)", "Gintupitiya (CMC ward no: 10)", "Masangas Weediya (CMC ward no: 11)", "New Bazaar (CMC ward no: 12)", "Grandpass North (CMC ward no: 13)", "Grandpass South (CMC ward no: 14)", "Maligawatte West (CMC ward no: 15)", "Aluthkade East (CMC ward no: 16)", "Aluthkade West (CMC ward no: 17)", "Kehelwatte (CMC ward no: 18)", "Kochchikade South (CMC ward no: 19)", "Fort (CMC ward no: 20)", "Kopannaweediya (CMC ward no: 21)", "Wekanda (CMC ward no: 22)", "Hunupitiya (CMC ward no: 23)", "Suduwella (CMC ward no: 24)", "Panchikawatte (CMC ward no: 25)", "Maradana (CMC ward no: 26)", "Maligakanda (CMC ward no: 27)", "Maligawatte East (CMC ward no: 28)", "Dematagoda (CMC ward no: 29)", "Wanathamulla (CMC ward no: 30)", "Kuppiyawatte East (CMC ward no: 31)", "Kuppiyawatte West (CMC ward no: 32)", "Borella North (CMC ward no: 33)", "Narahenpita (CMC ward no: 34)", "Borella South (CMC ward no: 35)", "Cinnamon Gardens (CMC ward no: 36)", "Kollupitiya (CMC ward no: 37)", "Bambalapitiya (CMC ward no: 38)", "Milagiriya (CMC ward no: 39)", "Thimbirigasyaya (CMC ward no: 40)", "Kirula (CMC ward no: 41)", "Havelok Twon (CMC ward no: 42)", "Wellewatte North (CMC ward no: 43)", "Kirillapone (CMC ward no: 44)", "Pamankada East (CMC ward no: 45)", "Pamankada West (CMC ward no: 46)", "Wellewatte South (CMC ward no: 47)"};
	private final String TAG = BreedingsiteHistoryAdapter.class.getName();

	public BreedingsiteHistoryAdapter(Activity a) {

		activity = a;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public BreedingsiteHistoryAdapter(Activity a, ArrayList d, Resources resLocal) {

		activity = a;
		res = resLocal;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void clear() {
		if (!data.isEmpty()) {
			data.clear();
		}
	}

	public void add(HistoryPojo historyObj) {
		data.add(historyObj);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public HistoryPojo getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void onClick(View arg0) {
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.xml_breedingsite_list_tabitem, null);
		}

		//Initialization of components 
		TextView txt_msgd_datetime = (TextView) convertView.findViewById(R.id.txt_msgd_datetime);
		TextView txt_msgd_ward = (TextView) convertView.findViewById(R.id.txt_msgd_ward);
		TextView txt_msgd_address = (TextView) convertView.findViewById(R.id.txt_msgd_address);
		TextView txt_msgd_assessment = (TextView) convertView.findViewById(R.id.txt_msgd_assessment);
		TextView txt_msgd_cmcaction = (TextView) convertView.findViewById(R.id.txt_msgd_cmcaction);
		ImageView imgv_download_image = (ImageView) convertView.findViewById(R.id.imgv_download_image);

		if (data.size() > 0) { //If data available, add to the adapter
			tempValues = data.get(position);

			txt_msgd_datetime.setText(tempValues.getDate());

			int ward = 0;
			try
			{
				ward = Integer.valueOf(tempValues.getWard());
			}
			catch(Exception ex)
			{
				Log.e(TAG, " "+ex.getMessage());
				ward = 0;
			}

			if(ward>=0 && ward <=47)
			{
				txt_msgd_ward.setText(wardsArray[ward]);
			}
			else
			{
				txt_msgd_ward.setText(wardsArray[0]);
			}
			txt_msgd_address.setText(tempValues.getAddress());
			txt_msgd_assessment.setText(tempValues.getAssessment());
			txt_msgd_cmcaction.setText("CMC Message : " + tempValues.getCmcMessage());
			
			int isImage = tempValues.getImage();
			
			if(isImage>0)
			{
				imgv_download_image.setImageResource(R.drawable.logo_icon_download_enable);
			}
			else
			{
				imgv_download_image.setImageResource(R.drawable.logo_icon_download_disable);
			}
			
		}
		else
		{
			Toast.makeText(activity, getString(R.string.msg_data_no_para1), Toast.LENGTH_LONG).show();
		}

		convertView.setOnClickListener(new OnItemClickListener(position));

		return convertView;
	}

	private CharSequence getString(int msgDataNoPara1) {
		// TODO Auto-generated method stub
		return null;
	}

	//Implementation of the detail item
	private class OnItemClickListener implements OnClickListener {

		private final int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {

			//Pass the clicked item's information to the detail activity
			HistoryPojo tempValues = getItem(mPosition);

			int image = tempValues.getImage();
			String id = tempValues.getId();
			String gps = tempValues.getGps();
			String address = tempValues.getAddress();
			String date = tempValues.getDate();
			String ward;
			String remarks = tempValues.getRemarks();
			String assessment = tempValues.getAssessment();
			String cmcmessage = tempValues.getCmcMessage();
			String imagepath = tempValues.getImagepath();

			int iward = 0;
			try
			{
				iward = Integer.valueOf(tempValues.getWard());
			}
			catch(Exception ex)
			{
				Log.e(TAG, " "+ex.getMessage());
				iward = 0;
			}

			if(iward>=0 && iward <=47)
			{
				ward = wardsArray_long[iward];
			}
			else
			{
				ward = wardsArray[0];
			}

			Intent intent = new Intent(activity, UiBreedingsiteHistoryDetails.class);

			intent.putExtra("image", image);
			intent.putExtra("id", id);
			intent.putExtra("gps", gps);
			intent.putExtra("address", address);
			intent.putExtra("date", date);
			intent.putExtra("ward", ward);
			intent.putExtra("remarks", remarks);
			intent.putExtra("assessment", assessment);
			intent.putExtra("cmcmessage", cmcmessage);
			intent.putExtra("imagepath", imagepath);

			activity.startActivity(intent);
		}
	}
}
