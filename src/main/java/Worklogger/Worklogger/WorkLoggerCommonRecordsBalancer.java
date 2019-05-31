package Worklogger.Worklogger;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.input.WorklogInput;
import com.atlassian.jira.rest.client.api.domain.input.WorklogInputBuilder;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;


public class WorkLoggerCommonRecordsBalancer {
	private List<WorkLoggerRecord> workLSet = new ArrayList<WorkLoggerRecord>();
	private String jiraServerUri;
	private String jiraLogin;
	private String jiraPassword;
	private String issueKey;
	private DateTime startDate;
	private DateTime endDate;
	private int duration;
	private int fluktPart;
	public WorkLoggerCommonRecordsBalancer(String jiraServerUri, String jiraLogin, String jiraPassword, String issueKey, DateTime startDate, DateTime endDate, int duration, int fluktPart) {
		super();
		this.jiraServerUri 	= jiraServerUri;
		this.jiraLogin 	= jiraLogin;
		this.jiraPassword 	= jiraPassword;
		this.issueKey 	= issueKey;
		this.duration 	= duration;
		this.fluktPart 	= fluktPart;
		this.startDate 	= startDate;
		this.endDate 	= endDate;
		this.initializeWorkSet();
	}
	
	private void initializeWorkSet() {
		DateTime curDate = this.startDate;
		do {
			if (curDate.getDayOfWeek() != DateTimeConstants.SATURDAY 
					&& curDate.getDayOfWeek() != DateTimeConstants.SUNDAY)
				addRecordToDate(curDate);
			curDate = curDate.plusDays(1);
		} while(curDate.isBefore(this.endDate));
		System.out.println(this.workLSet);
	}
	
	private void addRecordToDate(DateTime rDate) {
		Random rn = new Random();
		this.workLSet.add(new WorkLoggerRecord(rDate, (int) (0.18 * this.duration * (1 + rn.nextInt(this.fluktPart) / this.duration)), 
				"учет времени", 
				this.issueKey));
		this.workLSet.add(new WorkLoggerRecord(rDate, (int) (0.25 * this.duration * (1 + rn.nextInt(this.fluktPart) / this.duration)), 
				"расходы времени связанные с распланировкой работ, откладыванием, фиксацией и возобновлением статуса заданий", 
				this.issueKey));
		this.workLSet.add(new WorkLoggerRecord(rDate, (int) (0.32 * this.duration * (1 + rn.nextInt(this.fluktPart) / this.duration)), 
				"чтение корреспонденции, легкая переписка, контроль статусов задач", 
				this.issueKey));
		this.workLSet.add(new WorkLoggerRecord(rDate, (int) (0.25 * this.duration * (1 + rn.nextInt(this.fluktPart) / this.duration)), 
				"другие мероприятия", 
				this.issueKey));
	}
	
	public void postWorkLog() {
		for (Iterator iterator = workLSet.iterator(); iterator.hasNext();) {
			WorkLoggerRecord workLoggerRecord = (WorkLoggerRecord) iterator.next();
			addWorkLog(workLoggerRecord.getIssueKey(), workLoggerRecord.getStardDate(), workLoggerRecord.getCountMinutes(), workLoggerRecord.getDescription());
		}
	}
	
	public void addWorkLog(String issueID, DateTime rDate, int duration, String comments) {
		URI jiraServerUri = URI.create(this.jiraServerUri);

        AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();

        AuthenticationHandler auth = new BasicHttpAuthenticationHandler(this.jiraLogin, this.jiraPassword);

        JiraRestClient restClient = factory.create(jiraServerUri, auth);

		IssueRestClient issueClient = restClient.getIssueClient();
		com.atlassian.jira.rest.client.api.domain.Issue issue = restClient.getIssueClient().getIssue(issueID).claim();

		WorklogInput worklogInput = new WorklogInputBuilder(issue.getSelf()).setStartDate(rDate)
			.setComment(comments)
			.setMinutesSpent(duration)
			.build();
		Promise<Void> result = issueClient.addWorklog(issue.getWorklogUri(), worklogInput);
		result.claim();
	}
	
}
