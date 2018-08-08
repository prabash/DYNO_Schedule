/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author Prabash
 */
public class DateTimeUtil
{

    private static final String dateFormat = "dd/MM/yyyy";
    private static final String timeFormat = "HH:mm:ss";
    private static final String dateTimeFormat = "dd/MM/yyyy HH:mm:ss";

    public static SimpleDateFormat getDefaultSimpleDateFormat()
    {
        return new SimpleDateFormat(dateFormat);
    }

    public static DateTimeFormatter getDefaultDateFormat()
    {
        return DateTimeFormatter.ofPattern(dateFormat);
    }

    public static SimpleDateFormat getDefaultSimpleTimeFormat()
    {
        return new SimpleDateFormat(timeFormat);
    }

    public static DateTimeFormatter getDefaultTimeFormat()
    {
        return DateTimeFormatter.ofPattern(timeFormat);
    }

    public static SimpleDateFormat getDefaultSimpleDateTimeFormat()
    {
        return new SimpleDateFormat(dateTimeFormat);
    }

    public static DateTimeFormatter getDefaultDateTimeFormat()
    {
        return DateTimeFormatter.ofPattern(dateTimeFormat);
    }

    public static Date asDate(LocalDate localDate)
    {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime)
    {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date)
    {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date)
    {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date ConcatenateDateTime(Date date, Date time)
    {
        String startingDate = getDefaultSimpleDateFormat().format(date);
        String startingTime = getDefaultSimpleTimeFormat().format(time);
        LocalDate datePart = LocalDate.parse(startingDate, DateTimeUtil.getDefaultDateFormat());
        LocalTime timePart = LocalTime.parse(startingTime);
        return DateTimeUtil.asDate(LocalDateTime.of(datePart, timePart));
    }
}
