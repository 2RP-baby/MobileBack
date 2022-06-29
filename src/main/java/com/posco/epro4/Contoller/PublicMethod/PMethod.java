package com.posco.epro4.Contoller.PublicMethod;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;

public class PMethod {
    /**
     * 전달받은 map의 value 값이 비어있는 String인 경우,
     * null로 값을 갱신한다.
     * 
     * @param map
     * @param value
     */
    // ? : 앞뒤 스페이스바 제거, 공백만 들어있는 경우 제거를 해줘야할까?
    public static void setEmptyValueInMap(HashMap<String, String> map, String value) {
        System.out.println("before map state : " + map.toString());
        for (String key : map.keySet()) {
            System.out.println("key : " + key + ", value : " + map.get(key));
            if (map.get(key) != null)
                map.get(key).trim();
            map.put(key, (map.get(key) == "" ? null : map.get(key)));
        }
        System.out.println("after map state : " + map.toString());
    }

    /**
     * 전달받은 String 값이 null인지 확인한 후,
     * 1) null이라면 null을 반환한다.
     * 2) null이 아니라면 Integer로 반환한다.
     * 
     * @param value
     * @return
     */
    public static Integer getStringToInteger(String value) {
        return value == null ? null : Integer.parseInt(value);
    }

    public static Long getStringToLong(String value) {
        return value == null ? null : Long.parseLong(value);
    }

    public static long getDateInterval(LocalDateTime startDateTime, LocalDateTime endDateTime, String unit) {
        // LocalDateTime dayDate1 = date1.truncatedTo(ChronoUnit.DAYS);
        // LocalDateTime dayDate2 = date2.truncatedTo(ChronoUnit.DAYS);
        // startDateTime = LocalDateTime.of(2022, 12, 20, 9, 30, 30);
        // endDateTime = LocalDateTime.of(2022, 12, 20, 10, 0, 40);

        switch (unit) {
            case "year":
                return ChronoUnit.YEARS.between(startDateTime, endDateTime);
            case "month":
                return ChronoUnit.MONTHS.between(startDateTime, endDateTime);
            case "week":
                return ChronoUnit.WEEKS.between(startDateTime, endDateTime);
            case "day":
                return ChronoUnit.DAYS.between(startDateTime, endDateTime);
            case "hour":
                return ChronoUnit.HOURS.between(startDateTime, endDateTime);
            case "minutes":
                return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
            case "second":
                return ChronoUnit.SECONDS.between(startDateTime, endDateTime);
            default:
                return 0;
        }

    }

    public static Date getDateFromString(String dateStr) {
        Date date = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // yyyy-MM-dd HH:mm:ss
            date = formatter.parse(dateStr);
        } catch (NullPointerException e) {
            System.out.println("dateStr is null...");
        } catch (Exception e) {
            System.out.println("getDateFromString Error!!!");
            e.printStackTrace();
        }
        return date;
    }

    public static String getDigittedLong(Long number, int digit) {
        // 예시 ) %03d
        // % : 뒤에 나오는 문자로 빈 자리를 대체한다.
        // 0 : 빈 자리에 채워질 문자
        // 3 : 자릿수
        // d : 정수
        return String.format("%0" + digit + "d", number);
    }
}