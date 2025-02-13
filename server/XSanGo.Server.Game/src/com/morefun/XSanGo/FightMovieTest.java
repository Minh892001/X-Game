package com.morefun.XSanGo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.XSanGo.Protocol.FightMovieView;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.morefun.XSanGo.fightmovie.FightMovieValidater;
import com.morefun.XSanGo.fightmovie.FightMovieValidater.ValidateParam;
import com.morefun.XSanGo.util.HttpUtil;
import com.morefun.XSanGo.util.HttpUtil.ResponseContent;
import com.morefun.XSanGo.util.TextUtil;




/**
 *
 * @author qinguofeng
 */

public class FightMovieTest {
	public static void main(String[] args) throws IOException {
		BufferedReader brdata = null;
		BufferedReader brself = null;
		BufferedReader bropponent = null;
		BufferedReader brids = null;
		File mdata = new File("data.txt");
		File mself = new File("self.txt");
		File mopponent = new File("opponent.txt");
		File mids = new File("ids.txt");
		try {
			brdata = new BufferedReader(new FileReader(mdata));
			brself = new BufferedReader(new FileReader(mself));
			bropponent = new BufferedReader(new FileReader(mopponent));
			brids = new BufferedReader(new FileReader(mids));
			String linedata = brdata.readLine();
			String lineself = brself.readLine();
			String lineopponent = bropponent.readLine();
			String lineid = brids.readLine();
			// int index = 0;
			while (linedata != null) {
				try {
					if (linedata.length() > 0) {
						FightMovieView movieView = TextUtil.GSON
								.fromJson(TextUtil.ungzip(linedata),
										FightMovieView.class);

						PvpOpponentFormationView selfFormationView = TextUtil.GSON
								.fromJson(TextUtil.ungzip(lineself),
										PvpOpponentFormationView.class);
						PvpOpponentFormationView opponentFormationView = TextUtil.GSON
								.fromJson(TextUtil.ungzip(lineopponent),
										PvpOpponentFormationView.class);

						FightMovieValidater.ValidateParam param = new ValidateParam(
								selfFormationView, opponentFormationView, 1, "dev-1-511755", "qgfqgf.mf", movieView.soloMovie);
						param.setType(2);
						param.setExtra("103");
						
						String ssstr = TextUtil.GSON.toJson(param);
						System.out.println(ssstr);

						byte[] parambytes = TextUtil.GSON.toJson(param)
								.getBytes();
						// 参数总长度 = 阵容json数据长度 + 战报数据长度 + 4字节的阵容长度信息
						int length = parambytes.length
								+ movieView.validateMovie.length + 4;
						// 格式: [总长度][阵容json数据长度][阵容json数据][战报数据]
						ByteBuffer buffer = ByteBuffer.allocate(length + 4);
						// 放入长度信息
						buffer.putInt(length);
						buffer.putInt(parambytes.length);
						// 放入具体数据
						buffer.put(parambytes).put(movieView.validateMovie);
						buffer.rewind();

						byte[] data = buffer.array();

						ResponseContent checkRes = HttpUtil.doPost(
								"http://192.168.3.108:19080/push_validate?player_uuid=1231231&scene_id=3423", data);
//
						System.out.println(TextUtil.unicodeToString(checkRes.getContent()));
						System.out.println("save file.");
						// Calendar cal = Calendar.getInstance();
						// String tag = "" + cal.get(Calendar.DAY_OF_YEAR) +
						// cal.get(Calendar.HOUR_OF_DAY) +
						// cal.get(Calendar.MINUTE);
						File f = new File("reports/Report_PVP_" + lineid);
						boolean res = f.createNewFile();
						if (res) {
							FileOutputStream fos = new FileOutputStream(f);
							fos.write(data);
							fos.flush();
							fos.close();
						}
					}
					linedata = brdata.readLine();
					lineself = brself.readLine();
					lineopponent = bropponent.readLine();
					lineid = brids.readLine();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} finally {
			try {
				if (brdata != null) {
					brdata.close();
				}
				if (brself != null) {
					brself.close();
				}
				if (bropponent != null) {
					bropponent.close();
				}
				if (brids != null) {
					brids.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("End.");
		}
	}
}
