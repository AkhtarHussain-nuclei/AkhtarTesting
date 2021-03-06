package PlivoApiAutomation;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, strict = true,
 features = {"./src/test/resources/Features"},
 tags = {"@Plivo"}
)
public class RunCucumberTest {
}
