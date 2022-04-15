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

		Jenkins j;
		if ((j = Jenkins.getInstanceOrNull()) != null) {
			Queue queue = j.getQueue();
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
      return "Purge Jobs with Offline Agents";
	}

	@Override
	public String getIconFileName() {
		return "null";
	}

	public String getIconClassName() {
		return "icon-gear";
	}

	@Override
	public String getUrlName() {
      return "endOfflineAgentJobs";
	}
}
