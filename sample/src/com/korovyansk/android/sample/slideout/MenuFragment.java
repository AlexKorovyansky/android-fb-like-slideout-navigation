package com.korovyansk.android.sample.slideout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.korovyansk.android.slideout.SlideOutEndCallback;

public class MenuFragment extends ListFragment{
	int mSelectedItemIndex;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, new String[] { " Sample 1", " Sample2", " Sample1", " Sample2", " Sample1", " Sample2"}));
		getListView().setCacheColorHint(0);
		((MenuActivity)getActivity()).getSlideoutHelper().setSlideOutEndCallback(new MySlideOutEndCallback());
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		((MenuActivity)getActivity()).getSlideoutHelper().close();
		mSelectedItemIndex = position;
	}

	class MySlideOutEndCallback implements SlideOutEndCallback{

		@Override
		public void run(boolean isBack) {
			startActivity(new Intent(getActivity(), mSelectedItemIndex % 2 == 1 ? Sample2Activity.class : SampleActivity.class));
		}		
	}
}
