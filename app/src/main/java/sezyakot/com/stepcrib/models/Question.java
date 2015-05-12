package sezyakot.com.stepcrib.models;

import android.database.Cursor;

import sezyakot.com.stepcrib.db.DBAdapter;

/**
 * Created by cat on 5/8/2015.
 */
public class Question {
	private int mId;
	private String mText;
	private int mSubjectId;
	private int mSerialNumber;

	public String getCorrectAnswer() {
		return mCorrectAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		mCorrectAnswer = correctAnswer;
	}

	private String mCorrectAnswer;

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}

	public String getText() {
		return mText;
	}

	public void setText(String text) {
		mText = text;
	}

	public int getSubjectId() {
		return mSubjectId;
	}

	public void setSubjectId(int subjectId) {
		mSubjectId = subjectId;
	}

	public int getSerialNumber() {
		return mSerialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		mSerialNumber = serialNumber;
	}

	public static Question fromCursor(Cursor c) {
		if (c != null) {
			Question q = new Question();
			q.setId(c.getInt(c.getColumnIndex(DBAdapter.QUEST_ID)));
			q.setText(c.getString(c.getColumnIndex(DBAdapter.QUEST_TEXT)));
			q.setSubjectId(c.getInt(c.getColumnIndex(DBAdapter.QUEST_SUBJ_ID)));
			q.setSerialNumber(c.getInt(c.getColumnIndex(DBAdapter.QUEST_SERNO)));
			return q;
		}
		return new Question();
	}
}
