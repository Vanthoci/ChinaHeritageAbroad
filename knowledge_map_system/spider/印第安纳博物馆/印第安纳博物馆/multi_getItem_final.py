import requests
import xlwt  # 将数据存入excel
import json
import re
import csv
from bs4 import BeautifulSoup
from concurrent.futures import ThreadPoolExecutor

import time

head = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36",
}

#从matchs.txt文读取文件
matchs = []
with open("ids.txt", "r", encoding="utf-8") as fp:
    matchs = fp.readlines()
    for i in range(len(matchs)):
        matchs[i] = matchs[i].strip()

len = len(matchs)

fieldnames = ['Id', 'name', 'author', 'relicTime', 'imageUrl', 'description', 'realUrl']

def process_item(i):
    print(i)
    info = {}
    url = "https://artmuseum.indiana.edu/collections-online/browse/object.php?number=" + matchs[i][1:-1]
    info['realUrl'] = url
    response = requests.get(url,headers=head)
    data = response.text
    soup = BeautifulSoup(data, 'html.parser')
    # get info
    tombstone_lines = soup.find_all('div', class_='tombstone-line')
    pattern_key = re.compile(r'<strong>(.*?)</strong>') 
    pattern_value = re.compile(r'</span>\s*(.*?)\s*</div>', re.DOTALL)
    for j in tombstone_lines:
        key = pattern_key.findall(str(j))
        value = pattern_value.findall(str(j))
        # print(key[0],value)   
        if(key[0] == 'Title'): 
            value[0] = value[0].replace('<i>','')
            value[0] = value[0].replace('</i>','')
            key[0] = 'name'
        elif(key[0] == 'Accession Number'):
            key[0] = 'Id'
        elif(key[0] == 'Artist'):
            key[0] = 'author'
        elif(key[0] == 'Date'):
            key[0] = 'relicTime'
        elif(key[0] == 'Credit Line'):
            key[0] = 'description'
        else:
            key[0] = 'abort'
        if(key[0] != 'abort'):
            value[0] = value[0].replace('\n', '')
            value[0] = value[0].replace('\r', '')
            value[0] = value[0].strip()
            info[key[0]] = value[0]
    # 写入CSV文件
    with open('output.csv', 'a', newline='') as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writerow(info)


with ThreadPoolExecutor(max_workers=100) as executor:
    executor.map(process_item, range(0,len))



