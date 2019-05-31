package Worklogger.Worklogger;

import org.joda.time.DateTime;

public class WorkLoggerRecord {
	private DateTime stardDate;
	private int countMinutes;
	private String description;
	private String issueKey;
	
	public WorkLoggerRecord(DateTime stardDate, int countMinutes, String description, String issueKey) {
		super();
		this.stardDate = stardDate;
		this.countMinutes = countMinutes;
		this.description = description;
		this.issueKey = issueKey;
	}

	public DateTime getStardDate() {
		return stardDate;
	}

	public void setStardDate(DateTime stardDate) {
		this.stardDate = stardDate;
	}

	public int getCountMinutes() {
		return countMinutes;
	}

	public void setCountMinutes(int countMinutes) {
		this.countMinutes = countMinutes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIssueKey() {
		return issueKey;
	}

	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
	}

	@Override
	public String toString() {
		return "WorkLoggerRecord [stardDate=" + stardDate + ", countMinutes=" + countMinutes + ", description="
				+ description + ", issueKey=" + issueKey + "]";
	}
		

}
