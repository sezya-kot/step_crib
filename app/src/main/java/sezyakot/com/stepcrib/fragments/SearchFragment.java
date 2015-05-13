package sezyakot.com.stepcrib.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;

import com.wrapp.floatlabelededittext.FloatLabeledEditText;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import sezyakot.com.stepcrib.R;
import sezyakot.com.stepcrib.activities.MainActivity;
import sezyakot.com.stepcrib.adapters.BaseQuestionAdapter;
import sezyakot.com.stepcrib.db.DBAdapter;
import sezyakot.com.stepcrib.models.Question;

/**
 * Created by cat on 5/8/2015.
 */
public class SearchFragment extends Fragment implements TextWatcher, Filter.FilterListener {

	private static final String TAG = SearchFragment.class.getSimpleName();
	private static final String SEARCH_TOKEN = "search_token";
	@InjectView(R.id.questions) RecyclerView mRecyclerView;
	@InjectView(R.id.hint_search_token) FloatLabeledEditText mHint;
	@InjectView(R.id.search_token) EditText mSearchToken;

	List<Question> mQuestions;

	DBAdapter mDBAdapter = null;

	BaseQuestionAdapter mAdapter;
	private String mSearchTokenStr;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.search_fragment, container, false);
		setRetainInstance(true);
		setHasOptionsMenu(true);
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

		mDBAdapter = DBAdapter.getInstance(getActivity());
		try {
			mDBAdapter.createDataBase();
		} catch (IOException e) {
			Log.e(TAG, "Database haven't created!");
		}

		mQuestions = mDBAdapter.getAllQuestions();

		mAdapter = new BaseQuestionAdapter(getActivity(), mQuestions);

		mRecyclerView.setHasFixedSize(true);
		LinearLayoutManager llm = new LinearLayoutManager(getActivity());
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(llm);

		if (savedInstanceState != null) {
			mSearchTokenStr = savedInstanceState.getString(SEARCH_TOKEN);
			mAdapter.getFilter().filter(mSearchTokenStr, this);
		}

//		mAdapter.setOnItemClickListener(this);
		mRecyclerView.setAdapter(mAdapter);
		mSearchToken.addTextChangedListener(this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mSearchToken.removeTextChangedListener(this);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		Log.d(TAG, "beforeTextChanged(): " + s);

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Log.d(TAG, "onTextChanged(): " + s);
		doSearch();
	}

	private void doSearch() {
		if (mSearchToken != null /*&& mSearchToken.length() > 0*/) {
			mSearchTokenStr = mSearchToken.getText().toString().trim().toLowerCase();
			mAdapter.getFilter().filter(mSearchTokenStr, this);
		} else {
			mAdapter.refresh();
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		Log.d(TAG, "afterTextChanged(): " + s);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		if (item.getItemId() == R.id.clear) {
			mSearchToken.setText("");
			mSearchTokenStr = "";
			doSearch();
		}
		if (item.getItemId() == R.id.hide_keyboard) {
			if (getActivity().getCurrentFocus() != null) {
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
			}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onFilterComplete(int count) {
		Log.d(TAG, "Count: " + count);
		if (count == 4007) {
			mHint.setHint(getString(R.string.write_question_hint));
		} else {
			mHint.setHint("Found: " + count);
		}
	}
}
