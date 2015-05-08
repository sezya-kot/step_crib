package sezyakot.com.stepcrib.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import sezyakot.com.stepcrib.R;

/**
 * Created by cat on 4/25/2015.
 */
public class BaseStringAdapter extends RecyclerView.Adapter<BaseStringAdapter.ViewHolder> {

	private LayoutInflater mInflater;
	private List<String> mList = Collections.emptyList();
	protected OnItemClickListener mListener;

	public interface OnItemClickListener{
		public void onItemClick(View v, int position);
	}

	public BaseStringAdapter(Context ctx, List<String> stringList) {
		mInflater = LayoutInflater.from(ctx);
		mList = stringList;
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
		String menuItem = mList.get(position);
		holder.name.setText(menuItem);
	}

	public void setData(List<String> list) {
		mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		TextView name;

		public ViewHolder(View itemView) {
			super(itemView);
			name = (TextView) itemView.findViewById(R.id.question);
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
}
