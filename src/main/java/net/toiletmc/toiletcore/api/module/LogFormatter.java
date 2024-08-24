package net.toiletmc.toiletcore.api.module;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public String format(LogRecord record) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN);
        String date = dateFormat.format(new Date(record.getMillis()));
        String loggerName = record.getLoggerName();
        String level = record.getLevel().getLocalizedName();
        String message = formatMessage(record);
        return String.format("[%s] [%s/%s]: %s%n", date, loggerName, level, message);
    }
}
