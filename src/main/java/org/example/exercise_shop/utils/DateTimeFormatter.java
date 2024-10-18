package org.example.exercise_shop.utils;

import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class DateTimeFormatter {

    private static final Logger log = LoggerFactory.getLogger(DateTimeFormatter.class);
    Map<Long, Function<LocalDateTime, String>> strategyMap = new LinkedHashMap<>();

    public DateTimeFormatter() {
        strategyMap.put(ChronoUnit.MINUTES.getDuration().getSeconds(), this::formatInSeconds);
        strategyMap.put(ChronoUnit.HOURS.getDuration().getSeconds(), this::formatInMinutes);
        strategyMap.put(ChronoUnit.DAYS.getDuration().getSeconds(), this::formatInHours);
        strategyMap.put(ChronoUnit.MONTHS.getDuration().getSeconds(), this::formatInDays);
        strategyMap.put(ChronoUnit.YEARS.getDuration().getSeconds(), this::formatInMonths);
        strategyMap.put(Long.MAX_VALUE, this::formatInYears);
    }

    private String formatInSeconds(LocalDateTime localDateTime){
        long timeSecond = ChronoUnit.SECONDS.between(localDateTime, LocalDateTime.now());
        return timeSecond + " seconds ago";
    }

    private String formatInMinutes(LocalDateTime localDateTime){
        long timeMinute = ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now());
        return timeMinute + " minutes ago";
    }

    private String formatInHours(LocalDateTime localDateTime){
        long timeHour = ChronoUnit.HOURS.between(localDateTime, LocalDateTime.now());
        return timeHour + " hours ago";
    }

    private String formatInDays(LocalDateTime localDateTime){
        long timeDay = ChronoUnit.DAYS.between(localDateTime, LocalDateTime.now());
        return timeDay + " days ago";
    }

    private String formatInMonths(LocalDateTime localDateTime){
        long timeMonth = ChronoUnit.MONTHS.between(localDateTime, LocalDateTime.now());
        return timeMonth + " months ago";
    }

    private String formatInYears(LocalDateTime localDateTime){
        long timeYear = ChronoUnit.YEARS.between(localDateTime, LocalDateTime.now());
        return timeYear + " years ago";
    }

    public String format(LocalDateTime localDateTime){
        long timeSecond = ChronoUnit.SECONDS.between(localDateTime, LocalDateTime.now());
        log.info("Time second: " + timeSecond);
        var strategy= strategyMap.entrySet().stream().filter(entry -> timeSecond < entry.getKey())
                .findFirst().orElseThrow(() -> new ApplicationException(ErrorCode.ERROR_CONVERT_TIME));

        return strategy.getValue().apply(localDateTime);
    }

}


