package com.example.tryanimation.try_architecture_code.injection

import android.content.Context
import com.crocodic.core.data.CoreSession
import com.crocodic.core.helper.okhttp.SSLTrust
import com.example.tryanimation.try_architecture_code.api.ApiService
import com.example.tryanimation.try_architecture_code.database.MyDatabaseTry
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext

@Module
@InstallIn(SingletonComponent::class)
class ComponentModule {


    @Provides
    fun provideGson() =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        MyDatabaseTry.getMyDatabaseTry(context)


    @Provides
    fun provideUserDao(database: MyDatabaseTry) = database.userDao()


    @Provides
    fun provideSession(@ApplicationContext context: Context) =
        CoreSession(context)


    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl("http://3.1.6.193/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build().create(ApiService::class.java)
    }

    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        session: CoreSession,
    ): OkHttpClient {
        val unsafeTrustManager = SSLTrust().createUnsafeTrustManager()
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(unsafeTrustManager), null)

        val okHttpClient = OkHttpClient().newBuilder()
            .sslSocketFactory(sslContext.socketFactory, unsafeTrustManager)
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val accessToken = session.getString("token")

                val requestBuilder = original.newBuilder()
                    .header("Access-Token", accessToken)
                    .method(original.method, original.body)

                val request = requestBuilder.build()
                chain.proceed(request)
            }

        return okHttpClient.build()

    }
}