package com.spqrta.dynalist

import com.spqrta.dynalyst.base.network.BaseRequestManager

object DynalistApiClient : BaseRequestManager() {

    override fun getBaseUrl() = "https://dynalist.io/api/v1/"

    val api = retrofit.create(DynalistApi::class.java)

}