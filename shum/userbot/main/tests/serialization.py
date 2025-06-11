import asyncio
import unittest

from main.examples.channel_post import CHANNEL_POST
from main.model.message import BotMessage


class MockClient:
    pass


class MyTestCase(unittest.TestCase):

    def test_all(self):
        loop = asyncio.get_event_loop()
        loop.run_until_complete(self.deserialization())

    async def deserialization(self):
        print(await BotMessage.create(MockClient(), CHANNEL_POST))
