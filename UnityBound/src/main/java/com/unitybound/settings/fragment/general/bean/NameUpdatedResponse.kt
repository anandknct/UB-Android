package com.unitybound.settings.fragment.general.bean

import com.google.gson.annotations.SerializedName

data class NameUpdatedResponse(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)