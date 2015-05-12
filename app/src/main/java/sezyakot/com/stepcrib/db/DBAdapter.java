package sezyakot.com.stepcrib.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import sezyakot.com.stepcrib.models.Question;

public class DBAdapter extends SQLiteOpenHelper {

	private static final String TAG = DBAdapter.class.getSimpleName();

	public static final String ANSW_ID = "_id";
	public static final String ANSW_IS_CORRECT = "is_correct";
	public static final String ANSW_QUEST_ID = "quest_id";
	public static final String ANSW_TEXT = "a_text";
	public static final String DB_NAME = "stepDB";
	public static final int DATABASE_VERSION = 100;
	public static final String QUEST_ID = "_id";
	public static final String QUEST_SERNO = "ser_num";
	public static final String QUEST_SUBJ_ID = "subj_id";
	public static final String QUEST_TEXT = "q_text";
	public static final String[] SUBJ_COLUMNS = {"_id", "name"};
	public static final String SUBJ_ID = "_id";
	public static final String SUBJ_NAME = "name";
	public static final String TAB_ANSWERS = "Answers";
	public static final String TAB_QUESTIONS = "Questions";
	public static final String TAB_SUBJS = "Subjects";
	private static final String get_answers_query = String.format("select %s, %s from %s where %s = ?", new Object[]{"a_text", "is_correct", "Answers", "quest_id"});
	private static final String get_count_quests_query = String.format("select count(*) from %s where %s = ?", new Object[]{"Questions", "subj_id"});
	private static final String get_quest_query = String.format("select %s,%s from %s where %s = ? and %s = ?", new Object[]{"q_text", "_id", "Questions", "subj_id", "ser_num"});
	public String DB_PATH;
	private final Context mCtx;
	private SQLiteDatabase mDatabase;

	public DBAdapter(Context paramContext) {
		super(paramContext, DB_NAME, null, DATABASE_VERSION);
		this.mCtx = paramContext;
		this.DB_PATH = (paramContext.getFilesDir().getParent() + "/databases/");
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own database.
	 */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			//do nothing - database already exist
		} else {

			//By calling this method and empty database will be created into the default system path
			//of your application so we are gonna be able to overwrite that database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each time you open the application.
	 *
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			//database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created empty database in the
	 * system folder, from where it can be accessed and handled.
	 * This is done by transfering bytestream.
	 */
	private void copyDataBase() throws IOException {

		//Open your local db as the input stream
		InputStream myInput = mCtx.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		//Open the database
		String path = DB_PATH + DB_NAME;
		mDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

	}

	@Override
	public synchronized void close() {

		if (mDatabase != null)
			mDatabase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	// Add your public helper methods to access and get content from the database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
	// to you to create adapters for your views.

	public Cursor getAllRowsSubjects() {
		Cursor localCursor = this.mDatabase.query(true, TAB_SUBJS, SUBJ_COLUMNS, null, null, null, null, null, null);
		if (localCursor != null) {
			localCursor.moveToFirst();
		}
		return localCursor;
	}

	public List<Question> getAllQuestions() {
		return getQuestions(null);
	}

	public List<Question> getQuestions(String token) {
		SQLiteDatabase db = getReadableDatabase();
		String query;
		if (token != null && token.length() > 0) {
			query = String.format("SELECT * FROM %s WHERE %s LIKE \"%s\"", TAB_QUESTIONS, QUEST_TEXT, "%" + token + "%");
		} else {
			query = String.format("SELECT * FROM %s, %s WHERE %s.%s=%s.%s AND %s.%s=1", TAB_QUESTIONS, TAB_ANSWERS, TAB_ANSWERS, ANSW_QUEST_ID, TAB_QUESTIONS, QUEST_ID, TAB_ANSWERS, ANSW_IS_CORRECT);
		}
		Cursor c = db.rawQuery(query, null);
		List<Question> list = new ArrayList<>();
		while (c.moveToNext()) {
			Question q = new Question();
			q.setId(c.getInt(c.getColumnIndex(QUEST_ID)));
			q.setText(c.getString(c.getColumnIndex(QUEST_TEXT)));
			q.setSubjectId(c.getInt(c.getColumnIndex(QUEST_SUBJ_ID)));
			q.setSerialNumber(c.getInt(c.getColumnIndex(QUEST_SERNO)));
			q.setCorrectAnswer(c.getString(c.getColumnIndex(ANSW_TEXT)));

			list.add(q);
		}
		db.close();
		return list;
	}
}



