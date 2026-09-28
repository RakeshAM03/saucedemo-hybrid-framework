package helper;

import java.io.FileInputStream;

import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader
{
	private static final String EXCEL_PATH = System.getProperty("user.dir") + "/testdata/testdata.xlsx";

	public static Object[][] getData(String sheetName)
	{
		try (FileInputStream fis = new FileInputStream(EXCEL_PATH);
				Workbook workbook = new XSSFWorkbook(fis))
		{
			Sheet sheet = workbook.getSheet(sheetName);

			if (sheet == null)
			{
				throw new RuntimeException("Sheet '" + sheetName + "' not found in " + EXCEL_PATH);
			}

			int rowCount = sheet.getLastRowNum();

			Row headerRow = sheet.getRow(0);

			int columnCount = headerRow.getLastCellNum();

			Object[][] data = new Object[rowCount][columnCount];

			DataFormatter formatter = new DataFormatter();

			for (int i = 1; i <= rowCount; i++)
			{
				Row row = sheet.getRow(i);

				for (int j = 0; j < columnCount; j++)
				{
					data[i - 1][j] = formatter.formatCellValue(row.getCell(j));
				}
			}

			return data;
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not read Excel file " + EXCEL_PATH + " : " + e.getMessage(), e);
		}
	}
}
