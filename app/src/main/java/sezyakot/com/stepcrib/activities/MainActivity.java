package sezyakot.com.stepcrib.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import sezyakot.com.stepcrib.R;
import sezyakot.com.stepcrib.fragments.SearchFragment;


public class MainActivity extends ActionBarActivity {

	public final String TAG = getClass().getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

		if (fragment == null) {
			fragment = new SearchFragment();
			getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragment_container, fragment)
				.commit();
		}

//		DatabaseAdapter dbAdapter = new DatabaseAdapter(this);
//
//		Cursor cursor = dbAdapter.getAllRowsSubjects();
//		String l;
//		while (cursor.moveToNext()) {
//			l = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.SUBJ_NAME));
//			Log.d(TAG, DatabaseAdapter.SUBJ_NAME + " is " + l);
//		}
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//
//
//		return super.onOptionsItemSelected(item);
//	}
}
