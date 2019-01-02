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

/**
 * Created by Ben Vesel on 11/25/2016.
 */

import android.provider.BaseColumns;

/**
 * Defines table constraints for the local database stored for this application
 */
public final class DBTableContract {

    //Prevent someone from instantiating the contract class
    private DBTableContract() {}

    /**
     * A static class which provides the "contract" or column names between the SQLite table
     */
    protected static class OverviewTableEntry implements BaseColumns {
        protected static final String TABLE_NAME = "score_overview";
        protected static final String COLUMN_TOTAL_SCORE = "total_score"; // Datatype REAL
        protected static final String COLUMN_VEHICLE_SPEED_SCORE = "vehicle_speed_score"; // Datatype REAL
        protected static final String COLUMN_ENGINE_SPEED_SCORE = "engine_speed_score"; // Datatype REAL
        protected static final String COLUMN_ACCELERATOR_SCORE = "accelerator_score"; // Datatype REAL
        protected static final String COLUMN_MPGE_SCORE = "mpge_score"; // Datatype REAL
        protected static final String COLUMN_TIMESTAMP = "timestamp"; // Datatype STRING stored as DD-MM-YYYY HH:MM:SS
    }

    /**
      * A static class providing the contract for the remote syncing database table
      */
    protected static class SyncingTableEntry implements BaseColumns {
        protected static final String TABLE_NAME = "sync_table";
        protected static final String COLUMN_SYNC_FLAG = "sync_flag"; //Datatype CHARACTER(1)
        protected static final String COLUMN_VARIABLE = "variable"; //Datatype VARCHAR(30)
        protected static final String COLUMN_VALUE = "value"; //Datatype REAL
        protected static final String COLUMN_FREQUENCY = "frequency"; //Datatype INT
    }
}
