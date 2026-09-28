/*
 * ============================================================================
 * TestDataGenerator  (one-time tool - NOT part of the framework)
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   Creates testdata/testdata.xlsx with the sheets validlogin, lockedlogin
 *   and checkout. Run it only if the Excel file needs to be rebuilt.
 *
 * USED BY:
 *   Nobody at test time. It lives in tools/ (outside src/) so Maven never
 *   compiles or runs it. ExcelReader later READS the file this tool WRITES.
 *
 * HOW TO RUN (from the project root):
 *   mvn -q dependency:build-classpath -Dmdep.outputFile=target/cp.txt
 *   java -cp "$(cat target/cp.txt)" tools/TestDataGenerator.java
 * ============================================================================
 */

// FileOutputStream writes bytes to a file on disk
import java.io.FileOutputStream;

// IOException is thrown if the file cannot be written
import java.io.IOException;

// Cell represents one cell of a row
import org.apache.poi.ss.usermodel.Cell;

// Row represents one row of a sheet
import org.apache.poi.ss.usermodel.Row;

// Sheet represents one tab of the workbook
import org.apache.poi.ss.usermodel.Sheet;

// Workbook represents the whole Excel file
import org.apache.poi.ss.usermodel.Workbook;

// XSSFWorkbook creates the modern .xlsx format
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestDataGenerator
{
	/**
	 * Entry point: builds all three sheets and saves the workbook.
	 *
	 * @param args not used
	 * @throws IOException if the file cannot be written
	 */
	public static void main(String[] args) throws IOException
	{
		// Create a new, empty .xlsx workbook in memory
		Workbook workbook = new XSSFWorkbook();

		// Sheet 1: valid users. First inner array = header row, the rest = data rows
		String[][] validLogin = {
				{ "username", "password" },
				{ "standard_user", "secret_sauce" },
				{ "problem_user", "secret_sauce" } };

		// Write the validlogin sheet
		writeSheet(workbook, "validlogin", validLogin);

		// Sheet 2: locked user and the exact error SauceDemo shows
		String[][] lockedLogin = {
				{ "username", "password", "expectedError" },
				{ "locked_out_user", "secret_sauce", "Epic sadface: Sorry, this user has been locked out." } };

		// Write the lockedlogin sheet
		writeSheet(workbook, "lockedlogin", lockedLogin);

		// Sheet 3: checkout form details
		String[][] checkout = {
				{ "firstName", "lastName", "postalCode" },
				{ "Rakesh", "AM", "560001" } };

		// Write the checkout sheet
		writeSheet(workbook, "checkout", checkout);

		// Path where the framework expects the file
		String path = System.getProperty("user.dir") + "/testdata/testdata.xlsx";

		// Open the output file; try-with-resources closes it automatically
		try (FileOutputStream fos = new FileOutputStream(path))
		{
			// Save the in-memory workbook into the file
			workbook.write(fos);
		}

		// Free the memory held by the workbook
		workbook.close();

		// Confirm success in the console
		System.out.println("Created " + path);
	}

	/**
	 * Creates one sheet and fills it with the given table of text.
	 *
	 * @param workbook  the workbook to add the sheet to
	 * @param sheetName name of the new sheet tab
	 * @param table     rows x columns of text; row 0 is the header
	 */
	private static void writeSheet(Workbook workbook, String sheetName, String[][] table)
	{
		// Add a new tab with this name
		Sheet sheet = workbook.createSheet(sheetName);

		// Loop over every row of the table
		for (int i = 0; i < table.length; i++)
		{
			// Create row number i in the sheet
			Row row = sheet.createRow(i);

			// Loop over every column of this row
			for (int j = 0; j < table[i].length; j++)
			{
				// Create the cell at column j
				Cell cell = row.createCell(j);

				// Store the value as TEXT (even "560001"), so Excel keeps it as a string
				cell.setCellValue(table[i][j]);
			}
		}

		// Loop over the columns once more
		for (int j = 0; j < table[0].length; j++)
		{
			// Widen each column to fit its content, so the file is readable when opened
			sheet.autoSizeColumn(j);
		}
	}
}
