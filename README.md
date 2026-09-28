# SauceDemo Hybrid Selenium Automation Framework

[![UI Tests](https://github.com/RakeshAM03/saucedemo-hybrid-framework/actions/workflows/tests.yml/badge.svg)](https://github.com/RakeshAM03/saucedemo-hybrid-framework/actions/workflows/tests.yml)
![Java 21](https://img.shields.io/badge/Java-21-blue) ![Selenium 4.49](https://img.shields.io/badge/Selenium-4.49.0-43B02A) ![TestNG 7.12](https://img.shields.io/badge/TestNG-7.12.0-orange)

**Interview Kit (framework walkthrough, E2E flow, 195 Q&As):** https://rakesham03.github.io/Interview.io/
**Portfolio:** https://rakesham03.github.io/

A hybrid (Page Object Model + Data-Driven + TestNG) UI test automation framework built with **Java 21**, **Selenium 4**, and **TestNG**. Test data is read from Excel, configuration is externalized to a properties file, and HTML reports with on-failure screenshots are generated using **ChainTest**.

The framework automates the demo shop:
`https://www.saucedemo.com`

Covered scenarios: valid login, locked-out login, add to cart, full checkout, and price sorting.

---

## Tech Stack

| Tool / Library | Version | Purpose |
|----------------|---------|---------|
| Java           | 21      | Language |
| Selenium Java  | 4.49.0  | Browser automation (Selenium Manager handles drivers) |
| TestNG         | 7.12.0  | Test runner & assertions |
| Apache POI     | 5.5.1   | Reading Excel (`.xlsx`) test data |
| ChainTest      | 1.0.12  | HTML reporting & screenshot embedding |
| Maven Surefire | 3.5.6   | Runs the TestNG suite from `xmlfiles/${suitefile}` (3.6.0 ignores testng.xml) |
| Maven          | 3.x     | Build & dependency management |

---

## Project Structure

```
saucedemo-hybrid-framework/
├── config/
│   └── config.properties              # Environment, browser, waits & screenshot settings
├── testdata/
│   └── testdata.xlsx                  # Sheets: validlogin, lockedlogin, checkout
├── tools/
│   └── TestDataGenerator.java         # One-time generator for testdata.xlsx (not compiled by Maven)
├── xmlfiles/
│   └── testng.xml                     # TestNG suite: listeners + test classes
├── src/
│   ├── main/java/
│   │   ├── base/
│   │   │   ├── BaseClass.java         # @BeforeMethod/@AfterMethod, ThreadLocal driver
│   │   │   └── BasePage.java          # Explicit-wait-backed actions (click, type, getText ...)
│   │   ├── factory/
│   │   │   └── BrowserFactory.java    # Launches Chrome/Firefox/Edge with options
│   │   ├── pages/                     # Page Object classes (fluent, By locators)
│   │   │   ├── LoginPage.java
│   │   │   ├── ProductsPage.java
│   │   │   ├── CartPage.java
│   │   │   ├── CheckoutInfoPage.java
│   │   │   ├── CheckoutOverviewPage.java
│   │   │   └── CheckoutCompletePage.java
│   │   ├── helper/
│   │   │   ├── ConfigReader.java      # Loads config once; -D overrides win
│   │   │   ├── ExcelReader.java       # Reads a sheet into Object[][] (header skipped)
│   │   │   ├── DataProviders.java     # TestNG @DataProvider methods
│   │   │   └── Utility.java           # Screenshot (file + Base64)
│   │   └── listeners/
│   │       └── ReportListener.java    # Logs events, embeds screenshots in ChainTest
│   └── test/
│       ├── java/testcases/
│       │   ├── LoginTest.java
│       │   ├── CartTest.java
│       │   ├── CheckoutTest.java
│       │   └── SortTest.java
│       └── resources/
│           └── chaintest.properties   # ChainTest report configuration
├── reports/chaintest/                 # Generated HTML reports (Index.html, Email.html)
├── screenshots/                       # Captured screenshots
├── .gitignore
├── README.md
└── pom.xml
```

---

## Prerequisites

- **JDK 21** (or newer) installed and `JAVA_HOME` set
- **Maven 3.x** installed
- A supported browser installed: **Chrome** (default), **Firefox**, or **Edge**
  - Selenium Manager downloads the matching driver automatically, so there's nothing to install by hand.

Verify your setup:

```bash
java -version
mvn -version
```

---

## Configuration

All runtime behavior is controlled from `config/config.properties`. **Any key can be overridden on the command line** with `-Dkey=value`.

| Key | Description | Default |
|-----|-------------|---------|
| `qaenv` | Application base URL | `https://www.saucedemo.com` |
| `browser` | Browser to run on | `chrome` (`firefox`, `edge`) |
| `headless` | Run browser without a window | `false` |
| `pageloadtime` | Page load timeout (seconds) | `60` |
| `implicitwait` | Implicit wait (seconds). Kept at 0 because only explicit waits are used | `0` |
| `explicitwait` | Explicit wait used by `BasePage` (seconds) | `10` |
| `screenshot_on_failure` | Screenshot on test failure | `true` |
| `screenshot_on_success` | Screenshot on test pass | `false` |
| `screenshot_on_skip` | Screenshot on skip | `false` |
| `retry` | Retry count (reserved) | `0` |

### Test Data

`testdata/testdata.xlsx` (row 1 of each sheet is the header):

| Sheet | Columns | Used by |
|-------|---------|---------|
| `validlogin` | username, password | `LoginTest.validLoginTest`; row 1 is also the default user for Cart/Checkout/Sort tests |
| `lockedlogin` | username, password, expectedError | `LoginTest.lockedOutUserTest` |
| `checkout` | firstName, lastName, postalCode | `CheckoutTest.completeCheckoutTest` |

To rebuild the file:

```bash
mvn -q dependency:build-classpath -Dmdep.outputFile=target/cp.txt
java -cp "$(cat target/cp.txt)" tools/TestDataGenerator.java
```

---

## How to Run

### 1. Run the full TestNG suite

```bash
mvn clean test -Dsuitefile=testng.xml
```

`suitefile` defaults to `testng.xml` in `pom.xml`, so `mvn clean test` also works.

### 2. Run with a different browser (no file edit needed)

```bash
mvn clean test -Dbrowser=firefox
```

### 3. Run in headless mode

```bash
mvn clean test -Dheadless=true
```

### 4. Run a single test class

```bash
mvn clean test -Dtest=CartTest
```

> `-Dtest` makes Surefire ignore `testng.xml`, so the listeners are not attached and no ChainTest report or screenshots are produced. Use it for quick debugging only. For a reported run of a subset, create another suite file (e.g. `xmlfiles/smoke.xml`) and pass `-Dsuitefile=smoke.xml`.

### 5. Run from an IDE (Eclipse / IntelliJ / VS Code)

- Import as an existing **Maven** project.
- Right-click `xmlfiles/testng.xml` → **Run As → TestNG Suite**, or
- Right-click a class in `src/test/java/testcases/` → **Run As → TestNG Test**.

---

## Reports

After a run, open the generated HTML reports:

- **ChainTest report:** `reports/chaintest/Index.html`
- **Email-friendly report:** `reports/chaintest/Email.html`
- **Surefire/TestNG reports:** `target/surefire-reports/index.html`

Reports include pass/fail status, logs, and embedded screenshots (per the `screenshot_on_*` flags). Screenshots are also saved to `screenshots/<testName>_<timestamp>.png`.

---

## How It Works (Flow)

1. `mvn test` → Surefire reads `xmlfiles/${suitefile}` and starts TestNG.
2. `testng.xml` registers `ChainTestListener` and `ReportListener`, then runs the 4 test classes.
3. For each test method, `BaseClass` (`@BeforeMethod`) reads config via `ConfigReader`, launches a browser via `BrowserFactory`, stores it in a `ThreadLocal`, and opens `qaenv`.
4. If the test has a `dataProvider`, `DataProviders` loads rows from `testdata.xlsx` via `ExcelReader`; the test runs once per row.
5. The test drives fluent page objects (`LoginPage` → `ProductsPage` → `CartPage` → ...). Each extends `BasePage` for explicit-wait-backed actions.
6. TestNG `Assert` verifies the outcome.
7. `ReportListener` logs the result and, according to the config flags, takes a screenshot with `Utility` and embeds it into ChainTest.
8. `BaseClass` (`@AfterMethod`) quits the browser.
9. At the end of the suite, ChainTest writes `reports/chaintest/Index.html`.

---

## Extending the Framework

- **Add a new page:** create a class in `src/main/java/pages/` extending `BasePage`, define `By` locators, return the next page object from navigation methods.
- **Add a new test:** create a class in `src/test/java/testcases/` extending `BaseClass`, then register it under `<classes>` in `xmlfiles/testng.xml`.
- **Add new test data:** add a sheet to `testdata/testdata.xlsx` and a matching `@DataProvider` in `DataProviders.java`.
- **Run in parallel:** add `parallel="methods" thread-count="3"` to `<suite>` in `testng.xml`; the `ThreadLocal` driver keeps sessions isolated.

---

## Continuous Integration

`.github/workflows/tests.yml` runs the full suite headless on GitHub Actions for every push and pull request:

```bash
mvn -B clean test -Dsuitefile=testng.xml -Dheadless=true -Dbrowser=chrome
```

The ChainTest report and screenshots are attached to each run as the **chaintest-report** artifact (Actions tab → run → Artifacts).

### Scheduled runs

The suite also runs automatically **four times a day: 6:00 AM, 12:00 PM, 6:00 PM and 12:00 AM IST** (cron `30 0,6,12,18 * * *` in UTC). GitHub may start scheduled runs a few minutes late when its runners are busy.
