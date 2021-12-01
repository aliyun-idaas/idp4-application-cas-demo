package com.idsmanager;


import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * @author Shengzhao Li
 * @since 1.2.0
 */
@ContextConfiguration(locations = {"classpath:/spring/*.xml"}, initializers = {TestApplicationContextInitializer.class})
public abstract class ContextTest extends AbstractTestNGSpringContextTests {


}