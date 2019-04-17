package StepDefinition;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks
{
    @Before("@smoke,@functional")
    public void beforevalidation()
    {
        System.out.println("Smoke/functional before hooks");
    }

    @After("@smoke")
    public void postvalidation()
    {
        System.out.println("Smoke after hooks");
    }
}
