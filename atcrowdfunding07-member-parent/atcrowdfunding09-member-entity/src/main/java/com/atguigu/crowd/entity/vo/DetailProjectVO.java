package com.atguigu.crowd.entity.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailProjectVO {
	
	private Integer projectId;
	private String projectName;
	private String projectDesc;
	private Integer followerCount;
	private Integer status;
	private Integer day;
	private String statusText;
	private Integer money;
	private Integer supportMoney;
	//完成度
	private Integer percentage;
	private String deployDate;
	//剩余天数
	private Integer lastDay;
	private Integer supporterCount;
	private String headerPicturePath;
	private List<String> detailPicturePathList;
	private List<DetailReturnVO> detailReturnVOList;

}
