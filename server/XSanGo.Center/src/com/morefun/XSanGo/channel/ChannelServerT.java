package com.morefun.XSanGo.channel;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.NumberUtil;

/**
 * 渠道专属服务器配置
 * 
 * @author linyun.su
 * 
 */
@ExcelTable(fileName = "channel_server_list.xls", sheetName = "ChannelServer")
public class ChannelServerT {
	@ExcelColumn(index = 0)
	public int channelId;

	@ExcelColumn(index = 1)
	public String serverIdConfig;

	private String[] idRanges;

	/**
	 * 检查指定的编号是否包含在本模板配置里
	 * 
	 * @param id
	 * @return
	 */
	public boolean containsServer(int id) {
		if (this.idRanges == null) {
			this.idRanges = this.serverIdConfig.split(",");
		}

		for (String range : this.idRanges) {
			if (range.contains("-")) {
				String[] minMax = range.split("-");
				int min = NumberUtil.parseInt(minMax[0]);
				int max = NumberUtil.parseInt(minMax[1]);
				if (id >= min && id <= max) {
					return true;
				}
			} else {
				if (id == NumberUtil.parseInt(range)) {
					return true;
				}
			}

		}

		return false;
	}
}
