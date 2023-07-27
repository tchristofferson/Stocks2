package com.thedasmc.stocks2.common;

public class Constants {

    public static final String API_URL = "https://api.thedasmc.com";
//    public static final String API_URL = "http://localhost:8080";

    public static final String POSITIVE_SHARE_LIMITS_CONDITION = "share-limits";

    public static final int STOCK_GUI_MAX = 10;
    public static final int STOCK_GUI_INV_SLOTS = 45;
    public static final int PORTFOLIO_PREVIOUS_BUTTON = STOCK_GUI_INV_SLOTS - 9;
    public static final int PORTFOLIO_NEXT_BUTTON = STOCK_GUI_INV_SLOTS - 1;
    public static final int STOCK_GUI_CLOSE_BUTTON = STOCK_GUI_INV_SLOTS - 5;

    //Fund status
    public static final char FUND_STATUS_OPEN = 'O';
    public static final char FUND_STATUS_CLOSED = 'C';
    public static final char FUND_STATUS_PENDING = 'P';

}
