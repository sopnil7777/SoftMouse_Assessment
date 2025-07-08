package Utilities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class reporting extends TestListenerAdapter {

    private ExtentSparkReporter htmlReporter;
    private static ExtentReports extent;
    private ExtentTest logger;

    @Override
    public void onStart(ITestContext context) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String reportFileName = "TestReport_" + timeStamp + ".html";
        String reportPath = System.getProperty("user.dir") + "/test-output/" + reportFileName;

        htmlReporter = new ExtentSparkReporter(reportPath);
        htmlReporter.config().setDocumentTitle("Automation Test Report");
        htmlReporter.config().setReportName("Test Execution Report");
        htmlReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        // Set environment/system info
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Tester", System.getProperty("user.name"));
        extent.setSystemInfo("Environment", "QA"); // You can load from config
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger = extent.createTest(result.getName());
        logger.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " PASSED", ExtentColor.GREEN));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger = extent.createTest(result.getName());
        logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " FAILED", ExtentColor.RED));
        logger.log(Status.FAIL, result.getThrowable());

        String screenshotPath = System.getProperty("user.dir") + "/Screenshots/" + result.getName() + ".png";
        File f = new File(screenshotPath);
        if (f.exists()) {
            try {
                logger.fail("Screenshot of failure:");
            } catch (Exception e) {
                logger.warning("Could not attach screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger = extent.createTest(result.getName());
        logger.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " SKIPPED", ExtentColor.ORANGE));
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
