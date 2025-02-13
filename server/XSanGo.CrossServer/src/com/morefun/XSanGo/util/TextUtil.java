/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.springframework.security.core.codec.Base64;

import com.google.gson.Gson;

/**
 * 字符串文本处理类
 * 
 * @author BruceSu
 * 
 */
public class TextUtil {
	public static final Gson GSON = new Gson();

	/**
	 * 格式化字符串
	 * 
	 * @param msg
	 * @param paramer
	 * @return
	 */
	public static String format(String msg, Object... paramers) {
		Object[] strParamers = new Object[paramers.length];
		for (int i = 0; i < paramers.length; i++) {
			Object paramer = paramers[i];
			strParamers[i] = paramer == null ? "NULL" : paramer.toString();
		}

		return MessageFormat.format(msg, strParamers);
	}

	/**
	 * 检测制定字符串是否为空
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isBlank(String text) {
		return text == null || text.isEmpty() || text.equals("");
	}

	/**
	 * 检测制定字符串是否为非空
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isNotBlank(String text) {
		return !isBlank(text);
	}

	/**
	 * 连接字符串
	 * 
	 * @param list
	 * @param symbol
	 */
	public static String join(Iterable<String> list, String symbol) {
		StringBuilder builder = new StringBuilder();
		int i = 0;
		for (String string : list) {
			if (i > 0) {
				builder.append(symbol);
			}

			builder.append(string);
			i++;
		}

		return builder.toString();
	}

	/**
	 * 过滤空白字符
	 * 
	 * @param str
	 * @return
	 */
	public static String filterBlank(String str) {
		String regEx = "\t|\r|\n";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("");
	}

	public static void main(String[] args) {
		// String test = "dd dd\taa\rbb\ncc";
		String test = "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";
		try {
			System.out.println(ungzip(test));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static byte[] objectToBytes(Serializable obj) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(baos);
			out.writeObject(obj);
			out.close();
		} catch (IOException e) {
			LogManager.error(e);
		}

		return baos.toByteArray();
	}

	public static Object bytesToObject(byte[] bytes) {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream in;
		Object obj = null;
		try {
			in = new ObjectInputStream(bais);
			obj = in.readObject();
			in.close();
		} catch (IOException e) {
			LogManager.error(e);
		} catch (ClassNotFoundException e) {
			LogManager.error(e);
		}
		return obj;
	}

	/**
	 * 通过一次对称的序列化反序列化操作来复制对象
	 * 
	 * @param instance
	 * @return
	 */
	public static <T extends Serializable> T clone(T instance) {
		return (T) bytesToObject(objectToBytes(instance));
	}

	public static String getHanzi(int number) {
		switch (number) {
		case 0:
			return "零";
		case 1:
			return "一";
		case 2:
			return "二";
		case 3:
			return "三";
		case 4:
			return "四";
		case 5:
			return "五";
		case 6:
			return "六";
		case 7:
			return "七";
		case 8:
			return "八";
		case 9:
			return "九";
		case 10:
			return "十";
		default:
			return null;
		}
	}

	/**
	 * 添加字符串，返回List
	 * 
	 * @param str
	 * @param addStr
	 * @return
	 */

	public static List<String> stringToList(String str) {
		List<String> resList;

		if (isBlank(str)) {
			resList = new ArrayList<String>();
		} else {
			resList = new ArrayList<String>(Arrays.asList(str.split(",")));
		}

		return resList;
	}

	public static List<String> stringToList(String str, String addStr) {
		List<String> resList = TextUtil.stringToList(str);

		resList.add(addStr);
		return resList;
	}

	/**
	 * gzip压缩
	 * 
	 * @param primStr
	 * @return
	 * @throws IOException
	 */
	public static String gzip(String primStr) throws IOException {
		if (primStr == null || primStr.length() == 0) {
			return primStr;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(primStr.getBytes());
		gzip.close();

		return new String(Base64.encode(out.toByteArray()));
	}

	/**
	 * gzip解压
	 * 
	 * @param compressedStr
	 * @return
	 * @throws IOException
	 */
	public static String ungzip(String compressedStr) throws IOException {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] compressed = Base64.decode(compressedStr.getBytes());
		ByteArrayInputStream in = new ByteArrayInputStream(compressed);
		GZIPInputStream ginzip = new GZIPInputStream(in);

		byte[] buffer = new byte[1024];
		int offset = -1;
		while ((offset = ginzip.read(buffer)) != -1) {
			out.write(buffer, 0, offset);
		}
		String decompressed = out.toString();
		ginzip.close();
		in.close();
		out.close();

		return decompressed;
	}

	/**
	 * zip压缩
	 * 
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static final String zip(String str) throws IOException {
		if (str == null) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ZipOutputStream zout = new ZipOutputStream(out);
		zout.putNextEntry(new ZipEntry("0"));
		zout.write(str.getBytes());
		zout.closeEntry();
		byte[] compressed = out.toByteArray();
		String compressedStr = new String(Base64.encode(compressed));
		zout.close();
		out.close();

		return compressedStr;
	}

	/**
	 * 使用zip进行解压缩
	 * 
	 * @param compressed
	 *            压缩后的文本
	 * @return 解压后的字符串
	 * @throws IOException
	 */
	public static final String unzip(String compressedStr) throws IOException {
		if (compressedStr == null) {
			return null;
		}

		byte[] compressed = Base64.decode(compressedStr.getBytes());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(compressed);
		ZipInputStream zin = new ZipInputStream(in);
		zin.getNextEntry();
		byte[] buffer = new byte[1024];
		int offset = -1;
		while ((offset = zin.read(buffer)) != -1) {
			out.write(buffer, 0, offset);
		}

		String decompressed = out.toString();
		zin.close();
		in.close();
		out.close();

		return decompressed;
	}

	/**
	 * unicode 编码转为中文
	 * 
	 * @param str
	 *            unicode编码字符串
	 * */
	public static String unicodeToString(String str) {

		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch + "");
		}
		return str;
	}
	//
	// public static void main(String[] args) {
	// String src =
	// "{\"view\":{\"id\":\"dev-1-312799\",\"index\":0,\"buffItemId\":\"dev-1-6657678\",\"postions\":[{\"index\":1,\"heroId\":\"dev-1-3471206\"},{\"index\":3,\"heroId\":\"dev-1-324143\"},{\"index\":4,\"heroId\":\"dev-1-324126\"},{\"index\":6,\"heroId\":\"dev-1-324117\"},{\"index\":7,\"heroId\":\"dev-1-3471146\"},{\"index\":9,\"heroId\":\"dev-1-345112\"},{\"index\":10,\"heroId\":\"dev-1-3471215\"},{\"index\":11,\"heroId\":\"dev-1-319687\"}],\"skills\":[],\"battlePower\":48658},\"heros\":[{\"id\":\"dev-1-3471206\",\"templateId\":2104,\"name\":\"真·孟获\",\"compositeCombat\":7410,\"level\":48,\"star\":5,\"qualityLevel\":4,\"exp\":0,\"levelupExp\":0,\"hpGrow\":0.0,\"powerGrow\":0.0,\"intelGrow\":0.0,\"qualityAddPercents\":0,\"properties\":[{\"grow\":0.0,\"code\":\"skills\",\"value\":42},{\"grow\":0.0,\"code\":\"brave\",\"value\":55},{\"grow\":0.0,\"code\":\"calm\",\"value\":20},{\"grow\":4448.1797,\"code\":\"hp\",\"value\":3190},{\"grow\":834.68,\"code\":\"armor\",\"value\":869},{\"grow\":297.0,\"code\":\"crit_res\",\"value\":0},{\"grow\":196.0,\"code\":\"resist\",\"value\":0},{\"grow\":4200.0,\"code\":\"hp%\",\"value\":0},{\"grow\":3900.0,\"code\":\"power%\",\"value\":0},{\"grow\":0.0,\"code\":\"hp_rec\",\"value\":391},{\"grow\":155.64,\"code\":\"magic\",\"value\":277},{\"grow\":800.0,\"code\":\"crit_res%\",\"value\":1844},{\"grow\":169.0,\"code\":\"crit\",\"value\":0},{\"grow\":0.0,\"code\":\"hit%\",\"value\":10000},{\"grow\":465.35,\"code\":\"power\",\"value\":320},{\"grow\":900.0,\"code\":\"crit%\",\"value\":1049},{\"grow\":2100.0,\"code\":\"armor%\",\"value\":0},{\"grow\":40.0,\"code\":\"anger_rec\",\"value\":50},{\"grow\":100.0,\"code\":\"magicDefPoro%\",\"value\":1142},{\"grow\":300.0,\"code\":\"defPoro%\",\"value\":5283},{\"grow\":0.0,\"code\":\"dodge%\",\"value\":0},{\"grow\":2800.0,\"code\":\"magic%\",\"value\":0},{\"grow\":0.0,\"code\":\"span\",\"value\":2400}],\"state\":\"InTheFormation\"},{\"id\":\"dev-1-324143\",\"templateId\":4201,\"name\":\"魔·吕布\",\"compositeCombat\":7276,\"level\":60,\"star\":3,\"qualityLevel\":5,\"exp\":0,\"levelupExp\":0,\"hpGrow\":0.0,\"powerGrow\":0.0,\"intelGrow\":0.0,\"qualityAddPercents\":0,\"properties\":[{\"grow\":0.0,\"code\":\"skills\",\"value\":24},{\"grow\":500.0,\"code\":\"anger_save%\",\"value\":0},{\"grow\":0.0,\"code\":\"brave\",\"value\":100},{\"grow\":0.0,\"code\":\"calm\",\"value\":15},{\"grow\":3171.8599,\"code\":\"hp\",\"value\":1654},{\"grow\":175.0,\"code\":\"armor\",\"value\":0},{\"grow\":234.0,\"code\":\"crit_res\",\"value\":0},{\"grow\":201.0,\"code\":\"resist\",\"value\":0},{\"grow\":3800.0,\"code\":\"hp%\",\"value\":0},{\"grow\":4200.0,\"code\":\"power%\",\"value\":0},{\"grow\":0.0,\"code\":\"hp_rec\",\"value\":355},{\"grow\":4950.0,\"code\":\"crit_damage%\",\"value\":0},{\"grow\":96.35,\"code\":\"magic\",\"value\":146},{\"grow\":0.0,\"code\":\"crit_res%\",\"value\":1187},{\"grow\":167.0,\"code\":\"crit\",\"value\":780},{\"grow\":0.0,\"code\":\"hit%\",\"value\":10000},{\"grow\":729.64,\"code\":\"power\",\"value\":395},{\"grow\":1000.0,\"code\":\"crit%\",\"value\":4807},{\"grow\":80.0,\"code\":\"anger_rec\",\"value\":74},{\"grow\":0.0,\"code\":\"magicDefPoro%\",\"value\":965},{\"grow\":400.0,\"code\":\"defPoro%\",\"value\":851},{\"grow\":0.0,\"code\":\"dodge%\",\"value\":0},{\"grow\":3100.0,\"code\":\"magic%\",\"value\":0},{\"grow\":0.0,\"code\":\"span\",\"value\":1900}],\"state\":\"InTheFormation\"},{\"id\":\"dev-1-324126\",\"templateId\":2201,\"name\":\"真·赵云\",\"compositeCombat\":6714,\"level\":46,\"star\":5,\"qualityLevel\":4,\"exp\":0,\"levelupExp\":0,\"hpGrow\":0.0,\"powerGrow\":0.0,\"intelGrow\":0.0,\"qualityAddPercents\":0,\"properties\":[{\"grow\":0.0,\"code\":\"skills\",\"value\":19},{\"grow\":500.0,\"code\":\"anger_save\",\"value\":0},{\"grow\":0.0,\"code\":\"brave\",\"value\":90},{\"grow\":0.0,\"code\":\"calm\",\"value\":50},{\"grow\":2810.69,\"code\":\"hp\",\"value\":2170},{\"grow\":290.0,\"code\":\"armor\",\"value\":0},{\"grow\":102.0,\"code\":\"crit_res\",\"value\":0},{\"grow\":245.0,\"code\":\"resist\",\"value\":0},{\"grow\":4300.0,\"code\":\"hp%\",\"value\":0},{\"grow\":3500.0,\"code\":\"power%\",\"value\":0},{\"grow\":360.0,\"code\":\"hp_rec\",\"value\":330},{\"grow\":119.2,\"code\":\"magic\",\"value\":236},{\"grow\":0.0,\"code\":\"crit_res%\",\"value\":658},{\"grow\":202.0,\"code\":\"crit\",\"value\":621},{\"grow\":0.0,\"code\":\"hit%\",\"value\":10000},{\"grow\":567.3,\"code\":\"power\",\"value\":510},{\"grow\":1500.0,\"code\":\"crit%\",\"value\":5309},{\"grow\":40.0,\"code\":\"anger_rec\",\"value\":50},{\"grow\":0.0,\"code\":\"magicDefPoro%\",\"value\":1436},{\"grow\":0.0,\"code\":\"defPoro%\",\"value\":1657},{\"grow\":1150.0,\"code\":\"dodge%\",\"value\":0},{\"grow\":2000.0,\"code\":\"magic%\",\"value\":0},{\"grow\":0.0,\"code\":\"span\",\"value\":1800}],\"state\":\"InTheFormation\"},{\"id\":\"dev-1-324117\",\"templateId\":2301,\"name\":\"真·刘备\",\"compositeCombat\":5107,\"level\":47,\"star\":3,\"qualityLevel\":4,\"exp\":0,\"levelupExp\":0,\"hpGrow\":0.0,\"powerGrow\":0.0,\"intelGrow\":0.0,\"qualityAddPercents\":0,\"properties\":[{\"grow\":0.0,\"code\":\"skills\",\"value\":20},{\"grow\":400.0,\"code\":\"anger_save\",\"value\":0},{\"grow\":500.0,\"code\":\"hp_cure%\",\"value\":0},{\"grow\":0.0,\"code\":\"brave\",\"value\":0},{\"grow\":0.0,\"code\":\"calm\",\"value\":0},{\"grow\":1754.6,\"code\":\"hp\",\"value\":1663},{\"grow\":277.0,\"code\":\"armor\",\"value\":329},{\"grow\":212.0,\"code\":\"crit_res\",\"value\":0},{\"grow\":179.0,\"code\":\"resist\",\"value\":0},{\"grow\":2200.0,\"code\":\"power%\",\"value\":0},{\"grow\":2800.0,\"code\":\"hp%\",\"value\":0},{\"grow\":0.0,\"code\":\"hp_rec\",\"value\":376},{\"grow\":99.479996,\"code\":\"magic\",\"value\":186},{\"grow\":400.0,\"code\":\"crit_res%\",\"value\":1341},{\"grow\":311.0,\"code\":\"crit\",\"value\":398},{\"grow\":0.0,\"code\":\"hit%\",\"value\":10000},{\"grow\":266.66,\"code\":\"power\",\"value\":225},{\"grow\":1800.0,\"code\":\"crit%\",\"value\":4487},{\"grow\":40.0,\"code\":\"anger_rec\",\"value\":50},{\"grow\":0.0,\"code\":\"defPoro%\",\"value\":2891},{\"grow\":300.0,\"code\":\"magicDefPoro%\",\"value\":1072},{\"grow\":0.0,\"code\":\"dodge%\",\"value\":0},{\"grow\":2200.0,\"code\":\"magic%\",\"value\":0},{\"grow\":0.0,\"code\":\"span\",\"value\":1700}],\"state\":\"InTheFormation\"},{\"id\":\"dev-1-3471146\",\"templateId\":2506,\"name\":\"真·祝融\",\"compositeCombat\":6163,\"level\":47,\"star\":5,\"qualityLevel\":4,\"exp\":0,\"levelupExp\":0,\"hpGrow\":0.0,\"powerGrow\":0.0,\"intelGrow\":0.0,\"qualityAddPercents\":0,\"properties\":[{\"grow\":0.0,\"code\":\"skills\",\"value\":16},{\"grow\":0.0,\"code\":\"brave\",\"value\":0},{\"grow\":0.0,\"code\":\"calm\",\"value\":0},{\"grow\":2603.51,\"code\":\"hp\",\"value\":1812},{\"grow\":295.24,\"code\":\"armor\",\"value\":0},{\"grow\":171.0,\"code\":\"crit_res\",\"value\":0},{\"grow\":188.0,\"code\":\"resist\",\"value\":0},{\"grow\":3700.0,\"code\":\"hp%\",\"value\":0},{\"grow\":2000.0,\"code\":\"power%\",\"value\":0},{\"grow\":500.0,\"code\":\"hp_rec\",\"value\":369},{\"grow\":1200.0,\"code\":\"resist_pierce%\",\"value\":0},{\"grow\":223.01999,\"code\":\"magic\",\"value\":342},{\"grow\":0.0,\"code\":\"crit_res%\",\"value\":1082},{\"grow\":183.0,\"code\":\"crit\",\"value\":637},{\"grow\":0.0,\"code\":\"hit%\",\"value\":10000},{\"grow\":390.6,\"code\":\"power\",\"value\":465},{\"grow\":1100.0,\"code\":\"crit%\",\"value\":5189},{\"grow\":2100.0,\"code\":\"armor%\",\"value\":0},{\"grow\":0.0,\"code\":\"anger_rec\",\"value\":50},{\"grow\":0.0,\"code\":\"defPoro%\",\"value\":1652},{\"grow\":500.0,\"code\":\"magicDefPoro%\",\"value\":1120},{\"grow\":0.0,\"code\":\"dodge%\",\"value\":0},{\"grow\":2900.0,\"code\":\"magic%\",\"value\":0},{\"grow\":0.0,\"code\":\"span\",\"value\":1600}],\"state\":\"InTheFormation\"},{\"id\":\"dev-1-345112\",\"templateId\":1506,\"name\":\"真·甄姬\",\"compositeCombat\":5348,\"level\":48,\"star\":5,\"qualityLevel\":4,\"exp\":0,\"levelupExp\":0,\"hpGrow\":0.0,\"powerGrow\":0.0,\"intelGrow\":0.0,\"qualityAddPercents\":0,\"properties\":[{\"grow\":0.0,\"code\":\"skills\",\"value\":18},{\"grow\":400.0,\"code\":\"anger_save\",\"value\":0},{\"grow\":600.0,\"code\":\"hp_cure%\",\"value\":0},{\"grow\":0.0,\"code\":\"brave\",\"value\":0},{\"grow\":0.0,\"code\":\"calm\",\"value\":0},{\"grow\":2050.5,\"code\":\"hp\",\"value\":1762},{\"grow\":180.0,\"code\":\"armor\",\"value\":0},{\"grow\":162.0,\"code\":\"crit_res\",\"value\":0},{\"grow\":260.0,\"code\":\"resist\",\"value\":672},{\"grow\":2500.0,\"code\":\"power%\",\"value\":0},{\"grow\":2200.0,\"code\":\"hp%\",\"value\":0},{\"grow\":440.0,\"code\":\"hp_rec\",\"value\":324},{\"grow\":137.88,\"code\":\"magic\",\"value\":477},{\"grow\":200.0,\"code\":\"crit_res%\",\"value\":1006},{\"grow\":274.0,\"code\":\"crit\",\"value\":637},{\"grow\":0.0,\"code\":\"hit%\",\"value\":10000},{\"grow\":323.0,\"code\":\"power\",\"value\":272},{\"grow\":1200.0,\"code\":\"crit%\",\"value\":5658},{\"grow\":170.0,\"code\":\"anger_rec\",\"value\":50},{\"grow\":0.0,\"code\":\"magicDefPoro%\",\"value\":3800},{\"grow\":200.0,\"code\":\"defPoro%\",\"value\":1058},{\"grow\":1200.0,\"code\":\"dodge%\",\"value\":0},{\"grow\":2200.0,\"code\":\"magic%\",\"value\":0},{\"grow\":0.0,\"code\":\"span\",\"value\":1700}],\"state\":\"InTheSupport\"},{\"id\":\"dev-1-3471215\",\"templateId\":1104,\"name\":\"真·曹洪\",\"compositeCombat\":5590,\"level\":45,\"star\":5,\"qualityLevel\":4,\"exp\":0,\"levelupExp\":0,\"hpGrow\":0.0,\"powerGrow\":0.0,\"intelGrow\":0.0,\"qualityAddPercents\":0,\"properties\":[{\"grow\":0.0,\"code\":\"skills\",\"value\":33},{\"grow\":1500.0,\"code\":\"hp_cure%\",\"value\":0},{\"grow\":500.0,\"code\":\"anger_save\",\"value\":0},{\"grow\":0.0,\"code\":\"brave\",\"value\":30},{\"grow\":0.0,\"code\":\"calm\",\"value\":85},{\"grow\":1923.75,\"code\":\"hp\",\"value\":2688},{\"grow\":147.0,\"code\":\"armor\",\"value\":360},{\"grow\":205.0,\"code\":\"crit_res\",\"value\":0},{\"grow\":945.0,\"code\":\"resist\",\"value\":828},{\"grow\":2400.0,\"code\":\"power%\",\"value\":0},{\"grow\":2900.0,\"code\":\"hp%\",\"value\":0},{\"grow\":0.0,\"code\":\"hp_rec\",\"value\":372},{\"grow\":351.56,\"code\":\"magic\",\"value\":292},{\"grow\":0.0,\"code\":\"crit_res%\",\"value\":1348},{\"grow\":45.0,\"code\":\"crit\",\"value\":118},{\"grow\":0.0,\"code\":\"hit%\",\"value\":10000},{\"grow\":113.16,\"code\":\"power\",\"value\":301},{\"grow\":900.0,\"code\":\"crit%\",\"value\":1072},{\"grow\":125.0,\"code\":\"anger_rec\",\"value\":50},{\"grow\":0.0,\"code\":\"magicDefPoro%\",\"value\":5535},{\"grow\":0.0,\"code\":\"defPoro%\",\"value\":2617},{\"grow\":0.0,\"code\":\"dodge%\",\"value\":0},{\"grow\":2400.0,\"code\":\"magic%\",\"value\":0},{\"grow\":0.0,\"code\":\"span\",\"value\":2400}],\"state\":\"InTheSupport\"},{\"id\":\"dev-1-319687\",\"templateId\":2401,\"name\":\"真·马良\",\"compositeCombat\":5050,\"level\":43,\"star\":3,\"qualityLevel\":4,\"exp\":0,\"levelupExp\":0,\"hpGrow\":0.0,\"powerGrow\":0.0,\"intelGrow\":0.0,\"qualityAddPercents\":0,\"properties\":[{\"grow\":0.0,\"code\":\"skills\",\"value\":10},{\"grow\":800.0,\"code\":\"hp_cure%\",\"value\":0},{\"grow\":0.0,\"code\":\"brave\",\"value\":0},{\"grow\":0.0,\"code\":\"calm\",\"value\":0},{\"grow\":789.06,\"code\":\"hp\",\"value\":1182},{\"grow\":125.0,\"code\":\"armor\",\"value\":0},{\"grow\":30.0,\"code\":\"crit_res\",\"value\":0},{\"grow\":45.0,\"code\":\"resist\",\"value\":668},{\"grow\":2400.0,\"code\":\"power%\",\"value\":0},{\"grow\":3300.0,\"code\":\"hp%\",\"value\":0},{\"grow\":300.0,\"code\":\"hp_rec\",\"value\":356},{\"grow\":437.47998,\"code\":\"magic\",\"value\":278},{\"grow\":0.0,\"code\":\"crit_res%\",\"value\":205},{\"grow\":25.0,\"code\":\"crit\",\"value\":537},{\"grow\":0.0,\"code\":\"hit%\",\"value\":10000},{\"grow\":209.23999,\"code\":\"power\",\"value\":133},{\"grow\":400.0,\"code\":\"crit%\",\"value\":3849},{\"grow\":0.0,\"code\":\"anger_rec\",\"value\":50},{\"grow\":0.0,\"code\":\"magicDefPoro%\",\"value\":3422},{\"grow\":1000.0,\"code\":\"defPoro%\",\"value\":836},{\"grow\":0.0,\"code\":\"dodge%\",\"value\":0},{\"grow\":2400.0,\"code\":\"magic%\",\"value\":0},{\"grow\":0.0,\"code\":\"span\",\"value\":1900}],\"state\":\"InTheSupport\"}]}";
	// String test1 = gzip(src);
	// String test2 = ungzip(test1);
	// System.out.println(src.equals(test2));
	//
	// String t3 = zip(src);
	// String t4 = unzip(t3);
	// System.out.println(src.equals(t4));
	// }
}
