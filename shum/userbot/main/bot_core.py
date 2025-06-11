import asyncio
import time

from telethon import events
from telethon.errors import MessageTooLongError
from telethon.events import NewMessage

from main.model.effect import EffectType
from main.model.entity import Entity
from main.model.message import BotMessage
from main.server_api import handle_action, poll_effects, ack_effects
from main.telethon_api import get_logged_in_client


def run(loop, x):
    return loop.run_until_complete(x)


async def execute_effect(client, effect):
    print("# Executing effect: ", effect)
    try:
        if effect.get("type") == EffectType.SEND_MESSAGE:
            entity = Entity.from_dict(effect["entity"])
            try:
                await client.send_message(entity=entity.to_telethon_entity(), message=effect["message"],
                                          reply_to=effect.get("reply_to"))
            except MessageTooLongError as e:
                print("Message is too long")
            except ValueError as e:
                await client.get_dialogs()
                await client.send_message(entity=entity.to_telethon_entity(), message=effect["message"],
                                          reply_to=effect.get("reply_to"))
        else:
            raise ValueError("Unknown effect type: " + str(effect))
    except Exception as e:
        raise e


async def bot_main():
    client = await get_logged_in_client()

    @client.on(events.NewMessage())
    async def handler(event: NewMessage.Event):
        print(event)
        try:
            message: BotMessage = await BotMessage.create(client, event.message)
            handle_action(dict(
                type="message",
                data=message.to_dict(),
            ))
        except Exception as e:
            handle_action(dict(
                type="error",
                data=dict(
                    error_message=str(e),
                    telegram_message=str(event.message),
                ),
            ))
            raise e

    while True:
        await client.connect()
        await client.get_me()
        result = poll_effects()
        effects = result["effects"]
        if effects:
            for effect in effects:
                await execute_effect(client, effect)
            ack_effects(effects)
        time.sleep(2)


def run_bot_in_loop():
    loop = asyncio.get_event_loop()
    try:
        run(loop, bot_main())
    except KeyboardInterrupt:
        pass
