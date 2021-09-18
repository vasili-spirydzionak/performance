package com.godeltech.test;

import org.junit.jupiter.api.Test;
import us.abstracta.jmeter.javadsl.core.DslTestPlan;
import us.abstracta.jmeter.javadsl.core.DslThreadGroup;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static us.abstracta.jmeter.javadsl.JmeterDsl.htmlReporter;
import static us.abstracta.jmeter.javadsl.JmeterDsl.httpSampler;
import static us.abstracta.jmeter.javadsl.JmeterDsl.testPlan;
import static us.abstracta.jmeter.javadsl.JmeterDsl.threadGroup;

public class PerformanceTest {
    private static final String REPORT_FILE_PATH = System.getProperty("user.dir")
            + "\\reports\\perforep";
    private static final String BASE_URL = "https://reqres.in/";
    private static final String TEST_PLAN_FILE_PATH = System.getProperty("user.dir") + "/testPlan";


    @Test
    public void testPerformance() throws Exception {
        DslThreadGroup dslThreadGroup =
                threadGroup()
                        .rampToAndHold(10, Duration.ofSeconds(30), Duration.ofSeconds(5))
                        .children(httpSampler(BASE_URL));
        DslTestPlan testPlan = testPlan(
                // number of threads and iterations are in the end overwritten by BlazeMeter engine settings
                dslThreadGroup,
                htmlReporter(REPORT_FILE_PATH)

        );
        //dslThreadGroup.showThreadsTimeline();
//        testPlan.saveAsJmx(TEST_PLAN_FILE_PATH);
        TestPlanStats stats = testPlan.run();

//        DslTestPlan.fromJmx("test-plan.jmx").run();
        assertThat(stats.overall().sampleTimePercentile99()).isLessThan(Duration.ofSeconds(5));
    }
}
