package framework;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * Class to encapsulate the excel data access layer of the framework
 * 
 * @author
 */
public class ExcelDataAccess {
	private final String filePath, fileName;

	private String datasheetName;

	/**
	 * Function to get the Excel sheet name
	 * 
	 * @return The Excel sheet name
	 */
	public String getDatasheetName() {
		return datasheetName;
	}

	/**
	 * Function to set the Excel sheet name
	 * 
	 * @param datasheetName The Excel sheet name
	 */
	public void setDatasheetName(String datasheetName) {
		this.datasheetName = datasheetName;
	}

	/**
	 * Constructor to initialize the excel data filepath and filename
	 * 
	 * @param filePath The absolute path where the excel data file is stored
	 * @param fileName The name of the excel data file (without the extension). Note
	 *                 that .xlsx files are not supported, only .xls files are
	 *                 supported
	 */
	public ExcelDataAccess(String filePath, String fileName) {
		this.filePath = filePath;
		this.fileName = fileName;
	}

	private void checkPreRequisites() {
		if (datasheetName == null) {
			System.out.println("ExcelDataAccess.datasheetName is not set!");
		}
	}

	private XSSFWorkbook openFileForReading() {
		String absoluteFilePath = filePath + Util.getFileSeparator() + fileName + ".xlsx";

		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(absoluteFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("The specified file \"" + absoluteFilePath + "\" does not exist!");
		}

		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(fileInputStream);
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error while opening the specified Excel workbook \"" + absoluteFilePath + "\"");
		}

		return workbook;
	}

	private void writeIntoFile(XSSFWorkbook workbook) {
		String absoluteFilePath = filePath + Util.getFileSeparator() + fileName + ".xlsx";

		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(absoluteFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("The specified file \"" + absoluteFilePath + "\" does not exist!");
		}

		try {
			workbook.write(fileOutputStream);
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error while writing into the specified Excel workbook \"" + absoluteFilePath + "\"");
		}
	}

	private XSSFSheet getWorkSheet(XSSFWorkbook workbook) {
		XSSFSheet worksheet = workbook.getSheet(datasheetName);
		if (worksheet == null) {
			System.out.println("The specified sheet \"" + datasheetName + "\"" + "does not exist within the workbook \""
					+ fileName + ".xlsx\"");
		}

		return worksheet;
	}

	/**
	 * Function to search for a specified key within a column, and return the
	 * corresponding row number
	 * 
	 * @param key         The value being searched for
	 * @param columnNum   The column number in which the key should be searched
	 * @param startRowNum The row number from which the search should start
	 * @return The row number in which the specified key is found (-1 if the key is
	 *         not found)
	 */
	public int getRowNum(String key, int columnNum, int startRowNum) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

		String currentValue;
		for (int currentRowNum = startRowNum; currentRowNum <= worksheet.getLastRowNum(); currentRowNum++) {

			XSSFRow row = worksheet.getRow(currentRowNum);
			XSSFCell cell = row.getCell(columnNum);
			currentValue = getCellValueAsString(cell, formulaEvaluator);

			if (currentValue.equals(key)) {
				return currentRowNum;
			}
		}

		return -1;
	}


	private String getCellValueAsString(XSSFCell cell, FormulaEvaluator formulaEvaluator) {
		if (cell == null || cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
			return "";
		} else {
			if (formulaEvaluator.evaluate(cell).getCellType() == XSSFCell.CELL_TYPE_ERROR) {
				System.out.println("Error in formula within this cell! " + "Error code: " + cell.getErrorCellValue());
			}

			DataFormatter dataFormatter = new DataFormatter();
			return dataFormatter.formatCellValue(formulaEvaluator.evaluateInCell(cell));
		}
	}

	/**
	 * Function to search for a specified key within a column, and return the
	 * corresponding row number
	 * 
	 * @param key       The value being searched for
	 * @param columnNum The column number in which the key should be searched
	 * @return The row number in which the specified key is found (-1 if the key is
	 *         not found)
	 */
	public int getRowNum(String key, int columnNum) {
		return getRowNum(key, columnNum, 0);
	}

	/**
	 * Function to get the last row number within the worksheet
	 * 
	 * @return The last row number within the worksheet
	 */
	public int getLastRowNum() {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		return worksheet.getLastRowNum();
	}

	/**
	 * Function to search for a specified key within a column, and return the
	 * corresponding occurence count
	 * 
	 * @param key         The value being searched for
	 * @param columnNum   The column number in which the key should be searched
	 * @param startRowNum The row number from which the search should start
	 * @return The occurence count of the specified key
	 */
	public int getRowCount(String key, int columnNum, int startRowNum) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

		int rowCount = 0;
		boolean keyFound = false;

		String currentValue;
		for (int currentRowNum = startRowNum; currentRowNum <= worksheet.getLastRowNum(); currentRowNum++) {

			XSSFRow row = worksheet.getRow(currentRowNum);
			XSSFCell cell = row.getCell(columnNum);
			currentValue = getCellValueAsString(cell, formulaEvaluator);

			if (currentValue.equals(key)) {
				rowCount++;
				keyFound = true;
			} else {
				if (keyFound) {
					break; 
				}
			}
		}

		return rowCount;
	}

	/**
	 * Function to search for a specified key within a column, and return the
	 * corresponding occurence count
	 * 
	 * @param key       The value being searched for
	 * @param columnNum The column number in which the key should be searched
	 * @return The occurence count of the specified key
	 */
	public int getRowCount(String key, int columnNum) {
		return getRowCount(key, columnNum, 0);
	}

	/**
	 * Function to search for a specified key within a row, and return the
	 * corresponding column number
	 * 
	 * @param key    The value being searched for
	 * @param rowNum The row number in which the key should be searched
	 * @return The column number in which the specified key is found (-1 if the key
	 *         is not found)
	 */
	public int getColumnNum(String key, int rowNum) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

		XSSFRow row = worksheet.getRow(rowNum);
		String currentValue;
		for (int currentColumnNum = 0; currentColumnNum < row.getLastCellNum(); currentColumnNum++) {

			XSSFCell cell = row.getCell(currentColumnNum);
			currentValue = getCellValueAsString(cell, formulaEvaluator);

			if (currentValue.equals(key)) {
				return currentColumnNum;
			}
		}

		return -1;
	}

	/**
	 * Function to get the value in the cell identified by the specified row and
	 * column numbers
	 * 
	 * @param rowNum    The row number of the cell
	 * @param columnNum The column number of the cell
	 * @return The value present in the cell
	 */
	public String getValue(int rowNum, int columnNum) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

		XSSFRow row = worksheet.getRow(rowNum);
		XSSFCell cell = row.getCell(columnNum);
		return getCellValueAsString(cell, formulaEvaluator);
	}

	/**
	 * Function to get the value in the cell identified by the specified row number
	 * and column header
	 * 
	 * @param rowNum       The row number of the cell
	 * @param columnHeader The column header of the cell
	 * @return The value present in the cell
	 */
	public String getValue(int rowNum, String columnHeader) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

		XSSFRow row = worksheet.getRow(0); // 0 because header is always in the
											// first row
		int columnNum = -1;
		String currentValue;
		for (int currentColumnNum = 0; currentColumnNum < row.getLastCellNum(); currentColumnNum++) {

			XSSFCell cell = row.getCell(currentColumnNum);
			currentValue = getCellValueAsString(cell, formulaEvaluator);

			if (currentValue.equals(columnHeader)) {
				columnNum = currentColumnNum;
				break;
			}
		}

		if (columnNum == -1) {
			System.out.println("The specified column header \"" + columnHeader + "\"" + "is not found in the sheet \""
					+ datasheetName + "\"!");
		} else {
			row = worksheet.getRow(rowNum);
			XSSFCell cell = row.getCell(columnNum);
			return getCellValueAsString(cell, formulaEvaluator);
		}
		return null;
	}



	/**
	 * Function to create a new Excel workbook
	 */
	public void createWorkbook() {
		XSSFWorkbook workbook = new XSSFWorkbook();

		writeIntoFile(workbook);
	}

	/**
	 * Function to add a sheet to the Excel workbook
	 * 
	 * @param sheetName The sheet name to be added
	 */
	public void addSheet(String sheetName) {
		XSSFWorkbook workbook = openFileForReading();

		XSSFSheet worksheet = workbook.createSheet(sheetName);
		worksheet.createRow(0); // include a blank row in the sheet created

		writeIntoFile(workbook);

		this.datasheetName = sheetName;
	}

	/**
	 * Function to add a new row to the Excel worksheet
	 * 
	 * @return The row number of the newly added row
	 */
	public int addRow() {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		int newRowNum = worksheet.getLastRowNum() + 1;
		worksheet.createRow(newRowNum);

		writeIntoFile(workbook);

		return newRowNum;
	}



	/**
	 * Function to merge the specified range of cells (all inputs are 0-based)
	 * 
	 * @param firstRow The first row
	 * @param lastRow  The last row
	 * @param firstCol The first column
	 * @param lastCol  The last column
	 */
	public void mergeCells(int firstRow, int lastRow, int firstCol, int lastCol) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		worksheet.addMergedRegion(cellRangeAddress);

		writeIntoFile(workbook);
	}

	/**
	 * Function to specify whether the row summaries appear below the detail within
	 * an outline (grouped set of rows)
	 * 
	 * @param rowSumsBelow Boolean value to specify row summaries below detail
	 *                     within an outline
	 */
	public void setRowSumsBelow(boolean rowSumsBelow) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		worksheet.setRowSumsBelow(rowSumsBelow);

		writeIntoFile(workbook);
	}

	/**
	 * Function to outline (i.e., group together) the specified rows
	 * 
	 * @param firstRow The first row
	 * @param lastRow  The last row
	 */
	public void groupRows(int firstRow, int lastRow) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		worksheet.groupRow(firstRow, lastRow);

		writeIntoFile(workbook);
	}


}