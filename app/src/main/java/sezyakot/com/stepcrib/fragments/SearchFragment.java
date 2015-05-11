package sezyakot.com.stepcrib.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import sezyakot.com.stepcrib.adapters.BaseQuestionAdapter;
import sezyakot.com.stepcrib.db.DBAdapter;
import sezyakot.com.stepcrib.models.Question;

/**
 * Created by cat on 5/8/2015.
 */
public class SearchFragment extends Fragment implements TextWatcher {

	private static final String TAG = SearchFragment.class.getSimpleName();
	private static final String SEARCH_TOKEN = "search_token";
	@InjectView(R.id.questions) RecyclerView mRecyclerView;
	@InjectView(R.id.search_token) EditText mSearchToken;

	DBAdapter mDBAdapter = null;


	BaseQuestionAdapter mAdapter;
	private String mSearchTokenStr;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.search_fragment, container, false);
		setRetainInstance(true);
		ButterKnife.inject(this, v);
		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(SEARCH_TOKEN, mSearchTokenStr);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mDBAdapter = new DBAdapter(getActivity());
		try {
			mDBAdapter.createDataBase();
		} catch (IOException e) {
			Log.e(TAG, "Database haven't created!");
		}

		List<Question> questions = mDBAdapter.getAllQuestions();

		mRecyclerView.setHasFixedSize(true);
		LinearLayoutManager llm = new LinearLayoutManager(getActivity());
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(llm);

		mAdapter = new BaseQuestionAdapter(getActivity(), questions);
//		mAdapter.setOnItemClickListener(this);

		mRecyclerView.setAdapter(mAdapter);

		if (savedInstanceState != null) {
			mSearchTokenStr = savedInstanceState.getString(SEARCH_TOKEN);
			mAdapter.getFilter().filter(mSearchTokenStr);
		}

		mSearchToken.addTextChangedListener(this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mSearchToken.removeTextChangedListener(this);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() >= 3) {
			if (mSearchToken != null && mSearchToken.length() > 0) {
				mSearchTokenStr = mSearchToken.getText().toString();
				mAdapter.getFilter().filter(mSearchTokenStr);
			} else {
				mAdapter.setData(mDBAdapter.getAllQuestions());
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}
}
