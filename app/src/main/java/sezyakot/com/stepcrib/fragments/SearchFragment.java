package sezyakot.com.stepcrib.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import sezyakot.com.stepcrib.R;
import sezyakot.com.stepcrib.adapters.BaseStringAdapter;
import sezyakot.com.stepcrib.db.DBAdapter;

/**
 * Created by cat on 5/8/2015.
 */
public class SearchFragment extends Fragment {

	private static final String TAG = SearchFragment.class.getSimpleName();
	@InjectView(R.id.questions) RecyclerView mRecyclerView;
	@InjectView(R.id.search_token) EditText mSearchToken;
	@InjectView(R.id.search) Button mSearch;

	BaseStringAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.search_fragment, container, false);
		ButterKnife.inject(this, v);
		return v;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final DBAdapter dbAdapter = new DBAdapter(getActivity());
		try {
			dbAdapter.createDataBase();
		} catch (IOException e) {
			Log.e(TAG, "Database haven't created!");
		}
		dbAdapter.getAllQuestions();

		List<String> calculatorList = dbAdapter.getAllQuestions();

		mRecyclerView.setHasFixedSize(true);
		LinearLayoutManager llm = new LinearLayoutManager(getActivity());
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(llm);

		mAdapter = new BaseStringAdapter(getActivity(), calculatorList);
//		mAdapter.setOnItemClickListener(this);

		mRecyclerView.setAdapter(mAdapter);

		mSearch
			.setOnClickListener
				(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (mSearchToken != null && mSearchToken.length() > 0) {
								List<String> list = dbAdapter.getQuestions(mSearchToken.getText().toString());
								mAdapter.setData(list);
							}
						}
					}
				);
	}
}
