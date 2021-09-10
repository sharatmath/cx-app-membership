package com.dev.prepaid;

import com.dev.prepaid.util.DateUtil;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class TestDate {

    @Test
    public void testFormat() throws ParseException {
        ZoneId zoneId = ZoneId.of( "Asia/Singapore" ); // Define a time zone rather than rely implicitly on JVM’s current default time zone.

        String concat = ":10.000Z";
        String format ="yyyy-MM-ddTHH:mm";
//        String data = "2021-09-02T13:15:10.249Z";
        String data = "2021-09-02T13:15";
        Instant instant = Instant.parse(data.concat(concat));
        ZonedDateTime zdt = ZonedDateTime.ofInstant( instant , zoneId );
        Date date = Date.from(zdt.toInstant());
        System.out.println(date);
//        DateUtil.stringToDate(data, format);

//        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String f  = DateUtil.dateToString(date, "yyyy-MM-dd'T'HH:mm:ss'Z'");
        System.out.println(f);
        System.out.println(f.substring(0, 16));

    }
}
