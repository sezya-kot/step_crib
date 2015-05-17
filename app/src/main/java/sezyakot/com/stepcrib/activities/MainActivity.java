package sezyakot.com.stepcrib.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import sezyakot.com.stepcrib.R;
import sezyakot.com.stepcrib.fragments.SearchFragment;


public class MainActivity extends AppCompatActivity {

	public final String TAG = getClass().getSimpleName();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		toolbar.findViewById(R.id.main_menu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, LoveActivity.class);
				startActivity(i);
			}
		});

		setSupportActionBar(toolbar);
		getSupportActionBar().setLogo(R.mipmap.ic_launcher);
		getSupportActionBar().setTitle(null);
		getSupportActionBar().setHomeButtonEnabled(true);

		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

		if (fragment == null) {
			fragment = new SearchFragment();
			getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragment_container, fragment)
				.commit();
		}
	}
}
