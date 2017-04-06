package com.ford.ev_coach;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class ScoreHistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {

    private static final String TAG = ScoreHistoryFragment.class.getSimpleName();
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

        getLoaderManager().initLoader(0, null, this);
        graph = (GraphView) getActivity().findViewById(R.id.history_graph);
        mScoreSelectSpinner = (Spinner) getActivity().findViewById(R.id.history_spinner);

        // Setup the spinner with the default values
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.history_graphs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mScoreSelectSpinner.setAdapter(adapter);
        mScoreSelectSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.score_history_fragment, container, false);
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
        Uri baseUri = Uri.fromFile(getActivity().getApplicationContext().getDatabasePath(getString(R.string.database_name)));
        String projections[] = {DBTableContract.OverviewTableEntry.COLUMN_TRIP_NUMBER,
                DBTableContract.OverviewTableEntry.COLUMN_TOTAL_SCORE,
                DBTableContract.OverviewTableEntry.COLUMN_VEHICLE_SPEED_SCORE,
                DBTableContract.OverviewTableEntry.COLUMN_ENGINE_SPEED_SCORE,
                DBTableContract.OverviewTableEntry.COLUMN_ACCELERATOR_SCORE,
                DBTableContract.OverviewTableEntry.COLUMN_MPGE_SCORE };

        // Argument Order: Context, URI, Projection/columns to return, selection (where clause), ? argument fill ins, order by
        return new CursorLoader(getActivity().getApplicationContext(), baseUri, projections, null, null, DBTableContract.OverviewTableEntry._ID);
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
