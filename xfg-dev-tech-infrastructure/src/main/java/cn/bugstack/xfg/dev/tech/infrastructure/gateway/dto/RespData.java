package cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RespData{

	@SerializedName("topics")
	private List<TopicsItem> topics;
}