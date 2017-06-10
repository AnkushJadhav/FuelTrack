package com.keelerapps.fueltrack;

import android.provider.BaseColumns;

/**
 * Created by ankush on 04/06/17.
 */

public final class FuelContract {

    private FuelContract() {}

    public static class FuelEntry implements BaseColumns {
        public static final String TABLE_NAME = "FuelRecords";
        public static final String COLUMN_COST = "COST";
        public static final String COLUMN_KILOMETRES = "KILOMETRES";
        public static final String COLUMN_LITRES = "LITRES";
        public static final String COLUMN_RATE = "RATE";
        public static final String COLUMN_FULL_TANK = "FULL_TANK";
        public static final String COLUMN_FUEL_DATE = "FUEL_DATE";
        public static final int COLUMNS_COUNT = 6;
    }
}
