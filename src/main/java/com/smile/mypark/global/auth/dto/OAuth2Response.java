package com.smile.mypark.global.auth.dto;

public interface OAuth2Response {

	String getProvider();

	String getProviderId();

	String getEmail();

	String getName();
}