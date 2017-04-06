package com.ford.ev_coach;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class ScoreHistoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {

    private static final String TAG = ScoreHistoryActivity.class.getSimpleName();
    private Cursor mCursor = null;

    private Spinner mScoreSelectSpinner = null;

    private LineGraphSeries<DataPoint> mTotalScoreSeries = new LineGraphSeries<>();
    private LineGraphSeries<DataPoint> mRpmScoreSeries = new LineGraphSeries<>();
    private LineGraphSeries<DataPoint> mVehicleSpeedScoreSeries = new LineGraphSeries<>();
    private LineGraphSeries<DataPoint> mAccelScoreSeries = new LineGraphSeries<>();
    private LineGraphSeries<DataPoint> mMpgScoreSeries = new LineGraphSeries<>();

    private GraphView graph = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        getLoaderManager().initLoader(0, null, this);
        graph = (GraphView) findViewById(R.id.history_graph);
        mScoreSelectSpinner = (Spinner) this.findViewById(R.id.history_spinner);

        // Setup the spinner with the default values
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.history_graphs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mScoreSelectSpinner.setAdapter(adapter);
        mScoreSelectSpinner.setOnItemSelectedListener(this);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursor = cursor;

        // Make sure the cursor isn't closed
        if(mCursor.isClosed()) {
            return;
        }

        // Go through all the cursor data
        while(!mCursor.isAfterLast()) {

            for(int i = 0; i < mCursor.getColumnCount(); i++) {
                Log.d(TAG, mCursor.getColumnName(i));
            }
            mCursor.moveToNext();
        }
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
    }


    public Loader<Cursor> onCreateLoader(int i, Bundle savedInstanceState) {
        Uri baseUri = Uri.parse("content://com.ford.ev_coach/ev_coach");
        Log.d(TAG, baseUri.toString());
        String projections[] = {
                DBTableContract.OverviewTableEntry.COLUMN_TOTAL_SCORE,
                DBTableContract.OverviewTableEntry.COLUMN_VEHICLE_SPEED_SCORE,
                DBTableContract.OverviewTableEntry.COLUMN_ENGINE_SPEED_SCORE,
                DBTableContract.OverviewTableEntry.COLUMN_ACCELERATOR_SCORE,
                DBTableContract.OverviewTableEntry.COLUMN_MPGE_SCORE };

        // Argument Order: Context, URI, Projection/columns to return, selection (where clause), ? argument fill ins, order by
        return new CursorLoader(getApplicationContext(), baseUri, projections, null, null, DBTableContract.OverviewTableEntry._ID);
    }

    public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
        graph.removeAllSeries();
        String spinnerSelect = mScoreSelectSpinner.getSelectedItem().toString();

        if(spinnerSelect.equals("Total Score")) {

        } else if(spinnerSelect.equals("Vehicle Speed Score")) {

        } else if(spinnerSelect.equals("Engine Speed Score")) {

        } else if(spinnerSelect.equals("Acceleration Score")) {

        } else { // MPGe score

        }

    }

    public void onNothingSelected(AdapterView<?> view) {}
}
