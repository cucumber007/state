import asyncio

from main.bot_core import run
from main.examples.channel_post import CHANNEL_POST
from main.examples.channel_post_forwarded_to_channel import CHANNEL_POST_FORWARDED_TO_CHANNEL
from main.model.message import BotMessage
from main.server_api import handle_action
from main.tests.serialization import MockClient


def assert_equal(expected, result):
    assert str(expected) == str(result), f"Expected: {expected}, got: {result}"


async def integration_tests_main():
    # client = await get_logged_in_client()
    # print("ny_i_dnipro", (await client.get_entity("https://t.me/ny_i_dnipro")).id)
    # print("dnipro_alerts", (await client.get_entity("https://t.me/dnipro_alerts")).id)
    # print("vanek_nikolaev", (await client.get_entity("https://t.me/vanek_nikolaev")).id)
    # print("https://t.me/+L1v0-gXJHzo0MTBi", (await client.get_entity("ShumTestChannel")).id)
    assert_equal("<Response [200]>", await message_test(CHANNEL_POST))
    assert_equal("<Response [200]>", await message_test(CHANNEL_POST_FORWARDED_TO_CHANNEL))


async def message_test(message):
    return handle_action(dict(
        type="message",
        data=(await BotMessage.create(MockClient(), message)).to_dict(),
    ))


if __name__ == '__main__':
    loop = asyncio.get_event_loop()
    try:
        run(loop, integration_tests_main())
    except KeyboardInterrupt:
        pass
