package ch.ice.junit.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BingWebTest.class, GoogleWebTest.class})
public class AllWebTests {

}
