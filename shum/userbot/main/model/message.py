from typing import Optional

from telethon.tl.types import Message

from main.model.entity import Entity
from main.util import format_datetime


class BotMessage:

    def __init__(
            self,
            forwarded_from: Optional[dict],
            from_entity: Optional[Entity],
            mentioned: bool,
            message_id: int,
            received_at: str,
            reply_to: Optional['BotMessage'],
            source_entity: Entity,
            text: str,
    ):
        self.forwarded_from = forwarded_from
        self.from_entity: Optional[Entity] = from_entity
        self.id = message_id
        self.mentioned = mentioned
        self.received_at = received_at
        self.reply_to = reply_to
        self.source_entity = source_entity
        self.text = text

    def to_dict(self):
        from_entity_dict = None
        if self.from_entity:
            from_entity_dict = self.from_entity.to_dict()

        return dict(
            forwarded_from=self.forwarded_from,
            from_entity=from_entity_dict,
            id=self.id,
            mentioned=self.mentioned,
            received_at=self.received_at,
            reply_to=self.reply_to.to_dict() if self.reply_to else None,
            source_entity=self.source_entity.to_dict(),
            text=self.text,
        )

    def __str__(self):
        return f"BotMessage({self.to_dict()})"

    @staticmethod
    async def create(client, message: Message):
        reply_to = None
        if message.reply_to:
            reply_to = await BotMessage.create(
                client=client,
                message=await client.get_messages(message.peer_id, ids=message.reply_to.reply_to_msg_id)
            )

        fwd_from = None
        if message.fwd_from:
            fwd_from_entity = Entity(message.fwd_from.from_id)
            fwd_from = dict(
                created_at=format_datetime(message.fwd_from.date),
                from_entity=fwd_from_entity.to_dict(),
                # int id
                channel_post=message.fwd_from.channel_post,
            )

        from_entity = None
        if message.from_id:
            from_entity = Entity(message.from_id)

        return BotMessage(
            forwarded_from=fwd_from,
            from_entity=from_entity,
            mentioned=message.mentioned,
            message_id=message.id,
            received_at=format_datetime(message.date),
            reply_to=reply_to,
            source_entity=Entity(message.peer_id),
            text=message.message,
        )
