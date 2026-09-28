import java.io.FileOutputStream;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestDataGenerator
{
	public static void main(String[] args) throws IOException
	{
		Workbook workbook = new XSSFWorkbook();

		String[][] validLogin = {
				{ "username", "password" },
				{ "standard_user", "secret_sauce" },
				{ "problem_user", "secret_sauce" } };

		writeSheet(workbook, "validlogin", validLogin);

		String[][] lockedLogin = {
				{ "username", "password", "expectedError" },
				{ "locked_out_user", "secret_sauce", "Epic sadface: Sorry, this user has been locked out." } };

		writeSheet(workbook, "lockedlogin", lockedLogin);

		String[][] checkout = {
				{ "firstName", "lastName", "postalCode" },
				{ "Rakesh", "AM", "560001" } };

		writeSheet(workbook, "checkout", checkout);

		String path = System.getProperty("user.dir") + "/testdata/testdata.xlsx";

		try (FileOutputStream fos = new FileOutputStream(path))
		{
			workbook.write(fos);
		}

		workbook.close();

		System.out.println("Created " + path);
	}

	private static void writeSheet(Workbook workbook, String sheetName, String[][] table)
	{
		Sheet sheet = workbook.createSheet(sheetName);

		for (int i = 0; i < table.length; i++)
		{
			Row row = sheet.createRow(i);

			for (int j = 0; j < table[i].length; j++)
			{
				Cell cell = row.createCell(j);

				cell.setCellValue(table[i][j]);
			}
		}

		for (int j = 0; j < table[0].length; j++)
		{
			sheet.autoSizeColumn(j);
		}
	}
}
