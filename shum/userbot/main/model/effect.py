from enum import Enum
from typing import Optional, Dict, Any

from main.model.entity import Entity


class EffectType(str, Enum):
    SEND_MESSAGE = "send_message"


class Effect:
    def __init__(self, effect_type: str, entity: Entity, message: str, reply_to: Optional[int]):
        self.type = effect_type
        self.entity = entity
        self.message = message
        self.reply_to = reply_to

    @staticmethod
    def from_dict(data: Dict[str, Any]) -> "Effect":
        return Effect(
            effect_type=data["type"],
            entity=Entity.from_dict(data["entity"]),
            message=data["message"],
            reply_to=data.get("reply_to"),
        )

    def to_dict(self) -> Dict[str, Any]:
        return {
            "type": self.type,
            "entity": self.entity.to_dict(),
            "message": self.message,
            "reply_to": self.reply_to if self.reply_to else None,
        }
