/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

public enum OrderStatus {
	OPEN {
		@Override
        public String toString() {
			return "Open";
		}
	},
	PENDING {
		@Override
        public String toString() {
			return "Pending";
		}
	},
	CONFIRMED {
		@Override
        public String toString() {
			return "Confirmed";
		}            	
	},
	SHIPPED {
		@Override
        public String toString() {
			return "Shipped";
		}
	},
	PARTIALLY_SHIPPED {
		@Override
        public String toString() {
			return "Partially Shipped";
		}
	},
	PARTIALLY_RECEIVED {
		@Override
        public String toString() {
			return "Partially Received";
		}
	},
	RECEIVED {
		@Override
        public String toString() {
			return "Received";
		}
	},
	NOT_RECEIVED {
		@Override
        public String toString() {
			return "Not Received";
		}
	},
	NOT_SUPPLIED {
		@Override
        public String toString() {
			return "Not Supplied";
		}
	}
	
}
