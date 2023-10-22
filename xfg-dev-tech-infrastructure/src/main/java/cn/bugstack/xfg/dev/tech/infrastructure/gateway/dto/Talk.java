package cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Talk{

	@SerializedName("owner")
	private Owner owner;

	@SerializedName("text")
	private String text;
}