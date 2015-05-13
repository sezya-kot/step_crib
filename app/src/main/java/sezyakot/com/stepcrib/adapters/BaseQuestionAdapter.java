package sezyakot.com.stepcrib.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sezyakot.com.stepcrib.R;
import sezyakot.com.stepcrib.fragments.SearchFragment;
import sezyakot.com.stepcrib.models.Question;

/**
 * Created by cat on 4/25/2015.
 */
public class BaseQuestionAdapter extends RecyclerView.Adapter<BaseQuestionAdapter.ViewHolder> implements Filterable {

	private static final String TAG = BaseQuestionAdapter.class.getSimpleName();
	private final Context mCtx;
	private LayoutInflater mInflater;
	private List<Question> mList = Collections.emptyList();
	private List<Question> mOriginList;
	protected OnItemClickListener mListener;
	private Filter mFilter;


	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new QuestionFilter();
		}
		return mFilter;
	}

	public void refresh() {
		mList = new ArrayList<>(mOriginList);
		notifyDataSetChanged();
	}

	public interface OnItemClickListener {

		public void onItemClick(View v, int position);
	}

	public BaseQuestionAdapter(final Context ctx, List<Question> questions) throws InstantiationError {
		mCtx = ctx;
		mInflater = LayoutInflater.from(ctx);
		mList = questions;
		mOriginList = new ArrayList<>(questions);

	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		mListener = listener;
	}


	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = mInflater.inflate(R.layout.item_card, parent, false);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		String text = mList.get(position).getText();
		String answer = mList.get(position).getCorrectAnswer();
		holder.name.setText(text);
		holder.answer.setText(mCtx.getString(R.string.answer) + answer);
	}

//	public void setData(List<Question> list) {
//		mList = list;
//		mOriginList = list;
//		notifyDataSetChanged();
//	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		TextView name;
		TextView answer;

		public ViewHolder(View itemView) {
			super(itemView);
			name = (TextView) itemView.findViewById(R.id.question);
			answer = (TextView) itemView.findViewById(R.id.id);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (mListener != null) {
				v.setTag(mList.get(getAdapterPosition()));
				mListener.onItemClick(v, getAdapterPosition());
			}
		}
	}

	private class QuestionFilter extends Filter {

		private final Object mLock = new Object();

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();

			if (mOriginList == null) {
				synchronized (mLock) {
					mOriginList = new ArrayList<>(mList);
				}
			}

			if (constraint == null || constraint.length() == 0) {
				synchronized (mLock) {
					results.values = mOriginList;
					results.count = mOriginList.size();
				}
			} else {
//				String constraint = constraint.toString();
				List<Question> values = mOriginList;
				int count = values.size();
				String filterString1 = constraint.toString().toLowerCase().trim();
				String filterString2 = constraint.toString().toLowerCase().replace("\u0456", "\u0069").trim();
				String filterString3 = constraint.toString().toLowerCase().replace("\u0456", "\u0069").trim();
				filterString2 = filterString2.replace("\u002D", "\u2013");
				filterString2 = filterString2.replace("\u0027", "\u2019");


				List<Question> newValues = new ArrayList<>(count);

				for (int i = 0; i < count; i++) {
					Question data = values.get(i);
					String orig = data.getText().toLowerCase();
//					Log.v(TAG, "For ID: " + data.getId() + " isContain: " + data.getText().toLowerCase().contains(filterString));
					//Here is where you compare the constraint(title/address) with values in your JobsGetSet, or any other logic
					if (orig.contains(filterString1) || orig.contains(filterString2) || orig.contains(filterString3))
					{
						newValues.add(data);
					}
				}
				results.values = newValues;
				results.count = newValues.size();
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			if (results != null && results.values != null) {
				mList.clear();
				mList.addAll((List<? extends Question>) results.values);
				notifyDataSetChanged();
			}
		}
	}

}
