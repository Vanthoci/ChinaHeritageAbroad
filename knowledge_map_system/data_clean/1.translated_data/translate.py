import hashlib
import random
import pandas as pd
import requests
import json

def translate(content, from_lang, to_lang):
    # 百度翻译API的URL
    url = "https://fanyi-api.baidu.com/api/trans/vip/translate"
    
    # 你的APP ID和密钥
    appid = "20240416002026333"
    secretKey = "E6KXfQju_7nCgqssb8uq"
    
    # 生成签名
    salt = random.randint(32768, 65536)
    sign = appid + content + str(salt) + secretKey
    sign = hashlib.md5(sign.encode()).hexdigest()
    
    # 构造请求参数
    params = {
        "q": content,
        "from": from_lang,
        "to": to_lang,
        "appid": appid,
        "salt": salt,
        "sign": sign
    }
    headers = {'Content-Type': 'application/x-www-form-urlencoded'}
    # 发送请求并获取响应
    response = requests.post(url, params=params,headers=headers)
    result = response.json()

    # Show response
    json.dumps(result, ensure_ascii=False)
    # print(json.dumps(result, ensure_ascii=False))
    
    # 返回翻译结果
    return result['trans_result'][0]['dst']

# 读取CSV文件
df = pd.read_csv("E:\\vscode1\\output.csv")
df_translated = pd.DataFrame(columns=df.columns)

# 对每一行的每一列进行翻译
for index, row in df.iterrows():
    new_row = {}
    for column in df.columns:
        if column=="realUrl":
            break
        content = str(row[column])
        translated_content = translate(content, "auto", "zh")
        new_row[column] = translated_content
    new_row["realUrl"]=row["realUrl"]
    print(new_row)
    df_translated = pd.concat([df_translated, pd.DataFrame(new_row, index=[0])], ignore_index=True)

# 保存翻译后的CSV文件
df_translated.to_csv("E:\\vscode1\\new_output.csv", index=False)

