from enum import Enum

from telethon.tl.types import PeerChannel, PeerChat, PeerUser


class EntityType(str, Enum):
    USER = "user"
    CHANNEL = "channel"
    CHAT = "chat"


class Entity:
    def __init__(self, telethon_entity):
        if isinstance(telethon_entity, PeerChannel):
            self.id = telethon_entity.channel_id
            self.type = EntityType.CHANNEL
        elif isinstance(telethon_entity, PeerChat):
            self.id = telethon_entity.chat_id
            self.type = EntityType.CHAT
        elif isinstance(telethon_entity, PeerUser):
            self.id = telethon_entity.user_id
            self.type = EntityType.USER
        else:
            raise ValueError(f"Unknown entity type: {type(telethon_entity)}")

    def to_dict(self):
        return {
            "type": self.type,
            "id": self.id,
        }

    def to_telethon_entity(self):
        if self.type == EntityType.USER:
            return PeerUser(self.id)
        elif self.type == EntityType.CHANNEL:
            return PeerChannel(self.id)
        elif self.type == EntityType.CHAT:
            return PeerChat(self.id)
        else:
            raise ValueError(f"Unknown entity type: {self.type}")

    @staticmethod
    def from_dict(data: dict):
        return Entity(
            telethon_entity={
                EntityType.USER: PeerUser,
                EntityType.CHANNEL: PeerChannel,
                EntityType.CHAT: PeerChat,
            }[data["type"]](data["id"])
        )
