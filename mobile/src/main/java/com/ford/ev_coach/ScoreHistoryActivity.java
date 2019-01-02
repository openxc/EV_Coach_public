/*
Copyright © 2018 Ford Motor Company

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

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
    private static final int MAX_POINTS = 10000;

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
        int i = 0;
        mCursor.moveToFirst();
        while(!mCursor.isAfterLast()) {

            mTotalScoreSeries.appendData(new DataPoint(i, mCursor.getFloat(mCursor.getColumnIndex(DBTableContract.OverviewTableEntry.COLUMN_TOTAL_SCORE))), true, MAX_POINTS);
            mVehicleSpeedScoreSeries.appendData(new DataPoint(i, mCursor.getFloat(mCursor.getColumnIndex(DBTableContract.OverviewTableEntry.COLUMN_VEHICLE_SPEED_SCORE))), true, MAX_POINTS);
            mRpmScoreSeries.appendData(new DataPoint(i, mCursor.getFloat(mCursor.getColumnIndex(DBTableContract.OverviewTableEntry.COLUMN_ENGINE_SPEED_SCORE))), true, MAX_POINTS);
            mAccelScoreSeries.appendData(new DataPoint(i, mCursor.getFloat(mCursor.getColumnIndex(DBTableContract.OverviewTableEntry.COLUMN_ACCELERATOR_SCORE))), true, MAX_POINTS);
            mMpgScoreSeries.appendData(new DataPoint(i, mCursor.getFloat(mCursor.getColumnIndex(DBTableContract.OverviewTableEntry.COLUMN_MPGE_SCORE))), true, MAX_POINTS);
            mCursor.moveToNext();
            i++;
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
        graph.getViewport().setXAxisBoundsManual(false);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Run");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Points");


        if(spinnerSelect.equals("Total Score")) {
            graph.addSeries(mTotalScoreSeries);
            graph.setTitle("Total Score Over Time");
            graph.getViewport().setMaxY(mTotalScoreSeries.getHighestValueY()+10);
        } else if(spinnerSelect.equals("Vehicle Speed Score")) {
            graph.addSeries(mVehicleSpeedScoreSeries);
            graph.setTitle("Vehicle Speed Score Over Time");
            graph.getViewport().setMaxY(mVehicleSpeedScoreSeries.getHighestValueY()+10);
        } else if(spinnerSelect.equals("Engine Speed Score")) {
            graph.addSeries(mRpmScoreSeries);
            graph.setTitle("RPM Score Over Time");
            graph.getViewport().setMaxY(mRpmScoreSeries.getHighestValueY()+10);
        } else if(spinnerSelect.equals("Acceleration Score")) {
            graph.addSeries(mAccelScoreSeries);
            graph.setTitle("Accel Score Over Time");
            graph.getViewport().setMaxY(mAccelScoreSeries.getHighestValueY()+10);
        } else { // MPGe score
            graph.addSeries(mMpgScoreSeries);
            graph.setTitle("MPGe Score Over Time");
            graph.getViewport().setMaxY(mMpgScoreSeries.getHighestValueY()+10);
        }

    }

    public void onNothingSelected(AdapterView<?> view) {}
}
