from telethon import TelegramClient

# Deepest Void
API_ID = 972686
API_HASH = "66c994ebffd8e03366db8ee488312988"
# Elari
PHONE = "+380931051671"
PHONE_HASH = "c5b0f1367e8baa9f58"


async def get_logged_in_client():
    client = TelegramClient('user', API_ID, API_HASH)
    await client.connect()
    if not await client.is_user_authorized():
        result = await client.send_code_request(PHONE)
        phone_hash = result.phone_code_hash
        otp = input('Enter OTP:')
        await client.sign_in(
            phone=PHONE,
            code=otp,
            phone_code_hash=phone_hash,
        )
    return client
