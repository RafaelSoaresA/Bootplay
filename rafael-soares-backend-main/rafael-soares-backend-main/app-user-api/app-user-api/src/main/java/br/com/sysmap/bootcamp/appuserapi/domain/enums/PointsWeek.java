package br.com.sysmap.bootcamp.appuserapi.domain.enums;

import java.time.DayOfWeek;

public enum PointsWeek {
    SUNDAY(25L, DayOfWeek.SUNDAY),
    MONDAY(7L, DayOfWeek.MONDAY),
    TUESDAY(6L, DayOfWeek.TUESDAY),
    WEDNESDAY(2L, DayOfWeek.WEDNESDAY),
    THURSDAY(10L, DayOfWeek.THURSDAY),
    FRIDAY(15L, DayOfWeek.FRIDAY),
    SATURDAY(20L, DayOfWeek.SATURDAY);

    private final Long points;
    private final DayOfWeek dayOfWeek;
    PointsWeek(Long points, DayOfWeek dayOfWeek) {

        this.points = points;
        this.dayOfWeek = dayOfWeek;
    }

    public Long getPoints() {
        return points;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public static PointsWeek fromDayOfWeek(DayOfWeek dayOfWeek) {
        PointsWeek dayPointsWeek = null;
        for(PointsWeek points : PointsWeek.values()) {
            if(points.getDayOfWeek() == dayOfWeek) {
                dayPointsWeek = points;
                break;
            }
        }
        return dayPointsWeek;
    }

}
