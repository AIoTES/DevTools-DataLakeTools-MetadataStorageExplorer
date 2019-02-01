package eu.activageproject.tool.cucumber.stepdefs;

import eu.activageproject.tool.DataLakeToolApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = DataLakeToolApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
