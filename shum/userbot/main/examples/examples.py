async def get_entity(client):
    username = await client.get_entity('username')
    username = await client.get_entity('t.me/username')
    username = await client.get_entity('https://telegram.dog/username')

    # Other kind of entities.
    friend_id = None
    channel = await client.get_entity('telegram.me/joinchat/AAAAAEkk2WdoDrB4-Q8-gg')
    contact = await client.get_entity('+34xxxxxxxxx')
    friend = await client.get_entity(friend_id)

    # Getting entities through their ID (User, Chat or Channel)
    some_id = None
    entity = await client.get_entity(some_id)

# NewMessage.Event(
#     message=Message(id=140, peer_id=PeerChat(chat_id=330683051),
#                     date=datetime.datetime(2024, 9, 6, 0, 42, 37, tzinfo=datetime.timezone.utc), message='Shum',
#                     out=False, mentioned=True, media_unread=True, silent=False, post=None, from_scheduled=None,
#                     legacy=None, edit_hide=None, pinned=None, noforwards=None, invert_media=None, offline=None,
#                     from_id=PeerUser(user_id=5775671423), from_boosts_applied=None, saved_peer_id=None, fwd_from=None,
#                     via_bot_id=None, via_business_bot_id=None, reply_to=None, media=None, reply_markup=None,
#                     entities=[MessageEntityMentionName(offset=0, length=4, user_id=7525194933)], views=None,
#                     forwards=None, replies=None, edit_date=None, post_author=None, grouped_id=None, reactions=None,
#                     restriction_reason=[], ttl_period=None, quick_reply_shortcut_id=None, effect=None, factcheck=None))

# NewMessage.Event(
#     message=Message(id=193, peer_id=PeerChat(chat_id=330683051),
#                     date=datetime.datetime(2024, 9, 6, 1, 26, 33, tzinfo=datetime.timezone.utc), message='тест',
#                     out=False, mentioned=True, media_unread=True, silent=False, post=None, from_scheduled=None,
#                     legacy=None, edit_hide=None, pinned=None, noforwards=None, invert_media=None, offline=None,
#                     from_id=PeerUser(user_id=5775671423), from_boosts_applied=None, saved_peer_id=None, fwd_from=None,
#                     via_bot_id=None, via_business_bot_id=None,
#                     reply_to=MessageReplyHeader(reply_to_scheduled=False, forum_topic=False, quote=False,
#                                                 reply_to_msg_id=192, reply_to_peer_id=None, reply_from=None,
#                                                 reply_media=None, reply_to_top_id=None, quote_text=None,
#                                                 quote_entities=[], quote_offset=None), media=None, reply_markup=None,
#                     entities=[], views=None, forwards=None, replies=None, edit_date=None, post_author=None,
#                     grouped_id=None, reactions=None, restriction_reason=[], ttl_period=None,
#                     quick_reply_shortcut_id=None, effect=None, factcheck=None))


# NewMessage.Event(original_update=UpdateNewChannelMessage(
#     message=Message(id=435289, peer_id=PeerChannel(channel_id=1480102152),
#                     date=datetime.datetime(2024, 9, 6, 1, 30, 59, tzinfo=datetime.timezone.utc), message='Shum',
#                     out=False, mentioned=True, media_unread=True, silent=False, post=False, from_scheduled=False,
#                     legacy=False, edit_hide=False, pinned=False, noforwards=False, invert_media=False, offline=False,
#                     from_id=PeerUser(user_id=5775671423), from_boosts_applied=None, saved_peer_id=None, fwd_from=None,
#                     via_bot_id=None, via_business_bot_id=None, reply_to=None, media=None, reply_markup=None,
#                     entities=[MessageEntityMentionName(offset=0, length=4, user_id=7525194933)], views=None,
#                     forwards=None,
#                     replies=MessageReplies(replies=0, replies_pts=475728, comments=False, recent_repliers=[],
#                                            channel_id=None, max_id=None, read_max_id=None), edit_date=None,
#                     post_author=None, grouped_id=None, reactions=None, restriction_reason=[], ttl_period=None,
#                     quick_reply_shortcut_id=None, effect=None, factcheck=None), pts=475728, pts_count=1),
#     pattern_match=None, message=Message(id=435289, peer_id=PeerChannel(channel_id=1480102152),
#                                         date=datetime.datetime(2024, 9, 6, 1, 30, 59,
#                                                                tzinfo=datetime.timezone.utc),
#                                         message='Shum', out=False, mentioned=True, media_unread=True,
#                                         silent=False, post=False, from_scheduled=False, legacy=False,
#                                         edit_hide=False, pinned=False, noforwards=False,
#                                         invert_media=False, offline=False,
#                                         from_id=PeerUser(user_id=5775671423), from_boosts_applied=None,
#                                         saved_peer_id=None, fwd_from=None, via_bot_id=None,
#                                         via_business_bot_id=None, reply_to=None, media=None,
#                                         reply_markup=None, entities=[
#             MessageEntityMentionName(offset=0, length=4, user_id=7525194933)], views=None, forwards=None,
#                                         replies=MessageReplies(replies=0, replies_pts=475728,
#                                                                comments=False, recent_repliers=[],
#                                                                channel_id=None, max_id=None,
#                                                                read_max_id=None), edit_date=None,
#                                         post_author=None, grouped_id=None, reactions=None,
#                                         restriction_reason=[], ttl_period=None,
#                                         quick_reply_shortcut_id=None, effect=None, factcheck=None))

# NewMessage.Event(original_update=UpdateNewChannelMessage(
#     message=Message(id=22, peer_id=PeerChannel(channel_id=2420029786),
#                     date=datetime.datetime(2024, 9, 6, 2, 17, 14, tzinfo=datetime.timezone.utc),
#                     message='Тривога буде тривати пока козаки не мінуснуть БпЛа  , для Дніпра без загроз \n\n🇺🇦 Підписатись | Радар Дніпро',
#                     out=False, mentioned=False, media_unread=False, silent=False, post=True, from_scheduled=False,
#                     legacy=False, edit_hide=False, pinned=False, noforwards=False, invert_media=False, offline=False,
#                     from_id=None, from_boosts_applied=None, saved_peer_id=None, fwd_from=MessageFwdHeader(
#             date=datetime.datetime(2024, 9, 6, 0, 59, 21, tzinfo=datetime.timezone.utc), imported=False,
#             saved_out=False, from_id=PeerChannel(channel_id=1748851694), from_name=None, channel_post=35232,
#             post_author=None, saved_from_peer=None, saved_from_msg_id=None, saved_from_id=None, saved_from_name=None,
#             saved_date=None, psa_type=None), via_bot_id=None, via_business_bot_id=None, reply_to=None, media=None,
#                     reply_markup=None, entities=[MessageEntityBold(offset=78, length=4),
#                                                  MessageEntityCustomEmoji(offset=78, length=4,
#                                                                           document_id=5332281535267618967),
#                                                  MessageEntityBold(offset=82, length=1),
#                                                  MessageEntityTextUrl(offset=83, length=26,
#                                                                       url='https://t.me/ny_i_dnipro'),
#                                                  MessageEntityBold(offset=83, length=26)], views=1, forwards=0,
#                     replies=None, edit_date=None, post_author=None, grouped_id=None, reactions=None,
#                     restriction_reason=[], ttl_period=None, quick_reply_shortcut_id=None, effect=None, factcheck=None),
#     pts=23, pts_count=1), pattern_match=None, message=Message(id=22, peer_id=PeerChannel(channel_id=2420029786),
#                                                               date=datetime.datetime(2024, 9, 6, 2, 17, 14,
#                                                                                      tzinfo=datetime.timezone.utc),
#                                                               message='Тривога буде тривати пока козаки не мінуснуть БпЛа  , для Дніпра без загроз \n\n🇺🇦 Підписатись | Радар Дніпро',
#                                                               out=False, mentioned=False, media_unread=False,
#                                                               silent=False, post=True, from_scheduled=False,
#                                                               legacy=False, edit_hide=False, pinned=False,
#                                                               noforwards=False, invert_media=False, offline=False,
#                                                               from_id=None, from_boosts_applied=None,
#                                                               saved_peer_id=None, fwd_from=MessageFwdHeader(
#         date=datetime.datetime(2024, 9, 6, 0, 59, 21, tzinfo=datetime.timezone.utc), imported=False, saved_out=False,
#         from_id=PeerChannel(channel_id=1748851694), from_name=None, channel_post=35232, post_author=None,
#         saved_from_peer=None, saved_from_msg_id=None, saved_from_id=None, saved_from_name=None, saved_date=None,
#         psa_type=None), via_bot_id=None, via_business_bot_id=None, reply_to=None, media=None, reply_markup=None,
#                                                               entities=[MessageEntityBold(offset=78, length=4),
#                                                                         MessageEntityCustomEmoji(offset=78, length=4,
#                                                                                                  document_id=5332281535267618967),
#                                                                         MessageEntityBold(offset=82, length=1),
#                                                                         MessageEntityTextUrl(offset=83, length=26,
#                                                                                              url='https://t.me/ny_i_dnipro'),
#                                                                         MessageEntityBold(offset=83, length=26)],
#                                                               views=1, forwards=0, replies=None, edit_date=None,
#                                                               post_author=None, grouped_id=None, reactions=None,
#                                                               restriction_reason=[], ttl_period=None,
#                                                               quick_reply_shortcut_id=None, effect=None,
#                                                               factcheck=None))
