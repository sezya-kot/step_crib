package sezyakot.com.stepcrib.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sezyakot.com.stepcrib.R;
import sezyakot.com.stepcrib.models.Question;

/**
 * Created by cat on 5/11/2015.
 */
public class QuestionCursorAdapter extends CursorRecyclerViewAdapter<QuestionCursorAdapter.ViewHolder> {

	public QuestionCursorAdapter(Context context,Cursor cursor){
		super(context,cursor);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView mTextView;
		public ViewHolder(View view) {
			super(view);
			mTextView = (TextView) view.findViewById(R.id.question);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.item_card, parent, false);
		ViewHolder vh = new ViewHolder(itemView);
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
		Question question = Question.fromCursor(cursor);
		viewHolder.mTextView.setText(question.getText());
	}
}
