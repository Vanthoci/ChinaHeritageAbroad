from openai import OpenAI
import httpx
import pandas as pd
from langchain_openai import ChatOpenAI


from langchain_openai.chat_models.base import BaseChatModel

# 调用Chat Completion API
llm= ChatOpenAI(
    model="gpt-3.5-turbo",  # Specify the model to use
    openai_api_base="https://apikeyplus.com",  # Set the custom API base URL
    openai_api_key="sk-qtSJopBKqAKTQVhx9b638e12Cb8643539a7eB64975F9E05a"  # Set your custom API key
)
response = llm.invoke('hello')
print(response)

# 读取 Excel 文件
# collection_data = pd.read_excel('unit.xlsx')
# data_to_learn = collection_data['藏品'].tolist()  # 假设你关注的数据在某一列


# #
#
# client = OpenAI(
#     base_url="https://hk.xty.app/v1",
#     api_key="sk-KuS6VKYhcbKqGPfnFfB2D7D8AbF8451f80177b8cC8CbDfAb",
#     http_client=httpx.Client(
#         base_url="https://hk.xty.app/v1",
#         follow_redirects=True,
#     ),
# )
#
#
# completion = client.chat.completions.create(
#   model="gpt-3.5-turbo",
#     messages=[
#         {"role": "system",
#          "content": "You are a museum assistant. Answer questions about the artifacts precisely with no extra information."},
#         {"role": "user", "content": "请告诉我司母戊大方鼎的作者是？"}  # 假设这是你的问题
#     ]
# )
#
# print(completion)