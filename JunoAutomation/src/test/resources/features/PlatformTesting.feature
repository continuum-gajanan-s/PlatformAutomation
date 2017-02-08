Feature: Test Casandra

@Test1 
Scenario: Start performance services
Given Start Remote connection
When Start platform services
And Start casandra server
And Close Remote Connection


@Test
Scenario: Test performance webservice
Given Call performance webservice
Then Verify Performance data