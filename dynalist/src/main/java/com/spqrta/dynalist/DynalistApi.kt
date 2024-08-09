package com.spqrta.dynalist

import com.spqrta.dynalist.model.EditBody
import com.spqrta.dynalist.model.EditResponse
import com.spqrta.dynalist.model.GetBody
import com.spqrta.dynalist.model.GetResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface DynalistApi {

    @POST("doc/edit")
    suspend fun edit(
        @Body body: EditBody,
    ): EditResponse

    @POST("doc/read")
    suspend fun getDoc(
        @Body body: GetBody,
    ): GetResponse

}
