/*
 * ============================================================================
 * ExcelReader
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   Opens testdata/testdata.xlsx and converts one sheet into Object[][]
 *   (rows x columns), skipping the header row. Every cell is returned as
 *   the text you SEE in Excel (DataFormatter), so 560001 stays "560001"
 *   instead of becoming "560001.0".
 *
 * USED BY:
 *   helper.DataProviders (every @DataProvider calls getData(sheetName)).
 *
 * WHERE IT SITS IN THE FLOW:
 *   TestNG asks DataProvider for data -> DataProviders -> ExcelReader
 *   -> Object[][] -> each row becomes one run of the @Test method.
 * ============================================================================
 */

// This class lives in the "helper" package
package helper;

// FileInputStream opens the Excel file on disk
import java.io.FileInputStream;

// IOException is thrown if the file cannot be read
import java.io.IOException;

// DataFormatter converts any cell (number, date, text) to the exact text Excel displays
import org.apache.poi.ss.usermodel.DataFormatter;

// Row represents one row of a sheet
import org.apache.poi.ss.usermodel.Row;

// Sheet represents one tab of the workbook
import org.apache.poi.ss.usermodel.Sheet;

// Workbook represents the whole Excel file
import org.apache.poi.ss.usermodel.Workbook;

// XSSFWorkbook is the POI implementation for the modern .xlsx format
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader
{
	// Full path to the test data file, starting from the project root
	private static final String EXCEL_PATH = System.getProperty("user.dir") + "/testdata/testdata.xlsx";

	/**
	 * Reads a whole sheet (except the header row) into a 2-D array.
	 *
	 * @param sheetName name of the sheet tab, e.g. "validlogin"
	 * @return Object[rows][columns] with every cell as a String; header row not included
	 * @throws RuntimeException if the file cannot be opened or the sheet does not exist
	 */
	public static Object[][] getData(String sheetName)
	{
		// try-with-resources: both the file stream and the workbook are closed automatically at the end
		try (FileInputStream fis = new FileInputStream(EXCEL_PATH);
				Workbook workbook = new XSSFWorkbook(fis))
		{
			// Find the sheet (tab) by its name
			Sheet sheet = workbook.getSheet(sheetName);

			// getSheet returns null if the name is wrong, so check it
			if (sheet == null)
			{
				// Stop with a clear message rather than a NullPointerException
				throw new RuntimeException("Sheet '" + sheetName + "' not found in " + EXCEL_PATH);
			}

			// getLastRowNum() is 0-based: header is row 0, so data row count = last index
			int rowCount = sheet.getLastRowNum();

			// Get the header row (row 0) to learn how many columns there are
			Row headerRow = sheet.getRow(0);

			// getLastCellNum() is already a count (last index + 1)
			int columnCount = headerRow.getLastCellNum();

			// Create the 2-D array: one line per data row, one slot per column
			Object[][] data = new Object[rowCount][columnCount];

			// DataFormatter turns each cell into the text Excel shows (numbers stay "560001")
			DataFormatter formatter = new DataFormatter();

			// Loop over data rows. Start at 1 to SKIP the header row
			for (int i = 1; i <= rowCount; i++)
			{
				// Get the current row from the sheet
				Row row = sheet.getRow(i);

				// Loop over every column of this row
				for (int j = 0; j < columnCount; j++)
				{
					// formatCellValue returns "" for a missing/blank cell, so no null checks needed.
					// i - 1 because array row 0 = Excel row 1 (the first data row)
					data[i - 1][j] = formatter.formatCellValue(row.getCell(j));
				}
			}

			// Hand the finished table back to the DataProvider
			return data;
		}
		// The file could not be opened or read
		catch (IOException e)
		{
			// Stop with a clear message; tests cannot run without their data
			throw new RuntimeException("Could not read Excel file " + EXCEL_PATH + " : " + e.getMessage(), e);
		}
	}
}
