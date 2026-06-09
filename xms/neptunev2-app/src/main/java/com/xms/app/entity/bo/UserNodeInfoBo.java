package com.xms.app.entity.bo;

import lombok.Data;

import java.util.List;

@Data
public class UserNodeInfoBo {

	/**
	 * 节点质押信息
	 */
	private List<DayInfo> dayInfo;

	@Data
	public static class DayInfo {
		/**
		 * 总共天数
		 */
		private Integer totalDay;
		/**
		 * 剩余天数
		 */
		private Integer haveDay;
	}
}

