/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.uaam;

public class UaamConstants {

	public static final String ROLE = "Role";
	public static final String EFFECTIVE_DATE = "Effective date";
	public static final String LAST_EDITED = "Last edited";
	public static final String LAST_EDITED_BY = "Last edted by";
	public static final String APPS = "App(s)";
	public static final String STATUS = "Status";
	public static final String SCHEDULED_EFFECTIVE_DATE = "Scheduled effective date";
	public static final String SCHEDULED_DELETION_DATE = "Scheduled deletion date";

	public enum ActiveRolesColumn {
		ROLE, EFFECTIVE_DATE, LAST_EDITED, LEST_EDITED_BY, APPS, STATUS		
	};
	
	public enum RolesBeingAddedColumn {
		ROLE, SCHEDULED_EFFECTIVE_DATE, LAST_EDITED, LEST_EDITED_BY, APPS		
	};
	
	public enum RolesBeingDeletedColumn {
		ROLE, SCHEDULED_DELETION_DATE, LAST_EDITED, LEST_EDITED_BY, APPS		
	};	
}
