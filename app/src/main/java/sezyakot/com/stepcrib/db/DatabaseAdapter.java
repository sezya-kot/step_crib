package sezyakot.com.stepcrib.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter extends SQLiteOpenHelper {

	public static final String ANSW_ID = "_id";
	public static final String ANSW_IS_CORRECT = "is_correct";
	public static final String ANSW_QUEST_ID = "quest_id";
	public static final String ANSW_TEXT = "a_text";
	public static final String DATABASE_NAME = "stepDB";
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
	private static final String TAG = "DBAdapter";
	private static final String get_answers_query = String.format("select %s, %s from %s where %s = ?", new Object[]{"a_text", "is_correct", "Answers", "quest_id"});
	private static final String get_count_quests_query = String.format("select count(*) from %s where %s = ?", new Object[]{"Questions", "subj_id"});
	private static final String get_quest_query = String.format("select %s,%s from %s where %s = ? and %s = ?", new Object[]{"q_text", "_id", "Questions", "subj_id", "ser_num"});
	public String DATABASE_PATH;
	private final Context context;
	private SQLiteDatabase db;

	public DatabaseAdapter(Context paramContext) {
		super(paramContext, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = paramContext;
		this.DATABASE_PATH = (paramContext.getFilesDir().getParent() + "/databases/");
	}

	private boolean checkDataBase() {
		try {
			SQLiteDatabase localSQLiteDatabase2 = SQLiteDatabase.openDatabase(this.DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);
			db = localSQLiteDatabase2;
		} catch (SQLiteException localSQLiteException) {
				Log.v("DBAdapter", "DB so far does not exist");
			db = null;
		}
		if (db != null) {
			db.close();
		}
		return db != null;
	}
  private void copyDataBase() throws IOException
  {
    InputStream localInputStream = this.context.getAssets().open(DATABASE_NAME);
    FileOutputStream localFileOutputStream = new FileOutputStream(this.DATABASE_PATH + DATABASE_NAME);
    byte[] arrayOfByte = new byte[1024];
    for (;;)
    {
      int i = localInputStream.read(arrayOfByte);
      if (i <= 0) {
        break;
      }
      localFileOutputStream.write(arrayOfByte, 0, i);
    }
    localFileOutputStream.flush();
    localFileOutputStream.close();
    localInputStream.close();
  }

  public void close()
  {
    try
    {
      if (this.db != null) {
        this.db.close();
      }
      super.close();
      return;
    }
    finally {}
  }

  private void createDataBase() throws IOException
  {
    if (checkDataBase()) {
      return;
    }
    getReadableDatabase();
    try
    {
      copyDataBase();
      return;
    }
    catch (IOException localIOException)
    {
      Log.v("DBAdapter", "File not copied");
      throw new Error("Error copying database");
    }
  }

  public Cursor getAllRowsSubjects()
  {
	  if (db ==null) db = getReadableDatabase();

    Cursor localCursor = this.db.query(true, TAB_SUBJS, SUBJ_COLUMNS, null, null, null, null, null, null);
    if (localCursor != null) {
      localCursor.moveToFirst();
    }
    return localCursor;
  }

  public int getCountQuestionsInSubject(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = this.db;
    String str = get_count_quests_query;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramLong);
    Cursor localCursor = localSQLiteDatabase.rawQuery(str, arrayOfString);
    localCursor.moveToFirst();
    int i = localCursor.getInt(0);
    localCursor.close();
    return i;
  }
//
//  public QuestionWithAnswer getQuestionBySN(int paramInt1, int paramInt2)
//  {
//    String str1 = "";
//    Object localObject1 = "";
//    SQLiteDatabase localSQLiteDatabase = this.db;
//    String str2 = get_quest_query;
//    String[] arrayOfString1 = new String[2];
//    arrayOfString1[0] = String.valueOf(paramInt1);
//    arrayOfString1[1] = String.valueOf(paramInt2);
//    Cursor localCursor1 = localSQLiteDatabase.rawQuery(str2, arrayOfString1);
//    for (;;)
//    {
//      try
//      {
//        if (localCursor1.getCount() > 0)
//        {
//          localCursor1.moveToFirst();
//          str1 = localCursor1.getString(localCursor1.getColumnIndex("q_text"));
//          String str3 = localCursor1.getString(localCursor1.getColumnIndex("_id"));
//          localObject1 = str3;
//        }
//        localCursor1.close();
//        Cursor localCursor2 = this.db.rawQuery(get_answers_query, new String[] { localObject1 });
//        String[] arrayOfString2 = new String[localCursor2.getCount()];
//        boolean[] arrayOfBoolean = new boolean[localCursor2.getCount()];
//        if (localCursor2.moveToFirst())
//        {
//          int i = 0;
//          arrayOfString2[i] = localCursor2.getString(localCursor2.getColumnIndex("a_text"));
//          if (localCursor2.getInt(localCursor2.getColumnIndex("is_correct")) == 1)
//          {
//            j = 1;
//            arrayOfBoolean[i] = j;
//            i++;
//            if (localCursor2.moveToNext()) {
//              continue;
//            }
//          }
//        }
//        else
//        {
//          return new QuestionWithAnswer(str1, arrayOfString2, arrayOfBoolean);
//        }
//      }
//      finally
//      {
//        localCursor1.close();
//      }
//      int j = 0;
//    }
//  }
//
  public boolean isOpen()
  {
    return this.db.isOpen();
  }

  public void onCreate(SQLiteDatabase db) {
	  Log.d(TAG, "Start project");
	  if (!checkDataBase()) {
		  try {
			  copyDataBase();
		  } catch (IOException e) {
			  Log.v(TAG, "File not copied");
		  }
	  }

  }

  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {}

  public void openDataBase() throws SQLException
  {
    this.db = SQLiteDatabase.openDatabase(this.DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);
  }

	public List<String> getAllQuestions() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(String.format("select %s from %s", QUEST_TEXT, TAB_QUESTIONS), null);
		List<String> list = new ArrayList<>();
		if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				list.add(cursor.getString(cursor.getColumnIndex(QUEST_TEXT)));
			}
		}
		db.close();
		return list;
	}

	public List<String> getQuestions(String token) {
		SQLiteDatabase db = getReadableDatabase();
		String query = String.format("SELECT %s FROM %s WHERE %s LIKE \"%s\"", QUEST_TEXT, TAB_QUESTIONS, QUEST_TEXT, "%" + token + "%");
		Cursor cursor = db.rawQuery(query, null);
		List<String> list = new ArrayList<>();
		if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				list.add(cursor.getString(cursor.getColumnIndex(QUEST_TEXT)));
			}
		}
		db.close();
		return list;
	}
}



