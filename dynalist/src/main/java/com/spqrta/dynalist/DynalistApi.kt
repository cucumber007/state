package com.spqrta.dynalist

import com.spqrta.dynalist.model.DynalistDocRemote
import com.spqrta.dynalist.model.DynalistDocsRemote
import com.spqrta.dynalist.model.EditBody
import com.spqrta.dynalist.model.EditResponse
import com.spqrta.dynalist.model.GetDocBody
import com.spqrta.dynalist.model.GetDocsBody
import retrofit2.http.Body
import retrofit2.http.POST

interface DynalistApi {

    @POST("doc/edit")
    suspend fun edit(
        @Body body: EditBody,
    ): EditResponse

    @POST("doc/read")
    suspend fun getDoc(
        @Body body: GetDocBody,
    ): DynalistDocRemote

    @POST("doc/read")
    suspend fun getDocs(
        @Body body: GetDocsBody,
    ): DynalistDocsRemote

}
