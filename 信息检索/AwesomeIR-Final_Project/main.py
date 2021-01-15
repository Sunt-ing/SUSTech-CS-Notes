# coding=utf-8
import logging
from model.invertedIndex import InvertedIndex
from web.websocket_server import WebsocketServer
from query.queryProcessor import QueryProcessor


def init():
    pass


def new_client(client, server):
    print("Client(%d) has joined." % client['id'])


def client_left(client, server):
    print("Client(%d) disconnected" % client['id'])


def message_back(client, server, message):
    print("后端收到了：" + message)
    result = QueryProcessor(index_dao).query(message)
    server.send_message(client, result)


data_addr = input("Please input your data addr:")
index_dao = InvertedIndex(data_addr)
# 如果host为空，则默认为本机IP
server = WebsocketServer(4200, host='', loglevel=logging.INFO)
server.set_fn_new_client(new_client)
server.set_fn_client_left(client_left)
server.set_fn_message_received(message_back)
server.run_forever()
