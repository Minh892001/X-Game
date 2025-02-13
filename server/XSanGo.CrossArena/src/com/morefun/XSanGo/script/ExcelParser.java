/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.script;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.NotSupportedException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.springframework.core.io.ClassPathResource;

import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.NumberUtil;

/**
 * excel脚本解析
 * 
 * @author Su LingYun
 * 
 */
public class ExcelParser {

	/**
	 * 根据类型定义解析相应的EXCEL文件
	 * 
	 * @param type
	 * @return
	 */
	public static <T> List<T> parse(Class<T> type) {
		ExcelTable tableAnnotation = type.getAnnotation(ExcelTable.class);
		if (tableAnnotation == null) {
			throw new IllegalArgumentException();
		}

		return parse(tableAnnotation.sheetName(), type);
	}

	public static <T> List<T> parse(String sheetName, Class<T> type) {

		ExcelTable tableAnnotation = type.getAnnotation(ExcelTable.class);
		if (tableAnnotation == null) {
			throw new IllegalArgumentException();
		}
		Field[] fields = type.getFields();
		List<Field> fieldList = new ArrayList<Field>();
		for (Field field : fields) {
			ExcelColumn columnAnnotation = field
					.getAnnotation(ExcelColumn.class);
			ExcelComponet compont = field.getAnnotation(ExcelComponet.class);
			if (columnAnnotation != null || compont != null) {
				field.setAccessible(true);
				fieldList.add(field);
			}
		}
		Workbook wb = null;
		List<T> resultList = new ArrayList<T>();

		try {
			ClassPathResource cpr = new ClassPathResource(
					tableAnnotation.fileName());
			wb = Workbook.getWorkbook(cpr.getFile());
			// wb = Workbook.getWorkbook(new FileInputStream(tableAnnotation
			// .fileName()));

			Sheet sheet = wb.getSheet(sheetName);
			for (int rowIndex = tableAnnotation.beginRow(); rowIndex < sheet
					.getRows(); rowIndex++) {
				T obj = type.newInstance();
				Cell[] cells = sheet.getRow(rowIndex);
				if (cells == null || cells.length == 0) {
					break;
				}

				for (Field field : fieldList) {
					ExcelColumn columnAnnotation = field
							.getAnnotation(ExcelColumn.class);
					ExcelComponet component = field
							.getAnnotation(ExcelComponet.class);
					Object value = null;
					if (columnAnnotation != null) {
						String cell = getCellContentIgnoreException(cells,
								columnAnnotation.index());
						value = parseCell(field.getType(), cell,
								columnAnnotation.dataType());
					} else {
						if (!field.getType().isArray()) {
							throw new NotSupportedException(
									"Only support the array now.");
						}

						Class<?> componentType = field.getType()
								.getComponentType();
						value = Array.newInstance(componentType,
								component.size());
						Field[] subFields = componentType.getFields();

						for (int i = 0; i < component.size(); i++) {
							Object element = componentType.newInstance();
							for (Field subField : subFields) {
								subField.setAccessible(true);

								ExcelColumn subAnno = subField
										.getAnnotation(ExcelColumn.class);
								if (subAnno != null) {
									int index = component.index() + i
											* component.columnCount()
											+ subAnno.index();

									subField.set(
											element,
											parseCell(
													subField.getType(),
													getCellContentIgnoreException(
															cells, index),
													subAnno.dataType()));
								}
							}

							Array.set(value, i, element);
						}
					}

					field.set(obj, value);
				}
				resultList.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultList;

	}

	@SuppressWarnings("unchecked")
	private static <T> T parseCell(Class<T> dataType, String cell,
			DataFormat format) {
		Object value = cell.trim();
		if (dataType.equals(byte.class) || dataType.equals(Byte.class)) {
			value = (byte) NumberUtil.parseInt(cell);
		} else if (dataType.equals(int.class) || dataType.equals(Integer.class)) {
			value = NumberUtil.parseInt(cell);
		} else if (dataType.equals(float.class) || dataType.equals(Float.class)
				|| dataType.equals(double.class)
				|| dataType.equals(Double.class)) {
			// 这里单精度和双精度都用单精度表示，因为不可能要求精确到双精度
			value = NumberUtil.parseFloat(cell);
		} else if (dataType.equals(boolean.class)
				|| dataType.equals(Boolean.class)) {
			value = Boolean.parseBoolean(cell);
		} else if (dataType.equals(Date.class)) {
			switch (format) {
			case DateTime:
				value = DateUtil.parseDate(cell);
				break;
			case OnlyDate:
				value = DateUtil.parseDate("yyyy-MM-dd", cell);
				break;
			case OnlyTime:
				value = DateUtil.parseDate("HH:mm:ss", cell);
				break;
			default:
				break;
			}
		}

		return (T) value;
	}

	/**
	 * 获取单元格内容，无视越界异常，越界返回空
	 * 
	 * @param cells
	 * @param index
	 * @return
	 */
	private static String getCellContentIgnoreException(Cell[] cells, int index) {
		try {
			return cells[index].getContents().trim();
		} catch (ArrayIndexOutOfBoundsException e) {
			return "";
		}
	}
}
