$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("a.feature");
formatter.feature({
  "line": 1,
  "name": "Application login",
  "description": "",
  "id": "application-login",
  "keyword": "Feature"
});
formatter.before({
  "duration": 131160,
  "status": "passed"
});
formatter.scenario({
  "line": 3,
  "name": "Home page default login",
  "description": "",
  "id": "application-login;home-page-default-login",
  "type": "scenario",
  "keyword": "Scenario",
  "tags": [
    {
      "line": 2,
      "name": "@regression"
    },
    {
      "line": 2,
      "name": "@smoke"
    }
  ]
});
formatter.step({
  "line": 4,
  "name": "User us is on NetbBanking landing page",
  "keyword": "Given "
});
formatter.step({
  "line": 5,
  "name": "User login into application with username \"eeee\" and password  \"12345\"",
  "keyword": "When "
});
formatter.step({
  "line": 6,
  "name": "Home page is populated",
  "keyword": "Then "
});
formatter.step({
  "line": 7,
  "name": "Cards are displayed",
  "keyword": "And "
});
formatter.match({
  "location": "MyStephens.userUsIsOnNetbBankingLandingPage()"
});
formatter.result({
  "duration": 132771160,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "eeee",
      "offset": 43
    },
    {
      "val": "12345",
      "offset": 64
    }
  ],
  "location": "MyStephens.userLoginIntoApplicationWithUsernameAndPassword(String,String)"
});
formatter.result({
  "duration": 5088790,
  "status": "passed"
});
formatter.match({
  "location": "MyStephens.homePageIsPopulated()"
});
formatter.result({
  "duration": 50963,
  "status": "passed"
});
formatter.match({
  "location": "MyStephens.cardsAreDisplayed()"
});
formatter.result({
  "duration": 47802,
  "status": "passed"
});
formatter.after({
  "duration": 128000,
  "status": "passed"
});
formatter.uri("r.feature");
formatter.feature({
  "line": 1,
  "name": "Application login",
  "description": "",
  "id": "application-login",
  "keyword": "Feature"
});
formatter.before({
  "duration": 157629,
  "status": "passed"
});
formatter.background({
  "line": 2,
  "name": "",
  "description": "",
  "type": "background",
  "keyword": "Background"
});
formatter.step({
  "line": 3,
  "name": "validate browser",
  "keyword": "Given "
});
formatter.step({
  "line": 4,
  "name": "Browser is trigerred",
  "keyword": "When "
});
formatter.step({
  "line": 5,
  "name": "check if browser started",
  "keyword": "Then "
});
formatter.match({
  "location": "MyStephens.validate_browser()"
});
formatter.result({
  "duration": 112198,
  "status": "passed"
});
formatter.match({
  "location": "MyStephens.browser_is_trigerred()"
});
formatter.result({
  "duration": 74272,
  "status": "passed"
});
formatter.match({
  "location": "MyStephens.check_if_browser_started()"
});
formatter.result({
  "duration": 58469,
  "status": "passed"
});
formatter.scenario({
  "comments": [
    {
      "line": 6,
      "value": "#Scenario: Home page default login"
    },
    {
      "line": 7,
      "value": "#    Given User us is on NetbBanking landing page"
    },
    {
      "line": 8,
      "value": "#    When User login into application with username \"eeee\" and password  \"12345\""
    },
    {
      "line": 9,
      "value": "#    Then Home page is populated"
    },
    {
      "line": 10,
      "value": "#    And Cards are displayed"
    }
  ],
  "line": 12,
  "name": "Home page default login",
  "description": "",
  "id": "application-login;home-page-default-login",
  "type": "scenario",
  "keyword": "Scenario",
  "tags": [
    {
      "line": 11,
      "name": "@functional"
    },
    {
      "line": 11,
      "name": "@regression"
    }
  ]
});
formatter.step({
  "line": 13,
  "name": "User us is on NetbBanking landing page",
  "keyword": "Given "
});
formatter.step({
  "line": 14,
  "name": "User login into application with username \"john\" and password  \"6789\"",
  "keyword": "When "
});
formatter.step({
  "line": 15,
  "name": "Home page is populated",
  "keyword": "Then "
});
formatter.step({
  "line": 16,
  "name": "Cards are displayed",
  "keyword": "And "
});
formatter.match({
  "location": "MyStephens.userUsIsOnNetbBankingLandingPage()"
});
formatter.result({
  "duration": 104692,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "john",
      "offset": 43
    },
    {
      "val": "6789",
      "offset": 64
    }
  ],
  "location": "MyStephens.userLoginIntoApplicationWithUsernameAndPassword(String,String)"
});
formatter.result({
  "duration": 200297,
  "status": "passed"
});
formatter.match({
  "location": "MyStephens.homePageIsPopulated()"
});
formatter.result({
  "duration": 239013,
  "status": "passed"
});
formatter.match({
  "location": "MyStephens.cardsAreDisplayed()"
});
formatter.result({
  "duration": 43851,
  "status": "passed"
});
formatter.scenarioOutline({
  "comments": [
    {
      "line": 19,
      "value": "#  Scenario: Home page default login"
    },
    {
      "line": 20,
      "value": "#    Given User us is on NetbBanking landing page"
    },
    {
      "line": 21,
      "value": "#    When User sign up with following details"
    },
    {
      "line": 22,
      "value": "#    |janny|abcd|444| email@com|"
    },
    {
      "line": 23,
      "value": "#    |ann|cred|3334| email2@com|"
    },
    {
      "line": 24,
      "value": "#    Then Home page is populated"
    },
    {
      "line": 25,
      "value": "#    And Cards are displayed"
    }
  ],
  "line": 27,
  "name": "Home page default login",
  "description": "",
  "id": "application-login;home-page-default-login",
  "type": "scenario_outline",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 26,
      "name": "@smoke"
    }
  ]
});
formatter.step({
  "line": 28,
  "name": "User us is on NetbBanking landing page",
  "keyword": "Given "
});
formatter.step({
  "line": 29,
  "name": "User login in application with username \u003cusername\u003e and password \u003cpassword\u003e",
  "keyword": "When "
});
formatter.step({
  "line": 30,
  "name": "Home page is populated",
  "keyword": "Then "
});
formatter.examples({
  "line": 32,
  "name": "",
  "description": "",
  "id": "application-login;home-page-default-login;",
  "rows": [
    {
      "cells": [
        "username",
        "password"
      ],
      "line": 33,
      "id": "application-login;home-page-default-login;;1"
    },
    {
      "cells": [
        "user1",
        "pass1"
      ],
      "line": 34,
      "id": "application-login;home-page-default-login;;2"
    },
    {
      "cells": [
        "ser2",
        "pass2"
      ],
      "line": 35,
      "id": "application-login;home-page-default-login;;3"
    },
    {
      "cells": [
        "user3",
        "pass3"
      ],
      "line": 36,
      "id": "application-login;home-page-default-login;;4"
    }
  ],
  "keyword": "Examples"
});
formatter.before({
  "duration": 83358,
  "status": "passed"
});
formatter.background({
  "line": 2,
  "name": "",
  "description": "",
  "type": "background",
  "keyword": "Background"
});
formatter.step({
  "line": 3,
  "name": "validate browser",
  "keyword": "Given "
});
formatter.step({
  "line": 4,
  "name": "Browser is trigerred",
  "keyword": "When "
});
formatter.step({
  "line": 5,
  "name": "check if browser started",
  "keyword": "Then "
});
formatter.match({
  "location": "MyStephens.validate_browser()"
});
formatter.result({
  "duration": 76247,
  "status": "passed"
});
formatter.match({
  "location": "MyStephens.browser_is_trigerred()"
});
formatter.result({
  "duration": 104691,
  "status": "passed"
});
formatter.match({
  "location": "MyStephens.check_if_browser_started()"
});
formatter.result({
  "duration": 52939,
  "status": "passed"
});
formatter.scenario({
  "line": 34,
  "name": "Home page default login",
  "description": "",
  "id": "application-login;home-page-default-login;;2",
  "type": "scenario",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 26,
      "name": "@smoke"
    }
  ]
});
formatter.step({
  "line": 28,
  "name": "User us is on NetbBanking landing page",
  "keyword": "Given "
});
formatter.step({
  "line": 29,
  "name": "User login in application with username user1 and password pass1",
  "matchedColumns": [
    0,
    1
  ],
  "keyword": "When "
});
formatter.step({
  "line": 30,
  "name": "Home page is populated",
  "keyword": "Then "
});
formatter.match({
  "location": "MyStephens.userUsIsOnNetbBankingLandingPage()"
});
formatter.result({
  "duration": 52543,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "user1",
      "offset": 40
    },
    {
      "val": "pass1",
      "offset": 59
    }
  ],
  "location": "MyStephens.user_login_in_application_with_username_and_password(String,String)"
});
formatter.result({
  "duration": 151704,
  "status": "passed"
});
formatter.match({
  "location": "MyStephens.homePageIsPopulated()"
});
formatter.result({
  "duration": 97186,
  "status": "passed"
});
formatter.after({
  "duration": 32395,
  "status": "passed"
});
formatter.before({
  "duration": 62024,
  "status": "passed"
});
formatter.background({
  "line": 2,
  "name": "",
  "description": "",
  "type": "background",
  "keyword": "Background"
});
formatter.step({
  "line": 3,
  "name": "validate browser",
  "keyword": "Given "
});
formatter.step({
  "line": 4,
  "name": "Browser is trigerred",
  "keyword": "When "
});
formatter.step({
  "line": 5,
  "name": "check if browser started",
  "keyword": "Then "
});
formatter.match({
  "location": "MyStephens.validate_browser()"
});
formatter.result({
  "duration": 143803,
  "status": "passed"
});
formatter.match({
  "location": "MyStephens.browser_is_trigerred()"
});
formatter.result({
  "duration": 54914,
  "status": "passed"
});
formatter.match({
  "location": "MyStephens.check_if_browser_started()"
});
formatter.result({
  "duration": 50568,
  "status": "passed"
});
formatter.scenario({
  "line": 35,
  "name": "Home page default login",
  "description": "",
  "id": "application-login;home-page-default-login;;3",
  "type": "scenario",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 26,
      "name": "@smoke"
    }
  ]
});
formatter.step({
  "line": 28,
  "name": "User us is on NetbBanking landing page",
  "keyword": "Given "
});
formatter.step({
  "line": 29,
  "name": "User login in application with username ser2 and password pass2",
  "matchedColumns": [
    0,
    1
  ],
  "keyword": "When "
});
formatter.step({
  "line": 30,
  "name": "Home page is populated",
  "keyword": "Then "
});
formatter.match({
  "location": "MyStephens.userUsIsOnNetbBankingLandingPage()"
});
formatter.result({
  "duration": 43061,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "ser2",
      "offset": 40
    },
    {
      "val": "pass2",
      "offset": 58
    }
  ],
  "location": "MyStephens.user_login_in_application_with_username_and_password(String,String)"
});
formatter.result({
  "duration": 179358,
  "status": "passed"
});
formatter.match({
  "location": "MyStephens.homePageIsPopulated()"
});
formatter.result({
  "duration": 28049,
  "status": "passed"
});
formatter.after({
  "duration": 30815,
  "status": "passed"
});
formatter.before({
  "duration": 45432,
  "status": "passed"
});
formatter.background({
  "line": 2,
  "name": "",
  "description": "",
  "type": "background",
  "keyword": "Background"
});
formatter.step({
  "line": 3,
  "name": "validate browser",
  "keyword": "Given "
});
formatter.step({
  "line": 4,
  "name": "Browser is trigerred",
  "keyword": "When "
});
formatter.step({
  "line": 5,
  "name": "check if browser started",
  "keyword": "Then "
});
formatter.match({
  "location": "MyStephens.validate_browser()"
});
formatter.result({
  "duration": 60839,
  "status": "passed"
});
formatter.match({
  "location": "MyStephens.browser_is_trigerred()"
});
formatter.result({
  "duration": 61630,
  "status": "passed"
});
formatter.match({
  "location": "MyStephens.check_if_browser_started()"
});
formatter.result({
  "duration": 68345,
  "status": "passed"
});
formatter.scenario({
  "line": 36,
  "name": "Home page default login",
  "description": "",
  "id": "application-login;home-page-default-login;;4",
  "type": "scenario",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 26,
      "name": "@smoke"
    }
  ]
});
formatter.step({
  "line": 28,
  "name": "User us is on NetbBanking landing page",
  "keyword": "Given "
});
formatter.step({
  "line": 29,
  "name": "User login in application with username user3 and password pass3",
  "matchedColumns": [
    0,
    1
  ],
  "keyword": "When "
});
formatter.step({
  "line": 30,
  "name": "Home page is populated",
  "keyword": "Then "
});
formatter.match({
  "location": "MyStephens.userUsIsOnNetbBankingLandingPage()"
});
formatter.result({
  "duration": 96000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "user3",
      "offset": 40
    },
    {
      "val": "pass3",
      "offset": 59
    }
  ],
  "location": "MyStephens.user_login_in_application_with_username_and_password(String,String)"
});
formatter.result({
  "duration": 186469,
  "status": "passed"
});
formatter.match({
  "location": "MyStephens.homePageIsPopulated()"
});
formatter.result({
  "duration": 79012,
  "status": "passed"
});
formatter.after({
  "duration": 69531,
  "status": "passed"
});
});