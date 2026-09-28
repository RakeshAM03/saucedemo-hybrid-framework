package testcases;

import java.util.ArrayList;

import java.util.Collections;

import java.util.List;

import org.testng.Assert;

import org.testng.annotations.Test;

import base.BaseClass;

import helper.DataProviders;

import pages.LoginPage;

import pages.ProductsPage;

public class SortTest extends BaseClass
{
	@Test(description = "Price (low to high) sort should order products by ascending price")
	public void sortPriceLowToHighTest()
	{
		String[] user = DataProviders.getDefaultUser();

		LoginPage loginPage = new LoginPage(getDriver());

		ProductsPage productsPage = loginPage.loginAs(user[0], user[1]);

		productsPage.sortBy("lohi");

		List<Double> prices = productsPage.getAllPrices();

		double minPrice = prices.stream()
				.mapToDouble(Double::doubleValue)
				.min()
				.getAsDouble();

		double firstPrice = productsPage.getFirstProductPrice();

		Assert.assertEquals(firstPrice, minPrice, 0.001,
				"First product price should be the lowest price after sorting low to high");

		List<Double> expectedOrder = new ArrayList<>(prices);

		Collections.sort(expectedOrder);

		Assert.assertEquals(prices, expectedOrder,
				"Prices are not in ascending order after sorting low to high");
	}
}
