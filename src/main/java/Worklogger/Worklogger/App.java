package Worklogger.Worklogger;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	DateTimeFormatter f = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    	DateTime dateTimeStart = f.parseDateTime(args[4]);
    	DateTime dateTimeEnd = f.parseDateTime(args[5]);
    	WorkLoggerCommonRecordsBalancer wlbl = new WorkLoggerCommonRecordsBalancer(args[0], args[1], args[2],
    			args[3], dateTimeStart, dateTimeEnd, 150, 1);
    	wlbl.postWorkLog();
    }
}
