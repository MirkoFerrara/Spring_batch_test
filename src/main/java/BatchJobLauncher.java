import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BatchJobLauncher {

    private static final Logger logger = LoggerFactory.getLogger(BatchJobLauncher.class);

    public static void main(String[] args) {

            // Carica il contesto specifico del job
            ApplicationContext context;
            Job job;
            String fileName = "", jobName = "";
            logger.info("STAMPA");
            if (args.length == 0) {
                args = new String[1];  // Create an array with 1 element
                args[0] = "db";  // Set the value manually
            }
                switch (args[0].trim()) {
                    case "ftp":
                        fileName = "ftpJobConfig";
                        jobName = "ftpImportJob";
                        break;
                    case "db":
                        fileName = "databaseTransferJobConfig";
                        jobName = "transferMyTableJob";
                }
                context = new ClassPathXmlApplicationContext(fileName + ".xml");
                job = (Job) context.getBean(jobName);

                JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        JobExecution execution = null;
        try {
            execution = jobLauncher.run(job, new JobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Job Status: " + execution.getStatus());
                logger.info("Job status: " + execution.getStatus());

    }
}