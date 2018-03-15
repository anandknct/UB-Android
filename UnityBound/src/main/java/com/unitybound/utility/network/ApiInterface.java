package com.unitybound.utility.network;


import com.unitybound.account.beans.ProfileFriendsResponse;
import com.unitybound.account.beans.addPost.AddPostResponse;
import com.unitybound.account.beans.coverUpdate.CoverPicUpdateResponse;
import com.unitybound.account.beans.hidePost.HidePostResponse;
import com.unitybound.account.beans.photos.ProfilePhotosResponse;
import com.unitybound.account.beans.profile.ProfileAboutResponse;
import com.unitybound.account.beans.timeline.TimelineResponse;
import com.unitybound.church.beans.ChurchListResponse;
import com.unitybound.church.beans.JoinByAccessCodeResponse;
import com.unitybound.church.beans.blockChurchMember.BlockChurchMemberResponse;
import com.unitybound.church.beans.blockChurchMember.UnBlockChurchMemberResponse;
import com.unitybound.church.beans.blockedUsers.ChurchBlockedUsersListResponse;
import com.unitybound.church.beans.churchDetail.ChurchDetailResponse;
import com.unitybound.church.beans.churchJoinedMembers.ChurchJoinedMembersResponse;
import com.unitybound.church.beans.churchRequestedMembers.ChurchJoinRequestedListResponse;
import com.unitybound.church.beans.coverPhotoAdd.AddCoverPhotoResponse;
import com.unitybound.church.beans.deleteMember.DeleteChurchMemberResponse;
import com.unitybound.church.beans.detailMembers.ChurchDetailMembersResponse;
import com.unitybound.church.beans.joinRequest.JoinChurchResponse;
import com.unitybound.church.beans.leaveChurch.LeaveChurchResponse;
import com.unitybound.church.beans.libraryAddFile.AddLibraryResponse;
import com.unitybound.church.beans.libraryListMode.LibraryListResponse;
import com.unitybound.church.beans.refreshAccessCode.RefreshAccessCodeResponse;
import com.unitybound.events.fragment.beans.EventsListResponse;
import com.unitybound.events.fragment.beans.addEvent.AddEventResponse;
import com.unitybound.events.fragment.beans.eventDetailAbout.EventDetailAboutResponse;
import com.unitybound.events.fragment.beans.eventDetailParticipants.EventDetailParticipantsResponse;
import com.unitybound.events.fragment.beans.eventDiscussion.EventDetailDiscussionResponse;
import com.unitybound.events.fragment.beans.eventStatusUpdate.EventStatusUpdateResponse;
import com.unitybound.events.fragment.beans.updateEvent.UpdateEventResponse;
import com.unitybound.forgotpassword.beans.ForgetPassowordResponse;
import com.unitybound.groups.beans.MyGroupsResponse;
import com.unitybound.groups.beans.PublicGroups.AllPublicGroupsResponse;
import com.unitybound.groups.beans.addGroup.AddGroupResponse;
import com.unitybound.groups.beans.blockGroupMember.BlockGroupMemberResponse;
import com.unitybound.groups.beans.blockUserList.BlockedUserListResponse;
import com.unitybound.groups.beans.deleteGroupMember.DeleteGroupMemberResponse;
import com.unitybound.groups.beans.groupComments.GroupCommentsResponse;
import com.unitybound.groups.beans.groupMembers.GroupMembersResponse;
import com.unitybound.groups.beans.groupPhotos.GroupPhotosResponse;
import com.unitybound.groups.beans.joinGroup.JoinGroupResponse;
import com.unitybound.groups.beans.joinedGroups.JoinedGroupsResponse;
import com.unitybound.login.beans.LoginResponse;
import com.unitybound.login.beans.socialLogin.SocialLoginResponse;
import com.unitybound.main.friendrequest.beans.FriendRequestListResponse;
import com.unitybound.main.friendrequest.beans.RejectReq.RejectReqResponse;
import com.unitybound.main.home.fragment.beans.addCommentsList.AddCommentsResponse;
import com.unitybound.main.home.fragment.beans.bookmarkPost.BookmarkPostResponse;
import com.unitybound.main.home.fragment.beans.commentsPost.CommentsPostResponse;
import com.unitybound.main.home.fragment.beans.commentsReplyList.CommentsReplyListResponse;
import com.unitybound.main.home.fragment.beans.favUnfav.FavouriteUnfavResponse;
import com.unitybound.main.home.fragment.beans.filterPost.FilterPostResponse;
import com.unitybound.main.home.fragment.beans.homeFeeds.AllPostResponse;
import com.unitybound.main.home.fragment.beans.like.LikePostResponse;
import com.unitybound.main.home.fragment.beans.like_comment.LikeCommentResponse;
import com.unitybound.main.home.fragment.beans.prayerAnswer.PrayerAnswerResponse;
import com.unitybound.main.my.prayer.request.model.AllPrayersResponse;
import com.unitybound.main.my.prayer.request.model.archive.ArchiveResponse;
import com.unitybound.main.search.beans.SearchResponse;
import com.unitybound.notification.beans.NotificationListResponse;
import com.unitybound.obtiuaries.beans.ObituariesListResponse;
import com.unitybound.obtiuaries.beans.detail.ObituariesDetailResponse;
import com.unitybound.settings.fragment.beans.UsersSettingsResponse;
import com.unitybound.settings.fragment.general.bean.NameUpdatedResponse;
import com.unitybound.signup.beans.SignUpResponse;
import com.unitybound.weddings.beans.WeadingDetailResponse.WeddingDetailResponse;
import com.unitybound.weddings.beans.addWedding.AddWeddingResponse;
import com.unitybound.weddings.beans.allWeddings.AllWeaddingsResponse;
import com.unitybound.weddings.beans.myWeddings.MyWeddingsResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

/**
 * Created @Author by 1Nikhiljogdand@gmail.com
 */
public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> sendLoginRequest(@Field("api_key") String api_key,
                                         @Field("username") String username,
                                         @Field("password") String password,
                                         @Field("fcmtoken") String fcmtoken);

    @FormUrlEncoded
    @POST("register")
    Call<SignUpResponse> registrationRequest(@Field("api_key") String api_key,
                                             @Field("first_name") String first_name,
                                             @Field("last_name") String last_name,
                                             @Field("email") String email,
                                             @Field("username") String username,
                                             @Field("password") String password,
                                             @Field("date_of_birth") String date_of_birth,
                                             @Field("access_code") String access_code,
                                             @Field("gender") String gender,
                                             @Field("relationship_status") String relationship_status,
                                             @Field("fcmtoken") String fcmtoken);


    @FormUrlEncoded
    @POST("social-login")
    Call<SocialLoginResponse> socialLoginRequest(@Field("api_key") String api_key,
                                                 @Field("email") String email,
                                                 @Field("provider") String provider,
                                                 @Field("first_name") String first_name,
                                                 @Field("last_name") String last_name,
                                                 @Field("profileImage") String profileImage,
                                                 @Field("accountid") String accountid,
                                                 @Field("fcmtoken") String fcmtoken);

    @FormUrlEncoded
    @POST("forget-password")
    Call<ForgetPassowordResponse> forgotPasswordRequest(@Field("api_key") String api_key,
                                                        @Field("email") String email);

    @FormUrlEncoded
    @POST("all-post")
    Call<AllPostResponse> sendAllPostRequest(@Field("api_key") String api_key,
                                             @Field("user_id") String username);

    @FormUrlEncoded
    @POST("comment-post")
    Call<CommentsPostResponse> getAllPostComments(@Field("api_key") String api_key,
                                                  @Field("user_id") String username,
                                                  @Field("post_id") String post_id);

    @FormUrlEncoded
    @POST("reply-comment-list")
    Call<CommentsReplyListResponse> getAllPostCommentsReplys(@Field("api_key") String api_key,
                                                             @Field("user_id") String username,
                                                             @Field("comment_id") String comment_id);

    @FormUrlEncoded
    @POST("filter-post")
    Call<FilterPostResponse> filterAllPostRequest(@Field("api_key") String api_key,
                                                  @Field("user_id") String username,
                                                  @Field("type") String type,
                                                  @Field("filterArea") String filterArea);

    @Multipart
    @POST("add-post")
    Call<AddPostResponse> addPostFeed(@PartMap() Map<String, RequestBody> partMap,
                                      @Part MultipartBody.Part file);

    @Multipart
    @POST("edit-post")
    Call<AddPostResponse> editPostFeed(@PartMap() Map<String, RequestBody> partMap,
                                       @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("like-post")
    Call<LikePostResponse> likePostRequest(@Field("api_key") String api_key,
                                           @Field("user_id") String username,
                                           @Field("post_id") String post_id);

    @FormUrlEncoded
    @POST("like-comment")
    Call<LikeCommentResponse> likeCommentRequest(@Field("api_key") String api_key,
                                                 @Field("user_id") String username,
                                                 @Field("comment_id") String comment_id);

    @FormUrlEncoded
    @POST("reply-comment")
    Call<AddCommentsResponse> replyCommentRequest(@Field("api_key") String api_key,
                                               @Field("user_id") String user_id,
                                               @Field("comment_id") String comment_id,
                                               @Field("comment") String comment);

    @FormUrlEncoded
    @POST("reply-comment-like")
    Call<LikeCommentResponse> replyCommentlikePostRequest(@Field("api_key") String api_key,
                                                       @Field("user_id") String username,
                                                       @Field("comment_id") String comment_id,
                                                       @Field("reply_comment_id") String reply_comment_id);

    @FormUrlEncoded
    @POST("bookmark-post")
    Call<BookmarkPostResponse> bookmarkPostRequest(@Field("api_key") String api_key,
                                                   @Field("user_id") String username,
                                                   @Field("post_id") String post_id);

    @FormUrlEncoded
    @POST("prayerAnswered")
    Call<PrayerAnswerResponse> PrayerAnsweredRequest(@Field("api_key") String api_key,
                                                     @Field("user_id") String username,
                                                     @Field("post_id") String post_id,
                                                     @Field("comment") String coomments);

    @FormUrlEncoded
    @POST("notification-list")
    Call<NotificationListResponse> notificationListRequest(@Field("api_key") String api_key,
                                                           @Field("user_id") String username);

    @FormUrlEncoded
    @POST("friend-request-list")
    Call<FriendRequestListResponse> friendRequestList(@Field("api_key") String api_key,
                                                      @Field("user_id") String username);


    @FormUrlEncoded
    @POST("cancel-friend-request")
    Call<RejectReqResponse> cancelFriendRequest(@Field("api_key") String api_key,
                                                @Field("user_id") String username,
                                                @Field("send_by_user_id") String send_by_user_id);

    @FormUrlEncoded
    @POST("accept-friend-request")
    Call<RejectReqResponse> acceptFriendRequest(@Field("api_key") String api_key,
                                                @Field("user_id") String username,
                                                @Field("_id") String accepted_user_id);

    @FormUrlEncoded
    @POST("profile-friends")
    Call<ProfileFriendsResponse> profileFriendRequestList(@Field("api_key") String api_key,
                                                          @Field("user_id") String username,
                                                          @Field("profile_user_id") String profile_user_id);

    @FormUrlEncoded
    @POST("all-church")
    Call<ChurchListResponse> getChurchList(@Field("api_key") String api_key,
                                           @Field("user_id") String username);

    @FormUrlEncoded
    @POST("invite-members")
    Call<DeleteChurchMemberResponse> churchSendInvite(@Field("api_key") String api_key,
                                              @Field("user_id") String userId,
                                              @Field("church_id") String mCHURCH_ID,
                                              @Field("churchFriends") String FrondJsonString);

    @FormUrlEncoded
    @POST("near-me")
    Call<ChurchListResponse> getNearByChurchList(@Field("api_key") String api_key,
                                                @Field("user_id") String username,
                                                 @Field("radius") String radius);

    @FormUrlEncoded
    @POST("my-church")
    Call<ChurchListResponse> getMyChurchList(@Field("api_key") String api_key,
                                             @Field("user_id") String username);

    @FormUrlEncoded
    @POST("follow-church")
    Call<ChurchListResponse> getFollowChurchList(@Field("api_key") String api_key,
                                                 @Field("user_id") String username);

    @FormUrlEncoded
    @POST("church-details")
    Call<ChurchDetailResponse> getChurchDetail(@Field("api_key") String api_key,
                                               @Field("user_id") String username,
                                               @Field("church_id") String church_id);

    @FormUrlEncoded
    @POST("church-members")
    Call<ChurchDetailMembersResponse> getChurchDetailMembers(@Field("api_key") String api_key,
                                                             @Field("user_id") String username,
                                                             @Field("church_id") String church_id);

    @FormUrlEncoded
    @POST("church-members")
    Call<ChurchJoinedMembersResponse> getChurchJoinedMembers(@Field("api_key") String api_key,
                                                             @Field("user_id") String username,
                                                             @Field("church_id") String church_id);

    @FormUrlEncoded
    @POST("block-member-church")
    Call<BlockChurchMemberResponse> blockChurchMembersMembers(@Field("api_key") String api_key,
                                                              @Field("user_id") String username,
                                                              @Field("church_id") String church_id,
                                                              @Field("block_user_id") String block_user_id);

    @FormUrlEncoded
    @POST("unblock-member-church")
    Call<UnBlockChurchMemberResponse> unblockChurchMembersMembers(@Field("api_key") String api_key,
                                                                  @Field("user_id") String username,
                                                                  @Field("church_id") String church_id,
                                                                  @Field("block_user_id") String block_user_id);

    @FormUrlEncoded
    @POST("delete-member-church")
    Call<DeleteChurchMemberResponse> deleteChurchMembersMembers(@Field("api_key") String api_key,
                                                                @Field("user_id") String username,
                                                                @Field("church_id") String church_id,
                                                                @Field("delete_user_id") String delete_user_id);

    @FormUrlEncoded
    @POST("favorite-post")
    Call<FavouriteUnfavResponse> changeFavoriteUnfav(@Field("api_key") String api_key,
                                                     @Field("user_id") String username,
                                                     @Field("post_id") String post_id);

    @FormUrlEncoded
    @POST("church-requested-members")
    Call<ChurchJoinRequestedListResponse> getChurchRequestedMembers(@Field("api_key") String api_key,
                                                                    @Field("user_id") String username,
                                                                    @Field("church_id") String church_id);

    @FormUrlEncoded
    @POST("church-blocked-members")
    Call<ChurchBlockedUsersListResponse> getChurchBlockedMembers(@Field("api_key") String api_key,
                                                                 @Field("user_id") String username,
                                                                 @Field("church_id") String church_id);

    @FormUrlEncoded
    @POST("join-church-request")
    Call<JoinChurchResponse> joinChurch(@Field("api_key") String api_key,
                                        @Field("user_id") String username,
                                        @Field("church_id") String church_id);

    @FormUrlEncoded
    @POST("join-church-by-access-code")
    Call<JoinByAccessCodeResponse> joinChurchByAccessCode(@Field("api_key") String api_key,
                                                          @Field("user_id") String username,
                                                          @Field("church_id") String church_id,
                                                          @Field("access_code") String access_code);

    @FormUrlEncoded
    @POST("refreashAccessCode")
    Call<RefreshAccessCodeResponse> refreshAccessCode(@Field("api_key") String api_key,
                                                      @Field("user_id") String username,
                                                      @Field("church_id") String church_id);

    @Multipart
    @POST("add-church")
    Call<JoinByAccessCodeResponse> addChurch(@PartMap() Map<String, RequestBody> partMap,
                                             @Part MultipartBody.Part file);

    @Multipart
    @POST("edit-church")
    Call<JoinByAccessCodeResponse> editChurchData(@PartMap() Map<String, RequestBody> partMap,
                                             @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("leave-church")
    Call<LeaveChurchResponse> leaveChurch(@Field("api_key") String api_key,
                                          @Field("user_id") String username,
                                          @Field("church_id") String church_id);

    @FormUrlEncoded
    @POST("all-events")
    Call<EventsListResponse> getAllEventsList(@Field("api_key") String api_key,
                                              @Field("user_id") String username);

    @FormUrlEncoded
    @POST("my-events")
    Call<EventsListResponse> getMyEventsList(@Field("api_key") String api_key,
                                             @Field("user_id") String username);

    @FormUrlEncoded
    @POST("private-events")
    Call<EventsListResponse> getPrivateEventsList(@Field("api_key") String api_key,
                                                  @Field("user_id") String username);

    @FormUrlEncoded
    @POST("accepted-events")
    Call<EventsListResponse> getAcceptedEventsList(@Field("api_key") String api_key,
                                                   @Field("user_id") String username);

    @Multipart
    @POST("add-event")
    Call<AddEventResponse> addEvent(@PartMap() Map<String, RequestBody> partMap,
                                    @Part MultipartBody.Part file);

    @Multipart
    @POST("update-event")
    Call<UpdateEventResponse> updateEvent(@PartMap() Map<String, RequestBody> partMap,
                                          @Part MultipartBody.Part file);

    @Multipart
    @POST("add-group")
    Call<AddGroupResponse> addGroup(@PartMap() Map<String, RequestBody> partMap,
                                    @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("event-about-page")
    Call<EventDetailAboutResponse> getEventDetailAbout(@Field("api_key") String api_key,
                                                       @Field("user_id") String username,
                                                       @Field("event_id") String event_id);

    @FormUrlEncoded
    @POST("event-member-status-update")
    Call<EventStatusUpdateResponse> getEventMemberStatusUpdate(@Field("api_key") String api_key,
                                                               @Field("user_id") String username,
                                                               @Field("event_id") String event_id,
                                                               @Field("status") String status);

    @FormUrlEncoded
    @POST("event-comment-page")
    Call<EventDetailDiscussionResponse> getEventComment(@Field("api_key") String api_key,
                                                        @Field("user_id") String username,
                                                        @Field("event_id") String event_id);

    @FormUrlEncoded
    @POST("event-member-page")
    Call<EventDetailParticipantsResponse> getEventDetailParticipants(@Field("api_key") String api_key,
                                                                     @Field("user_id") String username,
                                                                     @Field("event_id") String event_id);


    @FormUrlEncoded
    @POST("prayer-list")
    Call<AllPrayersResponse> getPrayerList(@Field("api_key") String api_key,
                                           @Field("user_id") String username);

    @FormUrlEncoded
    @POST("archived-prayer-list")
    Call<AllPrayersResponse> getArchivePrayerList(@Field("api_key") String api_key,
                                                  @Field("user_id") String username,
                                                  @Field("page_no") String page_no);

    @FormUrlEncoded
    @POST("my-prayer-list")
    Call<AllPrayersResponse> getMyPrayerList(@Field("api_key") String api_key,
                                             @Field("user_id") String username,
                                             @Field("page_no") String page_no);

    @FormUrlEncoded
    @POST("my-groups")
    Call<MyGroupsResponse> getMyGroupsList(@Field("api_key") String api_key,
                                           @Field("user_id") String username);

    @FormUrlEncoded
    @POST("joined-groups")
    Call<JoinedGroupsResponse> getMyJoinedGroupsList(@Field("api_key") String api_key,
                                                     @Field("user_id") String username);

    @FormUrlEncoded
    @POST("closed-groups")
    Call<AllPublicGroupsResponse> getClosedGroupsList(@Field("api_key") String api_key,
                                                      @Field("user_id") String username);

    @FormUrlEncoded
    @POST("all-public-groups")
    Call<AllPublicGroupsResponse> getAllPublicGroupsList(@Field("api_key") String api_key,
                                                         @Field("user_id") String username);

    @FormUrlEncoded
    @POST("group-comments")
    Call<GroupCommentsResponse> getGroupComments(@Field("api_key") String api_key,
                                                 @Field("user_id") String username,
                                                 @Field("group_id") String group_id);

    @FormUrlEncoded
    @POST("block-group-member")
    Call<BlockGroupMemberResponse> BlockGroupMembers(@Field("api_key") String api_key,
                                                     @Field("user_id") String username,
                                                     @Field("group_id") String event_id,
                                                     @Field("block_user_id") String block_user_id);

    @FormUrlEncoded
    @POST("unblock-group-member")
    Call<BlockGroupMemberResponse> unBlockGroupMembers(@Field("api_key") String api_key,
                                                       @Field("user_id") String user_id,
                                                       @Field("group_id") String group_id,
                                                       @Field("block_user_id") String block_user_id);

    @FormUrlEncoded
    @POST("group-blocked-members")
    Call<BlockedUserListResponse> getBlockGroupMembers(@Field("api_key") String api_key,
                                                       @Field("user_id") String username,
                                                       @Field("group_id") String group_id);

    @FormUrlEncoded
    @POST("delete-group-member")
    Call<DeleteGroupMemberResponse> deleteGroupMembers(@Field("api_key") String api_key,
                                                       @Field("user_id") String user_id,
                                                       @Field("group_id") String group_id,
                                                       @Field("delete_user_id") String delete_user_id);

    @FormUrlEncoded
    @POST("join-group")
    Call<JoinGroupResponse> joinGroupRequest(@Field("api_key") String api_key,
                                             @Field("user_id") String username,
                                             @Field("group_id") String event_id);

    @FormUrlEncoded
    @POST("leave-group")
    Call<JoinGroupResponse> leaveGroupRequest(@Field("api_key") String api_key,
                                              @Field("user_id") String username,
                                              @Field("group_id") String event_id);

    @FormUrlEncoded
    @POST("group-members")
    Call<GroupMembersResponse> groupMembersRequest(@Field("api_key") String api_key,
                                                   @Field("user_id") String username,
                                                   @Field("group_id") String event_id);

    @FormUrlEncoded
    @POST("group-photos")
    Call<GroupPhotosResponse> groupPhotosRequest(@Field("api_key") String api_key,
                                                 @Field("user_id") String username,
                                                 @Field("group_id") String event_id);

    @FormUrlEncoded
    @POST("obituary")
    Call<ObituariesListResponse> getAllObituary(@Field("api_key") String api_key,
                                                @Field("user_id") String username);

    @FormUrlEncoded
    @POST("friends-obituary")
    Call<ObituariesListResponse> getFriendsObituary(@Field("api_key") String api_key,
                                                    @Field("user_id") String username);

    @FormUrlEncoded
    @POST("my-obituary")
    Call<ObituariesListResponse> getMyObituary(@Field("api_key") String api_key,
                                               @Field("user_id") String username);

    @FormUrlEncoded
    @POST("obituary-details")
    Call<ObituariesDetailResponse> getObituaryDetail(@Field("api_key") String api_key,
                                                     @Field("user_id") String username,
                                                     @Field("obituary_id") String obituary_id);

    @FormUrlEncoded
    @POST("all-wedding")
    Call<AllWeaddingsResponse> getWeaddingList(@Field("api_key") String api_key,
                                               @Field("user_id") String username);

    @Multipart
    @POST("add-wedding")
    Call<AddWeddingResponse> addWeadding(@PartMap() Map<String, RequestBody> partMap,
                                         @Part MultipartBody.Part file,
                                         @Part MultipartBody.Part[] weddingImagesArrayParts);

    @Multipart
    @POST("update-wedding")
    Call<AddWeddingResponse> updateWeadding(@PartMap() Map<String, RequestBody> partMap,
                                            @Part MultipartBody.Part file,
                                            @Part MultipartBody.Part[] weddingImagesArrayParts);


    @FormUrlEncoded
    @POST("friends-wedding")
    Call<AllWeaddingsResponse> getFriendWeaddingList(@Field("api_key") String api_key,
                                                     @Field("user_id") String username);

    @FormUrlEncoded
    @POST("wedding-details")
    Call<WeddingDetailResponse> getWeaddingDetail(@Field("api_key") String api_key,
                                                  @Field("user_id") String username,
                                                  @Field("wedding_id") String wedding_id);

    @FormUrlEncoded
    @POST("my-wedding")
    Call<MyWeddingsResponse> getMyWeaddingList(@Field("api_key") String api_key,
                                               @Field("user_id") String username);

    @FormUrlEncoded
    @POST("friends-wedding")
    Call<MyWeddingsResponse> getFriendsWeaddingList(@Field("api_key") String api_key,
                                                    @Field("user_id") String username);

    @FormUrlEncoded
    @POST("profile-timeline")
    Call<TimelineResponse> getTimeline(@Field("api_key") String api_key,
                                       @Field("user_id") String username,
                                       @Field("profile_user_id") String profile_user_id);

    @FormUrlEncoded
    @POST("profile-about")
    Call<ProfileAboutResponse> getProfileAbout(@Field("api_key") String api_key,
                                               @Field("user_id") String username,
                                               @Field("profile_user_id") String profile_user_id);

    @FormUrlEncoded
    @POST("profile-photos")
    Call<ProfilePhotosResponse> getProfilePhotos(@Field("api_key") String api_key,
                                                 @Field("user_id") String username,
                                                 @Field("profile_user_id") String profile_user_id);

    @Multipart
    @POST("profile-cover-photo-update")
    Call<CoverPicUpdateResponse> updateCoverPhoto(@PartMap() Map<String, RequestBody> partMap,
                                                  @Part MultipartBody.Part file);

    @Multipart
    @POST("add-cover-image-church")
    Call<AddCoverPhotoResponse> addChurchCoverPhoto(@PartMap() Map<String, RequestBody> partMap,
                                                    @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("setting-name-update")
    Call<NameUpdatedResponse> updateName(@Field("api_key") String api_key,
                                         @Field("user_id") String username,
                                         @Field("first_name") String first_name,
                                         @Field("last_name") String last_name,
                                         @Field("middle_name") String middle_name);

    @FormUrlEncoded
    @POST("profile-email-update")
    Call<NameUpdatedResponse> updateEmail(@Field("api_key") String api_key,
                                          @Field("user_id") String username,
                                          @Field("email") String email);

    @FormUrlEncoded
    @POST("profile-phone-update")
    Call<NameUpdatedResponse> updatePhone(@Field("api_key") String api_key,
                                          @Field("user_id") String username,
                                          @Field("phone") String phone,
                                          @Field("phoneType") String phoneType,
                                          @Field("mobile") String mobile,
                                          @Field("mobileType") String mobileType);

    @FormUrlEncoded
    @POST("profile-password-update")
    Call<NameUpdatedResponse> updatePassword(@Field("api_key") String api_key,
                                             @Field("user_id") String username,
                                             @Field("old_password") String old_password,
                                             @Field("new_password") String new_password,
                                             @Field("confirm_password") String confirm_password);

    @FormUrlEncoded
    @POST("profile-gender-update")
    Call<NameUpdatedResponse> updateGender(@Field("api_key") String api_key,
                                           @Field("user_id") String username,
                                           @Field("gender") String gender);

    @FormUrlEncoded
    @POST("profile-marital-status-update")
    Call<NameUpdatedResponse> updateRelationship(@Field("api_key") String api_key,
                                                 @Field("user_id") String username,
                                                 @Field("maritalStatus") String maritalStatus);

    @FormUrlEncoded
    @POST("profile-about-me-update")
    Call<NameUpdatedResponse> updateAboutMe(@Field("api_key") String api_key,
                                            @Field("user_id") String username,
                                            @Field("about_me") String about_me);

    @FormUrlEncoded
    @POST("privacy-settings")
    Call<NameUpdatedResponse> updatePrivacy(@Field("api_key") String api_key,
                                            @Field("user_id") String username,
                                            @Field("who_can_look_me_up") String who_can_look_me_up);

    @FormUrlEncoded
    @POST("privacy-settings")
    Call<NameUpdatedResponse> updatePrivacyStuff(@Field("api_key") String api_key,
                                                 @Field("user_id") String username,
                                                 @Field("who_can_see_my_post") String who_can_look_me_up,
                                                 @Field("who_can_see_my_photo") String who_can_see_my_photo);

    @FormUrlEncoded
    @POST("privacy-settings")
    Call<NameUpdatedResponse> updatePrivacyStuffDefaultView(@Field("api_key") String api_key,
                                                            @Field("user_id") String username,
                                                            @Field("default_view") String default_view);

    @FormUrlEncoded
    @POST("privacy-settings")
    Call<NameUpdatedResponse> updateDefaultPostCreationType(@Field("api_key") String api_key,
                                                            @Field("user_id") String username,
                                                            @Field("default_post_creation_type") String default_post_creation_type);

    @FormUrlEncoded
    @POST("privacy-settings")
    Call<NameUpdatedResponse> updateDefaultFilter(@Field("api_key") String api_key,
                                                  @Field("user_id") String username,
                                                  @Field("default_filter") String default_filter);

    @FormUrlEncoded
    @POST("notification-setting")
    Call<NameUpdatedResponse> updateNotificationSettings(@Field("api_key") String api_key,
                                                         @Field("user_id") String username,

                                                         @Field("friends[devotionals]") int devotionals,
                                                         @Field("friends[prayerrequest]") int prayerrequest,
                                                         @Field("friends[praises]") int praises,
                                                         @Field("friends[testimonials]") int testimonials,
                                                         @Field("friends[comments]") int comments,
                                                         @Field("friends[likes]") int likes,
                                                         @Field("friends[reply]") int reply,

                                                         @Field("church[devotionals]") int churchdevotionals,
                                                         @Field("church[prayerrequest]") int churchprayerrequest,
                                                         @Field("church[praises]") int churchpraises,
                                                         @Field("church[testimonials]") int churchtestimonials,
                                                         @Field("church[comments]") int churchcomments,
                                                         @Field("church[likes]") int churchlikes,
                                                         @Field("church[reply]") int churchreply,

                                                         @Field("group[devotionals]") int groupdevotionals,
                                                         @Field("group[prayerrequest]") int groupprayerrequest,
                                                         @Field("group[praises]") int grouppraises,
                                                         @Field("group[testimonials]") int grouptestimonials,
                                                         @Field("group[comments]") int groupcomments,
                                                         @Field("group[likes]") int grouplikes,
                                                         @Field("group[reply]") int groupreply,

                                                         @Field("event[devotionals]") int eventdevotionals,
                                                         @Field("event[prayerrequest]") int eventprayerrequest,
                                                         @Field("event[praises]") int eventpraises,
                                                         @Field("event[testimonials]") int eventtestimonials,
                                                         @Field("event[comments]") int eventcomments,
                                                         @Field("event[likes]") int eventlikes,
                                                         @Field("event[reply]") int eventreply
    );

    @FormUrlEncoded
    @POST("general-settings")
    Call<UsersSettingsResponse> getAllSettings(@Field("api_key") String api_key,
                                               @Field("user_id") String username);

    @FormUrlEncoded
    @POST("all-search")
    Call<SearchResponse> getAllSearch(@Field("api_key") String api_key,
                                      @Field("user_id") String username,
                                      @Field("search_text") String search_text);

    @FormUrlEncoded
    @POST("archivePosts")
    Call<ArchiveResponse> archivePost(@Field("api_key") String api_key,
                                      @Field("user_id") String username,
                                      @Field("post_id") String post_id);

    @FormUrlEncoded
    @POST("hide-post")
    Call<HidePostResponse> hidePost(@Field("api_key") String api_key,
                                    @Field("user_id") String username,
                                    @Field("post_id") String post_id);

    @FormUrlEncoded
    @POST("delete-post")
    Call<HidePostResponse> deletePost(@Field("api_key") String api_key,
                                    @Field("user_id") String username,
                                    @Field("post_id") String post_id);

    @Multipart
    @POST("profile-pic-update")
    Call<CoverPicUpdateResponse> updateProfilePic(@PartMap() Map<String, RequestBody> partMap,
                                                  @Part MultipartBody.Part file);

    @Multipart
    @POST("add-comment")
    Call<AddCommentsResponse> addCommentsPost(@PartMap() Map<String, RequestBody> partMap,
                                              @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("church-library")
    Call<LibraryListResponse> churchLibrary(@Field("api_key") String api_key,
                                            @Field("user_id") String username,
                                            @Field("church_id") String church_id);

    @Multipart
    @POST("add-church-library")
    Call<AddLibraryResponse> addChurchLibraryFile(@PartMap() Map<String, RequestBody> partMap,
                                                  @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("delete-church-library")
    Call<AddLibraryResponse> deleteLibraryFile(@Field("api_key") String api_key,
                                            @Field("user_id") String username,
                                            @Field("library_id") String library_id);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

}