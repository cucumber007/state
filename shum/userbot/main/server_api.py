import requests
from requests.exceptions import ConnectionError

SERVER_URL = "http://127.0.0.1:8081/"


def handle_action(action):
    print("> POST /action", action)
    result = requests.post(SERVER_URL + "action", json=action)
    print(result)
    return result


def ack_effects(effects):
    print("> POST /effects", effects)
    try:
        result = requests.post(SERVER_URL + "effects", json=dict(effects=effects))
    except ConnectionError as e:
        print("Connection error", e)


def poll_effects():
    print("> GET /effects")
    try:
        result = requests.get(SERVER_URL + "effects")
        if (result.status_code != 200):
            print(result.status_code)
            return dict(effects=[])
        else:
            return result.json()
    except ConnectionError as e:
        print("Connection error", e)
        return dict(effects=[])
