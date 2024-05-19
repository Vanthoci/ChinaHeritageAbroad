import requests
import xlwt  # 将数据存入excel
import json
import re
from bs4 import BeautifulSoup
from concurrent.futures import ThreadPoolExecutor

head = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36",
}

def process_page(i):
    url = "https://artmuseum.indiana.edu/collections-online/browse/results.php?keyword=&page=" + str(i)
    response = requests.get(url,headers=head)
    data = response.text
    matchs = []
    soup = BeautifulSoup(data, 'html.parser')
    ass = soup.find_all('div', class_='search-result')
    pattern = re.compile(r'flex" href="object.php\?number=([^"]*)"') # 匹配所有的小数
    for a in ass: 
        match = pattern.findall(str(a))
        matchs.append(match[0])
        
    print(i)
    #保存matches
    with open("ids.txt", "a", encoding="utf-8") as fp:
        for i in matchs:
            json.dump(i, fp, ensure_ascii=False, indent=4)
            fp.write('\n')

with ThreadPoolExecutor(max_workers=10) as executor:
    executor.map(process_page, range(36,1160))