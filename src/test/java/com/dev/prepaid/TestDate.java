package com.dev.prepaid;

import com.dev.prepaid.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
@SpringBootTest
public class TestDate {



    @Test
    public void testFormat() throws ParseException {
//        ZoneId zoneId = ZoneId.of( "Asia/Singapore" ); // Define a time zone rather than rely implicitly on JVM’s current default time zone.
        ZoneId zoneId = ZoneId.systemDefault();
        String concat = ":00.000Z";
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

        ZoneId timeZone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = LocalDateTime.parse("2011-12-03T10:15:30",
                DateTimeFormatter.ISO_DATE_TIME).atZone(timeZone);

        System.out.println(zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));

    }
}
