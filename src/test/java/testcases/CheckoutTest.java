package testcases;

import org.testng.Assert;

import org.testng.annotations.Test;

import base.BaseClass;

import helper.DataProviders;

import pages.CartPage;

import pages.CheckoutCompletePage;

import pages.CheckoutInfoPage;

import pages.CheckoutOverviewPage;

import pages.LoginPage;

import pages.ProductsPage;

public class CheckoutTest extends BaseClass
{
	@Test(description = "User can buy two products end-to-end", dataProvider = "checkoutData", dataProviderClass = DataProviders.class)
	public void completeCheckoutTest(String firstName, String lastName, String postalCode)
	{
		String[] user = DataProviders.getDefaultUser();

		LoginPage loginPage = new LoginPage(getDriver());

		ProductsPage productsPage = loginPage.loginAs(user[0], user[1]);

		productsPage.addProductsToCart(2);

		CartPage cartPage = productsPage.openCart();

		Assert.assertEquals(cartPage.getCartItemCount(), 2,
				"Cart should contain 2 items before checkout");

		CheckoutInfoPage infoPage = cartPage.clickCheckout();

		CheckoutOverviewPage overviewPage = infoPage.fillDetails(firstName, lastName, postalCode);

		CheckoutCompletePage completePage = overviewPage.clickFinish();

		String confirmation = completePage.getConfirmationMessage();

		Assert.assertEquals(confirmation, "Thank you for your order!",
				"Order confirmation message is wrong");
	}
}
