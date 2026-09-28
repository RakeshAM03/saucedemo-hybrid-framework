package testcases;

import org.testng.Assert;

import org.testng.annotations.Test;

import base.BaseClass;

import helper.DataProviders;

import pages.LoginPage;

import pages.ProductsPage;

public class LoginTest extends BaseClass
{
	@Test(description = "Valid users should land on the Products page", dataProvider = "validLogin", dataProviderClass = DataProviders.class)
	public void validLoginTest(String username, String password)
	{
		LoginPage loginPage = new LoginPage(getDriver());

		ProductsPage productsPage = loginPage.loginAs(username, password);

		Assert.assertTrue(productsPage.isOnProductsPage(),
				"User '" + username + "' did not land on the Products page. URL was: " + productsPage.getCurrentUrl());
	}

	@Test(description = "Locked-out user should see an error and stay on login", dataProvider = "lockedLogin", dataProviderClass = DataProviders.class)
	public void lockedOutUserTest(String username, String password, String expectedError)
	{
		LoginPage loginPage = new LoginPage(getDriver());

		loginPage.attemptLogin(username, password);

		String actualError = loginPage.getErrorMessage();

		Assert.assertEquals(actualError, expectedError,
				"Locked-out error message is wrong");

		String currentUrl = loginPage.getCurrentUrl();

		Assert.assertFalse(currentUrl.contains("inventory.html"),
				"Locked-out user reached the inventory page. URL was: " + currentUrl);

		Assert.assertTrue(loginPage.isLoginButtonDisplayed(),
				"Login button is not displayed, so the user may have left the login page");
	}
}
