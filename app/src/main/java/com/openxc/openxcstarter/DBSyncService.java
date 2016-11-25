package com.openxc.openxcstarter;

import android.app.IntentService;
import android.content.Intent;

/**
 * A intent service launched by the StarterActivity and then saves all the data processed
 * to the local database.
 */
public class DBSyncService extends IntentService {

    public DBSyncService() {
        super("DBSyncService");
    }

    /**
     * Processes database information.
     *
     * @param workIntent - the intent passed and data that should be processed
     */
    @Override
    protected void onHandleIntent(Intent workIntent) {


        // Do work here based on contents of dataString

    }
}
