package cz.dubcat.xpboost.utils;

public class DayUtil {
    public static String getDayOfTheWeek(int calendarDay) {
        String day = "";
        switch (calendarDay) {
            case 2:
                day = "monday";
                break;
            case 3:
                day = "tuesday";
                break;
            case 4:
                day = "wednesday";
                break;
            case 5:
                day = "thursday";
                break;
            case 6:
                day = "friday";
                break;
            case 7:
                day = "saturday";
                break;
            case 1:
                day = "sunday";
                break;
        }
        
        return day;
    }
}
