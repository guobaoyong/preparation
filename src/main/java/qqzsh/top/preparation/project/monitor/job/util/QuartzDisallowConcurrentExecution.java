package qqzsh.top.preparation.project.monitor.job.util;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import qqzsh.top.preparation.project.monitor.job.domain.Job;
import qqzsh.top.preparation.project.monitor.job.domain.Job;

/**
 * 定时任务处理（禁止并发执行）
 * 
 * @author zsh
 *
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, Job job) throws Exception
    {
        JobInvokeUtil.invokeMethod(job);
    }
}
