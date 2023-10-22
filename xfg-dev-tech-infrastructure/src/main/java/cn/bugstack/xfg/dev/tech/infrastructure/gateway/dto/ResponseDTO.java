package cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ResponseDTO {

	@SerializedName("resp_data")
	private RespData respData;

	@SerializedName("succeeded")
	private boolean succeeded;
}