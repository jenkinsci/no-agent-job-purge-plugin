package org.jenkinsci.plugins.noslavejobpurge;

import java.io.IOException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.Extension;
import hudson.model.Queue;
import hudson.model.RootAction;
import hudson.model.Queue.Item;
import hudson.model.queue.CauseOfBlockage.BecauseLabelIsOffline;
import hudson.model.queue.CauseOfBlockage.BecauseNodeIsOffline;
import jenkins.model.Jenkins;

@Extension
public class PurgeNoAgentJobs implements RootAction {

	public void doEndOfflineAgentJobs(final StaplerRequest request, final StaplerResponse response) {

		Jenkins jenkins = Jenkins.getInstance();
		if (jenkins != null) {
			Queue queue = jenkins.getQueue();
			if (queue != null) {
				for (Item job : queue.getItems()) {
					if (job.getCauseOfBlockage() instanceof BecauseNodeIsOffline
							|| job.getCauseOfBlockage() instanceof BecauseLabelIsOffline) {
						queue.cancel(job);
					}
				}
			}
		}

		try {
			response.sendRedirect2(request.getRootPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getDisplayName() {
		return "Purge Jobs with offline Agents";
	}

	@Override
	public String getIconFileName() {
		return "/images/32x32/gear2.png";
	}

	@Override
	public String getUrlName() {
		return "/endofflineagentjobs";
	}

}
