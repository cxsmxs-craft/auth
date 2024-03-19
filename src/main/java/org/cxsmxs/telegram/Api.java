package org.cxsmxs.telegram;

import org.cxsmxs.telegram.entities.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface Api {
  @GET("deleteWebhook")
  Call<Response<Boolean>> deleteWebhook(
    @Query("drop_pending_updates") boolean dropPendingUpdates
  );

  @GET("sendMessage?parse_mode=HTML")
  Call<Response<Message>> sendMessage(
    @Query("chat_id") long chatId,
    @Query("text") String text,
    @Query("reply_to_message_id") Long replyToMessageId,
    @Query("disable_notification") Boolean disableNotification
  );

  @GET("getUpdates")
  Call<Response<List<Update>>> getUpdates(
    @Query("offset") long offset,
    @Query("limit") int limit,
    @Query("timeout") int timeout
  );

  @GET("getMe")
  Call<Response<User>> getMe();

  @POST("setMyCommands")
  Call<Response<Boolean>> setMyCommands(
    @Body SetMyCommands commands
  );
}