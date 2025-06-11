import datetime

from telethon.tl.types import Message, PeerChannel, MessageFwdHeader

CHANNEL_POST_FORWARDED_TO_CHANNEL = Message(
    id=73,
    peer_id=PeerChannel(channel_id=2420029786),
    date=datetime.datetime(2024, 9, 12, 22, 14, 10, tzinfo=datetime.timezone.utc),
    message='тест дніпро',
    out=False,
    mentioned=False,
    media_unread=False,
    silent=False,
    post=True,
    from_scheduled=False,
    legacy=False,
    edit_hide=False,
    pinned=False,
    noforwards=False,
    invert_media=False,
    offline=False,
    from_id=None,
    from_boosts_applied=None,
    saved_peer_id=None,
    fwd_from=MessageFwdHeader(
        date=datetime.datetime(2024, 9, 12, 22, 7, 22, tzinfo=datetime.timezone.utc), imported=False, saved_out=False,
        from_id=PeerChannel(channel_id=2420029786), from_name=None, channel_post=72, post_author=None,
        saved_from_peer=None, saved_from_msg_id=None, saved_from_id=None, saved_from_name=None, saved_date=None,
        psa_type=None), via_bot_id=None, via_business_bot_id=None, reply_to=None, media=None, reply_markup=None,
    entities=[],
    views=1,
    forwards=0,
    replies=None,
    edit_date=None,
    post_author=None,
    grouped_id=None,
    reactions=None,
    restriction_reason=[],
    ttl_period=None,
    quick_reply_shortcut_id=None,
    effect=None,
    factcheck=None
)
