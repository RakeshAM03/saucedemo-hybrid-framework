package testcases;

import org.testng.Assert;

import org.testng.annotations.Test;

import base.BaseClass;

import helper.DataProviders;

import pages.LoginPage;

import pages.ProductsPage;

public class CartTest extends BaseClass
{
	@Test(description = "Adding two products should show 2 on the cart badge")
	public void addTwoProductsTest()
	{
		String[] user = DataProviders.getDefaultUser();

		LoginPage loginPage = new LoginPage(getDriver());

		ProductsPage productsPage = loginPage.loginAs(user[0], user[1]);

		Assert.assertTrue(productsPage.isOnProductsPage(),
				"Login failed, so the cart cannot be tested");

		Assert.assertEquals(productsPage.getCartBadgeCount(), 0,
				"Cart badge should be 0 before adding any product");

		productsPage.addProductsToCart(2);

		Assert.assertEquals(productsPage.getCartBadgeCount(), 2,
				"Cart badge should be 2 after adding two products");
	}
}
