package com.emostn.publictransit2.db_helper;
import android.provider.BaseColumns;

public final class PublicTransitContract
{

	public PublicTransitContract(){}
	
	public static abstract class TrolleyCon implements BaseColumns {
        public static final String TABLE_NAME = "trolleys";

        public static final String ID = "id";
        public static final String NAME = "name";
		public static final String URL = "url";
		public static final String HTML = "html";
    }
	
	public static abstract class VehicleCon implements BaseColumns {
        public static final String TABLE_NAME = "vehicle";

        public static final String ID = "id";
        public static final String NAME = "name";
		public static final String TYPE = "type";
		public static final String ROUTE_DESCR = "routeDescription";
    }

    public static abstract class StopCon implements BaseColumns {
        public static final String TABLE_NAME = "stop";

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
    }

    public static abstract class DepartureTimeCon implements BaseColumns {
        public static final String TABLE_NAME = "departureTime";

        public static final String ID = "id";
        public static final String TIME = "time";
        public static final String FROM_DATE = "fromDate";
        public static final String TO_DATE = "toDate";
        public static final String WEEKDAY = "weekday";

        //TODO ADD column names based on model
    }
}
