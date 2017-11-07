package org.cosmic.mobuzz.general.ui.support;

import java.util.HashMap;
import java.util.List;

import org.cosmic.mobuzz.general.R;
import org.cosmic.mobuzz.general.util.GlobalMethods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//Adapter for the pop-up list items
public class PopuplistAdapter extends ArrayAdapter<String> {

	HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
	Context context;
	List<String> objects;

	public PopuplistAdapter(Context context, int resource) {
		super(context, resource);
	}

	public PopuplistAdapter(Context context, List<String> objects) {
		super(context, R.layout.xml_popup_list_detail, objects);
		this.context = context;
		this.objects = objects;

		for (int i = 0; i < objects.size(); ++i) {
			mIdMap.put(objects.get(i), i);
		}
	}

	public PopuplistAdapter(Context context, int textViewResourceId, List<String> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public long getItemId(int position) {
		String item = getItem(position);
		return mIdMap.get(item);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View converView, ViewGroup parent) {

		if (converView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			converView = new View(context);
			converView = inflater.inflate(R.layout.xml_popup_list_detail, null);
		}

		TextView txt_popup_content = (TextView) converView.findViewById(R.id.text1);
		txt_popup_content.setTypeface(GlobalMethods.getTypeface(context)); //Set the font
		txt_popup_content.setText(objects.get(position));

		return converView;
	}

}