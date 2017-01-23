package org.jenkinsci.plugins.noslavejobpurge;

import java.io.IOException;

import javax.servlet.ServletException;

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
public class PurgeNoSlaveJobs implements RootAction
{

   public void doEndOfflineSlaveJobs(final StaplerRequest request, final StaplerResponse response)
   {
      Queue queue = Jenkins.getInstance().getQueue();

      for (Item job : queue.getItems())
      {
         if (job.getCauseOfBlockage() instanceof BecauseNodeIsOffline
               || job.getCauseOfBlockage() instanceof BecauseLabelIsOffline)
         {
            System.out.println("Found offline Node for: " + job.getDisplayName());
            queue.cancel(job);
         }
      }

      try
      {
         response.sendRedirect2(request.getRootPath());
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public String getDisplayName()
   {
      // TODO:pbuckley Finalise me
      return "Purge Jobs with no Slave";
   }

   @Override
   public String getIconFileName()
   {
      // TODO:pbuckley Finalise me
      return "/images/32x32/gear2.png";
   }

   @Override
   public String getUrlName()
   {
      // TODO:pbuckley Finalise me
      return "/endofflineslavejobs";
   }

}
